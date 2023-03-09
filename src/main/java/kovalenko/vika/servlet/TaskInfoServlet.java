package kovalenko.vika.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kovalenko.vika.enums.JSP.TASK_INFO;
import static kovalenko.vika.utils.LinkConstant.TODO_INFO_LINK;

@WebServlet(name = "TaskInfoServlet", value = TODO_INFO_LINK)
public class TaskInfoServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req
                .getServletContext()
                .getRequestDispatcher(TASK_INFO.getValue())
                .forward(req, resp);
    }
}
