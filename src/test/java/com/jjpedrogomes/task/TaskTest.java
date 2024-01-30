package com.jjpedrogomes.task;

import com.jjpedrogomes.model.task.Status;
import com.jjpedrogomes.model.task.Task;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TaskTest {

    //Builds a new task with conclusionDate = LocalDate.now() and status COMPLETED
    private Task buildCompletedTask() {
        Task task = new Task("Test Task", null, null);
        task.setTaskCompleted();
        return task;
    }

    //Builds a new task with status IN_PROGRESS
    private Task buildInProgressTask() {
        return new Task("Test Task", null, null);
    }

    //Builds a new task with status PENDING
    private Task buildPendingTask() {
        Task task = new Task("Test Task", null, LocalDate.now().minusDays(1));
        task.setTaskCompleted();
        task.setTaskInProgress();
        return task;
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class set_task_completed {


        //Should not update the current conclusionDate and status should keep COMPLETED
        @Test
        void if_is_already_completed() {
            // Arrange
            Task task = buildCompletedTask();
            try(MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
                LocalDate localDatePlusOneDay = LocalDate.now().plusDays(1);
                mock.when(LocalDate::now).thenReturn(localDatePlusOneDay);
                // Act
                task.setTaskCompleted();
                System.out.println(task.getConclusionDate());
                // Assert
                assertThat(task.getStatus()).isEqualTo(Status.COMPLETED);
                assertThat(task.getConclusionDate()).isBefore(localDatePlusOneDay);
            }
        }

        //Should update the current conclusionDate and status should turn COMPLETED
        @Test
        void if_is_in_progress_or_pending() {
            // Arrange
            Task inProgressTask = buildInProgressTask();
            Task pendingTask = buildPendingTask();
            // Act
            inProgressTask.setTaskCompleted();
            pendingTask.setTaskCompleted();
            // Assert
            assertThat(inProgressTask.getStatus()).isEqualTo(Status.COMPLETED);
            assertThat(inProgressTask.getConclusionDate()).isEqualTo(LocalDate.now());
            assertThat(pendingTask.getStatus()).isEqualTo(Status.COMPLETED);
            assertThat(pendingTask.getConclusionDate()).isEqualTo(LocalDate.now());
        }
    }
}
