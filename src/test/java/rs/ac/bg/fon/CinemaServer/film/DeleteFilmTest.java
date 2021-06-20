package rs.ac.bg.fon.CinemaServer.film;

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
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
import rs.ac.bg.fon.CinemaServer.operation.film.CreateFilm;
import rs.ac.bg.fon.CinemaServer.operation.film.DeleteFilm;
import rs.ac.bg.fon.CinemaServer.operation.film.ReadFilmsWhere;

public class DeleteFilmTest {
	private AbstractGenericOperation deleteFilm;
	
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
		deleteFilm = new DeleteFilm();
	}
	
	@AfterEach
	void tearDown() throws Exception{
		deleteFilm = null;
	}
	
	@Test
	void constructorTest() {
		deleteFilm = new DeleteFilm();
		assertNotNull(deleteFilm);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> deleteFilm.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> deleteFilm.execute(new Term()));
	}
	
	@Test
	void testDeleteFilm() throws Exception {
		Film film = new Film();
		film.setDuration(100);
		film.setLanguage("eng");
		film.setName("test2");
		film.setYear(2021);
				
		AbstractGenericOperation createFilm = new CreateFilm();
		createFilm.execute(film);
		
		AbstractGenericOperation readFilmsWhere = new ReadFilmsWhere(" name = 'test2'");
		readFilmsWhere.execute(new Film());
		List<GenericEntity> films = ((ReadFilmsWhere)readFilmsWhere).getList();
		
		deleteFilm.execute(films.get(0));
		
		readFilmsWhere = new ReadFilmsWhere(" name = 'test2'");
		readFilmsWhere.execute(new Film());
		films = ((ReadFilmsWhere)readFilmsWhere).getList();
		
		assertEquals(0, films.size());
	}
}
