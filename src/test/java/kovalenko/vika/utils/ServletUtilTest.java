package kovalenko.vika.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.dto.TaskDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServletUtilTest {
    @Mock
    HttpServletRequest request;

    @Test
    void convert_map_to_json() throws JsonProcessingException {
        var object = new HashMap<String, String>();
        object.put("1", "test1");
        object.put("2", "test2");
        object.put("3", "test3");

        String expected = "{\"1\":\"test1\",\"2\":\"test2\",\"3\":\"test3\"}";

        assertEquals(expected, ServletUtil.convertToJson(object));
    }

    @Test
    void check_if_get_request() {
        when(request.getMethod()).thenReturn("get");
        assertTrue(ServletUtil.isGetRequest(request));

        when(request.getMethod()).thenReturn("post");
        assertFalse(ServletUtil.isGetRequest(request));
    }

    @Test
    void check_if_post_request() {
        when(request.getMethod()).thenReturn("post");
        assertTrue(ServletUtil.isPostRequest(request));

        when(request.getMethod()).thenReturn("put");
        assertFalse(ServletUtil.isPostRequest(request));
    }

    @Test
    void parameter_content_changed() {
        String expected = "some text";
        when(request.getAttribute(anyString())).thenReturn(expected);

        String actual = ServletUtil.checkIfParameterContentChanged(request, "changed");

        assertNotNull(request.getAttribute(anyString()));
        assertEquals(expected, actual);
    }

    @Test
    void parameter_content_not_changed() {
        String expected = "test";
        when(request.getAttribute(anyString())).thenReturn(null);
        when(request.getParameter(anyString())).thenReturn(expected);

        String actual = ServletUtil.checkIfParameterContentChanged(request, "not changed");

        assertNull(request.getAttribute(anyString()));
        assertEquals(expected, actual);
    }

    @Test
    void forward_with_error_message_and_code_400() throws ServletException, IOException {
        var response = Mockito.mock(HttpServletResponse.class);
        var servletContext = Mockito.mock(ServletContext.class);
        var dispatcher = Mockito.mock(RequestDispatcher.class);

        String errorMessage = "something went wrong";
        String requestDispatcher = "/dispatcher.jsp";

        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRequestDispatcher(requestDispatcher)).thenReturn(dispatcher);

        ServletUtil.forwardWithErrorMessageAndStatus400(request, response, errorMessage, requestDispatcher);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(request, times(1)).setAttribute(anyString(), eq(errorMessage));
        verify(dispatcher, times(1)).forward(request, response);
    }

    @Test
    void set_request_attributes_for_updating_tasks() {
        var task = Mockito.mock(TaskDTO.class);

        when(task.getTags()).thenReturn(List.of(new TagDTO()));
        ServletUtil.setRequestAttributesForUpdatingTask(request, task);

        when(task.getTags()).thenReturn(null);
        ServletUtil.setRequestAttributesForUpdatingTask(request, task);

        verify(request, times(8)).setAttribute(anyString(), any());
    }
}
