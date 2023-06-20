package kovalenko.vika.listener;

import kovalenko.vika.config.DBConfig;
import kovalenko.vika.dao.CommentDAO;
import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.utils.Hashing;
import kovalenko.vika.dao.impl.CommentDAOImp;
import kovalenko.vika.dao.impl.TagDAOImp;
import kovalenko.vika.dao.impl.TaskDAOImp;
import kovalenko.vika.dao.impl.UserDAOImp;
import kovalenko.vika.service.CommentService;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.impl.CommentServiceImp;
import kovalenko.vika.service.impl.TagServiceImp;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.service.impl.TaskServiceImp;
import kovalenko.vika.service.UserService;
import kovalenko.vika.service.impl.UserServiceImp;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;

@Slf4j
@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        log.debug("'Servlet context' initialization begins...");

        var dbConfig = new DBConfig();
        var sessionFactory = dbConfig.getSessionFactory();

        var hashing = new Hashing();
        UserDAO userDAO = new UserDAOImp(sessionFactory);
        TaskDAO taskDAO = new TaskDAOImp(sessionFactory);
        TagDAO tagDAO = new TagDAOImp(sessionFactory);
        CommentDAO commentDAO = new CommentDAOImp(sessionFactory);

        UserService userService = new UserServiceImp(userDAO, hashing);
        TaskService taskService = new TaskServiceImp(taskDAO, tagDAO);
        TagService tagService = new TagServiceImp(tagDAO);
        CommentService commentService = new CommentServiceImp(commentDAO, taskDAO, userDAO);

        var servletContext = sce.getServletContext();
        servletContext.setAttribute(USER_SERVICE, userService);
        servletContext.setAttribute(TASK_SERVICE, taskService);
        servletContext.setAttribute(TAG_SERVICE, tagService);
        servletContext.setAttribute(COMMENT_SERVICE, commentService);

        log.debug("'Servlet context' initialized successfully");
    }
}
