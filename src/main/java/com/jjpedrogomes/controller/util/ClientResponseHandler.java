package com.jjpedrogomes.controller.util;

public interface ClientResponseHandler {

    public ClientResponseHandlerImpl createErrorJsonResponse(int errorCode) ;

    public ClientResponseHandlerImpl createObjectJsonResponse(Object obj);
    
    public void commitJsonToResponse();
}
