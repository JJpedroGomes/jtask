package com.jjpedrogomes.controller.task;

import com.jjpedrogomes.controller.action.Action;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.lane.LaneDaoFactory;
import com.jjpedrogomes.model.task.TaskDao;
import com.jjpedrogomes.model.task.TaskDaoFactory;
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
        when(request.getAttribute("entityManager")).thenReturn(entityManager);
        when(request.getParameter("action")).thenReturn("CreateTask");
        when(request.getParameter("title")).thenReturn("any");
        when(request.getParameter("dueDate")).thenReturn("2007-12-03");
        when(request.getSession()).thenReturn(httpSession);
        when(response.getWriter()).thenReturn(writer);
        Method method = TaskController.class.getDeclaredMethod("newInstance", String.class, TaskDao.class, LaneDao.class);
        method.setAccessible(true);
        // Act
        Action action = (Action) method.invoke(taskController, "CreateTask", TaskDaoFactory.getInstance(), LaneDaoFactory.getInstance());
        // Assert
        assertThat(action).isInstanceOf(CreateTaskAction.class);
    }

    @Test
    void doPost_with_updateTask_action() throws Exception {
        // Arrange
        when(request.getAttribute("entityManager")).thenReturn(entityManager);
        when(request.getParameter("action")).thenReturn("UpdateTask");
        when(request.getParameter("title")).thenReturn("any");
        when(request.getParameter("dueDate")).thenReturn("2007-12-03");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getSession()).thenReturn(httpSession);
        when(response.getWriter()).thenReturn(writer);
 
        Method method = TaskController.class.getDeclaredMethod("newInstance", String.class, TaskDao.class, LaneDao.class);
        method.setAccessible(true);
        // Act
        Action action = (Action) method.invoke(taskController, "UpdateTask", TaskDaoFactory.getInstance(), LaneDaoFactory.getInstance());
        taskController.doPost(request, response);
        // Assert
        assertThat(action).isInstanceOf(UpdateTaskAction.class);
        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost_with_invalid_action_parameter() throws Exception{
        // Arrange
        Method method = TaskController.class.getDeclaredMethod("newInstance", String.class, TaskDao.class, LaneDao.class);
        method.setAccessible(true);
        Action action = (Action) method.invoke(taskController, "any", TaskDaoFactory.getInstance(), LaneDaoFactory.getInstance());
        // Act and Assert
        assertThat(action).isNull();
    }
}
