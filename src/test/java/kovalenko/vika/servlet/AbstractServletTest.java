package kovalenko.vika.servlet;

import kovalenko.vika.AbstractFilterServletTest;
import org.mockito.Mock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class AbstractServletTest extends AbstractFilterServletTest {
    @Mock
    protected ServletConfig config;
    @Mock
    protected ServletContext context;
    @Mock
    protected RequestDispatcher dispatcher;

    protected void init(HttpServlet servlet) throws ServletException {
        when(config.getServletContext()).thenReturn(context);
        servlet.init(config);
    }

    protected void whenDispatcher() {
        when(request.getServletContext()).thenReturn(context);
        when(context.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    protected void verifyForward(String jsp) throws ServletException, IOException {
        verify(context, times(1)).getRequestDispatcher(jsp);
        verify(dispatcher, times(1)).forward(request, response);
    }

    protected void verifyRedirect(String link) throws IOException {
        verify(response, times(1)).sendRedirect(link);
    }
}
