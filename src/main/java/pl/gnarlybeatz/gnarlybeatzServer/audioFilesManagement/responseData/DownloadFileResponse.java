package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadFileResponse {

    private String name;
    private String audioType;
    private byte[] file;

}
