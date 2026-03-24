import static org.junit.jupiter.api.Assertions.*;

import com.cognizant.digitalwalletsystem.service.UserService;
import com.cognizant.digitalwalletsystem.service.WalletService;
import org.junit.jupiter.api.Test;
import com.cognizant.digitalwalletsystem.factory.ServiceFactory;
import com.cognizant.digitalwalletsystem.model.User;
import com.cognizant.digitalwalletsystem.model.Wallet;
import java.math.BigDecimal;

public class WalletDepositTest {

    @Test
    void testBasicDeposit() throws Exception {
        UserService userService = ServiceFactory.getInstance().getUserService();
        WalletService walletService = ServiceFactory.getInstance().getWalletService();

        // 1. Create a fresh user
        String email = "deposit" + System.currentTimeMillis() + "@test.com";
        User user = userService.registerUser("Depositor", email, "9111111111", "Secured@1");

        // 2. Deposit 100.00
        BigDecimal amount = new BigDecimal("100.00");
        walletService.deposit(user.getId(), amount);

        // 3. Verify balance from DB
        Wallet wallet = walletService.getWalletByUserId(user.getId());

        // Using compareTo because BigDecimals with different scales (100 vs 100.00)
        // return 'false' in .equals()
        assertEquals(0, amount.compareTo(wallet.getBalance()), "Balance should be 100.00");
    }
}