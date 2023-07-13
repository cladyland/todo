package kovalenko.vika.servlet.task;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.exception.TaskException;
import kovalenko.vika.service.TagService;
import kovalenko.vika.service.UserService;
import kovalenko.vika.servlet.AbstractServletTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.ArrayList;

import static kovalenko.vika.enums.JSP.NEW_TAG;
import static kovalenko.vika.utils.constants.AttributeConstant.TAG_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_SERVICE;
import static kovalenko.vika.utils.constants.AttributeConstant.USER_TAGS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServletTest extends AbstractServletTest {
    final String TEST = "tag-test";
    final long ID = 1L;
    @Mock
    TagService tagService;
    @Mock
    UserService userService;
    TagServlet servlet;

    @BeforeEach
    void init() throws ServletException {
        when(context.getAttribute(TAG_SERVICE)).thenReturn(tagService);
        when(context.getAttribute(USER_SERVICE)).thenReturn(userService);

        servlet = new TagServlet();
        super.init(servlet);
    }

    @Test
    void get_forward_to_new_tag() throws ServletException, IOException {
        whenDispatcher();
        servlet.doGet(request, response);
        verifyForward(NEW_TAG.getValue());
    }

    @Test
    void do_post_save_tag_and_forward_to_new_tag() throws ServletException, IOException {
        setDoPostMockConditions();

        servlet.doPost(request, response);

        verify(tagService, times(1)).createTag(any(TagCommand.class));
        verify(tagService, times(1)).getUserTags(ID);
        verify(session, times(1)).setAttribute(USER_TAGS, new ArrayList<>());
        verifyForward(NEW_TAG.getValue());
    }

    @Test
    void do_post_not_save_tag_if_task_exception() throws ServletException, IOException {
        setDoPostMockConditions();
        doThrow(TaskException.class).when(tagService).createTag(any(TagCommand.class));

        servlet.doPost(request, response);

        verify(tagService, times(1)).createTag(any(TagCommand.class));
        verify(tagService, never()).getUserTags(ID);
        verify(session, never()).setAttribute(USER_TAGS, new ArrayList<>());
        verifyForward(NEW_TAG.getValue());
    }

    private void setDoPostMockConditions() {
        whenSession();
        whenDispatcher();
        when(session.getAttribute(USERNAME)).thenReturn(TEST);
        when(userService.getUserId(TEST)).thenReturn(ID);
    }
}
