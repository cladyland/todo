package kovalenko.vika.dao;

public class H2Env {
    public static final String H2_DIALECT = "org.hibernate.dialect.H2Dialect";
    public static final String H2_DRIVER = "org.h2.Driver";
    public static final String H2_URL = "jdbc:h2:mem:db;";
    public static final String H2_URL_CONFIG = "DB_CLOSE_DELAY=-1;INIT=runscript from 'src/test/resources/h2_init.sql'";
    public static final String H2_CONTEXT = "thread";
    public static final String H2_AUTO = "validate";
    public static final long LAST_ID_USERS = 3;
    public static final long LAST_ID_TAGS = 9;
    public static final long LAST_ID_TASKS = 5;

    private H2Env() {
    }
}
