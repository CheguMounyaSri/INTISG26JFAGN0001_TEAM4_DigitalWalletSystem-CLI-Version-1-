
import static org.junit.jupiter.api.Assertions.*;

import com.cognizant.digitalwalletsystem.dao.KYCDao;
import com.cognizant.digitalwalletsystem.dao.KYCDaoImpl;
import com.cognizant.digitalwalletsystem.dao.UserDao;
import com.cognizant.digitalwalletsystem.dao.UserDaoImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.cognizant.digitalwalletsystem.model.KYC;
import com.cognizant.digitalwalletsystem.model.User;
import java.sql.SQLException;

public class KYCDAOTest {
    private static KYCDao kycDao;
    private static UserDao userDao;

    @BeforeAll
    static void init() {
        kycDao = new KYCDaoImpl();
        userDao = new UserDaoImpl();
    }

    @Test
    void testKYCOperations() throws SQLException {
        // Setup: Need a user first
        User user = userDao.save(new User("KYC User", "kyc" + System.currentTimeMillis() + "@test.com", "1234567890", "pass"));

        String docNum = "DOC" + System.currentTimeMillis();
        KYC kyc = new KYC(user.getId(), "PAN", docNum, "PENDING");

        // Test 1: Save KYC
        KYC savedKyc = kycDao.save(kyc);
        assertNotNull(savedKyc);

        // Test 2: Find by User ID
        KYC found = kycDao.findByUserId(user.getId());
        assertEquals("PENDING", found.getStatus());

        // Test 3: Update Status (Admin Action)
        kycDao.updateStatus(user.getId(), "APPROVED");
        KYC updated = kycDao.findByUserId(user.getId());
        assertEquals("APPROVED", updated.getStatus());
    }
}