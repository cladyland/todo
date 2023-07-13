package kovalenko.vika.servlet.task;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.service.CommentService;
import kovalenko.vika.servlet.AbstractServletTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.servlet.ServletException;

import java.io.IOException;

import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_ADDED_TO_TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.COMMENT_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_ID;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.LinkConstant.TASK_INFO_LINK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServletTest extends AbstractServletTest {
    @Mock
    CommentService service;
    CommentServlet servlet;

    @BeforeEach
    void init() throws ServletException {
        when(context.getAttribute(COMMENT_SERVICE)).thenReturn(service);
        servlet = new CommentServlet();
        super.init(servlet);
    }

    @Test
    void do_post_save_comment_and_send_redirect() throws ServletException, IOException {
        whenSession();
        when(request.getParameter(TASK_ID)).thenReturn("12");
        when(session.getAttribute(USERNAME)).thenReturn("user-test");

        servlet.doPost(request, response);

        verify(service, times(1)).createComment(any(CommentCommand.class));
        verify(session, times(1)).setAttribute(COMMENT_ADDED_TO_TASK, 12L);
        verifyRedirect(TASK_INFO_LINK);
    }
}
