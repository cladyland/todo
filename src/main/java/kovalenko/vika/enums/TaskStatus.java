package kovalenko.vika.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum TaskStatus {
    PLANNED("planned"),
    IN_PROGRESS("in progress"),
    SUSPENDED("suspended"),
    DONE("done");

    private final String value;

    public static TaskStatus getStatusByName(String name){
        return Arrays.stream(values())
                .filter(status -> status.getValue().equals(name))
                .findFirst()
                .orElse(null);
    }
    public static List<TaskStatus> getAllStatuses(){
        return List.of(values());
    }
}