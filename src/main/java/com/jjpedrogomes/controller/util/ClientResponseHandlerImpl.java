package com.jjpedrogomes.controller.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class ClientResponseHandlerImpl implements ClientResponseHandler{
	
	private static final Logger logger = LogManager.getLogger(ClientResponseHandlerImpl.class);

    private HttpServletResponse response;
    private boolean isCommited;
    private String jsonString;
    private JsonObject json;
    
    public ClientResponseHandlerImpl(HttpServletResponse response) {
        this.response = response;
    }
    
    @Override
    public ClientResponseHandlerImpl createJsonResponse() {
    	setResponseContentToJson();
    	this.json = new JsonObject();
    	return this;
    }
    
    @Override
    public ClientResponseHandlerImpl setErrorCode(int errorCode) {
    	this.json.addProperty("error", errorCode);
    	return this;
    }
    
    @Override
    public ClientResponseHandlerImpl setMessage(String message) {
    	this.json.addProperty("message", message);
    	return this;
    }
    
    @Override
    public ClientResponseHandlerImpl setObject(Object obj) {
    	Gson gson = new Gson();
    	this.json = gson.toJsonTree(obj).getAsJsonObject();
    	return this;
    }
    

	@Override
	public ClientResponseHandlerImpl setObjectNotExposingSensitiveFields(Object obj) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    	this.json = gson.toJsonTree(obj).getAsJsonObject();
    	return this;
	}
    
    @Override
    public void commitJsonToResponse() {
    	if(json == null) {
    		return;
    	}
    	if (jsonString == null) {
    		this.jsonString = transformToJsonString(json);
    	}
    	if (isCommited) {
            return;
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.println(jsonString);
            this.isCommited = true;
        } catch (IOException e) {
        	logger.error("Unexpected error commiting json to print writer: {}", e.getMessage(), e);
        }
    }
    
	@Override
	public void flushToClient() {
		try {
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected error trying to flush to client");
		}
	}	


    private String transformToJsonString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    private void setResponseContentToJson() {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

	public String getCurrentJsonString() {
		return jsonString;
	}
	
	public JsonObject getCurrentJson() {
		return json;
	}
}

