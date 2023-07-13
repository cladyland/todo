package kovalenko.vika.filter.security;

import kovalenko.vika.filter.AbstractFilterTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostContentFilterTest extends AbstractFilterTest {
    final String TEST_1 = "test1";
    PostContentFilter filter;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        filter = new PostContentFilter();
        super.init(filter);
    }

    @AfterEach
    void destroy() {
        filter.destroy();
    }

    @Test
    void chain_do_filter() throws ServletException, IOException {
        when((request).getMethod()).thenReturn("get");

        doFilter(filter);

        verify(request, never()).getParameterNames();
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter_when_post_request_without_html() throws ServletException, IOException {
        var params = Collections.enumeration(List.of(TEST_1));
        String expected = "my string";

        when((request).getMethod()).thenReturn("post");
        when(request.getParameterNames()).thenReturn(params);
        when(request.getParameter(TEST_1)).thenReturn(expected);

        doFilter(filter);

        verify(request, never()).setAttribute(TEST_1, expected);
        verifyChainDoFilter();
    }

    @Test
    void chain_do_filter_when_post_request_with_html() throws ServletException, IOException {
        var params = Collections.enumeration(List.of(TEST_1, "test2"));
        String expected = "&#60;i&#62;my string&#60;/i&#62;";

        when((request).getMethod()).thenReturn("post");
        when(request.getParameterNames()).thenReturn(params);
        when(request.getParameter(anyString())).thenReturn("<i>my string</i>");

        doFilter(filter);

        verify(request, times(1)).setAttribute(TEST_1, expected);
        verify(request, times(1)).setAttribute("test2", expected);
        verifyChainDoFilter();
    }
}
