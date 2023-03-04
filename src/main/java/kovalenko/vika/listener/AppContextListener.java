package kovalenko.vika.listener;

import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.model.Tag;
import kovalenko.vika.model.Task;
import kovalenko.vika.model.User;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.TaskService;
import kovalenko.vika.service.UserService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(Tag.class)
                .buildSessionFactory();

        var userDAO = new UserDAO(sessionFactory);
        var taskDAO = new TaskDAO(sessionFactory);
        var tagDAO = new TagDAO(sessionFactory);

        var userService = new UserService(userDAO);
        var taskService = new TaskService(taskDAO, userDAO);
        var tagService = new TagService(tagDAO);

        var servletContext = sce.getServletContext();
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("taskService", taskService);
    }
}
