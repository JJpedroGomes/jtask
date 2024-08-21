package com.jjpedrogomes.model.lane;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.repository.lane.LaneDaoImpl;

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
			//
			verify(mockedLaneDao).save(lane);
		}
	}
}
