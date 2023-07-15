package kovalenko.vika.servlet;

import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.RegisterException;
import kovalenko.vika.service.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.HashMap;

import static kovalenko.vika.enums.JSP.REGISTER;
import static kovalenko.vika.utils.constants.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.LAST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.PARAMETERS;
import static kovalenko.vika.utils.constants.AttributeConstant.REGISTER_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_ATTR;
import static kovalenko.vika.utils.constants.LinkConstant.TODO_LINK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterServletTest extends AbstractServletTest {
    final String TEST = "register-test";
    @Mock
    RegisterService registerService;
    RegisterServlet servlet;

    @Override
    @BeforeEach
    protected void init() throws ServletException {
        when(context.getAttribute(REGISTER_SERVICE)).thenReturn(registerService);
        servlet = new RegisterServlet();
        super.init(servlet);
    }

    @Test
    void do_get_forward_to_register() throws ServletException, IOException {
        whenDispatcher();
        servlet.doGet(request, response);
        verifyForward(REGISTER.getValue());
    }

    @Test
    void do_post_register_and_redirect_to_todo() throws ServletException, IOException {
        var user = Mockito.mock(UserDTO.class);

        whenSession();
        when(request.getAttribute(PARAMETERS)).thenReturn(new HashMap<>());
        when(registerService.register(any(UserCommand.class))).thenReturn(user);

        servlet.doPost(request, response);

        verify(session, times(1)).setAttribute(USER_ATTR, user);
        verify(session, times(1)).setAttribute(USERNAME, user.getUsername());
        verifyRedirect(TODO_LINK);
    }

    @Test
    void do_post_not_register_with_invalid_data_and_forward_to_register_page() throws ServletException, IOException {
        whenDispatcher();
        when(request.getAttribute(PARAMETERS)).thenReturn(new HashMap<>());
        when(registerService.register(any(UserCommand.class))).thenThrow(new RegisterException(new HashMap<>()));
        when(request.getAttribute(USERNAME)).thenReturn(TEST);
        when(request.getAttribute(FIRST_NAME)).thenReturn(TEST);
        when(request.getAttribute(LAST_NAME)).thenReturn(TEST);

        servlet.doPost(request, response);

        verify(request, times(1)).setAttribute(USERNAME, TEST);
        verify(request, times(1)).setAttribute(FIRST_NAME, TEST);
        verify(request, times(1)).setAttribute(LAST_NAME, TEST);
        verifyForward(REGISTER.getValue());

        verify(request, never()).getSession();
        verify(response, never()).sendRedirect(TODO_LINK);
    }
}
