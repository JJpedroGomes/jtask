package com.jjpedrogomes.model.task;

import javax.persistence.EntityManager;

import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.task.TaskDaoImpl;

public class TaskDaoFactory {
	
	public static TaskDao getInstance() {
		try {
			EntityManager entityManager = JpaUtil.getEntityManager();
			return new TaskDaoImpl(entityManager);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static TaskDao getInstance(EntityManager entityManager) {
		try {
			return new TaskDaoImpl(entityManager);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
