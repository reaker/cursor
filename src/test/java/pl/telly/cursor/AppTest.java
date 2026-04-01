package pl.telly.cursor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void greetReturnsExpectedMessage() {
        App app = new App();
        assertEquals("Hello, World!", app.greet("World"));
    }
}
