package kovalenko.vika.servlet;

import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.RegisterException;
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
import java.util.HashMap;

import static kovalenko.vika.enums.JSP.REGISTER;
import static kovalenko.vika.utils.constants.AttributeConstant.ERROR_MESSAGE;
import static kovalenko.vika.utils.constants.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.LAST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.PARAMETERS;
import static kovalenko.vika.utils.constants.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.constants.LinkConstant.REGISTER_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@WebServlet(name = "RegisterServlet", value = REGISTER_LINK)
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        userService = (UserService) context.getAttribute(USER_SERVICE);

        log.debug("'RegisterServlet' initialized");
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

            log.warn("Failed to register user. Reason: '{}'", ex.getMessage());

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

        log.info("User '{}' successfully registered", command.getUsername());
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
