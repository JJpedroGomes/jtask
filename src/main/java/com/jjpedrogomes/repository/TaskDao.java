package com.jjpedrogomes.repository;

import com.jjpedrogomes.model.task.Task;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class TaskDao implements Dao<Task> {

    private EntityManager entityManager;

    public TaskDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Task task) {
        this.entityManager.persist(task);
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
