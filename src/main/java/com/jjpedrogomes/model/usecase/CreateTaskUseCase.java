package com.jjpedrogomes.model.usecase;

import com.jjpedrogomes.model.shared.UseCase;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.JpaUtil;
import com.jjpedrogomes.repository.TaskDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public class CreateTaskUseCase implements UseCase {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));
        Task task = new Task(title, description, dueDate);

        EntityManager entityManager = JpaUtil.getEntityManager();
        TaskDao taskDao = new TaskDao(entityManager);

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            taskDao.save(task);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
