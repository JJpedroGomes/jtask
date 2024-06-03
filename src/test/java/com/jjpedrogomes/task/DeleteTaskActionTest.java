package com.jjpedrogomes.task;

import com.jjpedrogomes.controller.action.DeleteTaskAction;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.task.TaskDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.jjpedrogomes.task.TaskTest.buildInProgressTask;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeleteTaskActionTest {

    @InjectMocks
    private DeleteTaskAction action;
    @Mock
    private TaskDao taskDao;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private Task task;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.task = buildInProgressTask();
    }

    @Test
    void delete_existing_task() {
        // Arrange
        when(request.getParameter("id")).thenReturn("1");
        when(taskDao.get(any(Long.class))).thenReturn(Optional.ofNullable(task));
        // Act
        action.execute(request, response);
        // Assert
        verify(taskDao).delete(task);
    }

    @Test
    void delete_non_existing_task() {
        // Arrange
        when(request.getParameter("id")).thenReturn("1");
        when(taskDao.get(any(Long.class))).thenReturn(Optional.empty());
        // Act
        action.execute(request, response);
        // Assert
        verify(taskDao, never()).delete(any(Task.class));
    }

    @Test
    void delete_but_the_id_is_not_in_the_format() {
        // Arrange
        when(request.getParameter("id")).thenReturn("wrong format");
        // Act & Assert
        assertThrows(NumberFormatException.class, () -> action.execute(request, response));
        verify(taskDao, never()).delete(task);
    }
}
