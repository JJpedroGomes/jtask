package com.jjpedrogomes.task;

import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.task.TaskDao;
import com.jjpedrogomes.model.util.JpaUtil;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jjpedrogomes.task.TaskTest.buildInProgressTask;
import static com.jjpedrogomes.task.TaskTest.buildPendingTask;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskDaoTest {

    private TaskDao taskDao;
    private EntityManager entityManager;
    private Task task;
    private Task taskPreSaved;
    private int tasksSavedCount;
    private List<Task> tasksSavedList = new ArrayList<Task>();

    @BeforeAll
    void createPreSavedTask() {
        EntityManager entityManager = JpaUtil.getEntityManager();
        TaskDao taskDaoEnv = new TaskDao(entityManager);

        Task task = buildPendingTask();

        entityManager.getTransaction().begin();
        taskDaoEnv.save(task);
        entityManager.getTransaction().commit();
        taskPreSaved = task;
        entityManager.close();

        tasksSavedCount++;
        tasksSavedList.add(taskPreSaved);
    }

    @BeforeEach
    void setUpBeforeEach() {
        this.task = buildInProgressTask();
        this.entityManager = JpaUtil.getEntityManager();
        this.taskDao = new TaskDao(entityManager);
    }

    @AfterEach
    void setUpAfterEach() {
        this.entityManager.close();
    }

    @Nested
    class task_dao_save {

        @Test
        void new_task() {
            // Act
            entityManager.getTransaction().begin();
            taskDao.save(task);
            entityManager.getTransaction().commit();
            // Assert
            assertThat(task.getId()).isNotNull();

            tasksSavedCount++;
            tasksSavedList.add(task);
        }

        @Test
        void with_error() throws NoSuchFieldException, IllegalAccessException {
            // Arrange
            Field title = Task.class.getDeclaredField("title");
            title.setAccessible(true);
            title.set(task, null);
            // Assert and Act
            entityManager.getTransaction().begin();
            assertThrows(Exception.class, () -> taskDao.save(task));
            entityManager.getTransaction().commit();
        }
    }

    @Nested
    class task_dao_get_ {

        @Test
        void by_id_successfully() {
            // Arrange
            entityManager.getTransaction().begin();
            taskDao.save(task);
            entityManager.getTransaction().commit();
            // Act
            Optional<Task> taskFound = taskDao.get(task.getId());
            // Assert
            assertThat(taskFound).isPresent();
            assertThat(taskFound.get()).isEqualTo(task);

            tasksSavedCount++;
            tasksSavedList.add(task);
        }

        @Test
        void by_id_but_task_dont_exist() {
            // Arrange
            Long id = 100L;
            // Act
            Optional<Task> taskFound = taskDao.get(id);
            // Assert
            assertThat(taskFound).isEmpty();
        }

        @Test
        void by_id_when_exception_thrown() {
            // Arrange
            EntityManager entityManager = mock(EntityManager.class);
            TaskDao taskDaoEnv = new TaskDao(entityManager);
            when(entityManager.find(eq(Task.class),anyLong())).thenThrow(new RuntimeException("Simulated Error"));
            // Act
            List<Task> taskList = taskDaoEnv.getAll();
            // Assert
            assertThat(taskList).isEmpty();
        }

        /**
         * Test case for getting a task list
         * Verifies that the list is returned correctly.
         */
        @Test
        void non_empty_list() {
            // Arrange
            Task newTask = buildPendingTask();
            entityManager.getTransaction().begin();
            taskDao.save(newTask);
            entityManager.getTransaction().commit();

            tasksSavedList.add(newTask);
            // Act
            List<Task> returnedTaskList = taskDao.getAll();
            // Assert
            assertThat(returnedTaskList)
                    .hasSize(1 + tasksSavedCount)
                    .containsExactlyElementsOf(tasksSavedList);
        }

        /**
         * Test case for trying to get a list of tasks, but is empty.
         * Verifies that an empty list is returned.
         */
        @Test
        void empty_list() {
            // Arrange
            EntityManager entityManager = mock(EntityManager.class);
            TaskDao taskDaoEnv = new TaskDao(entityManager);
            when(entityManager.createQuery(anyString(), eq(Task.class)))
                    .thenThrow(new RuntimeException("Simulated Error"));
            // Act
            List<Task> taskList = taskDaoEnv.getAll();
            // Assert
            assertThat(taskList).isEmpty();
        }
    }

    @Nested
    class task_dao_update {

        /**
         * Test case for updating a task successfully
         * Verifies that the transaction behaves correctly
         */
        @Test
        void task_successfully() {
            // Arrange
            Task newTask = buildPendingTask();
            newTask.setTitle("New title after Update");
            // Act
            entityManager.getTransaction().begin();
            Task taskAfterMerge = taskDao.update(newTask);
            entityManager.getTransaction().commit();
            // Assert
            Task taskUpdated = taskDao.get(taskAfterMerge.getId()).get();
            assertThat(taskAfterMerge.getTitle()).isEqualTo(newTask.getTitle());
            tasksSavedList.add(taskAfterMerge);
            tasksSavedCount++;
        }

    }

    @Nested
    class task_dao_delete {

        @Test
        void task_successfully() {
            // Act
            entityManager.getTransaction().begin();
            taskDao.delete(taskPreSaved);
            entityManager.getTransaction().commit();
            // Assert
            Optional<Task> optionalDeletedTask = taskDao.get(taskPreSaved.getId());
            assertThat(optionalDeletedTask).isEmpty();

            tasksSavedList.remove(taskPreSaved);
            tasksSavedCount--;
        }

        @Test
        void task_with_error() {
            // Arrange
            EntityManager entityManager = mock(EntityManager.class);
            TaskDao taskDao = new TaskDao(entityManager);
            // Mocking EntityManager behavior to throw an exception when remove method is called
            doThrow(RuntimeException.class).when(entityManager).remove(any());
            // Act and Assert
            assertThrows(RuntimeException.class, () -> taskDao.delete(taskPreSaved));
        }
    }
}
