package com.jjpedrogomes.controller.filter;

import com.jjpedrogomes.model.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class EntityManagerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        EntityManager entityManager = JpaUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            request.setAttribute("entityManager", entityManager);
            chain.doFilter(request, response);
            entityManager.getTransaction().commit();
        } catch (Exception exception) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new ServletException("Something went wrong with the transaction", exception);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void destroy() {
        JpaUtil.closeFactory();
    }
}
