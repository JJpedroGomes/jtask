package com.jjpedrogomes.model.usecase;

import com.jjpedrogomes.model.shared.UseCase;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.TaskDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class SetInProgressTaskUseCase implements UseCase {

    private static final Logger logger = LogManager.getLogger(CreateTaskUseCase.class);
    private final TaskDao taskDao;

    public SetInProgressTaskUseCase(TaskDao  taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String idParam = request.getParameter("id");
        try {
            Long id = Long.parseLong(idParam);
            logger.info("Getting task with id: " + id);
            Optional<Task> taskOptional = taskDao.get(id);
            taskOptional.ifPresent(task -> {
                task.setTaskInProgress();
                taskDao.update(task);
            });
        } catch (NumberFormatException exception) {
            logger.error("Invalid id parameter: " + idParam, exception);
        }
    }
}
