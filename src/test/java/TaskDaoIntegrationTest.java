import com.jjpedrogomes.controller.filter.EntityManagerFilter;
import com.jjpedrogomes.model.task.Task;
import com.jjpedrogomes.model.util.JpaUtil;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TaskDaoIntegrationTest {

    private EntityManager entityManager;
    @Mock
    private Task task;
    private EntityManagerFilter entityManagerFilter;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private FilterChain filterChainMock;

    @BeforeEach
    void setUp() {
        // Create EntityManager instance
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test");
        entityManager = entityManagerFactory.createEntityManager();

        // Initialize other dependencies
        entityManagerFilter = new EntityManagerFilter();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        filterChainMock = mock(FilterChain.class);
    }

    @Nested
    class task_dao_save {

        /**
         * Test case for saving a new task.
         * Verifies that the save operation is performed correctly by mocking EntityManager and EntityTransaction.
         */
    //        @Test
    //        void new_task() {
    //            // Arrange
    //            when(entityManagerMock.getTransaction()).thenReturn(Mockito.mock(EntityTransaction.class));
    //            // Act
    ////            taskDao.save(task);
    //            // Assert
    //            verify(entityManagerMock).persist(task);
    //        }

        //Todo: Solve this
        /**
         * Test case for trying to save a task, but an error occurs.
         * Verifies that the save operation fails gracefully and the transaction is rolled back.
         */
        @Test
        void with_error() throws ServletException, IOException {
            // Arrange
            EntityTransaction transactionMock = mock(EntityTransaction.class); // Create a mock EntityTransaction
            when(entityManager.getTransaction()).thenReturn(transactionMock); // Return the mock EntityTransaction

            // Arrange behavior for filterChainMock to throw an exception
            doThrow(new RuntimeException("Simulated Error")).when(filterChainMock).doFilter(requestMock, responseMock);

            // Act
            entityManagerFilter.doFilter(requestMock, responseMock, filterChainMock);

            // Assert
            verify(transactionMock).rollback(); // Verify that rollback was called on the mock EntityTransaction
            verify(entityManager).close();
        }
    }
}
