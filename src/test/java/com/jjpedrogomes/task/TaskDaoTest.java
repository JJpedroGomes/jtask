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
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static com.jjpedrogomes.task.TaskTest.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskDaoTest {

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
         * Test case for trying to save a task, but an error occurs.
         * Verifies that the save operation fails gracefully and the transaction is rolled back.
         */
        @Test
        void with_error() {
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

    @Nested
    class task_dao_get {

        /**
         * Test case for getting a task list
         * Verifies that the list is returned correctly.
         */
        @Test
        void non_empty_list() {
            // Arrange
            Task task1 = buildInProgressTask();
            Task task2 = buildCompletedTask();
            Task task3 = buildPendingTask();
            List<Task> taskList = Arrays.asList(task1, task2, task3);
            TypedQuery<Task> queryMock = mock(TypedQuery.class);
            when(entityManagerMock.createQuery(anyString(), eq(Task.class))).thenReturn(queryMock);
            when(queryMock.getResultList()).thenReturn(taskList);
            // Act
            List<Task> returnedTaskList = taskDao.getAll();
            // Assert
            assertThat(returnedTaskList)
                    .hasSize(3)
                    .containsExactlyInAnyOrder(task1, task2, task3);
            verify(queryMock, times(1)).getResultList();
        }

        /**
         * Test case for trying to get a list of tasks, but is empty.
         * Verifies that an empty list is returned.
         */
        @Test
        void empty_list() {
            // Arrange
            TypedQuery<Task> queryMock = mock(TypedQuery.class);
            when(entityManagerMock.createQuery(anyString(), eq(Task.class))).thenThrow(new PersistenceException("Simulated Error"));
            // Act
            List<Task> returnedTaskList = taskDao.getAll();
            // Assert
            assertThat(returnedTaskList).isEmpty();
            verify(entityManagerMock, times(1)).createQuery(anyString(), eq(Task.class));
        }
    }
}
