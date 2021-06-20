package rs.ac.bg.fon.CinemaServer.term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
import rs.ac.bg.fon.CinemaServer.operation.film.CreateFilm;
import rs.ac.bg.fon.CinemaServer.operation.film.ReadFilms;
import rs.ac.bg.fon.CinemaServer.operation.hall.ReadHalls;
import rs.ac.bg.fon.CinemaServer.operation.terms.CreateTerm;
import rs.ac.bg.fon.CinemaServer.operation.terms.DeleteTerm;
import rs.ac.bg.fon.CinemaServer.operation.terms.ReadTerms;

public class CreateTermTest {
	private AbstractGenericOperation createTerm;
	private static Hall hall;
	private static Film film;
	private static Term term;
	
	@BeforeAll
	static void start() throws Exception {
		FileOutputStream out = new FileOutputStream("config/dbconfig.properties");
		Properties properties = new Properties();
        properties.setProperty("url","jdbc:mysql://localhost:3306/cinema-test");
        properties.setProperty("username", "root");
        properties.setProperty("password", "root");
        properties.store(out, null);
        out.close();        
        
        AbstractGenericOperation readHalls = new ReadHalls();
        readHalls.execute(new Hall());
        hall = (Hall)((ReadHalls)readHalls).getList().get(0);
        
        AbstractGenericOperation readFilms = new ReadFilms();
        readFilms.execute(new Film());
        film = (Film)((ReadFilms)readFilms).getList().get(0);
        
        term = new Term();
	}
	
	@AfterAll
	static void end() throws Exception{
		AbstractGenericOperation deleteTerm = new DeleteTerm();
		deleteTerm.execute(term);
		
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
		createTerm = new CreateTerm();
	}
	
	@AfterEach
	void tearDown() throws Exception{
		createTerm = null;
	}
	
	@Test
	void constructorTest() {
		createTerm= new CreateFilm();
		assertNotNull(createTerm);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> createTerm.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> createTerm.execute(new Film()));
	}
	
	@Test
	void testAddTermInvalidData() throws Exception {
		Date date = new Date();
		term.setDate(date);
		term.setFilm(new Film());
		term.setHall(new Hall());
		term.setProjectionType("2D");
		
		assertThrows(java.lang.Exception.class, () ->  createTerm.execute(term));
	}
		
	
	@Test
	void testAddTerm() throws Exception{
		Date date = new Date();
		term.setDate(date);
		term.setFilm(film);
		term.setHall(hall);
		term.setProjectionType("2D");
		
		createTerm.execute(term);
		
		AbstractGenericOperation readTerms = new ReadTerms();
		readTerms.execute(new Term());
		List<GenericEntity> terms = ((ReadTerms)readTerms).getList();
		
		assertEquals(2, terms.size());
		term = (Term)terms.get(1);
		
		assertNotNull(terms);
		assertNotNull(term);
		assertEquals("2D", term.getProjectionType());
		assertEquals(hall.getHallID(), term.getHall().getHallID());
		assertEquals(film.getFilmID(), term.getFilm().getFilmID());
		
		SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
		String d1 = sdf.format(date);
		String d2 = sdf.format(term.getDate());
		assertEquals(d1, d2);
	}
}
