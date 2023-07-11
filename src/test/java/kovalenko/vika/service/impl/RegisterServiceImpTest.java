package kovalenko.vika.service.impl;

import jakarta.persistence.NoResultException;
import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.exception.RegisterException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceImpTest {
    final String TEST = "test";
    final String PASSWORD = "12_ABck*";
    final UserCommand VALID_USER = createUserData(TEST, TEST, TEST, PASSWORD);
    RegisterServiceImp service;
    @Mock
    UserDAO userDAO;
    @Mock
    Hashing hashing;
    @Mock
    Session session;

    @BeforeEach
    void init() {
        service = new RegisterServiceImp(userDAO, hashing);
        when(userDAO.getCurrentSession()).thenReturn(session);
        when(session.getTransaction()).thenReturn(Mockito.mock(Transaction.class));
        when(hashing.isPasswordStrong(anyString())).thenReturn(true);
    }

    @Test
    void register_success_with_valid_data() {
        when(userDAO.getUserId(anyString())).thenThrow(NoResultException.class);

        service.register(VALID_USER);

        Verifier.transactionBegins(session);
        verify(hashing, times(1)).getPasswordHash(PASSWORD);
        verify(userDAO, times(1)).save(any(User.class));
        Verifier.transactionCommitted(session);
    }

    @Test
    void register_throws_exception_when_username_is_busy() {
        when(userDAO.getUserId(anyString())).thenReturn(1L);

        assertThrowsRegisterException(VALID_USER);

        Verifier.transactionBegins(session);
        verify(hashing, times(1)).getPasswordHash(PASSWORD);
        verify(userDAO, never()).save(any(User.class));
        Verifier.transactionNotCommitted(session);
    }

    @Test
    void register_throws_exception_when_invalid_first_name() {
        var user = createUserData("test1", TEST, TEST, PASSWORD);

        assertThrowsRegisterException(user);
        verifyWhenInvalidData(PASSWORD);
    }

    @Test
    void register_throws_exception_when_invalid_last_name() {
        var user = createUserData(TEST, "test1", TEST, PASSWORD);

        assertThrowsRegisterException(user);
        verifyWhenInvalidData(PASSWORD);
    }

    @Test
    void register_throws_exception_when_invalid_username() {
        var user = createUserData(TEST, TEST, "<test>", PASSWORD);

        assertThrowsRegisterException(user);
        verifyWhenInvalidData(PASSWORD);
    }

    @Test
    void register_throws_exception_when_invalid_password() {
        var user = createUserData(TEST, TEST, TEST, "12_AB");
        when(hashing.isPasswordStrong(anyString())).thenReturn(false);

        assertThrowsRegisterException(user);
        verifyWhenInvalidData("12_AB");
    }

    private void assertThrowsRegisterException(UserCommand user) {
        assertThrows(RegisterException.class, () -> service.register(user));
    }

    private void verifyWhenInvalidData(String password) {
        Verifier.transactionBegins(session);
        verify(hashing, never()).getPasswordHash(password);
        verify(userDAO, never()).save(any(User.class));
        Verifier.transactionNotCommitted(session);
    }

    private UserCommand createUserData(String firstName, String lastName, String username, String password) {
        return UserCommand.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .build();
    }
}
