package com.jjpedrogomes.controller.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ClientResponseHandlerImpl implements ClientResponseHandler{
	
	private static final Logger logger = LogManager.getLogger(ClientResponseHandlerImpl.class);

    private HttpServletResponse response;
    private boolean isCommited;
    private String currentJson;

    public ClientResponseHandlerImpl(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public ClientResponseHandlerImpl createErrorJsonResponse(int errorCode) {
        setResponseContentToJson();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("error", errorCode);
        this.currentJson = transformToJsonString(jsonResponse);
        return this;
    }

    @Override
    public ClientResponseHandlerImpl createObjectJsonResponse(Object obj) {
        setResponseContentToJson();
        this.currentJson = transformToJsonString(obj);
        return this;
    }
    
    @Override
    public void commitJsonToResponse() {
    	if (isCommited) {
            return;
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.println(currentJson);
            writer.flush();
            this.isCommited = true;
        } catch (IOException e) {
        	logger.error("Unexpected error commiting json to print writer: {}", e.getMessage(), e);
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

	public String getCurrentJson() {
		return currentJson;
	}
}

