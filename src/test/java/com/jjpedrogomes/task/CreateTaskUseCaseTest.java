package com.jjpedrogomes.task;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.usecase.CreateTaskUseCase;
import com.jjpedrogomes.repository.TaskDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CreateTaskUseCaseTest {

    @InjectMocks
    private CreateTaskUseCase useCase;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private TaskDao taskDao;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    /**
     * Test case for trying to create a task with all valid parameters
     * Verifies that task is saved correctly.
     */
    @Test
    void create_task_with_all_params() {
        // Arrange
        String titleParam = "test title";
        String descriptionParam = "test description";
        LocalDate now = LocalDate.now();
        String dueDateParam = String.valueOf(now);
        when(request.getParameter("title")).thenReturn(titleParam);
        when(request.getParameter("description")).thenReturn(descriptionParam);
        when(request.getParameter("dueDate")).thenReturn(dueDateParam);
        // Act
        useCase.execute(request, response);
        // Assert
        verify(taskDao).save(any(Task.class));
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
}
