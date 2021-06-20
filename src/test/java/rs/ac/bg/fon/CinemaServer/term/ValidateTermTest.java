package rs.ac.bg.fon.CinemaServer.term;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import rs.ac.bg.fon.CinemaServer.operation.terms.ValidateTerm;

public class ValidateTermTest {
	private AbstractGenericOperation validateTerm;
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
		validateTerm = new ValidateTerm(new Film(), "");
	}
	
	@AfterEach
	void tearDown() throws Exception{
		validateTerm = null;
	}
	
	@Test
	void constructorTest() {
		validateTerm= new ValidateTerm(new Film(), "");
		assertNotNull(validateTerm);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> validateTerm.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> validateTerm.execute(new Film()));
	}
	
	@Test
	void testInvalidData() {
		term = new Term(0, new Date(), "2D", film, hall);
        java.sql.Timestamp timestamp = new java.sql.Timestamp(term.getDate().getTime());
        String where = " t.hallID=" + term.getHall().getHallID() + " AND t.date <= '" + timestamp+ "' AND DA TE_ADD(t.date, INTERVAL f.duration MINUTE) >= '" + timestamp + "' AND t.termID != " + term.getTermID();
        validateTerm = new ValidateTerm(new Film(), where);
        assertThrows(java.lang.Exception.class, () -> validateTerm.execute(new Term()));
	}
	
	@Test
	void testValidateTerm() throws Exception {
		term = new Term(0, new Date(), "3D", film, hall);
		AbstractGenericOperation createTerm = new CreateTerm();
		createTerm.execute(term);
		
		AbstractGenericOperation readTermsWhere = new ReadTermsWhere(" filmID = 40 and hallID = 5 and type = '3D'");
		readTermsWhere.execute(new Term());
		List<GenericEntity> terms = ((ReadTermsWhere)readTermsWhere).getList();
		term = (Term) terms.get(0);
		
		Term newTerm = new Term(0, new Date(), "2D", film, hall);
		java.sql.Timestamp timestamp = new java.sql.Timestamp(term.getDate().getTime());
        String where = " t.hallID=" + newTerm.getHall().getHallID() + " AND t.date <= '" + timestamp+ "' AND DATE_ADD(t.date, INTERVAL f.duration MINUTE) >= '" + timestamp + "' AND t.termID != " + newTerm.getTermID();
        validateTerm = new ValidateTerm(new Film(), where);
        validateTerm.execute(new Term());
        List<GenericEntity> result = ((ValidateTerm)validateTerm).getList();
        
        assertNotNull(result);
        assertTrue(!result.isEmpty());
	}
}
