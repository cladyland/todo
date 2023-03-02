package kovalenko.vika;

import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static kovalenko.vika.JSP.INDEX;

@WebServlet(name = "LoginServlet", value = "/")
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        var servletContext = config.getServletContext();
        userService = (UserService) servletContext.getAttribute("userService");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(INDEX.getValue())
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserDTO userDTO;

        try {
            userDTO = userService.validate(username, password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        req.getSession().setAttribute("user", userDTO);
        resp.sendRedirect("/todo");
    }
}
