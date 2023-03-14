package kovalenko.vika.servlet;

import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.RegisterException;
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
import java.util.HashMap;

import static kovalenko.vika.enums.JSP.REGISTER;
import static kovalenko.vika.utils.AttributeConstant.ERROR_MESSAGE;
import static kovalenko.vika.utils.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.AttributeConstant.LAST_NAME;
import static kovalenko.vika.utils.AttributeConstant.PARAMETERS;
import static kovalenko.vika.utils.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.LinkConstant.REGISTER_LINK;
import static kovalenko.vika.utils.LinkConstant.TODO_LINK;

@WebServlet(name = "RegisterServlet", value = REGISTER_LINK)
public class RegisterServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterServlet.class);
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        userService = (UserService) context.getAttribute(USER_SERVICE);

        LOG.debug("'RegisterServlet' initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(REGISTER.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var params = (HashMap<String, String>) req.getAttribute(PARAMETERS);
        UserCommand command = buildUserCommand(params);

        UserDTO userDTO;
        try {
            userDTO = userService.register(command);
        } catch (RegisterException ex) {
            req.setAttribute(ERROR_MESSAGE, ex.getMessage());
            req.setAttribute(USERNAME, command.getUsername());
            req.setAttribute(FIRST_NAME, command.getFirstName());
            req.setAttribute(LAST_NAME, command.getLastName());

            LOG.warn("Failed to register user. Reason: '{}'", ex.getMessage());

            resp.setStatus(422);
            req
                    .getServletContext()
                    .getRequestDispatcher(REGISTER.getValue())
                    .forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute(USER_ATTR, userDTO);
        session.setAttribute(USERNAME, userDTO.getUsername());
        resp.sendRedirect(TODO_LINK);

        LOG.info("User '{}' successfully registered", command.getUsername());
    }

    private UserCommand buildUserCommand(HashMap<String, String> params) {
        return UserCommand.builder()
                .firstName(params.get(FIRST_NAME))
                .lastName(params.get(LAST_NAME))
                .username(params.get(USERNAME))
                .password(params.get(PASSWORD))
                .build();
    }
}
