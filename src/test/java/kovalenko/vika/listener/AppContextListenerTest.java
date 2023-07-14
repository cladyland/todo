package kovalenko.vika.listener;

import kovalenko.vika.service.impl.CommentServiceImp;
import kovalenko.vika.service.impl.RegisterServiceImp;
import kovalenko.vika.service.impl.TagServiceImp;
import kovalenko.vika.service.impl.TaskServiceImp;
import kovalenko.vika.service.impl.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static kovalenko.vika.dao.H2Env.H2_CONTEXT;
import static kovalenko.vika.dao.H2Env.H2_DIALECT;
import static kovalenko.vika.dao.H2Env.H2_DRIVER;
import static kovalenko.vika.dao.H2Env.H2_URL;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.REGISTER_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.constants.DbEnvConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppContextListenerTest {
    @Mock
    ServletContextEvent event;
    @Mock
    ServletContext context;
    AppContextListener listener;

    @BeforeEach
    void init() {
        listener = new AppContextListener();
    }

    @Test
    void context_initialize_with_hibernate_properties() throws Exception {
        when(event.getServletContext()).thenReturn(context);

        new EnvironmentVariables().set(SQL_DIALECT, H2_DIALECT)
                .set(SQL_DRIVER, H2_DRIVER)
                .set(DB_URL, H2_URL)
                .set(DB_USERNAME, "")
                .set(DB_PASSWORD, "")
                .set(SESSION_CONTEXT, H2_CONTEXT)
                .set(SHOW_SQL, "true")
                .set(FORMAT_SQL, "true")
                .set(HBM2DDL_AUTO, "none")
                .execute(() -> listener.contextInitialized(event));

        verify(context, times(1)).setAttribute(eq(REGISTER_SERVICE), any(RegisterServiceImp.class));
        verify(context, times(1)).setAttribute(eq(USER_SERVICE), any(UserServiceImp.class));
        verify(context, times(1)).setAttribute(eq(TASK_SERVICE), any(TaskServiceImp.class));
        verify(context, times(1)).setAttribute(eq(TAG_SERVICE), any(TagServiceImp.class));
        verify(context, times(1)).setAttribute(eq(COMMENT_SERVICE), any(CommentServiceImp.class));
    }
}
