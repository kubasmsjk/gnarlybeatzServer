package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.CreateBlobOfMusicFileException;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.FileUploadException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilesStorageService {

    private final static String FOLDER_PATH = "D:/inzynierka/gnarlybeatzServer/src/main/java/pl/gnarlybeatz/gnarlybeatzServer/audioFilesManagement/audioFiles/";
    private final FileDataRepository fileDataRepository;
    private final AudioFilesSearchDao audioFilesSearchDao;

    public String uploadAudioFile(UploadAudioFileRequest request) {
        MultipartFile audioFile = request.getAudioFile();
        MultipartFile image = request.getImage();
        String audioFilePath = FOLDER_PATH + audioFile.getOriginalFilename();
        String imageFilePath = FOLDER_PATH + image.getOriginalFilename();

        FileData fileData = FileData.builder()
                .name(FilenameUtils.removeExtension(audioFile.getOriginalFilename()))
                .genre(request.getGenre())
                .mood(request.getMood())
                .bpm(request.getBpm())
                .key(request.getKey())
                .audioFiletype(audioFile.getContentType())
                .audioFilePath(audioFilePath)
                .imagePath(imageFilePath)
                .build();

        try {
            audioFile.transferTo(new File(audioFilePath));
            image.transferTo(new File(imageFilePath));
            fileDataRepository.save(fileData);
        } catch (IOException e) {
            return new FileUploadException("File can't be upload").getErrorMessages();
        }
        return "File upload";
    }

    public AudioFileDataResponse filterAndGetSpecificAudioFilesDataOrAll(int pageNo, int pageSize, FileDataRequest request) {
        Page<FileData> pageOfFilesData = createPageableFromSearchAndReturnPage(pageNo, pageSize, request);
        List<FileData> listOfFilesData = pageOfFilesData.getContent();
        List<AudioFileData> listOfAudioFilesData = createListOfAudioFilesData(listOfFilesData);

        return AudioFileDataResponse.builder()
                .content(listOfAudioFilesData)
                .pageNo(pageOfFilesData.getNumber())
                .pageSize(pageOfFilesData.getSize())
                .totalElements(pageOfFilesData.getTotalElements())
                .totalPages(pageOfFilesData.getTotalPages())
                .last(pageOfFilesData.isLast())
                .build();
    }

    private Page<FileData> createPageableFromSearchAndReturnPage(int pageNo, int pageSize, FileDataRequest request) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return audioFilesSearchDao.findAllByCriteria(request, pageable);
    }

    private List<AudioFileData> createListOfAudioFilesData(List<FileData> listOfFilesData) {
        return listOfFilesData.parallelStream().map(this::createAudioFileData).collect(Collectors.toList());
    }

    private AudioFileData createAudioFileData(FileData fileData) {
        byte[] audioFile = createBlobOfFile(fileData.getAudioFilePath());
        byte[] imageFile = createBlobOfFile(fileData.getImagePath());
        return AudioFileData.builder()
                .name(fileData.getName())
                .genre(fileData.getGenre())
                .mood(fileData.getMood())
                .bpm(fileData.getBpm())
                .key(fileData.getKey())
                .audioBlob(audioFile)
                .imageBlob(imageFile)
                .build();
    }

    private byte[] createBlobOfFile(String path) {
        try {
            return Files.readAllBytes(new File(path).toPath());
        } catch (IOException e) {
            throw new CreateBlobOfMusicFileException(e.getMessage());
        }
    }

    public FilterValues getFilterValues() {
        return FilterValues.builder()
                .bpm(fileDataRepository.findDistinctBpm())
                .key(fileDataRepository.findDistinctKey())
                .mood(fileDataRepository.findDistinctMood())
                .genre(fileDataRepository.findDistinctGenre())
                .build();
    }

//
//    public byte[] downloadAudioFile(String request) {
//        var file = checkIsFileExistAndReturnFile(request);
//        String filePath = file.get().getFilePath();
//        byte[] musicFile;
//        try {
//            musicFile = Files.readAllBytes(new File(filePath).toPath());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return musicFile;
//    }
//
//    private Optional<FileData> checkIsFileExistAndReturnFile(String request) {
//        var file = fileDataRepository.findByName(request);
//        if (file.isEmpty()) {
//            throw new FileNotExistException(Map.of("fileNotExistStatus", true));
//        }
//        return file;
//    }
}
