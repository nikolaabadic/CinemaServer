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
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaCommon.domain.User;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.CreateReservation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.DeleteReservation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.ReadReservationsWhere;
import rs.ac.bg.fon.CinemaServer.operation.terms.ReadTerms;
import rs.ac.bg.fon.CinemaServer.operation.user.ReadUsers;

public class ReadReservationsWhereTest {
	private AbstractGenericOperation readReservationWhere;
	private static User user;
	private static Term term;
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
		readReservationWhere = new ReadReservationsWhere("");
	}
	
	@AfterEach
	void tearDown() throws Exception{
		readReservationWhere = null;
	}
	
	@Test
	void constructorTest() {
		readReservationWhere= new ReadReservationsWhere("");
		assertNotNull(readReservationWhere);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> readReservationWhere.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> readReservationWhere.execute(new Film()));
	}
	
	@Test
	void testIncorrectWhereString() {
		readReservationWhere = new ReadReservationsWhere(" reservation = 1");
		assertThrows(java.lang.Exception.class, () -> readReservationWhere.execute(new Reservation()));
	}
	
	@Test
	void testReadReservations() throws Exception {
		Reservation reservation = new Reservation();
		Date date = new Date();
		reservation.setNumberOfTickets(1);
		reservation.setReservationDate(date);
		reservation.setUser(user);
		reservation.setTerm(term);
		
		AbstractGenericOperation createReservation = new CreateReservation();
		createReservation.execute(reservation);
		
		readReservationWhere = new ReadReservationsWhere(" termID = 62 and userID = 5 and number = 1");
		readReservationWhere.execute(new Reservation());
		List<GenericEntity> reservations = ((ReadReservationsWhere)readReservationWhere).getList();
		
		assertNotNull(reservations);
		assertEquals(1, reservations.size());
		
		createdReservation = (Reservation)reservations.get(0);
		assertEquals(reservation.getUser().getUserID(), createdReservation.getUser().getUserID());
		assertEquals(reservation.getTerm().getTermID(), createdReservation.getTerm().getTermID());
		assertEquals(reservation.getNumberOfTickets(), createdReservation.getNumberOfTickets());
	}
}
