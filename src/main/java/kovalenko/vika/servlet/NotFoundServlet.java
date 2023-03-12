package kovalenko.vika.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kovalenko.vika.enums.JSP.NOT_FOUND_JSP;
import static kovalenko.vika.utils.LinkConstant.NOT_FOUND_LINK;

@WebServlet(name = "NotFoundServlet", value = NOT_FOUND_LINK)
public class NotFoundServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        req
                .getServletContext()
                .getRequestDispatcher(NOT_FOUND_JSP.getValue())
                .forward(req, resp);
    }
}
