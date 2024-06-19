package com.jjpedrogomes.controller.task;

import com.jjpedrogomes.controller.action.CreateTaskAction;
import com.jjpedrogomes.controller.action.UpdateTaskAction;
import com.jjpedrogomes.controller.task.TaskController;
import com.jjpedrogomes.repository.task.TaskDaoImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskControllerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private EntityManager entityManager;
    @Mock
    private PrintWriter writer;
    @Mock
    TaskDaoImpl taskDao;
    @InjectMocks
    private TaskController taskController;
    
    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void doPost_with_createTask_action() throws Exception {
        // Arrange
        String fullQualifiedName = "com.jjpedrogomes.controller.action.CreateTaskAction";
        when(request.getAttribute("entityManager")).thenReturn(entityManager);
        when(request.getParameter("action")).thenReturn("CreateTask");
        when(request.getParameter("title")).thenReturn("any");
        when(request.getParameter("dueDate")).thenReturn("2007-12-03");
        when(request.getSession()).thenReturn(httpSession);
        when(response.getWriter()).thenReturn(writer);
        Method method = TaskController.class.getDeclaredMethod("getQualifiedClassName", String.class, HttpServletResponse.class);
        method.setAccessible(true);
        // Act
        String actionFullQualifiedName = (String) method.invoke(taskController, "CreateTask", response);
        Constructor<?> constructor = Class.forName(actionFullQualifiedName).getConstructor(TaskDaoImpl.class);
        CreateTaskAction instance = (CreateTaskAction) constructor.newInstance(taskDao);
        taskController.doPost(request, response);
        // Assert
        assertThat(actionFullQualifiedName).isEqualTo(fullQualifiedName);
        assertThat(instance).isInstanceOf(CreateTaskAction.class);
        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost_with_updateTask_action() throws Exception {
        // Arrange
        String fullQualifiedName = "com.jjpedrogomes.controller.action.UpdateTaskAction";
        when(request.getAttribute("entityManager")).thenReturn(entityManager);
        when(request.getParameter("action")).thenReturn("UpdateTask");
        when(request.getParameter("title")).thenReturn("any");
        when(request.getParameter("dueDate")).thenReturn("2007-12-03");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getSession()).thenReturn(httpSession);
        when(response.getWriter()).thenReturn(writer);
        Method method = TaskController.class.getDeclaredMethod("getQualifiedClassName", String.class, HttpServletResponse.class);
        method.setAccessible(true);
        // Act
        String actionFullQualifiedName = (String) method.invoke(taskController, "UpdateTask", response);
        Constructor<?> constructor = Class.forName(actionFullQualifiedName).getConstructor(TaskDaoImpl.class);
        UpdateTaskAction instance = (UpdateTaskAction) constructor.newInstance(taskDao);
        taskController.doPost(request, response);
        // Assert
        assertThat(actionFullQualifiedName).isEqualTo(fullQualifiedName);
        assertThat(instance).isInstanceOf(UpdateTaskAction.class);
        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost_with_invalid_action_parameter() throws Exception{
        // Arrange
        String invalidAction = "any";
        when(request.getParameter("action")).thenReturn(invalidAction);
        Method method = TaskController.class.getDeclaredMethod("getQualifiedClassName", String.class, HttpServletResponse.class);
        method.setAccessible(true);
        // Act and Assert
        String actionFullQualifiedName = (String) method.invoke(taskController, invalidAction, response);
        assertThrows(ClassNotFoundException.class, () -> Class.forName(actionFullQualifiedName).getConstructor(TaskDaoImpl.class));
        assertThrows(ServletException.class, () -> taskController.doPost(request, response));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
