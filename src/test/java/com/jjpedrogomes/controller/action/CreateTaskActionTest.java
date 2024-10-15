package com.jjpedrogomes.controller.action;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;

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
    private TaskDao taskDao;
    @Mock
    private LaneDao laneDao;
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
        String laneIdParam = "1";
        String dueDateParam = String.valueOf(now);
        
        Lane laneMock = mock(Lane.class);
        Task taskMock = mock(Task.class);
        
        when(laneDao.get(anyLong())).thenReturn(Optional.ofNullable(laneMock));
        when(request.getParameter("laneId")).thenReturn(laneIdParam);
        when(laneMock.getTasks()).thenReturn(Collections.unmodifiableList(Arrays.asList(taskMock)));
        
        when(taskMock.getDueDate()).thenReturn(LocalDate.now());
        when(taskMock.getLane()).thenReturn(laneMock);
        when(request.getParameter("title")).thenReturn(titleParam);
        when(request.getParameter("description")).thenReturn(descriptionParam);
        when(request.getParameter("dueDate")).thenReturn(dueDateParam);
        when(request.getSession()).thenReturn(httpSession);
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));
        // Act
        useCase.execute(request, response);
        // Assert
        verify(laneDao).update(any(Lane.class));
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
        String laneIdParam = "1";      
        
        Lane laneMock = mock(Lane.class);
        
        when(laneDao.get(anyLong())).thenReturn(Optional.ofNullable(laneMock));
        when(request.getParameter("title")).thenReturn(titleParam);
        when(request.getParameter("dueDate")).thenReturn(invalidDueDate);
        when(request.getParameter("laneId")).thenReturn(laneIdParam);
        // Act / Assert
        assertThrows(DateTimeParseException.class, () -> useCase.execute(request, response));
        verify(taskDao, never()).save(any(Task.class));
    }

//    @Test
//    void add_task_to_session_list() throws Exception {
//        // Arrange
//        CreateTaskAction createTaskAction = new CreateTaskAction(taskDao);
//        Method method = CreateTaskAction.class.getDeclaredMethod("addTaskToSession", HttpServletRequest.class,
//                Task.class);
//        method.setAccessible(true);
//
//        when(request.getSession()).thenReturn(httpSession);
//
//        List<Task> taskListInSession = new ArrayList<>();
//        when(httpSession.getAttribute("taskList")).thenReturn(taskListInSession);
//        // Act and Assert
//        Task task = buildInProgressTask();
//        assertDoesNotThrow(() -> {
//            method.invoke(createTaskAction, request, task);
//        });
//        verify(httpSession).setAttribute(eq("taskList"), taskListCaptor.capture());
//        List<Task> capturedTaskList = taskListCaptor.getValue();
//        assertThat(capturedTaskList).containsExactly(task);
//    }

//    @Test
//    void add_task_to_session_with_null_attribute() throws Exception {
//        // Arrange
//        Task task = buildInProgressTask();
//        CreateTaskAction createTaskAction = new CreateTaskAction(taskDao);
//        Method method = CreateTaskAction.class.getDeclaredMethod("addTaskToSession", HttpServletRequest.class,
//                Task.class);
//        method.setAccessible(true);
//
//        when(request.getSession()).thenReturn(httpSession);
//        // Act
//        method.invoke(createTaskAction, request, task);
//        // Assert
//        verify(httpSession).setAttribute(eq("taskList"), taskListCaptor.capture());
//        List<Task> capturedTaskList = taskListCaptor.getValue();
//        assertThat(capturedTaskList).containsExactly(task);
//    }

//    @Test
//    void add_task_to_session_but_session_is_null() throws Exception  {
//        // Arrange
//        Task task = buildInProgressTask();
//        CreateTaskAction createTaskAction = new CreateTaskAction(taskDao);
//        Method method = CreateTaskAction.class.getDeclaredMethod("addTaskToSession", HttpServletRequest.class,
//                Task.class);
//        method.setAccessible(true);
//        when(request.getSession()).thenReturn(null);
//        // Act
//        assertThrows( InvocationTargetException.class,
//                () -> {
//            method.invoke(createTaskAction, request, task);
//        });
//    }

//    @Test
//    void add_task_to_session_but_received_list_is_empty() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        // Arrange
//        Task task = buildInProgressTask();
//        CreateTaskAction createTaskAction = new CreateTaskAction(taskDao);
//        Method method = CreateTaskAction.class.getDeclaredMethod("addTaskToSession", HttpServletRequest.class,
//                Task.class);
//        method.setAccessible(true);
//        when(request.getSession()).thenReturn(httpSession);
//        when(httpSession.getAttribute("taskList")).thenReturn(Collections.emptyList());
//        // Act
//        method.invoke(createTaskAction, request, task);
//        // Assert
//        verify(httpSession).setAttribute(eq("taskList"), taskListCaptor.capture());
//        List<Task> capturedTaskList = taskListCaptor.getValue();
//        assertThat(capturedTaskList).containsExactly(task);
//    }
}
