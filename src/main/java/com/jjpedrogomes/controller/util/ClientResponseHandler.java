package com.jjpedrogomes.controller.util;

import com.google.gson.JsonObject;

public interface ClientResponseHandler {
   
    public void commitJsonToResponse();
    
    public ClientResponseHandlerImpl createJsonResponse();
    
    public ClientResponseHandlerImpl setErrorCode(int errorCode);
    
    public ClientResponseHandlerImpl setMessage(String Message);
    
    public ClientResponseHandlerImpl setObject(Object obj);

    public String getCurrentJsonString();
    
    public JsonObject getCurrentJson();
}
