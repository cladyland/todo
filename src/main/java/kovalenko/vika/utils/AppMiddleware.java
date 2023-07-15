package kovalenko.vika.utils;

import kovalenko.vika.exception.TaskException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class AppMiddleware {

    public static void checkIfTitleIsBlank(String title) {
        if (isBlank(title)) {
            throw new TaskException("Title cannot be blank!");
        }
    }

    public static boolean numberEqualsZero(int number) {
        return number == 0;
    }
}
