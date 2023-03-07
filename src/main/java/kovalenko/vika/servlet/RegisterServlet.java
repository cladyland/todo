package kovalenko.vika.servlet;

import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kovalenko.vika.enums.JSP.REGISTER;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var context = config.getServletContext();
        userService = (UserService) context.getAttribute("userService");
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
        UserDTO userDTO = userService.register(command);
        req.getSession().setAttribute("user", userDTO);
        resp.sendRedirect("/todo");
    }

    private UserCommand buildUserCommand(HttpServletRequest req){
        return UserCommand.builder()
                .firstName(req.getParameter("firstName"))
                .lastName(req.getParameter("lastName"))
                .username(req.getParameter("username"))
                .password(req.getParameter("password"))
                .build();
    }
}
