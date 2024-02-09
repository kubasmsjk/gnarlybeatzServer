package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.audioFileData.AudioFileData;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioFileDataResponse {
    private List<AudioFileData> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
