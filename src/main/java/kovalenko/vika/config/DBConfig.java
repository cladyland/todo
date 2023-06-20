package kovalenko.vika.config;

import kovalenko.vika.model.Comment;
import kovalenko.vika.model.Tag;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

@Slf4j
@Getter
public class DBConfig {
    private SessionFactory sessionFactory;

    public DBConfig() {
        configureSessionFactory();
    }

    private void configureSessionFactory() {
        this.sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(Comment.class)
                .setProperties(setProperties())
                .buildSessionFactory();

        log.debug("Session factory initialized and built");
    }

    private Properties setProperties() {
        var properties = new Properties();
        properties.setProperty(Environment.DIALECT, System.getenv("sql_dialect"));
        properties.setProperty(Environment.DRIVER, System.getenv("sql_driver"));
        properties.setProperty(Environment.URL, System.getenv("db_url"));
        properties.setProperty(Environment.USER, System.getenv("db_username"));
        properties.setProperty(Environment.PASS, System.getenv("db_password"));
        properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, System.getenv("session_context"));
        properties.setProperty(Environment.SHOW_SQL, System.getenv("show_sql"));
        properties.setProperty(Environment.FORMAT_SQL, System.getenv("format_sql"));
        properties.setProperty(Environment.HBM2DDL_AUTO, System.getenv("hbm2ddl_auto"));

        log.debug("DB properties are set: {}", properties);

        return properties;
    }
}
