package com.jjpedrogomes.controller.lane;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.lane.LaneService;
import com.jjpedrogomes.model.lane.LaneServiceImpl;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.repository.lane.LaneDaoImpl;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CreateLaneActionTest {
	
	private LaneService laneService;
	private UserDao<User> userDao;
	private LaneDao laneDao;
	private CreateLaneAction createLaneAction;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	@BeforeEach
	void setUpBeforeEach() {
		this.userDao = mock(UserDao.class);
		this.laneDao = mock(LaneDao.class);
		this.laneService = new LaneServiceImpl(laneDao);
		this.createLaneAction = new CreateLaneAction(laneService, userDao);
		this.request = mock(HttpServletRequest.class);
		this.response = mock(HttpServletResponse.class);
		this.session = mock(HttpSession.class);
	}
	
	@Test
	void should_create_lane_sucessfully() {
		// Arrange
		String name = "Todo";
		String email = "email@email.com";
		
		when(request.getParameter("name")).thenReturn(name);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(email);
		when(userDao.getUserByEmail(email)).thenReturn(Optional.ofNullable(mock(User.class)));
		// Act
		createLaneAction.execute(request, response);
		// Assert
		verify(laneDao).save(any(Lane.class));		
	}
	
	@Test
	void should_not_create_lane_and_set_response_status_to_badrequest() {
		String name = null;
		String email = "email@email.com";
		
		when(request.getParameter("name")).thenReturn(name);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(email);
		when(userDao.getUserByEmail(email)).thenReturn(Optional.ofNullable(mock(User.class)));
		// Act
		createLaneAction.execute(request, response);
		// Assert
		verify(laneDao, never()).save(any(Lane.class));
		verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}	
}
