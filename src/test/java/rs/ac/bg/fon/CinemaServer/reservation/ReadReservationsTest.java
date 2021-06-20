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
import rs.ac.bg.fon.CinemaServer.operation.reservation.ReadReservations;
import rs.ac.bg.fon.CinemaServer.operation.terms.ReadTerms;
import rs.ac.bg.fon.CinemaServer.operation.user.ReadUsers;

public class ReadReservationsTest {
	private AbstractGenericOperation readReservation;
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
		readReservation = new ReadReservations();
	}
	
	@AfterEach
	void tearDown() throws Exception{
		readReservation = null;
	}
	
	@Test
	void constructorTest() {
		readReservation= new ReadReservations();
		assertNotNull(readReservation);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> readReservation.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> readReservation.execute(new Film()));
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
		
		readReservation.execute(new Reservation());
		List<GenericEntity> reservations = ((ReadReservations)readReservation).getList();
		
		assertNotNull(reservations);
		assertEquals(1, reservations.size());
		
		createdReservation = (Reservation)reservations.get(0);
		assertEquals(reservation.getUser().getUserID(), createdReservation.getUser().getUserID());
		assertEquals(reservation.getTerm().getTermID(), createdReservation.getTerm().getTermID());
		assertEquals(reservation.getNumberOfTickets(), createdReservation.getNumberOfTickets());
	}
}
