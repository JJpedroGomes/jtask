package com.jjpedrogomes.controller.action;

import com.jjpedrogomes.controller.shared.Action;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CreateTaskAction implements Action {

    private static final Logger logger = LogManager.getLogger(CreateTaskAction.class);
    private final TaskDao taskDao;

    public CreateTaskAction(TaskDao taskDao) {
        this.taskDao = taskDao;
    }


    /**
     * Executes the use case to create a new task.
     *
     * @param request  The HttpServletRequest containing task information.
     * @param response The HttpServletResponse for providing feedback.
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        validateTitle(title);
        String description = request.getParameter("description");
        String dueDateParam = request.getParameter("dueDate");

        LocalDate dueDate = null;
        if (dueDateParam != null) {
            dueDate = parseDueDate(dueDateParam);
        }
        Task task = new Task(title, description, dueDate);
        logger.info("Creating task...");
        taskDao.save(task);
        addToList(request, task);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    private void addToList(HttpServletRequest request, Task task) {
        try {
            List<Task> taskList = (List<Task>) request.getSession().getAttribute("taskList");
            if (taskList == null) {
                taskList = new ArrayList<Task>();
            }
            taskList.add(task);
            logger.info("Adding Task to List");

            request.getSession().setAttribute("taskList", taskList);
            request.setAttribute("taskList", taskList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void validateTitle(String title) {
        if (title == null) {
            IllegalArgumentException exception = new IllegalArgumentException();
            logger.error("Task title cannot be null", exception);
            throw exception;
        }
    }

    private LocalDate parseDueDate(String dueDateParam) {
        try {
            return LocalDate.parse(dueDateParam);
        } catch (DateTimeParseException exception) {
            logger.error("Due data is not valid format", exception);
            throw exception;
        }
    }
}
