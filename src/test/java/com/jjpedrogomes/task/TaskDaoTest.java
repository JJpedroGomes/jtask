package com.jjpedrogomes.task;

import com.jjpedrogomes.model.lane.Lane;
import com.jjpedrogomes.model.lane.LaneDao;
import com.jjpedrogomes.model.lane.LaneDaoFactory;
import com.jjpedrogomes.model.lane.LaneFactory;
import com.jjpedrogomes.model.lane.LaneServiceFactory;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.user.Email;
import com.jjpedrogomes.model.user.Password;
import com.jjpedrogomes.model.user.User;
import com.jjpedrogomes.model.util.JpaUtil;
import com.jjpedrogomes.repository.task.TaskDaoImpl;
import com.jjpedrogomes.user.UserDaoTest;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.time.LocalDate;
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

    private TaskDaoImpl taskDao;
    private EntityManager entityManager;
    private Task task;
    private Task taskPreSaved;
    private int tasksSavedCount;
    private List<Task> tasksSavedList = new ArrayList<Task>();
    
    void setUpBeforeAll() {
    	User user = new User("Gabriel Toledo", new Email("taskdaotest@email.com"), new Password("a1b2c3d4"), LocalDate.of(1974, 9, 27));
    	UserDaoTest.persistUser(user);
    	
    	Lane lane = LaneServiceFactory.getInstance().createLane("todo", user);
    	
    	this.taskPreSaved = new Task("new", null, null, lane);
    	LaneDaoFactory.getInstance().update(lane);
    }

	@BeforeEach
    void setUpBeforeEach() {
        this.task = buildInProgressTask();
        this.entityManager = JpaUtil.getEntityManager();
        this.taskDao = new TaskDaoImpl(entityManager);
    }

    @AfterEach
    void setUpAfterEach() {
        this.entityManager.close();
    }

//    @Nested
//    class task_dao_save {
//
//        @Test
//        void new_task() {
//            // Act
//            entityManager.getTransaction().begin();
//            taskDao.save(task);
//            entityManager.getTransaction().commit();
//            // Assert
//            assertThat(task.getId()).isNotNull();
//
//            tasksSavedCount++;
//            tasksSavedList.add(task);
//        }
//
//        @Test
//        void with_error() throws NoSuchFieldException, IllegalAccessException {
//            // Arrange
//            Field title = Task.class.getDeclaredField("title");
//            title.setAccessible(true);
//            title.set(task, null);
//            // Assert and Act
//            entityManager.getTransaction().begin();
//            assertThrows(Exception.class, () -> taskDao.save(task));
//            entityManager.getTransaction().commit();
//        }
//    }

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
            TaskDaoImpl taskDaoEnv = new TaskDaoImpl(entityManager);
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
            TaskDaoImpl taskDaoEnv = new TaskDaoImpl(entityManager);
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
            TaskDaoImpl taskDao = new TaskDaoImpl(entityManager);
            // Mocking EntityManager behavior to throw an exception when remove method is called
            doThrow(RuntimeException.class).when(entityManager).remove(any());
            // Act and Assert
            assertThrows(RuntimeException.class, () -> taskDao.delete(taskPreSaved));
        }
    }
}
