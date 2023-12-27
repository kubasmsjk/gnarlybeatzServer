package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterValues {
    private Set<String> bpm = new TreeSet<>();
    private Set<String> key = new TreeSet<>();
    private Set<String> mood = new TreeSet<>();
    private Set<String> genre = new TreeSet<>();
}
