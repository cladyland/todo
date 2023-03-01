package kovalenko.vika;

import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.model.User;
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
                .buildSessionFactory();

        var userDAO = new UserDAO(sessionFactory);
        var userService = new UserService(userDAO);

        var servletContext = sce.getServletContext();
        servletContext.setAttribute("userService", userService);
    }
}
