package kovalenko.vika.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JSP {
    INDEX_JSP("/WEB-INF/index.jsp"),
    REGISTER("/WEB-INF/user/register.jsp"),
    TODO("/WEB-INF/user/todo.jsp"),
    NEW_TASK("/WEB-INF/task/new_task.jsp"),
    TASK_INFO("/WEB-INF/task/task_info.jsp"),
    TASK_UPDATE("/WEB-INF/task/update_task.jsp"),
    NEW_TAG("/WEB-INF/task/tag.jsp"),
    NOT_FOUND_JSP("/WEB-INF/error/not_found.jsp");

    private final String value;

}
