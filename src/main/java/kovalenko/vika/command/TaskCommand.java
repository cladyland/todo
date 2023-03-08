package kovalenko.vika.command;

import kovalenko.vika.dto.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskCommand {
    private Long userId;
    private String title;
    private String description;
    private Set<TagDTO> tags;
}
