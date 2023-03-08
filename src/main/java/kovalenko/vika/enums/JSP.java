package kovalenko.vika.enums;

public enum JSP {
    INDEX("/WEB-INF/index.jsp"),
    REGISTER("/WEB-INF/register.jsp"),
    TODO("/WEB-INF/todo.jsp"),
    NEW_TASK("/WEB-INF/new_task.jsp"),
    TASK_UPDATE("/WEB-INF/update_task.jsp"),
    NEW_TAG("/WEB-INF/new_tag.jsp");

    private final String value;

    JSP(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
