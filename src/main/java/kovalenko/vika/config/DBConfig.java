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

import static kovalenko.vika.utils.constants.DbEnvConstant.*;

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
        var properties = new Properties() {{
            setProperty(Environment.DIALECT, System.getenv(SQL_DIALECT));
            setProperty(Environment.DRIVER, System.getenv(SQL_DRIVER));
            setProperty(Environment.URL, System.getenv(DB_URL));
            setProperty(Environment.USER, System.getenv(DB_USERNAME));
            setProperty(Environment.PASS, System.getenv(DB_PASSWORD));
            setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, System.getenv(SESSION_CONTEXT));
            setProperty(Environment.SHOW_SQL, System.getenv(SHOW_SQL));
            setProperty(Environment.FORMAT_SQL, System.getenv(FORMAT_SQL));
            setProperty(Environment.HBM2DDL_AUTO, System.getenv(HBM2DDL_AUTO));
        }};

        log.debug("DB properties are set: {}", properties);

        return properties;
    }
}
