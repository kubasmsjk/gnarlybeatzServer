package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.audioFileData.AudioFileData;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.audioFileData.AudioFilesSearchCriteria;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.favoriteBeats.FavoriteBeats;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.favoriteBeats.FavoriteBeatsRepository;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData.FileData;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData.FileDataRepository;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.requestData.FileDataRequest;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.requestData.UploadAudioFileRequest;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData.AudioFileDataResponse;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData.AudioFileUpdateDataResponse;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData.DownloadFileResponse;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData.FilterValuesResponse;
import pl.gnarlybeatz.gnarlybeatzServer.stripe.transaction.TransactionHistoryRepository;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectRequestInterface;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.CreateBlobOfMusicFileException;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.FileNotExistException;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.FileUploadException;
import pl.gnarlybeatz.gnarlybeatzServer.user.UserRepository;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilesService {

    private final static String MUSIC_FOLDER_PATH = "D:/inzynierka/gnarlybeatzServer/src/main/resources/static/music/";
    private final static String IMAGES_FOLDER_PATH = "D:/inzynierka/gnarlybeatzServer/src/main/resources/static/images/";

    private final FileDataRepository fileDataRepository;
    private final FavoriteBeatsRepository favoriteBeatsRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final UserRepository userRepository;
    private final AudioFilesSearchCriteria audioFilesSearchCriteria;
    private final ObjectValidator<ObjectRequestInterface> updateFileValidator;
    @Value("${stripe.apikey}")
    private String stripeKey;

    public String uploadOrUpdateAudioFile(UploadAudioFileRequest request) {
        Stripe.apiKey = stripeKey;
        updateFileValidator.validate(request);

        String imageFilePath = ((request.getImage() == null) ? createImageFilePath("gnarlyLogo.png") : createImageFilePath(request.getImage().getOriginalFilename()));
        String mp3AudioFileFilePath = createMusicFilePath(request.getMp3AudioFile().getOriginalFilename());
        String wavAudioFilePath = createMusicFilePath(request.getWavAudioFile().getOriginalFilename());

        var file = request.getUpdateBeat().equals("-") ? fileDataRepository.findByNameAndAudioFileType(FilenameUtils.removeExtension(request.getMp3AudioFile().getOriginalFilename()), "audio/mpeg") :
                fileDataRepository.findByNameAndAudioFileType(request.getUpdateBeat(), "audio/mpeg");
        var updatedWavFileOptional = !request.getUpdateBeat().equals("-") ? fileDataRepository.findByNameAndAudioFileType(request.getUpdateBeat(), "audio/wav") : Optional.<FileData>empty();

        Product mp3AudioFileProductInStripe;
        Product wavAudioFileProductInStripe;

        if (file.isPresent() && updatedWavFileOptional.isPresent() && !request.getUpdateBeat().equals("-")) {

            FileData updatedMp3File = updateFileSetTheSameData(file.get(), request, imageFilePath);
            updatedMp3File.setName(FilenameUtils.removeExtension(request.getMp3AudioFile().getOriginalFilename()));
            updatedMp3File.setAudioFileType(request.getMp3AudioFile().getContentType());
            updatedMp3File.setAudioFilePath(mp3AudioFileFilePath);

            FileData updatedWavFile = updateFileSetTheSameData(updatedWavFileOptional.get(), request, imageFilePath);
            updatedWavFile.setName(FilenameUtils.removeExtension(request.getWavAudioFile().getOriginalFilename()));
            updatedWavFile.setAudioFileType(request.getWavAudioFile().getContentType());
            updatedWavFile.setAudioFilePath(wavAudioFilePath);

            try {
                request.getMp3AudioFile().transferTo(new File(mp3AudioFileFilePath));
                request.getWavAudioFile().transferTo(new File(wavAudioFilePath));
                if (request.getImage() != null) {
                    request.getImage().transferTo(new File(imageFilePath));
                }
                fileDataRepository.save(updatedMp3File);
                fileDataRepository.save(updatedWavFile);

                Product resourceMp3 = Product.retrieve(updatedMp3File.getProductStripeId());
                Product resourceWav = Product.retrieve(updatedWavFile.getProductStripeId());
                ProductUpdateParams mp3AudioFileProductParamsInStripe = updateProductInStripe(request, FilenameUtils.removeExtension(request.getMp3AudioFile().getOriginalFilename()), request.getMp3AudioFile().getContentType());
                ProductUpdateParams wavAudioFileProductParamsInStripe = updateProductInStripe(request, FilenameUtils.removeExtension(request.getWavAudioFile().getOriginalFilename()), request.getWavAudioFile().getContentType());
                resourceMp3.update(mp3AudioFileProductParamsInStripe);
                resourceWav.update(wavAudioFileProductParamsInStripe);
            } catch (StripeException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                return new FileUploadException("File can't be upload").getErrorMessages();
            }
            return "File updated";
        }

        if (file.isPresent()) {
            return "File exists";
        } else {
            try {
                mp3AudioFileProductInStripe = createProductInStripe(request, request.getMp3AudioFile());
                wavAudioFileProductInStripe = createProductInStripe(request, request.getWavAudioFile());
            } catch (StripeException e) {
                return e.getMessage();
            }
            FileData mp3AudioFileData = buildFileData(request, FilenameUtils.removeExtension(request.getMp3AudioFile().getOriginalFilename()), request.getMp3AudioFile().getContentType(), mp3AudioFileFilePath, imageFilePath, mp3AudioFileProductInStripe);
            FileData wavAudioFileData = buildFileData(request, FilenameUtils.removeExtension(request.getWavAudioFile().getOriginalFilename()), request.getWavAudioFile().getContentType(), wavAudioFilePath, imageFilePath, wavAudioFileProductInStripe);
            try {
                request.getMp3AudioFile().transferTo(new File(mp3AudioFileFilePath));
                request.getWavAudioFile().transferTo(new File(wavAudioFilePath));
                if (request.getImage() != null) {
                    request.getImage().transferTo(new File(imageFilePath));
                }
                fileDataRepository.save(mp3AudioFileData);
                fileDataRepository.save(wavAudioFileData);
            } catch (IOException e) {
                return new FileUploadException("File can't be upload").getErrorMessages();
            }
        }
        return "File uploaded";
    }

    private FileData updateFileSetTheSameData(FileData fileData, UploadAudioFileRequest request, String imageFilePath) {
        fileData.setGenre(request.getGenre());
        fileData.setMood(request.getMood());
        fileData.setBpm(request.getBpm());
        fileData.setKey(request.getKey());
        fileData.setImagePath(imageFilePath);
        fileData.setActive(true);
        return fileData;
    }

    private String createImageFilePath(String originalFilename) {
        return IMAGES_FOLDER_PATH + originalFilename;
    }

    private String createMusicFilePath(String originalFilename) {
        return MUSIC_FOLDER_PATH + originalFilename;
    }

    private Product createProductInStripe(UploadAudioFileRequest request, MultipartFile audioFile) throws StripeException {
        return (Objects.equals(audioFile.getContentType(), "audio/mpeg") ? createProduct(request, audioFile, 1000L) : createProduct(request, audioFile, 2000L));
    }

    private Product createProduct(UploadAudioFileRequest request, MultipartFile audioFile, long unitAmount) throws StripeException {
        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName(FilenameUtils.removeExtension(audioFile.getOriginalFilename()))
                .putMetadata("genre", request.getGenre())
                .putMetadata("mood", request.getMood())
                .putMetadata("bpm", request.getBpm())
                .putMetadata("key", request.getKey())
                .putMetadata("audioFileType", Objects.requireNonNull(audioFile.getContentType()))
                .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                        .setUnitAmount(unitAmount)
                        .setCurrency("usd")
                        .build())
                .build();
        return Product.create(productParams);
    }

    private ProductUpdateParams updateProductInStripe(UploadAudioFileRequest request, String name, String type) throws StripeException {
        return ProductUpdateParams.builder()
                .setName(name)
                .putMetadata("genre", request.getGenre())
                .putMetadata("mood", request.getMood())
                .putMetadata("bpm", request.getBpm())
                .putMetadata("key", request.getKey())
                .putMetadata("audioFileType", type)
                .build();
    }

    private FileData buildFileData(UploadAudioFileRequest request, String name, String contentType, String audioFileFilePath, String imageFilePath, Product productInStripe) {
        return FileData.builder()
                .name(name)
                .genre(request.getGenre())
                .mood(request.getMood())
                .bpm(request.getBpm())
                .key(request.getKey())
                .audioFileType(contentType)
                .audioFilePath(audioFileFilePath)
                .imagePath(imageFilePath)
                .productStripeId(productInStripe.getId())
                .productStripePriceId(productInStripe.getDefaultPrice())
                .isActive(true)
                .build();
    }

    public AudioFileDataResponse filterAndGetSpecificAudioFilesDataOrAll(int pageNo, int pageSize, FileDataRequest
            request) {
        Page<FileData> pageOfFilesData = createPageableFromSearchAndReturnPage(pageNo, pageSize, request);
        List<FileData> listOfFilesData = pageOfFilesData.getContent();
        List<AudioFileData> listOfAudioFilesData = createListOfAudioFilesData(listOfFilesData, request.getUserId());

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
        return audioFilesSearchCriteria.findAllByCriteria(request, pageable);
    }

    private List<AudioFileData> createListOfAudioFilesData(List<FileData> listOfFilesData, Long userId) {
        return listOfFilesData.parallelStream().map(fileData -> createAudioFileData(fileData, userId)).collect(Collectors.toList());
    }

    private AudioFileData createAudioFileData(FileData fileData, Long userId) {
        byte[] audioFile = createBlobOfFile(fileData.getAudioFilePath());
        byte[] imageFile = createBlobOfFile(fileData.getImagePath());
        String deluxeProductStripePriceId = fileDataRepository.productStripePriceIdByType(fileData.getName(), "audio/wav");
        boolean isPurchased;
        if (userId != null) {
            var user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Couldn't find your account."));
            var beat = fileDataRepository.findByProductStripePriceId(fileData.getProductStripePriceId()).orElseThrow(() -> new FileNotExistException(Map.of("file", "File not exists")));
            isPurchased = transactionHistoryRepository.findByUserAndBeat(user, beat).isPresent();
            if (!isPurchased) {
                beat = fileDataRepository.findByProductStripePriceId(deluxeProductStripePriceId).orElseThrow(() -> new FileNotExistException(Map.of("file", "File not exists")));
                isPurchased = transactionHistoryRepository.findByUserAndBeat(user, beat).isPresent();
            }
        } else {
            isPurchased = false;
        }
        return AudioFileData.builder()
                .name(fileData.getName())
                .genre(fileData.getGenre())
                .mood(fileData.getMood())
                .bpm(fileData.getBpm())
                .key(fileData.getKey())
                .audioBlob(audioFile)
                .imageBlob(imageFile)
                .productStripeId(fileData.getProductStripeId())
                .standardProductStripePriceId(fileData.getProductStripePriceId())
                .deluxeProductStripePriceId(deluxeProductStripePriceId)
                .purchased(isPurchased)
                .build();
    }

    private byte[] createBlobOfFile(String path) {
        try {
            return Files.readAllBytes(new File(path).toPath());
        } catch (IOException e) {
            throw new CreateBlobOfMusicFileException(e.getMessage());
        }
    }

    public FilterValuesResponse getFilterValues(Long userId, String path) {
        if (path.equals("beats")) {
            return FilterValuesResponse.builder()
                    .bpm(fileDataRepository.findDistinctBpm())
                    .key(fileDataRepository.findDistinctKey())
                    .mood(fileDataRepository.findDistinctMood())
                    .genre(fileDataRepository.findDistinctGenre())
                    .build();
        } else {
            return FilterValuesResponse.builder()
                    .bpm(fileDataRepository.findDistinctBpmByUserId(userId))
                    .key(fileDataRepository.findDistinctKeyByUserId(userId))
                    .mood(fileDataRepository.findDistinctMoodByUserId(userId))
                    .genre(fileDataRepository.findDistinctGenreByUserId(userId))
                    .build();
        }
    }

    public String addFavoriteBeat(Long userId, String beatName) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Couldn't find your account."));
        var fileData = fileDataRepository.findByNameAndAudioFileType(beatName, "audio/mpeg").orElseThrow(() -> new FileNotExistException(Map.of("file", "File not exists")));
        Optional<FavoriteBeats> beat = favoriteBeatsRepository.findByBeatAndUser(fileData, user);
        if (beat.isPresent()) {
            return "File is already in favorites.";
        } else {
            var favoriteBeat = FavoriteBeats.builder()
                    .beat(fileData)
                    .user(user)
                    .build();
            favoriteBeatsRepository.save(favoriteBeat);
        }
        return "File has been added.";
    }

    public String removeFavoriteBeat(Long userId, String beatName) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Couldn't find your account."));
        var fileData = fileDataRepository.findByNameAndAudioFileType(beatName, "audio/mpeg").orElseThrow(() -> new FileNotExistException(Map.of("file", "File not exists")));
        Optional<FavoriteBeats> beat = favoriteBeatsRepository.findByBeatAndUser(fileData, user);
        beat.ifPresent(favoriteBeatsRepository::delete);
        return "File has been removed.";
    }


    public List<DownloadFileResponse> downloadAudioFile(Long userId) {
        var transaction = transactionHistoryRepository.findBeatByUserId(userId);
        List<DownloadFileResponse> audioToDownload = new ArrayList<>();
        transaction.forEach(e -> {
            if (e.getAudioFileType().equals("audio/wav")) {
                audioToDownload.add(createMusicFileToDownload(e));
                var fileData = fileDataRepository.findByNameAndAudioFileType(e.getName(), "audio/mpeg").orElseThrow(() -> new FileNotExistException(Map.of("file", "File not exists")));
                audioToDownload.add(createMusicFileToDownload(fileData));
            } else {
                audioToDownload.add(createMusicFileToDownload(e));
            }
        });
        return audioToDownload;
    }

    private DownloadFileResponse createMusicFileToDownload(FileData fileData) {
        String filePath = fileData.getAudioFilePath();
        byte[] musicFile;
        try {
            musicFile = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return DownloadFileResponse.builder()
                .name(fileData.getName())
                .audioType(fileData.getAudioFileType())
                .file(musicFile)
                .build();
    }

    public Map<String, AudioFileUpdateDataResponse> getAllMp3AudioFiles() {
        return fileDataRepository.getAllProductsByType("audio/mpeg").stream()
                .collect(Collectors.toMap(FileData::getName, e ->
                        AudioFileUpdateDataResponse.builder()
                                .name(e.getName())
                                .genre(e.getGenre())
                                .mood(e.getMood())
                                .bpm(e.getBpm())
                                .key(e.getKey())
                                .build()
                ));
    }

    public Map<String, AudioFileUpdateDataResponse> getAllMp3ArchiveFiles() {
        return fileDataRepository.getAllMp3ArchiveByType("audio/mpeg").stream()
                .collect(Collectors.toMap(FileData::getName, e ->
                        AudioFileUpdateDataResponse.builder()
                                .name(e.getName())
                                .genre(e.getGenre())
                                .mood(e.getMood())
                                .bpm(e.getBpm())
                                .key(e.getKey())
                                .build()
                ));
    }

    public String archiveBeat(FileDataRequest request) {
        List<FileData> beatsListByName = fileDataRepository.findBeatsListByName(request.getName());
        if (!beatsListByName.isEmpty()) {
            beatsListByName.forEach(beat -> beat.setActive(false));
            fileDataRepository.saveAll(beatsListByName);
        } else {
            return "Beat not found.";
        }
        return "Beat archive.";
    }
}
