package kovalenko.vika.command;

import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskCommand {
    private Long userId;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
}
