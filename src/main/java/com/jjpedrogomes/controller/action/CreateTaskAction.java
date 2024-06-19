package com.jjpedrogomes.controller.action;

import com.jjpedrogomes.controller.util.GsonUtil;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.task.TaskDaoImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CreateTaskAction implements Action {

    private static final Logger logger = LogManager.getLogger(CreateTaskAction.class);
    private final TaskDaoImpl taskDao;

    public CreateTaskAction(TaskDaoImpl taskDao) {
        this.taskDao = taskDao;
    }


    /**
     * Executes the use case to create a new task.
     * and add it to the task list from the session request
     *
     * @param request  The HttpServletRequest containing task information.
     * @param response The HttpServletResponse for providing feedback.
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDate = request.getParameter("dueDate");

        logger.info("Creating task...");
        Task task = createTask(title, description, dueDate);
        taskDao.save(task);
        addTaskToSession(request, task);

        GsonUtil.convertObjectToJson(response, task);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    private Task createTask(String title, String description, String dueDateParam) {
        LocalDate dueDate = null;
        if (title != null) {
            try {
                dueDate = LocalDate.parse(dueDateParam);
            } catch (DateTimeParseException exception) {
                logger.error("Due data is not valid format", exception);
                throw exception;
            }
            return new Task(title, description, dueDate);
        } else {
            IllegalArgumentException exception = new IllegalArgumentException();
            logger.error("Task title cannot be null", exception);
            throw exception;
        }
    }

    private void addTaskToSession(HttpServletRequest request, Task task) {
        try {
            HttpSession session = request.getSession();
            List<Task> taskList = null;
            Object sessionList = session.getAttribute("taskList");
            if (sessionList instanceof List && !((List) sessionList).isEmpty()) {
                taskList = new ArrayList<Task>((ArrayList<Task>)sessionList);
            } else {
                taskList = new ArrayList<Task>();
            }
            taskList.add(task);
            logger.info("Adding Task to List session");
            session.setAttribute("taskList", taskList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
