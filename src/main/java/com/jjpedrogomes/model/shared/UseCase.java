package com.jjpedrogomes.model.shared;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UseCase {

    void execute(HttpServletRequest request, HttpServletResponse response);
}
