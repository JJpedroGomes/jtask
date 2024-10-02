package com.jjpedrogomes.controller.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class GsonUtil {

    public static void convertObjectToJson(HttpServletResponse response, Object obj) {
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = gson.toJson(obj);
        addJsonToResponse(response, json);
    }

    private static void addJsonToResponse(HttpServletResponse response, String json) {
        try (PrintWriter writer = response.getWriter()) {
            writer.println(json);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
