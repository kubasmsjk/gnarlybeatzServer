package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.requestData.FileDataRequest;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.requestData.UploadAudioFileRequest;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData.AudioFileDataResponse;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData.AudioFileUpdateDataResponse;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData.DownloadFileResponse;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData.FilterValuesResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class FilesController {

    private final FilesService filesService;

    @PostMapping("/archiveBeat")
    public ResponseEntity<String> archiveBeat(
            @RequestBody FileDataRequest request
    ) {
        return ResponseEntity.ok(filesService.archiveBeat(request));
    }

    @PostMapping("/uploadOrUpdate")
    public ResponseEntity<String> uploadOrUpdateAudioFile(
            @ModelAttribute UploadAudioFileRequest request
    ) {
        return ResponseEntity.ok(filesService.uploadOrUpdateAudioFile(request));
    }

    @GetMapping("/listOfAudioFilesToUpdate")
    public ResponseEntity<Map<String, AudioFileUpdateDataResponse>> getAllMp3AudioFiles() {
        return ResponseEntity.ok(filesService.getAllMp3AudioFiles());
    }

    @GetMapping("/listOfArchiveBeat")
    public ResponseEntity<Map<String, AudioFileUpdateDataResponse>> getAllMp3ArchiveFiles() {
        return ResponseEntity.ok(filesService.getAllMp3ArchiveFiles());
    }

    @GetMapping("/search")
    public ResponseEntity<AudioFileDataResponse> filterAndGetSpecificAudioFilesDataOrAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize,
            FileDataRequest request
    ) {
        return ResponseEntity.ok(filesService.filterAndGetSpecificAudioFilesDataOrAll(pageNo, pageSize, request));
    }

    @GetMapping("/filter/values")
    public ResponseEntity<FilterValuesResponse> getFilterValues(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "path") String path
    ) {
        return ResponseEntity.ok(filesService.getFilterValues(userId, path));
    }

    @PostMapping("/favoriteBeats/add")
    public ResponseEntity<String> addFavoriteBeat(
            @RequestParam(value = "id") Long userId,
            @RequestParam(value = "name") String beatName
    ) {
        return ResponseEntity.ok(filesService.addFavoriteBeat(userId, beatName));
    }

    @PostMapping("/favoriteBeats/remove")
    public ResponseEntity<String> removeFavoriteBeat(
            @RequestParam(value = "id") Long userId,
            @RequestParam(value = "name") String beatName
    ) {
        return ResponseEntity.ok(filesService.removeFavoriteBeat(userId, beatName));
    }

    @GetMapping("/download")
    public ResponseEntity<List<DownloadFileResponse>> downloadFile(
            @RequestParam(value = "userId") Long userId
    ) {
        return ResponseEntity.ok(filesService.downloadAudioFile(userId));
    }

}
