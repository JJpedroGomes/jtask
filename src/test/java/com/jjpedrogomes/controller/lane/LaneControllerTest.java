package com.jjpedrogomes.controller.lane;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.lane.LaneService;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LaneControllerTest {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@BeforeEach
	void setUp() {
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
	}
	
	@Test
	void should_create_a_instance_of_createLaneAction() throws Exception {
		// Arrange
		LaneController laneController = new LaneController();
		Method method = getMethod();
		// Act
		Action action = (Action) method.invoke(laneController, "CreateLane");
		//Assert
		assertThat(action).isInstanceOf(CreateLaneAction.class);
	}
	
	@Test
	void should_create_a_instance_of_switchLanePositionAction() throws Exception {
		// Arrange
		LaneController laneController = new LaneController();
		Method method = getMethod();
		// Act
		Action action = (Action) method.invoke(laneController, "SwitchLanePosition");
		//Assert
		assertThat(action).isInstanceOf(SwitchLanePositionAction.class);
	}

	private Method getMethod() throws NoSuchMethodException {
		Method method = LaneController.class.getDeclaredMethod("newInstance", String.class);
		method.setAccessible(true);
		return method;
	}
	
	@Test
	void should_not_instantiate_any_action_and_set_response_for_bad_request() throws Exception {
		// Arrange
		when(request.getParameter("action")).thenReturn(null);
		LaneController laneController = new LaneController();
		// Act
		laneController.doPost(request, response);
		// Assert
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);;
	}

}
