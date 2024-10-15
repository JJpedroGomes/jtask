package com.jjpedrogomes.controller.util;

import com.google.gson.JsonObject;

public interface ClientResponseHandler {
   
    void commitJsonToResponse();
    
    void flushToClient();
    
    ClientResponseHandlerImpl createJsonResponse();
    
    ClientResponseHandlerImpl setErrorCode(int errorCode);
    
    ClientResponseHandlerImpl setMessage(String Message);
    
    ClientResponseHandlerImpl setObject(Object obj);

    ClientResponseHandlerImpl setObjectNotExposingSensitiveFields(Object obj);
    
    String getCurrentJsonString();
    
    JsonObject getCurrentJson();
}
