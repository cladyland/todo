package kovalenko.vika.utils;

import kovalenko.vika.exception.TaskException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class AppUtil {
    public static Set<Long> convertIds(String[] ids) {
        if (isNull(ids)) {
            return null;
        }
        return Arrays.stream(ids)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    public static void checkIfTitleIsBlank(String title){
        if (isBlank(title)){
            throw new TaskException("Title cannot be blank!");
        }
    }
}
