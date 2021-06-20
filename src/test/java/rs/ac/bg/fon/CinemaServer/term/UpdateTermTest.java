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
import rs.ac.bg.fon.CinemaServer.operation.terms.UpdateTerm;

public class UpdateTermTest {
	private AbstractGenericOperation updateTerm;
	private static Hall hall;
	private static Film film;
	private static Term updatedTerm;
	
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
        
        updatedTerm = new Term();
	}
	
	@AfterAll
	static void end() throws Exception{
		AbstractGenericOperation deleteTerm = new DeleteTerm();
		deleteTerm.execute(updatedTerm);
				
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
		updateTerm = new UpdateTerm();
	}
	
	@AfterEach
	void tearDown() throws Exception{
		updateTerm = null;
	}
	
	@Test
	void constructorTest() {
		updateTerm= new UpdateTerm();
		assertNotNull(updateTerm);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> updateTerm.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> updateTerm.execute(new Film()));
	}
	
	@Test
	void testUpdateTermIncorrectData() throws Exception {
		Term term = new Term();
		Date date = new Date();
		term.setDate(date);
		term.setFilm(film);
		term.setHall(hall);
		term.setProjectionType("3D");
		
		AbstractGenericOperation createTerm = new CreateTerm();
		createTerm.execute(term);
		
		AbstractGenericOperation readTermsWhere = new ReadTermsWhere(" filmID = 40 and hallID = 5 and type = '3D'");
		readTermsWhere.execute(new Term());
		List<GenericEntity> terms = ((ReadTermsWhere)readTermsWhere).getList();
		
		updatedTerm = (Term)terms.get(0);
		Term testTerm = (Term)terms.get(0);
		testTerm.setFilm(new Film());
		
		assertThrows(java.lang.Exception.class, () -> updateTerm.execute(testTerm));
	
		AbstractGenericOperation deleteTerm = new DeleteTerm();
		deleteTerm.execute(updatedTerm);
	}
	
	@Test
	void testUpdateTerm() throws Exception {		
		Date date = new Date();
		Term term = new Term(0, date, "3D", film, hall);
		
		AbstractGenericOperation createTerm = new CreateTerm();
		createTerm.execute(term);
		
		AbstractGenericOperation readTermsWhere = new ReadTermsWhere(" filmID = 40 and hallID = 5 and type ='3D'");
		readTermsWhere.execute(new Term());
		List<GenericEntity> terms = ((ReadTermsWhere)readTermsWhere).getList();
		
		assertEquals(1, terms.size());
		
		updatedTerm = (Term)terms.get(0);
		updatedTerm.setProjectionType("2D");
		
		updateTerm.execute(updatedTerm);
		
		readTermsWhere = new ReadTermsWhere(" filmID = 40 and hallID = 5 and termID != 62");
		readTermsWhere.execute(new Term());
		terms = ((ReadTermsWhere)readTermsWhere).getList();
		
		assertEquals(1, terms.size());
		updatedTerm = (Term)terms.get(0);
		
		assertNotNull(terms);
		assertNotNull(updatedTerm);
		assertEquals(1, terms.size());
		assertEquals("2D", updatedTerm.getProjectionType());
	}
	
}
