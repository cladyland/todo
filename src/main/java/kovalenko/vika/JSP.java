package kovalenko.vika;

public enum JSP {
    INDEX("/WEB-INF/index.jsp"),
    REGISTER("/WEB-INF/register.jsp"),
    TODO("/WEB-INF/todo.jsp");

    private final String value;

    JSP(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
