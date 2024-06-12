package com.jjpedrogomes.controller.filter;

import com.jjpedrogomes.controller.filter.EntityManagerFilter;
import com.jjpedrogomes.model.util.JpaUtil;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EntityManagerUnitTest {

    private EntityManager entityManager;
    private EntityManagerFilter entityManagerFilter;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private FilterChain filterChainMock;
    EntityTransaction transactionMock;

    @BeforeAll
    static void setUpStaticMocks() {
        mockStatic(JpaUtil.class);
    }

    @BeforeEach
    void setUp() {
        entityManagerFilter = new EntityManagerFilter();
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        filterChainMock = mock(FilterChain.class);
        entityManager = mock(EntityManager.class);
        transactionMock = mock(EntityTransaction.class);

        BDDMockito.given(JpaUtil.getEntityManager()).willReturn(entityManager);
    }

    @Nested
    class transactional_requests {

        @Test
        void successful() throws ServletException, IOException {
            // Arrange
            when(requestMock.getMethod()).thenReturn("POST");
            when(entityManager.getTransaction()).thenReturn(transactionMock);
            when(entityManager.getTransaction().isActive()).thenReturn(Boolean.TRUE);
            // Act
            entityManagerFilter.doFilter(requestMock, responseMock, filterChainMock);
            // Assert
            verify(entityManager.getTransaction()).begin();
            verify(requestMock).setAttribute("entityManager", entityManager);
            verify(filterChainMock).doFilter(requestMock, responseMock);
            verify(entityManager.getTransaction()).commit();
            verify(entityManager).close();
        }

        @Test
        void with_error() throws ServletException, IOException {
            // Arrange
            when(entityManager.getTransaction()).thenReturn(transactionMock);
            when(transactionMock.isActive()).thenReturn(true);
            when(requestMock.getMethod()).thenReturn("POST");
            // Arrange behavior for filterChainMock to throw an exception
            doThrow(new RuntimeException("Simulated Error")).when(filterChainMock).doFilter(requestMock, responseMock);
            // Act
            assertThrows(ServletException.class, () -> {
                entityManagerFilter.doFilter(requestMock, responseMock, filterChainMock);
            });
            // Assert
            verify(transactionMock).rollback();
            verify(entityManager).close();
        }

        @Test
        void not_beginning_transaction() throws ServletException, IOException {
            // Arrange
            when(requestMock.getMethod()).thenReturn("GET");
            when(entityManager.getTransaction()).thenReturn(transactionMock);
            when(entityManager.getTransaction().isActive()).thenReturn(Boolean.FALSE);
            // Act
            entityManagerFilter.doFilter(requestMock, responseMock, filterChainMock);
            // Assert
            verify(transactionMock, never()).begin();
            verify(transactionMock, never()).commit();
        }
    }
}
