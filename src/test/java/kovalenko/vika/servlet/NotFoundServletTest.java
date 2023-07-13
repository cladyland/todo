package kovalenko.vika.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kovalenko.vika.enums.JSP.NOT_FOUND_JSP;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NotFoundServletTest extends AbstractServletTest {
    NotFoundServlet servlet;

    @BeforeEach
    void init() {
        servlet = new NotFoundServlet();
    }

    @Test
    void do_get_forward_to_not_found() throws ServletException, IOException {
        whenDispatcher();
        servlet.doGet(request, response);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verifyForward(NOT_FOUND_JSP.getValue());
    }
}
