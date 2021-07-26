package ucf.assignments;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void verifySerialNumberFormat() {
        String goodSerialNumber = "1234567890";
        String badSerialNumber = "bad123";
        Assertions.assertEquals(true, Item.verifySerialNumberFormat(goodSerialNumber));
        Assertions.assertEquals(false, Item.verifySerialNumberFormat(badSerialNumber));
    }

    @Test
    void verifyValueFormat() {
        String goodValue = "299.99";
        String badValue = "399.999";
        Assertions.assertEquals(true, Item.verifyValueFormat(goodValue));
        Assertions.assertEquals(false, Item.verifyValueFormat(badValue));
    }

    @Test
    void verifyNameFormat() {
        String goodName = "Xbox";
        String badName = "1";
        String tooLongName = "Testing string that is too long, Testing string that is too long, " +
                            "Testing string that is too long, Testing string that is too long, " +
                            "Testing string that is too long, Testing string that is too long, " +
                            "Testing string that is too long, Testing string that is too long, " +
                            "Testing string that is too long, Testing string that is too long, " +
                            "Testing string that is too long, Testing string that is too long, " +
                            "Testing string that is too long, Testing string that is too long, ";
        Assertions.assertEquals(true, Item.verifyNameFormat(goodName));
        Assertions.assertEquals(false, Item.verifyNameFormat(badName));
        Assertions.assertEquals(false, Item.verifyNameFormat(tooLongName));
    }
}