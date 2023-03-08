package kovalenko.vika.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JSP {
    INDEX("/WEB-INF/index.jsp"),
    REGISTER("/WEB-INF/register.jsp"),
    TODO("/WEB-INF/todo.jsp"),
    NEW_TASK("/WEB-INF/new_task.jsp"),
    TASK_UPDATE("/WEB-INF/update_task.jsp"),
    NEW_TAG("/WEB-INF/new_tag.jsp");

    private final String value;

}
