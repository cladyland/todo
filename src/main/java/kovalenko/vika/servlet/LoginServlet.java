package kovalenko.vika.servlet;

import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.ValidationException;
import kovalenko.vika.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static kovalenko.vika.enums.JSP.INDEX_JSP;
import static kovalenko.vika.utils.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.LinkConstant.LOGIN_LINK;
import static kovalenko.vika.utils.LinkConstant.TODO_LINK;

@WebServlet(name = "LoginServlet", value = LOGIN_LINK)
public class LoginServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var servletContext = config.getServletContext();
        userService = (UserService) servletContext.getAttribute(USER_SERVICE);

        LOG.info("'LoginServlet' initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(INDEX_JSP.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);

        UserDTO userDTO;
        try {
            userDTO = userService.validate(username, password);
        } catch (ValidationException ex) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            LOG.warn("The user has not been validated. Reason: {}", ex.getMessage());

            req
                    .getServletContext()
                    .getRequestDispatcher(INDEX_JSP.getValue())
                    .forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute(USER_ATTR, userDTO);
        session.setAttribute(USERNAME, username);
        resp.sendRedirect(TODO_LINK);

        LOG.info("User '{}' is authorized", username);
    }
}