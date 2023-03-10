package kovalenko.vika.listener;

import kovalenko.vika.dao.CommentDAO;
import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.model.Comment;
import kovalenko.vika.utils.Hashing;
import kovalenko.vika.dao.impl.CommentDAOImp;
import kovalenko.vika.dao.impl.TagDAOImp;
import kovalenko.vika.dao.impl.TaskDAOImp;
import kovalenko.vika.dao.impl.UserDAOImp;
import kovalenko.vika.model.Tag;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import kovalenko.vika.service.CommentService;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.impl.CommentServiceImp;
import kovalenko.vika.service.impl.TagServiceImp;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.service.impl.TaskServiceImp;
import kovalenko.vika.service.UserService;
import kovalenko.vika.service.impl.UserServiceImp;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static kovalenko.vika.utils.AttributeConstant.COMMENT_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.TASK_SERVICE;
import static kovalenko.vika.utils.AttributeConstant.USER_SERVICE;


@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(AppContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("'Servlet context' initialization begins...");

        SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(Comment.class)
                .buildSessionFactory();

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

        LOG.info("'Servlet context' initialized successfully");
    }
}
