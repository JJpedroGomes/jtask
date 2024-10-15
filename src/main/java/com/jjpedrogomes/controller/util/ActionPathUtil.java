package com.jjpedrogomes.controller.util;

import javax.servlet.ServletException;


public class ActionPathUtil {

	public static String getQualifiedClassName(PathConstants pathConstant, String action) {
        if (action == null) {
            throw new RuntimeException("Action provided is null");
        }
        StringBuilder builder = new StringBuilder();
        return builder.append(pathConstant.getPath()).append(action).append("Action").toString();
    }
}
