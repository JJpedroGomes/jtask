package com.jjpedrogomes.controller.util;

import com.google.gson.Gson;
import com.jjpedrogomes.model.task.Task;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GsonUtil {

    public static void convertObjectToJson(HttpServletResponse response, Task task) {
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = gson.toJson(task);
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
