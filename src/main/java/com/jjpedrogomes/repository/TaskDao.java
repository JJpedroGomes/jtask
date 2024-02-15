package com.jjpedrogomes.repository;

import com.jjpedrogomes.model.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class TaskDao implements Dao<Task> {

    private final EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(TaskDao.class);

    public TaskDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Task task) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            this.entityManager.persist(task);
            transaction.commit();
            logger.info("Task created successfully.");
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) {
                logger.error("Error occurred: ", exception);
                transaction.rollback();
            }
        }
    }

    @Override
    public Optional<Task> get(long id) {
        return Optional.ofNullable(entityManager.find(Task.class, id));
    }

    @Override
    public List<Task> getAll() {
        return null;
    }

    @Override
    public void update(Task task, String[] params) {

    }

    @Override
    public void delete(Task task) {
        task = entityManager.merge(task);
        this.entityManager.remove(task);
    }
}
