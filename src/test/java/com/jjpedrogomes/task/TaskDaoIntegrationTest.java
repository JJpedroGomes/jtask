package com.jjpedrogomes.task;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.repository.TaskDao;
import org.junit.jupiter.api.*;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.jjpedrogomes.task.TaskTest.buildInProgressTask;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskDaoIntegrationTest {

    @InjectMocks
    private TaskDao taskDao;
    @Mock
    private EntityManager entityManagerMock;
    @Mock
    private EntityTransaction transactionMock;
    private Task task;

    @BeforeEach
    void setUp() {
        // Initializes mocks before each test method runs
        MockitoAnnotations.initMocks(this);
        // Creates a new task for testing
        this.task = buildInProgressTask();
    }

    @Nested
    class task_dao_save {

        /**
         * Test case for saving a new task.
         * Verifies that the save operation is performed correctly by mocking EntityManager and EntityTransaction.
         */
        @Test
        void new_task() {
            // Arrange
            when(entityManagerMock.getTransaction()).thenReturn(transactionMock);
            // Act
            try {
                taskDao.save(task);
            } catch (Exception e) {
                // Ignore
            }
            // Assert
            InOrder inOrder = inOrder(entityManagerMock, transactionMock);
            inOrder.verify(entityManagerMock).getTransaction();
            inOrder.verify(transactionMock).begin();
            inOrder.verify(entityManagerMock).persist(task);
            inOrder.verify(transactionMock).commit();
        }

        /**
         * Test case for saving a task when the task object is null.
         * Verifies that the save operation fails gracefully and the transaction is rolled back.
         */
        @Test
        void test_save_with_null_task() {
            // Arrange
            when(entityManagerMock.getTransaction()).thenReturn(transactionMock);
            when(transactionMock.isActive()).thenReturn(true);
            // Stubbing behavior
            doThrow(new RuntimeException("Simulated Error")).when(entityManagerMock).persist(task);
            // Act
            taskDao.save(task);
            // Assert
            verify(entityManagerMock).getTransaction();
            verify(transactionMock).begin();
            verify(entityManagerMock).persist(task);
            verify(transactionMock).rollback();
        }
    }
}
