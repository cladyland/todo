package kovalenko.vika.service.impl;

import jakarta.persistence.NoResultException;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.exception.ValidationException;
import kovalenko.vika.model.User;
import kovalenko.vika.service.Verifier;
import kovalenko.vika.utils.Hashing;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {
    final String TEST = "test";
    @Mock
    UserDAO userDAO;
    @Mock
    Hashing hashing;
    @Mock
    Session session;
    @Mock
    User userMock;
    UserServiceImp service;

    @BeforeEach
    void init() {
        service = new UserServiceImp(userDAO, hashing);
        when(userDAO.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(Mockito.mock(Transaction.class));
    }

    @Test
    void get_user_id() {
        when(userDAO.getUserId(anyString())).thenReturn(1L);

        service.getUserId(TEST);

        Verifier.transactionBegins(session);
        verify(userDAO, times(1)).getUserId(TEST);
        Verifier.transactionCommitted(session);
    }

    @Test
    void validate() {
        when(userDAO.getUserByUsername(TEST, session)).thenReturn(userMock);
        when(userMock.getUsername()).thenReturn(TEST);
        when(userMock.getPasswordHash()).thenReturn(TEST);
        when(hashing.validatePassword(anyString(), anyString())).thenReturn(true);

        service.validate(TEST, TEST);

        Verifier.transactionBegins(session);
        verify(userDAO, times(1)).getUserByUsername(TEST, session);
        verify(hashing, times(1)).validatePassword(TEST, TEST);
        Verifier.transactionCommitted(session);
    }

    @Test
    void validate_throws_exception_when_username_incorrect() {
        when(userDAO.getUserByUsername(TEST, session)).thenReturn(userMock);
        when(userMock.getUsername()).thenReturn(TEST.toUpperCase());

        assertThrowsValidationException();

        Verifier.transactionBegins(session);
        verify(hashing, never()).validatePassword(TEST, TEST);
        Verifier.transactionNotCommitted(session);
    }

    @Test
    void validate_throws_exception_when_username_not_exist() {
        when(userDAO.getUserByUsername(TEST, session)).thenThrow(NoResultException.class);

        assertThrowsValidationException();

        Verifier.transactionBegins(session);
        verify(hashing, never()).validatePassword(TEST, TEST);
        Verifier.transactionNotCommitted(session);
    }

    @Test
    void validate_throws_exception_when_password_incorrect() {
        when(userDAO.getUserByUsername(TEST, session)).thenReturn(userMock);
        when(userMock.getUsername()).thenReturn(TEST);
        when(userMock.getPasswordHash()).thenReturn(TEST);
        when(hashing.validatePassword(anyString(), anyString())).thenReturn(false);

        assertThrowsValidationException();

        Verifier.transactionBegins(session);
        verify(hashing, times(1)).validatePassword(TEST, TEST);
        Verifier.transactionNotCommitted(session);
    }

    private void assertThrowsValidationException() {
        assertThrows(ValidationException.class, () -> service.validate(TEST, TEST));
    }
}
