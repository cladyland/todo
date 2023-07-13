package kovalenko.vika.servlet;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractServletTest {
    @Mock
    protected ServletConfig config;
    @Mock
    protected ServletContext context;
    @Mock
    protected HttpServletRequest request;
    @Mock
    protected HttpServletResponse response;
    @Mock
    protected RequestDispatcher dispatcher;
    @Mock
    protected HttpSession session;

    protected void init(HttpServlet servlet) throws ServletException {
        when(config.getServletContext()).thenReturn(context);
        servlet.init(config);
    }

    protected void whenDispatcher() {
        when(request.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    protected void whenSession() {
        when(request.getSession()).thenReturn(session);
    }

    protected void verifyForward(String jsp) throws ServletException, IOException {
        verify(context, times(1)).getRequestDispatcher(jsp);
        verify(dispatcher, times(1)).forward(request, response);
    }

    protected void verifyRedirect(String link) throws IOException {
        verify(response, times(1)).sendRedirect(link);
    }
}
