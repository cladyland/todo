package kovalenko.vika.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kovalenko.vika.enums.JSP.TASK_INFO;
import static kovalenko.vika.utils.LinkConstant.TASK_INFO_LINK;

@WebServlet(name = "TaskInfoServlet", value = TASK_INFO_LINK)
public class TaskInfoServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TaskInfoServlet.class);

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
