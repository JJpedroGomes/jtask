package com.jjpedrogomes.repository.lane;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneDao;

public class LaneDaoImpl implements LaneDao{
	
	private final EntityManager entityManager;
	private static final Logger logger = LogManager.getLogger(LaneDaoImpl.class);
	
	public LaneDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Optional<Lane> get(long id) {
		Lane lane = this.entityManager.find(Lane.class, id);
		return Optional.ofNullable(lane);
	}

	@Override
	public List<Lane> getAllFromUser(Long id) {
		try {
			String jpql = "SELECT l from Lane l WHERE l.user.id = :id";
			logger.info("Selecting all Lanes");
			
			TypedQuery<Lane> query = entityManager.createQuery(jpql, Lane.class);
			query.setParameter("id", id);
			
			return query.getResultList();
		} catch (RuntimeException e) {
			logger.error("Error while retrieving all lanes", e);
            return Collections.emptyList();
		}
	}

	@Override
	public void save(Lane lane) {
		EntityTransaction transaction = this.entityManager.getTransaction();
		if (!transaction.isActive()) {
			transaction.begin();
		}
		try {
			this.entityManager.persist(lane);
			transaction.commit();
			logger.info("Lane saved successfully.");
		} catch (RuntimeException e) {
			logger.error("Unexpected error saving lane: {}", e.getMessage());
			transaction.rollback();
			throw new PersistenceException(e.getMessage());
		}
	}

	@Override
	public Lane update(Lane lane) {
		EntityTransaction transaction = this.entityManager.getTransaction();
		if (!transaction.isActive()) {
			transaction.begin();
		}
		try {
			Lane managedLane = this.entityManager.merge(lane);
			transaction.commit();
			logger.info("Lane updated successfully.");
			return managedLane;
		} catch (RuntimeException e) {
			logger.error("Unexpected error saving lane: {}", e.getMessage());
			transaction.rollback();
			throw new PersistenceException(e.getMessage());
		}
	}

	@Override
	public void delete(Lane lane) {
		// Begin transaction if not active
	    EntityTransaction transaction = this.entityManager.getTransaction();
	    if (!transaction.isActive()) {
	        transaction.begin();
	    }
	    // Merge the lane to ensure it is managed
	    Lane managedLane = this.entityManager.merge(lane);
	    managedLane.clearTasks();
	    // Remove the managed entity
	    this.entityManager.remove(managedLane);
	    // Commit the transaction
	    transaction.commit(); // Commit here to ensure changes are saved
	    logger.info("Lane deleted successfully.");
	}
	

}
