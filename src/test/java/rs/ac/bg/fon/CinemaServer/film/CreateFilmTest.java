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

public class CreateFilmTest{
	private AbstractGenericOperation createFilm;
	private static Film film;
	
	@BeforeAll
	static void start() throws Exception {
		FileOutputStream out = new FileOutputStream("config/dbconfig.properties");
		Properties properties = new Properties();
        properties.setProperty("url","jdbc:mysql://localhost:3306/cinema-test");
        properties.setProperty("username", "root");
        properties.setProperty("password", "root");
        properties.store(out, null);
        out.close();
        
		film = new Film();
	}
	@AfterAll
	static void end() throws Exception{
        AbstractGenericOperation deleteFilm = new DeleteFilm();
        deleteFilm.execute(film);
        
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
		createFilm = new CreateFilm();
	}
	
	@AfterEach
	void tearDown() throws Exception{
		createFilm = null;
	}
	
	@Test
	void constructorTest() {
		createFilm = new CreateFilm();
		assertNotNull(createFilm);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> createFilm.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> createFilm.execute(new Term()));
	}
	
	@Test
	void testAddFilm() throws Exception {
		film.setDuration(100);
		film.setLanguage("eng");
		film.setName("test");
		film.setYear(2021);
				
		createFilm.execute(film);
		
		AbstractGenericOperation readFilms = new ReadFilms();
		readFilms.execute(new Film());
		List<GenericEntity> films = ((ReadFilms)readFilms).getList();
		
		assertEquals(2, films.size());
		film = (Film)films.get(1);
		
		assertNotNull(films);
		assertNotNull(film);
		assertEquals(2021, film.getYear());
		assertEquals("test", film.getName());
		assertEquals("eng", film.getLanguage());
		assertEquals(100, film.getDuration());
	}
}
