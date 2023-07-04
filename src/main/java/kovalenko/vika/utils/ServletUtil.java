package kovalenko.vika.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.enums.TaskPriority;
import kovalenko.vika.enums.TaskStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;
import static kovalenko.vika.utils.constants.AttributeConstant.ERROR_MESSAGE;
import static kovalenko.vika.utils.constants.AttributeConstant.PRIORITIES;
import static kovalenko.vika.utils.constants.AttributeConstant.STATUSES;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK;
import static kovalenko.vika.utils.constants.AttributeConstant.TASK_TAGS;

public class ServletUtil {

    public static <T> String convertToJson(T object) throws JsonProcessingException {
        ObjectWriter mapper = new ObjectMapper().writer();
        return mapper.writeValueAsString(object);
    }

    public static boolean isGetRequest(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("GET");
    }

    public static void forwardWithErrorMessage(HttpServletRequest request, HttpServletResponse response,
                                               String errorMessage, String requestDispatcher) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        request.setAttribute(ERROR_MESSAGE, errorMessage);

        request
                .getServletContext()
                .getRequestDispatcher(requestDispatcher)
                .forward(request, response);
    }

    public static void setRequestAttributesForUpdatingTask(HttpServletRequest request, TaskDTO task) {
        request.setAttribute(TASK, task);
        request.setAttribute(PRIORITIES, TaskPriority.getAllPriorities());
        request.setAttribute(STATUSES, TaskStatus.getAllStatuses());
        request.setAttribute(TASK_TAGS, getTaskTagIds(task));
    }

    private static String getTaskTagIds(TaskDTO task) {
        List<TagDTO> taskTags = task.getTags();
        if (isNull(taskTags) || taskTags.isEmpty()) {
            return null;
        }
        StringBuilder taskTagIds = new StringBuilder();

        taskTags
                .forEach(tag -> taskTagIds.append(tag.getId()).append(" "));

        int indexOfLastSpace = taskTagIds.length() - 1;
        taskTagIds.deleteCharAt(indexOfLastSpace);

        return taskTagIds.toString();
    }
}
