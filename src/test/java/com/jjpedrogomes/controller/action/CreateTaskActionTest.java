package com.jjpedrogomes.controller.action;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.jjpedrogomes.controller.task.CreateTaskAction;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.task.TaskDaoImpl;
import static com.jjpedrogomes.task.TaskTest.buildInProgressTask;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CreateTaskActionTest {

    @InjectMocks
    private CreateTaskAction useCase;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession httpSession;
    @Mock
    private TaskDaoImpl taskDao;
    @Captor
    private ArgumentCaptor<List<Task>> taskListCaptor;


    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    /**
     * Test case for trying to create a task with all valid parameters
     * Verifies that task is saved correctly.
     */
    @Test
    void create_task_with_all_params() throws Exception {
        // Arrange
        String titleParam = "test title";
        String descriptionParam = "test description";
        LocalDate now = LocalDate.now();
        String dueDateParam = String.valueOf(now);
        when(request.getParameter("title")).thenReturn(titleParam);
        when(request.getParameter("description")).thenReturn(descriptionParam);
        when(request.getParameter("dueDate")).thenReturn(dueDateParam);
        when(request.getSession()).thenReturn(httpSession);
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));
        // Act
        useCase.execute(request, response);
        // Assert
        verify(taskDao).save(any(Task.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Test case for trying to create a task, but the given argument is invalid.
     * Verifies that the correct exception is thrown.
     */
    @Test
    void create_task_with_invalid_param() {
        // Arrange
        String titleParam = "test title";
        String invalidDueDate = "invalid_date_format";
        when(request.getParameter("title")).thenReturn(titleParam);
        when(request.getParameter("dueDate")).thenReturn(invalidDueDate);
        // Act / Assert
        assertThrows(DateTimeParseException.class, () -> useCase.execute(request, response));
        verify(taskDao, never()).save(any(Task.class));
    }

    @Test
    void create_task_with_null_title() {
        // Arrange
        when(request.getParameter("title")).thenReturn(null);
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request,response));
        verify(taskDao, never()).save(any(Task.class));
    }

    @Test
    void add_task_to_session_list() throws Exception {
        // Arrange
        CreateTaskAction createTaskAction = new CreateTaskAction(taskDao);
        Method method = CreateTaskAction.class.getDeclaredMethod("addTaskToSession", HttpServletRequest.class,
                Task.class);
        method.setAccessible(true);

        when(request.getSession()).thenReturn(httpSession);

        List<Task> taskListInSession = new ArrayList<>();
        when(httpSession.getAttribute("taskList")).thenReturn(taskListInSession);
        // Act and Assert
        Task task = buildInProgressTask();
        assertDoesNotThrow(() -> {
            method.invoke(createTaskAction, request, task);
        });
        verify(httpSession).setAttribute(eq("taskList"), taskListCaptor.capture());
        List<Task> capturedTaskList = taskListCaptor.getValue();
        assertThat(capturedTaskList).containsExactly(task);
    }

    @Test
    void add_task_to_session_with_null_attribute() throws Exception {
        // Arrange
        Task task = buildInProgressTask();
        CreateTaskAction createTaskAction = new CreateTaskAction(taskDao);
        Method method = CreateTaskAction.class.getDeclaredMethod("addTaskToSession", HttpServletRequest.class,
                Task.class);
        method.setAccessible(true);

        when(request.getSession()).thenReturn(httpSession);
        // Act
        method.invoke(createTaskAction, request, task);
        // Assert
        verify(httpSession).setAttribute(eq("taskList"), taskListCaptor.capture());
        List<Task> capturedTaskList = taskListCaptor.getValue();
        assertThat(capturedTaskList).containsExactly(task);
    }

    @Test
    void add_task_to_session_but_session_is_null() throws Exception  {
        // Arrange
        Task task = buildInProgressTask();
        CreateTaskAction createTaskAction = new CreateTaskAction(taskDao);
        Method method = CreateTaskAction.class.getDeclaredMethod("addTaskToSession", HttpServletRequest.class,
                Task.class);
        method.setAccessible(true);
        when(request.getSession()).thenReturn(null);
        // Act
        assertThrows( InvocationTargetException.class,
                () -> {
            method.invoke(createTaskAction, request, task);
        });
    }

    @Test
    void add_task_to_session_but_received_list_is_empty() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        Task task = buildInProgressTask();
        CreateTaskAction createTaskAction = new CreateTaskAction(taskDao);
        Method method = CreateTaskAction.class.getDeclaredMethod("addTaskToSession", HttpServletRequest.class,
                Task.class);
        method.setAccessible(true);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("taskList")).thenReturn(Collections.emptyList());
        // Act
        method.invoke(createTaskAction, request, task);
        // Assert
        verify(httpSession).setAttribute(eq("taskList"), taskListCaptor.capture());
        List<Task> capturedTaskList = taskListCaptor.getValue();
        assertThat(capturedTaskList).containsExactly(task);
    }
}
