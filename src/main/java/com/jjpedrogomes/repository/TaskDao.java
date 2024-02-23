package com.jjpedrogomes.repository;

import com.jjpedrogomes.model.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// This class represents a Data Access Object (DAO) responsible for managing Task entities in the database.
public class TaskDao implements Dao<Task> {

    private final EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(TaskDao.class);
    public TaskDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Saves a task to the database.
     *
     * @param task The task to be saved.
     */
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

    /**
     * Updates the given Task entity in the database.
     *
     * @param task The Task entity to be updated.
     * @throws IllegalArgumentException if the given task is not managed by the entity manager.
     */
    @Override
    public void update(Task task) {
        EntityTransaction transaction = null;
        try {
            if (!entityManager.contains(task)) {
                throw new IllegalArgumentException("Task is not managed by the entity manager");
            }
            transaction = entityManager.getTransaction();
            transaction.begin();
            this.entityManager.merge(task);
            transaction.commit();
            logger.info("Task updated successfully.");
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error occurred while updating task", exception);
        }
    }

    @Override
    public Optional<Task> get(long id) {
        try {
            Task task = this.entityManager.find(Task.class, id);
            return Optional.ofNullable(task);
        } catch (Exception exception) {
            logger.error("Error while retrieving task with id: " + id, exception);
            return Optional.empty();
        }
    }

    /**
     * @return List of all tasks
     * @throws IllegalStateException if the task list is empty.
     */
    @Override
    public List<Task> getAll() {
        String query = "SELECT t from Task t";
        try {
            logger.info("Selecting all Tasks");
            List<Task> list = this.entityManager.createQuery(query, Task.class).getResultList();
            if (list.isEmpty()) throw new IllegalStateException("Task List is current empty");
            return list;
        } catch (Exception exception) {
            logger.error("Error while retrieving all tasks", exception);
            return Collections.emptyList();
        }
    }

    //Todo basic logic
    @Override
    public void delete(Task task) {
        task = entityManager.merge(task);
        this.entityManager.remove(task);
    }
}
