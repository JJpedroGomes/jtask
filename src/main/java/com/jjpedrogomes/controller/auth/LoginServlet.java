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

import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.repository.user.UserDao;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		EntityManager entityManager = (EntityManager) request.getAttribute("entityManager");
		UserDao userDao = new UserDao(entityManager);
		
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
			response.sendRedirect(request.getContextPath() + "/login.jsp?error=Invalid credentials");
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
	}
}
