package com.jjpedrogomes.repository.lane;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.lane.LaneFactory;
import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.util.JpaUtil;
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
		
		void should_return_lane_by_id() {
			
		}
		
		void should_return_all_lane_from_a_user() {
			
		}
		
		void should_return_an_empty_optional_getting_by_id() {
			
		}
		
		void should_return_an_empty_list() {
			
		}
	}
	
	@Nested
	class lane_dao_delete {
		
		void should_delete_lane_sucessfully() {
			
		}	
	}
	
	@Nested
	class lane_dao_update {
		
		
		void should_update_lane_successfully() {
			
		}
		
		void should_try_to_update_and_then_rollback() {
			
		}
	}
}
