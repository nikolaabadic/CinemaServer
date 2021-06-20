package rs.ac.bg.fon.CinemaServer.reservation;

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
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaCommon.domain.User;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
import rs.ac.bg.fon.CinemaServer.operation.hall.ReadHalls;
import rs.ac.bg.fon.CinemaServer.operation.reservation.CreateReservation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.DeleteReservation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.ReadReservationsWhere;
import rs.ac.bg.fon.CinemaServer.operation.reservation.ValidateReservation;
import rs.ac.bg.fon.CinemaServer.operation.terms.ReadTerms;
import rs.ac.bg.fon.CinemaServer.operation.user.ReadUsers;

public class ValidateReservationTest {
	private AbstractGenericOperation validateReservation;
	private static User user;
	private static Term term;
	private static Hall hall;
	private static Reservation createdReservation;
	
	@BeforeAll
	static void start() throws Exception {
		FileOutputStream out = new FileOutputStream("config/dbconfig.properties");
		Properties properties = new Properties();
        properties.setProperty("url","jdbc:mysql://localhost:3306/cinema-test");
        properties.setProperty("username", "root");
        properties.setProperty("password", "root");
        properties.store(out, null);
        out.close();        
        
        AbstractGenericOperation readTerms = new ReadTerms();
        readTerms.execute(new Term());
        term = (Term)((ReadTerms)readTerms).getList().get(0);
        
        AbstractGenericOperation readUsers = new ReadUsers();
        readUsers.execute(new User());
        user = (User)((ReadUsers)readUsers).getList().get(0);
        
        AbstractGenericOperation readHalls = new ReadHalls();
        readHalls.execute(new Hall());
        hall = (Hall)((ReadHalls)readHalls).getList().get(0);
	}
	
	@AfterAll
	static void end() throws Exception{
		AbstractGenericOperation deleteReservation = new DeleteReservation();
		deleteReservation.execute(createdReservation);
		
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
		validateReservation = new ValidateReservation(new Reservation(), hall, "", "");
	}
	
	@AfterEach
	void tearDown() throws Exception{
		validateReservation = null;
	}
	
	@Test
	void constructorTest() {
		validateReservation = new ValidateReservation(new Reservation(), hall, "", "");
		assertNotNull(validateReservation);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> validateReservation.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> validateReservation.execute(new Film()));
	}
	
	@Test
	void testInvalidData() {
		Reservation reservation = new Reservation();
		Date date = new Date();
		reservation.setNumberOfTickets(1);
		reservation.setReservationDate(date);
		reservation.setUser(user);
		reservation.setTerm(term);
		
		validateReservation = new ValidateReservation(reservation, hall,
				"h.Capacity-SM(r.Number) AS result ",
				"t.termId = " + term.getTermID());
		assertThrows(java.lang.Exception.class, () -> validateReservation.execute(new Term()));
	}
	
	@Test
	void testValidateReservation() throws Exception {
		Reservation reservation = new Reservation();
		Date date = new Date();
		reservation.setNumberOfTickets(10);
		reservation.setReservationDate(date);
		reservation.setUser(user);
		reservation.setTerm(term);
		
		AbstractGenericOperation createReservation = new CreateReservation();
		createReservation.execute(reservation);
		
		AbstractGenericOperation readReservationWhere = new ReadReservationsWhere(" termID = 62 and userID = 5 and number = 10");
		readReservationWhere.execute(new Reservation());
		List<GenericEntity> reservations = ((ReadReservationsWhere)readReservationWhere).getList();
		
		assertNotNull(reservations);
		assertEquals(1, reservations.size());
		
		createdReservation = (Reservation)reservations.get(0);
		
		validateReservation = new ValidateReservation(new Reservation(), new Hall(),
				"h.Capacity-SUM(r.Number) AS result ",
				"t.termId = " + term.getTermID());
		
		validateReservation.execute(new Term());
		int res = ((ValidateReservation)validateReservation).getResult();
		assertEquals(90, res);
	}
}
