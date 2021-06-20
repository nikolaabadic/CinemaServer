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
import rs.ac.bg.fon.CinemaServer.operation.film.ReadFilms;

public class ReadFilmsTest {
	private AbstractGenericOperation readFilms;
	private static Film createdFilm;
	
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
        AbstractGenericOperation deleteFilm = new DeleteFilm();
        deleteFilm.execute(createdFilm);
        
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
		readFilms = new ReadFilms();
	}
	
	@AfterEach
	void tearDown() throws Exception{
		readFilms = null;
	}
	
	@Test
	void constructorTest() {
		readFilms = new ReadFilms();
		assertNotNull(readFilms);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> readFilms.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> readFilms.execute(new Term()));
	}
	
	@Test
	void testReadFilms() throws Exception {
		Film film = new Film();
		film.setDuration(100);
		film.setLanguage("eng");
		film.setName("test2");
		film.setYear(2021);
				
		AbstractGenericOperation createFilm = new CreateFilm();
		createFilm.execute(film);
		
		readFilms.execute(new Film());
		List<GenericEntity> films = ((ReadFilms)readFilms).getList();
		
		assertNotNull(films);
		assertEquals(2, films.size());
		
		createdFilm = (Film)films.get(1);
		assertEquals(film.getFilmID(), createdFilm.getFilmID());
	}
}
