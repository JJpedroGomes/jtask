package com.jjpedrogomes.controller.util;

import javax.servlet.http.HttpServletResponse;

public class ErrorResponseUtil {

    private ErrorResponseUtil() {
        // Private constructor to prevent instantiation
    }

    public static void handleErrorResponse(int errorCode, String message, HttpServletResponse response) {
        ClientResponseHandler clientResponseHandler = new ClientResponseHandlerImpl(response);
        clientResponseHandler.createJsonResponse();

        clientResponseHandler.setErrorCode(errorCode);
        clientResponseHandler.setMessage(message);

        clientResponseHandler.commitJsonToResponse();
        clientResponseHandler.flushToClient();
    }
}

