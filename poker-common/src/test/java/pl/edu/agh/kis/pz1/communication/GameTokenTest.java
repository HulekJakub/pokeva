package pl.edu.agh.kis.pz1.communication;

import org.junit.Test;
import pl.edu.agh.kis.pz1.communication.exceptions.BadGameTokenException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
/**
 * Test class
 */
public class GameTokenTest {
    @Test
    public void toToken() {
        //TODO
        assertTrue(true);
    }

    @Test
    public void throwBadGameTokenExceptionTest() throws BadGameTokenException {
        assertThrows(BadGameTokenException.class, this::testGameTokenConstructor);
    }

    private void testGameTokenConstructor() throws BadGameTokenException {
        new GameToken("123 321");
    }
}
