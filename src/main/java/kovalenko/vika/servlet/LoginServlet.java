package kovalenko.vika.servlet;

import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.ValidationException;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static kovalenko.vika.enums.JSP.INDEX_JSP;
import static kovalenko.vika.utils.constants.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.constants.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_TAGS;
import static kovalenko.vika.utils.constants.LinkConstant.LOGIN_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@WebServlet(name = "LoginServlet", value = LOGIN_LINK)
public class LoginServlet extends HttpServlet {
    private UserService userService;
    private TagService tagService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var servletContext = config.getServletContext();
        userService = (UserService) servletContext.getAttribute(USER_SERVICE);
        tagService = (TagService) servletContext.getAttribute(TAG_SERVICE);

        log.debug("'LoginServlet' initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToIndex(req, resp);
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
            log.warn("The user has not been validated. Reason: {}", ex.getMessage());

            forwardToIndex(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute(USER_ATTR, userDTO);
        session.setAttribute(USERNAME, username);
        session.setAttribute(USER_TAGS, tagService.getUserTags(userService.getUserId(username)));

        resp.sendRedirect(TODO_LINK);

        log.info("User '{}' is authorized", username);
    }

    private void forwardToIndex(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(INDEX_JSP.getValue())
                .forward(req, resp);
    }
}
