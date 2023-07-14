package kovalenko.vika.dao;

import kovalenko.vika.model.Comment;
import kovalenko.vika.model.Tag;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;

import java.util.Properties;

import static kovalenko.vika.dao.H2Env.H2_CONTEXT;
import static kovalenko.vika.dao.H2Env.H2_DIALECT;
import static kovalenko.vika.dao.H2Env.H2_DRIVER;
import static kovalenko.vika.dao.H2Env.H2_AUTO;
import static kovalenko.vika.dao.H2Env.H2_URL;
import static kovalenko.vika.dao.H2Env.H2_URL_CONFIG;
import static kovalenko.vika.utils.constants.DbEnvConstant.DB_URL;
import static kovalenko.vika.utils.constants.DbEnvConstant.HBM2DDL_AUTO;
import static kovalenko.vika.utils.constants.DbEnvConstant.SESSION_CONTEXT;
import static kovalenko.vika.utils.constants.DbEnvConstant.SQL_DIALECT;
import static kovalenko.vika.utils.constants.DbEnvConstant.SQL_DRIVER;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractDAOTest {
    protected SessionFactory factory;
    protected Session session;

    @SneakyThrows
    protected AbstractDAOTest() {
        this.factory = buildSessionFactory();
        this.session = factory.getCurrentSession();
    }

    protected abstract void init();

    private SessionFactory buildSessionFactory() throws Exception {
        return new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(Comment.class)
                .setProperties(setEnvAndProperties())
                .buildSessionFactory();
    }

    private Properties setEnvAndProperties() throws Exception {
        return new EnvironmentVariables()
                .set(SQL_DIALECT, H2_DIALECT)
                .set(SQL_DRIVER, H2_DRIVER)
                .set(DB_URL, H2_URL + H2_URL_CONFIG)
                .set(SESSION_CONTEXT, H2_CONTEXT)
                .set(HBM2DDL_AUTO, H2_AUTO)
                .execute(() -> new Properties() {
                    {
                        setProperty(Environment.DIALECT, System.getenv(SQL_DIALECT));
                        setProperty(Environment.DRIVER, System.getenv(SQL_DRIVER));
                        setProperty(Environment.URL, System.getenv(DB_URL));
                        setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, System.getenv(SESSION_CONTEXT));
                        setProperty(Environment.HBM2DDL_AUTO, System.getenv(HBM2DDL_AUTO));
                    }
                });
    }
}
