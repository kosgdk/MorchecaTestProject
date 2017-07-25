package morcheca.dao;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import morcheca.dao.interfaces.UserDao;
import morcheca.entities.User;
import net.sf.ehcache.CacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.database.DatabaseUnitils;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.*;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext({"/db_tests/application-context-dbTest.xml", "classpath:security-context.xml"})
@Transactional(value= TransactionMode.DISABLED)
@DataSet("db_tests/dataset/UserDaoHibernateImplTestDataset.xml")
public class UserDaoHibernateImplTest {

	@SpringBeanByType
	UserDao dao;

	private final String name = "TestUser2";
	private final String wrongName = "WrongName";
	private final String email = "testEmail3@example.com";
	private final String wrongEmail = "wrongEmail@example.com";


	@Before
	public void setUp(){
		SQLStatementCountValidator.reset();
		CacheManager.getInstance().clearAll();
		DatabaseUnitils.updateSequences();
	}


	@Test
	public void getById_ShouldReturnEntity(){
		Long id = 1L;
		User user = dao.getById(id);

		assertSelectCount(1);
		assertNotNull(user);
		assertEquals(id, user.getId());
	}

	@Test
	public void getById_IdDoesNotExist_ShouldReturnNull(){
		Long id = 10L;
		User user = dao.getById(id);

		assertSelectCount(1);
		assertNull(user);
	}

	@Test
	public void getByName_ShouldReturnEntity(){
		String name = "TestUser2";
		User user = dao.getByName(name);

		assertSelectCount(1);
		assertNotNull(user);
		assertEquals(name, user.getName());
	}

	@Test
	public void getByName_NameDoesNotExist_ShouldReturnNull(){
		User user = dao.getByName("NonExistentName");

		assertSelectCount(1);
		assertNull(user);
	}

	@Test
	public void getByName_NullName_ShouldReturnNull(){
		User user = dao.getByName(null);

		assertSelectCount(0);
		assertNull(user);
	}

	@Test
	public void getByNameOrEmail_ShouldReturnListOfEntities(){
		List<User> users = dao.getByNameOrEmail(name, email);
		assertSelectCount(1);
		assertNotNull(users);
		assertEquals(2, users.size());
		assertEquals((Long) 2L, users.get(0).getId());
		assertEquals((Long) 3L, users.get(1).getId());
	}

	@Test
	public void getByNameOrEmail_NameDoesNotExist_ShouldReturnListOfEntities(){
		List<User> users = dao.getByNameOrEmail(wrongName, email);
		assertSelectCount(1);
		assertNotNull(users);
		assertEquals(1, users.size());
		assertEquals((Long) 3L, users.get(0).getId());
	}

	@Test
	public void getByNameOrEmail_EmailDoesNotExist_ShouldReturnListOfEntities(){
		List<User> users = dao.getByNameOrEmail(name, wrongEmail);
		assertSelectCount(1);
		assertNotNull(users);
		assertEquals(1, users.size());
		assertEquals((Long) 2L, users.get(0).getId());
	}

	@Test
	public void getByNameOrEmail_NameAndEmailDoesNotExist_ShouldReturnEmptyList(){
		List<User> users = dao.getByNameOrEmail(wrongName, wrongEmail);
		assertSelectCount(1);
		assertNotNull(users);
		assertEquals(0, users.size());
	}

	@Test
	public void getByNameOrEmail_NullName_ShouldReturnListOfEntities(){
		List<User> users = dao.getByNameOrEmail(null, email);
		assertSelectCount(1);
		assertNotNull(users);
		assertEquals(1, users.size());
		assertEquals((Long) 3L, users.get(0).getId());
	}

	@Test
	public void getByNameOrEmail_NullEmail_ShouldReturnListOfEntities(){
		List<User> users = dao.getByNameOrEmail(name, null);
		assertSelectCount(1);
		assertNotNull(users);
		assertEquals(1, users.size());
		assertEquals((Long) 2L, users.get(0).getId());
	}

	@Test
	public void getAll_ShouldReturnListOfEntitiesSortedByIdDesc(){
		List<User> users = dao.getAll();
		assertSelectCount(1);
		assertNotNull(users);
		assertEquals(3, users.size());
		assertEquals((Long) 3L, users.get(0).getId());
		assertEquals((Long) 2L, users.get(1).getId());
		assertEquals((Long) 1L, users.get(2).getId());
	}

	@Test
	@ExpectedDataSet("db_tests/dataset/UserDaoHibernateImplTest.save_shouldSaveEntity.ExpectedDataSet.xml")
	public void save_ShouldSaveEntity(){
		User user = new User("TestUser4", "testEmail4@example.com", "password4", "password4");

		dao.save(user);
		assertInsertCount(1);
		assertEquals((Long) 1000L, user.getId());
	}

	@Test
	public void save_NullEntity_ShouldNotQueryDatabase(){
		dao.save(null);
		assertInsertCount(0);
	}

	@Test
	@ExpectedDataSet("db_tests/dataset/UserDaoHibernateImplTest.update_EncryptPasswordTrue_ShouldEncryptPasswordAndUpdateEntity.ExpectedDataSet.xml")
	public void update_EncryptPasswordTrue_ShouldEncryptPasswordAndUpdateEntity(){
		User user = dao.getById(1L);
		user.setName("NewTestUser1");
		user.setEmail("newTestEmail1@example.com");
		user.setPassword("newPassword1");

		dao.update(user, true);
		assertUpdateCount(1);
	}

	@Test
	@ExpectedDataSet("db_tests/dataset/UserDaoHibernateImplTest.update_EncryptPasswordFalse_ShouldNotEncryptPasswordAndDoUpdateEntity.ExpectedDataSet.xml")
	public void update_EncryptPasswordFalse_ShouldNotEncryptPasswordAndDoUpdateEntity(){
		User user = dao.getById(1L);
		user.setName("NewTestUser1");
		user.setEmail("newTestEmail1@example.com");
		user.setPassword("newPassword1");

		dao.update(user, false);
		assertUpdateCount(1);
	}

	@Test
	public void update_NullEntity_ShouldNotQueryDatabase(){
		dao.update(null, true);
		assertUpdateCount(0);
	}

	@Test
	@ExpectedDataSet("db_tests/dataset/UserDaoHibernateImplTest.delete_ShouldDeleteEntity.ExpectedDataSet.xml")
	public void delete_ShouldDeleteEntity(){
		User user = dao.getById(1L);

		dao.delete(user);
		assertDeleteCount(1);
	}

	@Test
	public void delete_NullEntity_ShouldNotQueryDatabase(){
		dao.delete(null);
		assertDeleteCount(0);
	}

	@Test
	@ExpectedDataSet("db_tests/dataset/UserDaoHibernateImplTest.delete_ShouldDeleteEntity.ExpectedDataSet.xml")
	public void deleteById_ShouldDeleteEntity(){
		dao.deleteById(1L);
		assertDeleteCount(1);
	}

	@Test
	public void deleteById_NullId_ShouldNotQueryDatabase(){
		dao.deleteById(null);
		assertDeleteCount(0);
	}

}