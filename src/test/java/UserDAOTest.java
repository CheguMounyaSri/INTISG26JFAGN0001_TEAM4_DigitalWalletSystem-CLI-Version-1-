import static org.junit.jupiter.api.Assertions.*;

import com.cognizant.digitalwalletsystem.dao.UserDao;
import com.cognizant.digitalwalletsystem.dao.UserDaoImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.cognizant.digitalwalletsystem.model.User;
import java.sql.SQLException;

public class UserDAOTest {
    private static UserDao userDao;

    @BeforeAll
    static void init() {
        userDao = new UserDaoImpl();
    }

    @Test
    void testUserLifecycle() throws SQLException {
        String email = "test" + System.currentTimeMillis() + "@gmail.com";
        User user = new User("mounya", email, "123456788", "hashed_pass");

        // Test 1: Save
        User savedUser = userDao.save(user);
        assertNotNull(savedUser);
        assertTrue(savedUser.getId() > 0);

        // Test 2: Find by email
        User found = userDao.findByEmail(email);
        assertNotNull(found);
        assertEquals("mounya", found.getName());

        // Test 3: Exists check (If your DAO has this method)
        // assertTrue(userDao.existsByEmail(email));
    }
}