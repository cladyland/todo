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

import static kovalenko.vika.enums.JSP.REGISTER;
import static kovalenko.vika.utils.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.AttributeConstant.LAST_NAME;
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
        UserCommand command = buildUserCommand(req);
        UserDTO userDTO;
        try {
            userDTO = userService.register(command);
        } catch (RegisterException ex){
            req.setAttribute("error", ex.getMessage());
            req.setAttribute(USERNAME, command.getUsername());
            req.setAttribute(FIRST_NAME, command.getFirstName());
            req.setAttribute(LAST_NAME, command.getLastName());

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
    }

    private UserCommand buildUserCommand(HttpServletRequest req){
        return UserCommand.builder()
                .firstName(req.getParameter(FIRST_NAME))
                .lastName(req.getParameter(LAST_NAME))
                .username(req.getParameter(USERNAME))
                .password(req.getParameter(PASSWORD))
                .build();
    }
}
