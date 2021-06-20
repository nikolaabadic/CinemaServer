package rs.ac.bg.fon.CinemaServer.reservation;

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
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaCommon.domain.User;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.CreateReservation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.DeleteReservation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.ReadReservations;
import rs.ac.bg.fon.CinemaServer.operation.terms.ReadTerms;
import rs.ac.bg.fon.CinemaServer.operation.user.ReadUsers;

public class CreateReservationTest {
	private AbstractGenericOperation createReservation;
	private static User user;
	private static Term term;
	private static Reservation reservation;
	
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
        
        reservation = new Reservation();
	}
	
	@AfterAll
	static void end() throws Exception{
		AbstractGenericOperation deleteReservation = new DeleteReservation();
		deleteReservation.execute(reservation);
		
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
		createReservation = new CreateReservation();
	}
	
	@AfterEach
	void tearDown() throws Exception{
		createReservation = null;
	}
	
	@Test
	void constructorTest() {
		createReservation= new CreateReservation();
		assertNotNull(createReservation);
	}
	
	@Test
	void testPreconditionsNull() {
		assertThrows(java.lang.Exception.class, () -> createReservation.execute(null));
	}
	
	@Test
	void testPreconditionsInvalidData() {
		assertThrows(java.lang.Exception.class, () -> createReservation.execute(new Film()));
	}
	
	@Test
	void testAddReservationInvalidData() throws Exception {
		Date date = new Date();
		reservation.setNumberOfTickets(1);
		reservation.setReservationDate(date);
		reservation.setUser(new User());
		reservation.setTerm(new Term());
		
		assertThrows(java.lang.Exception.class, () ->  createReservation.execute(term));
	}
		
	
	@Test
	void testAddReservation() throws Exception{
		Date date = new Date();
		reservation.setNumberOfTickets(1);
		reservation.setReservationDate(date);
		reservation.setUser(user);
		reservation.setTerm(term);
		
		createReservation.execute(reservation);
		
		AbstractGenericOperation readReservations = new ReadReservations();
		readReservations.execute(new Reservation());
		List<GenericEntity> reservations = ((ReadReservations)readReservations).getList();
		
		assertEquals(1, reservations.size());
		reservation = (Reservation)reservations.get(0);
		
		assertNotNull(reservations);
		assertNotNull(reservation);
		assertEquals(1, reservation.getNumberOfTickets());
		assertEquals(term.getTermID(), reservation.getTerm().getTermID());
		assertEquals(user.getUserID(), reservation.getUser().getUserID());
		
		SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
		String d1 = sdf.format(date);
		String d2 = sdf.format(reservation.getReservationDate());
		assertEquals(d1, d2);
	}
}
