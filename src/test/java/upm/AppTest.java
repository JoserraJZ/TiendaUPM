package test.java.upm;

import main.java.upm.App;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AppTest {
    @Test
    void testApp() {
        assertDoesNotThrow(App::new);
    }
}
