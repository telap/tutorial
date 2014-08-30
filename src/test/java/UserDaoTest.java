import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.telap.dao.BaseDaoTestCase;
import org.telap.dao.UserDao;
import org.telap.model.User;

public class UserDaoTest extends BaseDaoTestCase {
	@Autowired
	private UserDao dao;

	@Test(expected = DataAccessException.class)
	public void testGetUserInvalid() throws Exception {
		dao.get(1000L);
	}

	@Test
	public void testUserSearch() throws Exception {
		dao.reindex();
		List<User> found = dao.search("Matt");
		User user = found.get(0);
		user = dao.get(-2L);
		dao.saveUser(user);
		flush();
		flushSearchIndexes();
		found = dao.search("MattX");
		user = found.get(0);
	}
}
