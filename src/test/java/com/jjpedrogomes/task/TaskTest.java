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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskTest {

    //Builds a new task with conclusionDate = LocalDate.now() and status COMPLETED
    private Task buildCompletedTask() {
        Task task = new Task("Test Task", null, null);
        task.setTaskCompleted();
        return task;
    }

    //Builds a new task with status IN_PROGRESS
    protected static Task buildInProgressTask() {
        return new Task("Test Task", null, null);
    }

    //Builds a new task with status PENDING
    private Task buildPendingTask() {
        Task task = new Task("Test Task", null, LocalDate.now().minusDays(1));
        return task;
    }

    @Nested
    class set_task_due_date {

        /**
         * Test case for the "setDueDate" method of the Task class.
         * Verifies that setting a due date to a date after today updates the task due date
         * and sets the task status to IN_PROGRESS.
         */
        @Test
        void to_date_after_today() {
            // Arrange
            Task task = buildPendingTask();
            LocalDate localDatePlusOne = LocalDate.now().plusDays(1);
            // Act
            task.setDueDate(localDatePlusOne);
            // Assert
            assertThat(task.getDueDate()).isEqualTo(localDatePlusOne);
            assertThat(task.getStatus()).isEqualTo(Status.IN_PROGRESS);
        }

        /**
         * Test case for the "setDueDate" method of the Task class.
         * Verifies that setting a due date to a date in the past, must maintain the status PENDING
         * and updates the due date.
         */
        @Test
        void to_date_in_the_past() {
            // Arrange
            Task task = buildPendingTask();
            LocalDate localDateMinusFive = LocalDate.now().minusDays(5);
            // Act
            task.setDueDate(localDateMinusFive);
            // Assert
            assertThat(task.getDueDate()).isEqualTo(localDateMinusFive);
            assertThat(task.getStatus()).isEqualTo(Status.PENDING);
        }
    }

    @Nested
    class set_task_in_progress {

        /**
         * This test case verifies the behavior when the task is already in progress.
         * The test asserts that the task status remains as IN_PROGRESS.
         * It also asserts that the conclusion date remains null, indicating that the task is still in progress without a conclusion date.
         */
        @Test
        void if_is_already_in_progress() {
            // Arrange
            Task task = buildInProgressTask();
            // Act
            task.setTaskInProgress();
            // Assert
            assertThat(task.getStatus()).isEqualTo(Status.IN_PROGRESS);
            assertThat(task.getConclusionDate()).isNull();
        }

        /**
         * This test case verifies the behavior when the task is pending.
         * The test asserts that the task status remains as PENDING.
         * It also asserts that the conclusion date remains null, indicating that the task is still in progress without a conclusion date.
         */
        @Test
        void if_is_already_pending() {
            // Arrange
            Task task = buildPendingTask();
            // Act
            task.setTaskInProgress();
            // Assert
            assertThat(task.getStatus()).isEqualTo(Status.PENDING);
            assertThat(task.getConclusionDate()).isNull();
        }

        /**
         * This test case verifies the behavior when the task is completed and then set to in progress.
         * The test asserts that the task status updates to IN_PROGRESS after calling setTaskInProgress().
         * It also asserts that the conclusion date remains null, indicating that the task is now in progress without a conclusion date.
         */
        @Test
        void if_is_already_completed_to_in_progress() {
            // Arrange
            Task task = buildCompletedTask();
            // Act
            task.setTaskInProgress();
            // Assert
            assertThat(task.getStatus()).isEqualTo(Status.IN_PROGRESS);
            assertThat(task.getConclusionDate()).isNull();
        }

        /**
         * This test case verifies the behavior when the task is completed but has a pending due date.
         * The test asserts that the task status remains PENDING after calling setTaskInProgress().
         * It also asserts that the conclusion date remains null, indicating that the task status remains unchanged as pending.
         */
        @Test
        void if_is_completed_with_due_date_pending() {
            // Arrange
            Task task = buildPendingTask();
            task.setTaskCompleted();
            // Act
            task.setTaskInProgress();
            // Assert
            assertThat(task.getStatus()).isEqualTo(Status.PENDING);
            assertThat(task.getConclusionDate()).isNull();
        }
    }

    @Nested
    class set_task_completed {


        /**
         * This test case verifies the behavior when the task is completed and the method setTaskCompleted is called.
         * The LocalDate.now() method is stubbed to return a future date (one day ahead of the current date).
         * The test asserts that the task status remains COMPLETED after calling setTaskCompleted().
         * It also asserts that the conclusion date is not modified, maintaining the first conclusion data applied
         */
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

        /**
         * This test case verifies the behavior when the task is in progress or pending
         * and the method setTaskCompleted is called.
         * The test asserts that the status of the tasks is updated to COMPLETED after calling setTaskCompleted().
         * It also asserts that the conclusion date is set to the current Local Date.
         */
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
