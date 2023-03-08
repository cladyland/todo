package kovalenko.vika.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum TaskPriority {
    HIGH("high"),
    MEDIUM("medium"),
    LOW("low");

    private final String value;

    public static TaskPriority getPriorityByName(String name){
        return Arrays.stream(values())
                .filter(priority -> priority.getValue().equals(name))
                .findFirst()
                .orElse(null);
    }
    public static List<TaskPriority> getAllPriorities(){
        return List.of(values());
    }
}
