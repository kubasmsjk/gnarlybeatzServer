package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.audioFileData;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.favoriteBeats.FavoriteBeats;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData.FileData;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.requestData.FileDataRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AudioFilesSearchCriteria {
    private final EntityManager entityManager;

    public Page<FileData> findAllByCriteria(
            FileDataRequest request,
            Pageable pageable
    ) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileData> criteriaQuery = criteriaBuilder.createQuery(FileData.class);
        List<Predicate> predicates = new ArrayList<>();

        Root<FileData> root = criteriaQuery.from(FileData.class);

        if (request.getPathname() != null && !request.getPathname().isEmpty() && request.getPathname().equals("profile")) {
            Join<FileData, FavoriteBeats> favoriteBeatsJoin = root.join("favoriteBeats");
            Predicate userIdPredicate = criteriaBuilder
                    .equal(favoriteBeatsJoin.get("user").get("id"), request.getUserId());
            predicates.add(userIdPredicate);
        }

        if (request.getName() != null && !request.getName().isEmpty()) {
            Predicate namePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
            predicates.add(namePredicate);
        }
        if (request.getGenre() != null && !request.getGenre().isEmpty() && !request.getGenre().equals("-")) {
            Predicate genrePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("genre")), "%" + request.getGenre().toLowerCase() + "%");
            predicates.add(genrePredicate);
        }
        if (request.getMood() != null && !request.getMood().isEmpty() && !request.getMood().equals("-")) {
            Predicate moodPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("mood")), "%" + request.getMood().toLowerCase() + "%");
            predicates.add(moodPredicate);
        }
        if (request.getBpm() != null && !request.getBpm().isEmpty() && !request.getBpm().equals("-")) {
            Predicate bpmPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("bpm")), "%" + request.getBpm().toLowerCase() + "%");
            predicates.add(bpmPredicate);
        }
        if (request.getKey() != null && !request.getKey().isEmpty() && !request.getKey().equals("-")) {
            Predicate keyPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("key")), "%" + request.getKey().toLowerCase() + "%");
            predicates.add(keyPredicate);
        }

        if (!predicates.isEmpty()) {
            Predicate contentType = criteriaBuilder.equal(root.get("audioFileType"), "audio/mpeg");
            Predicate isActive = criteriaBuilder.equal(root.get("isActive"), true);
            predicates.add(contentType);
            predicates.add(isActive);
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        } else {
            criteriaQuery.where(criteriaBuilder.equal(root.get("audioFileType"), "audio/mpeg"), criteriaBuilder.equal(root.get("isActive"), true));
        }

        TypedQuery<FileData> query = entityManager.createQuery(criteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        List<FileData> result = query.getResultList();
        return new PageImpl<>(result, pageable, result.size());
    }
}
