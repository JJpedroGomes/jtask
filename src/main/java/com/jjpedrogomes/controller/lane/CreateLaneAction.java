package com.jjpedrogomes.controller.lane;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneService;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;

public class CreateLaneAction implements Action {
	
	private final LaneService laneService;
	private final UserDao<User> userDao;
	private static final Logger logger = LogManager.getLogger(CreateLaneAction.class);
	
	public CreateLaneAction (LaneService laneService, UserDao<User> userDao) {
		this.laneService = laneService;
		this.userDao = userDao;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		try {
			String nameParam = request.getParameter("name");
			String userEmail = (String) request.getSession().getAttribute("user");
			
			if(nameParam == null) {
				throw new IllegalArgumentException();
			}
	
			userDao.getUserByEmail(userEmail).ifPresent(user -> {
				laneService.createLane(nameParam, user);
				response.setStatus(HttpServletResponse.SC_CREATED);
			});
		} catch (RuntimeException e) {
			logger.info("Unexpected error occured trying to create lane, "
					+ "check for request parameters");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
