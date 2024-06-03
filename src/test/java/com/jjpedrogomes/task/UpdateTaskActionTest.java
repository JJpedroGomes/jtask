package com.jjpedrogomes.task;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.task.TaskDao;
import com.jjpedrogomes.controller.action.UpdateTaskAction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static com.jjpedrogomes.task.TaskTest.buildInProgressTask;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UpdateTaskActionTest {

    @InjectMocks
    private UpdateTaskAction useCase;
    @Mock
    private TaskDao taskDao;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;
    private Task task;

    @BeforeEach
    void setUp() {
        initMocks(this);
        this.task = buildInProgressTask();
    }

    /**
     * Test case for trying to update a task with all editable params.
     * Verifies that the managed task now has the corresponding attributes.
     */
    @Test
    void update_existing_task_with_all_params() throws IOException {
        // Arrange
        String idParam = "1";
        String titleParam = "New title";
        String descriptionParam = "New description";
        LocalDate now = LocalDate.now();
        String dueDateParam = String.valueOf(now);
        when(request.getParameter("id")).thenReturn(idParam);
        when(request.getParameter("title")).thenReturn(titleParam);
        when(request.getParameter("description")).thenReturn(descriptionParam);
        when(request.getParameter("dueDate")).thenReturn(dueDateParam);
        when(taskDao.get(any(Long.class))).thenReturn(Optional.ofNullable(task));
        when(response.getWriter()).thenReturn(writer);
        // Act
        useCase.execute(request, response);
        // Assert
        assertThat(task.getTitle()).isEqualTo(titleParam);
        assertThat(task.getDescription()).isEqualTo(descriptionParam);
        assertThat(task.getDueDate()).isEqualTo(now);
    }

    /**
     * Test case for trying to update a task, but the given task id don't exist.
     * Verifies that the update method is not executed.
     */
    @Test
    void update_non_existing_task() {
        // Arrange
        String idParam = "1";
        when(request.getParameter("id")).thenReturn(idParam);
        when(taskDao.get(any(Long.class))).thenReturn(Optional.empty());
        // Act
        useCase.execute(request, response);
        // Assert
        verify(taskDao, never()).update(task);
    }

    /**
     * Test case for trying to update a task, but the given argument is invalid.
     * Verifies that the correct exception is thrown.
     */
    @Test
    void update_with_invalid_arguments() {
        // Arrange
        String idParam = "1";
        when(request.getParameter("id")).thenReturn(idParam);
        String invalidDueDate = "invalid_date_format";
        when(request.getParameter("dueDate")).thenReturn(invalidDueDate);
        when(taskDao.get(any(Long.class))).thenReturn(Optional.ofNullable(task));
        // Act / Assert
        assertThrows(DateTimeParseException.class, () -> useCase.execute(request, response));
    }

    /**
     * Test case for trying to update a task with null arguments.
     * Verifies that only the title has been updated.
     */
    @Test
    void update_with_null_arguments() throws IOException {
        // Arrange
        String idParam = "1";
        String titleParam = "New title";
        when(request.getParameter("id")).thenReturn(idParam);
        when(request.getParameter("title")).thenReturn(titleParam);
        when(request.getParameter("description")).thenReturn(null);
        when(request.getParameter("dueDate")).thenReturn(null);
        when(taskDao.get(any(Long.class))).thenReturn(Optional.ofNullable(task));
        when(response.getWriter()).thenReturn(writer);
        // Act
        useCase.execute(request, response);
        // Assert
        assertThat(task.getTitle()).isEqualTo(titleParam);
        assertThat(task.getDescription()).isNull();
        assertThat(task.getDueDate()).isNull();
    }

    /**
     * Test case for trying to update a task, but the given id is invalid.
     * Verifies that the correct exception is thrown.
     */
    @Test
    void update_with_invalid_id() {
        // Arrange
        String idParam = "invalid_id";
        when(request.getParameter("id")).thenReturn(idParam);
        // Act Assert
        assertThrows(NumberFormatException.class, () -> useCase.execute(request, response));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(taskDao, never()).update(task);
    }
}
