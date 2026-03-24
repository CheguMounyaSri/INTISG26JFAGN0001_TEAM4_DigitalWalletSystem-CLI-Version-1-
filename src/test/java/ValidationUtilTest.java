
import static org.junit.jupiter.api.Assertions.*;

import com.cognizant.digitalwalletsystem.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import com.cognizant.digitalwalletsystem.exception.InvalidInputException;
import java.math.BigDecimal;

public class ValidationUtilTest {

    @Test
    void testEmailValidation() {
        // Valid email should not throw anything
        assertDoesNotThrow(() -> ValidationUtil.validateEmail("test@gmail.com"));

        // Invalid email should throw exception
        assertThrows(InvalidInputException.class, () -> ValidationUtil.validateEmail("wrong-email"));
    }

    @Test
    void testAmountValidation() {
        // Negative amount should fail
        assertThrows(InvalidInputException.class, () -> {
            ValidationUtil.validateAmount(new BigDecimal("-50.00"));
        });

        // Zero should fail
        assertThrows(InvalidInputException.class, () -> {
            ValidationUtil.validateAmount(BigDecimal.ZERO);
        });
    }
}