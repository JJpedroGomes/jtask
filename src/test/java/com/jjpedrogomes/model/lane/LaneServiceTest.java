package com.jjpedrogomes.model.lane;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.lane.LaneDaoImpl;
import com.jjpedrogomes.repository.user.UserDaoImpl;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LaneServiceTest {
	
	@Nested
	class create_lane {

		@Test
		void createLaneSucessfully() {
			// Arrange
			String name = "Todo";			
			User user = mock(User.class);
			LaneDaoImpl mockedLaneDao = mock(LaneDaoImpl.class);
			
			// Act
			LaneService laneService = new LaneServiceImpl(mockedLaneDao);
			Lane lane = laneService.createLane(name, user);
			// Assert
			verify(mockedLaneDao).save(lane);
		}
	}
	
	@Nested
	class switch_lane_position {
		
		@Test
		void should_switch_lane_position() {
			// Arrange
			LaneDao laneDaoMock = mock(LaneDao.class);
			UserDao userDaoMock = mock(UserDao.class);	
			Lane laneMock = mock(Lane.class);
			
			LaneService laneService = new LaneServiceImpl(laneDaoMock, userDaoMock);
			
			when(laneDaoMock.get(any(Long.class))).thenReturn(Optional.ofNullable(laneMock));
			// Act
			laneService.switchLanePosition(1l, 1);
			// Assert
			verify(laneMock).switchLanePositionForUser(any(Integer.class));
			assertThat(laneMock.getPosition()).isEqualTo(0);
		}
		
		@Test
		void should_throw_runtime_exception_trying_to_switch_lane_position() {
			// Arrange
			UserDao userDao = new UserDaoImpl(JpaUtil.getEntityManager());
			LaneService laneService = LaneServiceFactory.getInstance(userDao);
			// Act
			assertThrows(RuntimeException.class, () -> laneService.switchLanePosition(33l, 0));
		}
	}
}
