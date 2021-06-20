package rs.ac.bg.fon.CinemaServer.hall;

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
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
import rs.ac.bg.fon.CinemaServer.operation.hall.ReadHallsWhere;

public class ReadHallsWhereTest {
	private AbstractGenericOperation readHallsWhere;
	
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
		readHallsWhere = new ReadHallsWhere("");
	}
	
	@AfterEach
	void tearDown() throws Exception{
		readHallsWhere = null;
	}
	
	@Test
	void constructorTest() {
		readHallsWhere = new ReadHallsWhere("");
		assertNotNull(readHallsWhere);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> readHallsWhere.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> readHallsWhere.execute(new Film()));
	}
	
	@Test
	void testIncorrectWhereString() {
		readHallsWhere = new ReadHallsWhere(" hall = 1");
		assertThrows(java.lang.Exception.class, () -> readHallsWhere.execute(new Hall()));
	}
	
	@Test
	void testReadHallsWhere() throws Exception {
		readHallsWhere = new ReadHallsWhere(" hallID = 5");
		readHallsWhere.execute(new Hall());
		List<GenericEntity> halls = ((ReadHallsWhere)readHallsWhere).getList();
		
		assertNotNull(halls);
		assertEquals(1, halls.size());
		
		Hall h = (Hall)halls.get(0);		
		assertEquals(5, h.getHallID());
		assertEquals("hall 1", h.getName());
		assertEquals(100, h.getCapacity());
	}
}
