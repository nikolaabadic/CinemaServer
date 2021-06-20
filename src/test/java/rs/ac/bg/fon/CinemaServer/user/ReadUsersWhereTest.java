package rs.ac.bg.fon.CinemaServer.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.User;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
import rs.ac.bg.fon.CinemaServer.operation.user.ReadUsersWhere;

public class ReadUsersWhereTest {
	private AbstractGenericOperation readUsersWhere;
	
	@BeforeAll
	static void start() throws Exception {
		FileOutputStream out = new FileOutputStream("config/dbconfig.properties");
		Properties properties = new Properties();
        properties.setProperty("url","jdbc:mysql://localhost:3306/cinema-test");
        properties.setProperty("username", "root");
        properties.setProperty("password", "root");
        properties.store(out, null);
        out.close();
	}
	
	@AfterAll
	static void end() throws Exception{             
		FileOutputStream out = new FileOutputStream("config/dbconfig.properties");
		Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://localhost:3306/cinema");
        properties.setProperty("username", "root");
        properties.setProperty("password", "root");
        properties.store(out, null);
        out.close();
    }
	
	@BeforeEach
	void setUp() throws Exception{
		readUsersWhere = new ReadUsersWhere("");
	}
	
	@AfterEach
	void tearDown() throws Exception{
		readUsersWhere = null;
	}
	
	@Test
	void constructorTest() {
		readUsersWhere = new ReadUsersWhere("");
		assertNotNull(readUsersWhere);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> readUsersWhere.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> readUsersWhere.execute(new Film()));
	}
	
	@Test
	void testIncorrectWhereString() {
		readUsersWhere = new ReadUsersWhere(" user = 1");
		assertThrows(java.lang.Exception.class, () -> readUsersWhere.execute(new User()));
	}
	
	@Test
	void testReadUsersWhere() throws Exception {
		readUsersWhere = new ReadUsersWhere(" userID = 5 ");
		readUsersWhere.execute(new User());
		List<GenericEntity> users = ((ReadUsersWhere)readUsersWhere).getList();
		
		assertNotNull(users);
		assertEquals(1, users.size());
		
		User u = (User)users.get(0);
		assertEquals(5, u.getUserID());
		assertEquals("user ", u.getName());
		assertEquals("user", u.getSurname());
		assertEquals("user", u.getUsername());
		assertEquals("user", u.getPassword());
	}
}
