package com.jjpedrogomes.repository.lane;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.lane.LaneFactory;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;
import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.task.TaskDaoImpl;
import com.jjpedrogomes.repository.user.UserDaoImpl;
import com.jjpedrogomes.user.UserDaoTest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LaneDaoTest {
	
	private EntityManager entityManager;
	private User user;
	private LaneDao laneDao;
	
	@BeforeAll
	static void setUpBeforeAll() {
		User user = new User("Gabriel Toledo", new Email("lanedaotest@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
		UserDaoTest.persistUser(user);
	}
	
	@BeforeEach
	void setUpBeforeEach() {
		this.entityManager = JpaUtil.getEntityManager();
		this.laneDao = new LaneDaoImpl(entityManager);
		
		UserDaoImpl userDao = new UserDaoImpl(JpaUtil.getEntityManager());
		this.user = userDao.getUserByEmail("lanedaotest@email.com").get();
	}

	@Nested
	class lane_dao_save {
		
		@Test
		void should_create_new_user_sucessfully() {
			// Arrange
			Lane lane = LaneFactory.createLane("Todo", user);
			// Act
			laneDao.save(lane);
			// Assert
			assertThat(lane.getId()).isNotNull();
		}
		
		@Test
		void should_try_to_create_new_user_and_then_rollback() {
			Lane lane = LaneFactory.createLane("Todo", user);
			lane.setPosition(null);
			assertThrows(PersistenceException.class, () -> laneDao.save(lane));
		}
	}
	
	@Nested
	class lane_dao_get {
		
		@Test
		void should_return_lane_by_id() {
			// Arrange
			Lane lane = LaneFactory.createLane("Todo", user);
			saveNewLane(lane);
			// Act
			Lane laneFromDb = laneDao.get(lane.getId()).get();
			// Assert
			assertThat(laneFromDb).isEqualTo(lane);
		}
		
		@Test
		void should_return_all_lane_from_a_user() {
			// Arrange
			User user = new User("Gabriel Toledo", new Email("lanedaotest2@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			createAndPersistNewUser(user);
			
			List<Lane> laneList = new ArrayList<Lane>();
			Collections.addAll(laneList, LaneFactory.createLane("Todo", user), LaneFactory.createLane("Backlog", user));
			saveNewLanes(laneList);
			// Act
			List<Lane> allFromUser = laneDao.getAllFromUser(user.getId());
			// Assert
			assertThat(allFromUser).containsAll(laneList);
		}
		
		@Test
		void should_return_an_empty_list(){
			// Arrange
			EntityManager entityManager = mock(EntityManager.class);
			when(entityManager.createQuery(anyString(), eq(Lane.class)))
				.thenThrow(new RuntimeException("Simulated Error"));
			// Act
			List<Lane> allFromUser = laneDao.getAllFromUser(null);
			// Assert
			assertThat(allFromUser).isEmpty();
		}
	}
	
	@Nested
	class lane_dao_delete {
		
		@Test
		void should_delete_lane_and_all_its_tasks_sucessfully() {
			// Arrange
			Lane lane = LaneFactory.createLane("Todo", user);
			saveNewLane(lane);
			
			Task task1 = new Task("Task1", null, null);
			Task task2 = new Task("Task2", null, null);
			Task task3 = new Task("Task3", null, null);
			
			Lane laneFromDb = entityManager.find(Lane.class, lane.getId());
			
			List<Task> tasks = new ArrayList<Task>();
			Collections.addAll(tasks, task1, task2, task3);
			
			saveNewTasks(tasks);
			// Act
			laneDao.delete(laneFromDb);
			// Assert
			List<Task> tasksAfterDelete = getTasks(lane);
			
			assertThat(tasksAfterDelete).isEmpty();
			assertThat(laneDao.get(lane.getId())).isEmpty();
		}	
	}
	
	@Nested
	class lane_dao_update {
		
		@Test
		void should_update_lane_successfully() {
			// Arrange
			User user = new User("Rodrygo Goes", new Email("lanedaotestUpdate@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
			UserDaoTest.persistUser(user);
			
			Lane lane = LaneFactory.createLane("Todo", user);
			saveNewLane(lane);

			Lane lane2 = LaneFactory.createLane("Backlog", user);
			saveNewLane(lane2);
			
			Lane lane3 = LaneFactory.createLane("Pending", user);
			saveNewLane(lane3);
			
			Lane lane4 = LaneFactory.createLane("Urgent", user);
			saveNewLane(lane4);
			// Act
			lane2.setName("In Progress");
			lane2.switchLanePositionForUser(0);
			
			lane4.switchLanePositionForUser(2);

			UserDao userDao = new UserDaoImpl(entityManager);
			userDao.update(user);
			// Assert
			Lane laneFromDb = entityManager.find(Lane.class, lane.getId());
			Lane lane2FromDb = entityManager.find(Lane.class, lane2.getId());
			Lane lane4FromDb = entityManager.find(Lane.class, lane4.getId());
			assertTrue(lane2FromDb.getName().equals("In Progress"));
			assertTrue(lane2FromDb.getPosition().equals(0));
			assertTrue(laneFromDb.getPosition().equals(1));
			assertTrue(lane4FromDb.getPosition().equals(2));
		}
	}
	
	private void saveNewLane(Lane lane) {
		LaneDao laneDao = new LaneDaoImpl(JpaUtil.getEntityManager());
		laneDao.save(lane);
	}
	
	private void saveNewLanes(List<Lane> laneList) {
		LaneDao laneDao = new LaneDaoImpl(JpaUtil.getEntityManager());
		laneList.forEach(lane -> laneDao.save(lane));		
	}
	
	private void createAndPersistNewUser(User user) {
		UserDaoTest.persistUser(user);
	}
	
	private void saveNewTasks(List<Task> tasks) {
		tasks.forEach(task -> {
			EntityManager entityManager = JpaUtil.getEntityManager();
			TaskDao<Task> taskDao = new TaskDaoImpl(entityManager);
			
			entityManager.getTransaction().begin();
			taskDao.save(task);
			entityManager.getTransaction().commit();
		});
	}
	
	private List<Task> getTasks(Lane lane) {
		EntityManager entityManager = JpaUtil.getEntityManager();
		
		String jpql = "SELECT t from Task t where t.lane = :lane";
		TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);
		query.setParameter("lane", lane);
		
		return query.getResultList();
	}
}
