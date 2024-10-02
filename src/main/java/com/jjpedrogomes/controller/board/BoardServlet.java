package com.jjpedrogomes.controller.board;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneService;
import com.jjpedrogomes.model.lane.LaneServiceFactory;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.model.user.UserDaoFactory;

@WebServlet(
        name = "BoardServlet",
        urlPatterns = "/board"
)
public class BoardServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(BoardServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Rendering user board");
		
		String userEmail = (String) request.getSession().getAttribute("user");
		if (userEmail == null) {
            handleError(request, response, "User not found");
            return;
        }
	
		UserDao<User> userDao = UserDaoFactory.getInstance();
		try {
			Optional<User> u = userDao.getUserByEmail(userEmail);
			if(u.isPresent()) {
				User user = u.get();
				
				LaneService laneService = LaneServiceFactory.getInstance(userDao);		
				List<Lane> lanes = laneService.getAllLaneForUser(user);
				
				request.setAttribute("lanes", lanes);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/board.jsp");
				dispatcher.forward(request,response);
			} else {
				handleError(request, response, "User not found");
			}
		} catch (Exception e) {
			logger.error("Error rendering user board", e);
            handleError(request, response, "An error occurred while rendering the board. Please try again later.");
		}
	}
	
	 private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
	        request.setAttribute("errorMessage", errorMessage);
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/error.jsp");
	        dispatcher.forward(request, response);
	 }
}
