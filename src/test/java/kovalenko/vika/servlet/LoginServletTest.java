package kovalenko.vika.servlet;

import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.ValidationException;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import static kovalenko.vika.enums.JSP.INDEX_JSP;
import static kovalenko.vika.utils.constants.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_TAGS;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginServletTest extends AbstractServletTest {
    final String TEST = "login-test";
    @Mock
    UserService userService;
    @Mock
    TagService tagService;
    LoginServlet servlet;

    @BeforeEach
    void init() throws ServletException {
        when(context.getAttribute(USER_SERVICE)).thenReturn(userService);
        when(context.getAttribute(TAG_SERVICE)).thenReturn(tagService);

        servlet = new LoginServlet();
        super.init(servlet);
    }

    @Test
    void do_get_forward_to_index() throws ServletException, IOException {
        whenDispatcher();
        servlet.doGet(request, response);
        verifyForward(INDEX_JSP.getValue());
    }

    @Test
    void do_post_redirect_to_todo_if_valid_user_data() throws ServletException, IOException {
        var user = Mockito.mock(UserDTO.class);
        var tags = new ArrayList<TagDTO>();

        whenSession();
        when(request.getParameter(USERNAME)).thenReturn(TEST);
        when(userService.validate(TEST, null)).thenReturn(user);
        when(tagService.getUserTags(anyLong())).thenReturn(tags);

        servlet.doPost(request, response);

        verify(session, times(1)).setAttribute(USER_ATTR, user);
        verify(session, times(1)).setAttribute(USERNAME, TEST);
        verify(session, times(1)).setAttribute(USER_TAGS, tags);
        verifyRedirect(TODO_LINK);
    }

    @Test
    void do_post_forward_to_index_if_invalid_user_data() throws ServletException, IOException {
        whenDispatcher();
        when(userService.validate(null, null)).thenThrow(ValidationException.class);

        servlet.doPost(request, response);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verifyForward(INDEX_JSP.getValue());
        verify(request, never()).getSession();
        verify(response, never()).sendRedirect(TODO_LINK);
    }
}
