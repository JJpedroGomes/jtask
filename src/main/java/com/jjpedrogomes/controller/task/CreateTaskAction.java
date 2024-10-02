package com.jjpedrogomes.controller.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.controller.util.GsonUtil;
import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.lane.LaneDaoFactory;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;

public class CreateTaskAction implements Action {

    private static final Logger logger = LogManager.getLogger(CreateTaskAction.class);
    private final TaskDao<Task> taskDao;
    private final LaneDao laneDao;

    public CreateTaskAction(TaskDao taskDao, LaneDao laneDao) {
        this.taskDao = taskDao;
        this.laneDao = laneDao;
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
        
        String laneIdParam = request.getParameter("laneId");
        if(laneIdParam == null) {
        	throw new IllegalArgumentException();
        }
        
        Task task = createTask(title, description, dueDate);
        
        laneDao.get(Long.valueOf(laneIdParam)).ifPresent(lane -> {
            logger.info("Creating task...");
            lane.addTaskLastToLane(task);
            
            laneDao.update(lane);
            GsonUtil.convertObjectToJson(response, new TaskDto(lane.getTasks().get(lane.getTasks().size() - 1)));
            response.setStatus(HttpServletResponse.SC_CREATED);
        });
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
}
