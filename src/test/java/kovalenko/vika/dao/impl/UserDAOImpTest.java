package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.AbstractDAOTest;
import kovalenko.vika.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static kovalenko.vika.dao.H2Env.LAST_ID_USERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserDAOImpTest extends AbstractDAOTest {
    private String expUsername;
    private String expFirstName;
    private String expLastName;
    private String expPassHash;
    UserDAOImp dao;

    @Override
    @BeforeEach
    protected void init() {
        dao = new UserDAOImp(factory);
        session.getTransaction().begin();
    }

    @Test
    void save_new_user() {
        long expId = LAST_ID_USERS + 1;
        expUsername = "test_username";
        expFirstName = "Java";
        expLastName = "Rush";
        expPassHash = "some_password_hash";

        var user = User.builder()
                .username(expUsername)
                .firstName(expFirstName)
                .lastName(expLastName)
                .passwordHash(expPassHash)
                .build();

        User actual = dao.save(user);

        assertEquals(expId, actual.getId());
        assertEquals(expUsername, actual.getUsername());
        assertEquals(expFirstName, actual.getFirstName());
        assertEquals(expLastName, actual.getLastName());
        assertEquals(expPassHash, actual.getPasswordHash());

        assertNotNull(actual.getCreateDate());
        assertNotNull(actual.getLastUpdate());
    }

    @Test
    void update_user_password() {
        expUsername = "test1";
        expFirstName = "John";
        expLastName = "Brown";
        expPassHash = "new_password_hash";

        User user = dao.getUserByUsername(expUsername, session);
        user.setPasswordHash(expPassHash);

        User updated = dao.update(user);

        assertEquals(expUsername, updated.getUsername());
        assertEquals(expFirstName, updated.getFirstName());
        assertEquals(expLastName, updated.getLastName());
        assertEquals(expPassHash, updated.getPasswordHash());
    }

    @Test
    void get_user_by_username() {
        expUsername = "test3";
        expFirstName = "Lewis";
        expLastName = "Miller";

        User user = dao.getUserByUsername(expUsername, session);

        assertEquals(expUsername, user.getUsername());
        assertEquals(expFirstName, user.getFirstName());
        assertEquals(expLastName, user.getLastName());
    }

    @Test
    void get_user_id_by_username() {
        long expId = 2L;
        expUsername = "test2";

        assertEquals(expId, dao.getUserId(expUsername));
    }
}
