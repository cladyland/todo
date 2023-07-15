package kovalenko.vika.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.RegisterException;
import kovalenko.vika.service.RegisterService;
import kovalenko.vika.utils.ServletUtil;
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
import java.util.Map;

import static java.util.Objects.nonNull;
import static kovalenko.vika.enums.JSP.REGISTER;
import static kovalenko.vika.utils.constants.AttributeConstant.ERRORS_MAP;
import static kovalenko.vika.utils.constants.AttributeConstant.ERROR_MESSAGE;
import static kovalenko.vika.utils.constants.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.LAST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.PARAMETERS;
import static kovalenko.vika.utils.constants.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.constants.AttributeConstant.REGISTER_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.constants.LinkConstant.REGISTER_LINK;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;

@Slf4j
@WebServlet(name = "RegisterServlet", value = REGISTER_LINK)
public class RegisterServlet extends HttpServlet {
    private final static String REGISTER_FAIL = "Failed to register user. Reason: '{}'";
    private RegisterService registerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        registerService = (RegisterService) context.getAttribute(REGISTER_SERVICE);

        log.debug("'RegisterServlet' initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardToRegisterPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var params = (HashMap<String, String>) req.getAttribute(PARAMETERS);
        UserCommand command = buildUserCommand(params);

        UserDTO userDTO;
        try {
            userDTO = registerService.register(command);
        } catch (RegisterException ex) {
            req.setAttribute(USERNAME, ServletUtil.checkIfParameterContentChanged(req, USERNAME));
            req.setAttribute(FIRST_NAME, ServletUtil.checkIfParameterContentChanged(req, FIRST_NAME));
            req.setAttribute(LAST_NAME, ServletUtil.checkIfParameterContentChanged(req, LAST_NAME));

            setRegisterFailAttribute(ex, req, resp);

            forwardToRegisterPage(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute(USER_ATTR, userDTO);
        session.setAttribute(USERNAME, userDTO.getUsername());

        resp.sendRedirect(TODO_LINK);

        log.info("User '{}' successfully registered", command.getUsername());
    }

    private void forwardToRegisterPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(REGISTER.getValue())
                .forward(req, resp);
    }

    private UserCommand buildUserCommand(HashMap<String, String> params) {
        return UserCommand.builder()
                .firstName(params.get(FIRST_NAME))
                .lastName(params.get(LAST_NAME))
                .username(params.get(USERNAME))
                .password(params.get(PASSWORD))
                .build();
    }

    private void setRegisterFailAttribute(RegisterException ex, HttpServletRequest req, HttpServletResponse resp) throws JsonProcessingException {
        Map<String, String> errors = ex.getErrors();
        if (nonNull(errors)) {
            String json = ServletUtil.convertToJson(errors);
            req.setAttribute(ERRORS_MAP, json);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.warn(REGISTER_FAIL, errors);
        } else {
            String message = ex.getMessage();
            req.setAttribute(ERROR_MESSAGE, message);
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            log.warn(REGISTER_FAIL, message);
        }
    }
}
