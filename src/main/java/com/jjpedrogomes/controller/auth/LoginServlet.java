package com.jjpedrogomes.controller.auth;

import java.io.IOException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jjpedrogomes.controller.util.PathConstants;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.user.UserDao;
import com.jjpedrogomes.repository.user.UserDaoImpl;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(LoginServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		logger.info("Entering method doPost() in Login Servlet");
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
		UserDao<User> userDao = new UserDaoImpl(entityManager);
		
		Optional<User> userOptional = userDao.getUserByCredentials(email, password);
		if (userOptional.isPresent()) {
			HttpSession oldSession = request.getSession(false);
			if (oldSession != null) {
				oldSession.invalidate();
			}	
			
			HttpSession session = request.getSession();
			session.setAttribute("user", email);
			session.setAttribute("displayName", userOptional.get().getName());
			
			// Set session expiration time to 30 minutes
            session.setMaxInactiveInterval(30 * 60);
            
            response.sendRedirect(request.getContextPath() + "/main");
		} else {
			response.sendRedirect(request.getContextPath() + PathConstants.LOGIN_SCREEN.getPath() + "?error=Invalid credentials");
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// in auth filter, if user is already logged in, redirects to /main
		response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
	}
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}
}
