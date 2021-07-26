package ucf.assignments;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serial;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    @Test
    void serializeToHTML() {
        Serializer testSerializer = new Serializer("SWITCH1234", "Nintendo Switch", 299.99);
        String expectedValue = "  \t\t<tr>\n" +
                "  \t\t\t<td>" + "SWITCH1234" + "</td>\n" +
                "  \t\t\t<td>" + "Nintendo Switch" + "</td>\n" +
                "  \t\t\t<td>" + "299.99" + "</td>\n" +
                "  \t\t</tr>\n";
        Assertions.assertEquals(expectedValue, testSerializer.serializeToHTML());
    }
}