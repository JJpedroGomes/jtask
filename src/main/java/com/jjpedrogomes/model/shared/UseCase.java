package com.jjpedrogomes.model.shared;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface representing a use case in a web application.
 * Use cases encapsulate a specific behavior or action that can be performed within the application.
 * Implementations of this interface define the logic to execute a particular use case.
 * Implementing classes should provide concrete implementations of the execute method to perform the desired behavior.
 * The execute method takes HttpServletRequest and HttpServletResponse parameters
 * to handle incoming requests and generate appropriate responses.
 */
public interface UseCase {

    /**
     * Executes the use case logic based on the provided HttpServletRequest and HttpServletResponse.
     * Implementations of this method define the behavior to be performed when the use case is invoked.
     * @param request The HTTP servlet request object containing user input and context information.
     * @param response The HTTP servlet response object used to generate output or redirect responses.
     */
    void execute(HttpServletRequest request, HttpServletResponse response);
}
