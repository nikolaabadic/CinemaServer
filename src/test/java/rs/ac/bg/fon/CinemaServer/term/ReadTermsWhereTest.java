package rs.ac.bg.fon.CinemaServer.term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileOutputStream;
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
import rs.ac.bg.fon.CinemaServer.operation.film.ReadFilms;
import rs.ac.bg.fon.CinemaServer.operation.hall.ReadHalls;
import rs.ac.bg.fon.CinemaServer.operation.terms.CreateTerm;
import rs.ac.bg.fon.CinemaServer.operation.terms.DeleteTerm;
import rs.ac.bg.fon.CinemaServer.operation.terms.ReadTermsWhere;

public class ReadTermsWhereTest {
	private AbstractGenericOperation readTermsWhere;
	private static Term createdTerm;
	private static Hall hall;
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
        
        AbstractGenericOperation readHalls = new ReadHalls();
        readHalls.execute(new Hall());
        hall = (Hall)((ReadHalls)readHalls).getList().get(0);
        
        AbstractGenericOperation readFilms = new ReadFilms();
        readFilms.execute(new Film());
        film = (Film)((ReadFilms)readFilms).getList().get(0);
	}
	@AfterAll
	static void end() throws Exception{        
        AbstractGenericOperation deleteTerm = new DeleteTerm();
        deleteTerm.execute(createdTerm);
        
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
		readTermsWhere = new ReadTermsWhere("");
	}
	
	@AfterEach
	void tearDown() throws Exception{
		readTermsWhere = null;
	}
	
	@Test
	void constructorTest() {
		readTermsWhere = new ReadTermsWhere("");
		assertNotNull(readTermsWhere);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> readTermsWhere.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> readTermsWhere.execute(new Film()));
	}

	@Test
	void testIncorrectWhereString() {
		readTermsWhere = new ReadTermsWhere(" term = 1");
		assertThrows(java.lang.Exception.class, () -> readTermsWhere.execute(new Term()));
	}
	
	@Test
	void testReadTerms() throws Exception {
		Term term = new Term();
		term.setDate(new Date());
		term.setFilm(film);
		term.setHall(hall);
		term.setProjectionType("3D");
		
		AbstractGenericOperation createTerm = new CreateTerm();
		createTerm.execute(term);
		
		readTermsWhere = new ReadTermsWhere(" filmID = 40 and hallID = 5 and type = '3D'");
		readTermsWhere.execute(new Term());
		List<GenericEntity> terms = ((ReadTermsWhere)readTermsWhere).getList();
		
		assertNotNull(terms);
		assertEquals(1, terms.size());
		createdTerm = (Term)terms.get(0);
		assertEquals(term.getFilm().getFilmID(), createdTerm.getFilm().getFilmID());
		assertEquals(term.getProjectionType(), createdTerm.getProjectionType());
		assertEquals(term.getHall().getHallID(), createdTerm.getHall().getHallID());
	}
}
