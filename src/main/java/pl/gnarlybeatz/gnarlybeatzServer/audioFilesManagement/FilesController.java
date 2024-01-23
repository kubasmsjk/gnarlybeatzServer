package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class FilesController {

    private final FilesStorageService filesStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAudioFile(
            @ModelAttribute UploadAudioFileRequest request
    ) {
        return ResponseEntity.ok(filesStorageService.uploadAudioFile(request));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/search")
    public ResponseEntity<AudioFileDataResponse> filterAndGetSpecificAudioFilesDataOrAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize,
            FileDataRequest request
    ) {
        return ResponseEntity.ok(filesStorageService.filterAndGetSpecificAudioFilesDataOrAll(pageNo, pageSize, request));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/filter/values")
    public ResponseEntity<FilterValues> getFilterValues() {
        return ResponseEntity.ok(filesStorageService.getFilterValues());
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/favorite-beats/add")
    public ResponseEntity<String> addFavoriteBeat(
            @RequestParam(value = "id") Long userId,
            @RequestParam(value = "name") String beatName
    ) {
        return ResponseEntity.ok(filesStorageService.addFavoriteBeat(userId,beatName));
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/favorite-beats/remove")
    public ResponseEntity<String> removeFavoriteBeat(
            @RequestParam(value = "id") Long userId,
            @RequestParam(value = "name") String beatName
    ) {
        return ResponseEntity.ok(filesStorageService.removeFavoriteBeat(userId,beatName));
    }

//
//    @CrossOrigin(origins = "*")
//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<byte[]> downloadFile(
//            @PathVariable String fileName
//    ) {
//        return ResponseEntity.ok(filesStorageService.downloadAudioFile(fileName));
//    }

}
