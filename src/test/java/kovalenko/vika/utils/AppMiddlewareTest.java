package kovalenko.vika.utils;

import kovalenko.vika.exception.TaskException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppMiddlewareTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "       "})
    void when_title_is_blank_throw_task_exception(String title) {
        assertThrows(TaskException.class, () -> AppMiddleware.checkIfTitleIsBlank(title));
    }

    @Test
    void when_title_is_not_blank_do_not_throw_exception() {
        assertDoesNotThrow(() -> AppMiddleware.checkIfTitleIsBlank("title"));
    }

    @Test
    void when_number_equals_zero_return_true() {
        assertTrue(AppMiddleware.numberEqualsZero(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 1, -1, Integer.MAX_VALUE})
    void when_number_not_zero_return_false(int value) {
        assertFalse(AppMiddleware.numberEqualsZero(value));
    }
}
