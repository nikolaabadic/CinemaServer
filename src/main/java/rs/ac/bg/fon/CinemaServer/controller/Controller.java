package rs.ac.bg.fon.CinemaServer.controller;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.Admin;
import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaCommon.domain.User;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
import rs.ac.bg.fon.CinemaServer.operation.admin.ReadAdmins;
import rs.ac.bg.fon.CinemaServer.operation.film.CreateFilm;
import rs.ac.bg.fon.CinemaServer.operation.film.DeleteFilm;
import rs.ac.bg.fon.CinemaServer.operation.film.ReadFilms;
import rs.ac.bg.fon.CinemaServer.operation.film.ReadFilmsWhere;
import rs.ac.bg.fon.CinemaServer.operation.hall.ReadHalls;
import rs.ac.bg.fon.CinemaServer.operation.hall.ReadHallsWhere;
import rs.ac.bg.fon.CinemaServer.operation.reservation.CreateReservation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.DeleteReservation;
import rs.ac.bg.fon.CinemaServer.operation.reservation.ReadReservations;
import rs.ac.bg.fon.CinemaServer.operation.reservation.ReadReservationsByUsername;
import rs.ac.bg.fon.CinemaServer.operation.reservation.ReadReservationsWhere;
import rs.ac.bg.fon.CinemaServer.operation.reservation.ValidateReservation;
import rs.ac.bg.fon.CinemaServer.operation.terms.CreateTerm;
import rs.ac.bg.fon.CinemaServer.operation.terms.DeleteTerm;
import rs.ac.bg.fon.CinemaServer.operation.terms.ReadTerms;
import rs.ac.bg.fon.CinemaServer.operation.terms.ReadTermsWhere;
import rs.ac.bg.fon.CinemaServer.operation.terms.UpdateTerm;
import rs.ac.bg.fon.CinemaServer.operation.terms.ValidateTerm;
import rs.ac.bg.fon.CinemaServer.operation.user.ReadUsers;
import rs.ac.bg.fon.CinemaServer.operation.user.ReadUsersWhere;

public class Controller {
    private static Controller instance;
    
   
    private Controller(){
    }
    public static Controller getInstance(){
        if(instance==null){
            instance=new Controller();
        }
        return instance;
    }
    
    public Admin login(String username, String password) throws Exception {
        AbstractGenericOperation operation = new ReadAdmins();
        operation.execute(new Admin());
        List<GenericEntity> admins = ((ReadAdmins)operation).getList();
        
        for (GenericEntity a : admins) {
            Admin admin = (Admin)a;
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        throw new Exception("Unknown admin!");
    }
    
    public void addFilm(Film film) throws Exception{
        for(Term t : film.getTerms()){
            validateTerm(t);
        }
        AbstractGenericOperation operation = new CreateFilm();
        operation.execute(film);   
    }
    
    public List<GenericEntity> getFilms(GenericEntity object, String where) throws Exception {
        AbstractGenericOperation operation = new ReadFilmsWhere(where);
        operation.execute(object);
        return ((ReadFilmsWhere)operation).getList();
    }
    
    public List<GenericEntity> getFilms(GenericEntity object) throws Exception {
        AbstractGenericOperation operation = new ReadFilms();
        operation.execute(object);
        return ((ReadFilms)operation).getList();
    }
    
    public List<GenericEntity> getHalls(GenericEntity object) throws Exception {
        AbstractGenericOperation operation = new ReadHalls();
        operation.execute(object);
        return ((ReadHalls)operation).getList();
    }
    
    public List<GenericEntity> getTerms(GenericEntity object) throws Exception {
        AbstractGenericOperation operation = new ReadTerms();
        operation.execute(object);
        List<GenericEntity> terms = ((ReadTerms)operation).getList();
        
        for(GenericEntity t : terms){
            Term term = (Term)t;
            String whereFilm = " filmID=" + term.getFilm().getFilmID();
            term.setFilm((Film)getFilms(new Film(), whereFilm).get(0));
            String whereHall = " hallID=" + term.getHall().getHallID();
            term.setHall((Hall)getHalls(new Hall(), whereHall).get(0));
        }
         
        return terms;
    }
    
    public void addTerm(Term term) throws Exception{
        validateTerm(term);
        
        AbstractGenericOperation operation = new CreateTerm();
        operation.execute(term);     
    }
    
    public void deleteFilm(Film film) throws Exception{        
        AbstractGenericOperation operation = new DeleteFilm();
        operation.execute(film);
    }
    
    public List<GenericEntity> getUsers(GenericEntity object) throws Exception {
        AbstractGenericOperation operation = new ReadUsers();
        operation.execute(object);
        return ((ReadUsers)operation).getList();
    }

    public void addReservation(Reservation reservation) throws Exception {
        validateReservation(reservation);
        
        AbstractGenericOperation operation = new CreateReservation();
        operation.execute(reservation); 
    }

    public List<GenericEntity> getReservations(GenericEntity object) throws Exception {
        AbstractGenericOperation operation = new ReadReservations();
        operation.execute(object);
        List<GenericEntity> reservations = ((ReadReservations)operation).getList();
                
        for(GenericEntity r : reservations){
            Reservation reservation= (Reservation)r;
            String whereUser = " userID=" + reservation.getUser().getUserID();
            reservation.setUser((User)getUsers(new User(),whereUser).get(0));
            String whereTerm = " termID=" + reservation.getTerm().getTermID();
            reservation.setTerm((Term)getTerms(new Term(), whereTerm).get(0));            
        } 
        return reservations;
    }

    public void deleteTerm(Term term) throws Exception {
        AbstractGenericOperation operation = new DeleteTerm();
        operation.execute(term); 
    }

    public void editTerm(Term term) throws Exception {
        validateTerm(term);
        
        AbstractGenericOperation operation = new UpdateTerm();
        operation.execute(term);
    }

    public void deleteReservation(Reservation reservation) throws Exception {
        AbstractGenericOperation operation = new DeleteReservation();
        operation.execute(reservation); 
    }

    public List<GenericEntity> getTerms(GenericEntity object, String where) throws Exception {
        AbstractGenericOperation operation = new ReadTermsWhere(where);
        operation.execute(object);
        List<GenericEntity> halls = getHalls(new Hall());
        List<GenericEntity> list = ((ReadTermsWhere)operation).getList();
        
        for(GenericEntity entity : list){
            Term t = (Term)entity;
            for(GenericEntity h : halls){
                Hall hall = (Hall)h;
                if(hall.getHallID() == t.getHall().getHallID()){
                    t.setHall(hall);
                }
            }
            String whereFilm = " filmID=" + t.getFilm().getFilmID();
            t.setFilm((Film)getFilms(new Film(), whereFilm).get(0));
        }        
        return list;
    }
    
    public List<GenericEntity> getReservations(GenericEntity object, String where) throws Exception {
        AbstractGenericOperation operation = new ReadReservationsWhere(where);
        operation.execute(object);
        List<GenericEntity> list = ((ReadReservationsWhere)operation).getList();
        
        for(GenericEntity entity : list){
            Reservation reservation = (Reservation)entity;
            String whereUser = " userID=" + reservation.getUser().getUserID();
            reservation.setUser((User)getUsers(new User(),whereUser).get(0));
            String whereTerm = " termID=" + reservation.getTerm().getTermID();
            reservation.setTerm((Term)getTerms(new Term(), whereTerm).get(0));
        }        
        
        return list;
    }
    
    public List<GenericEntity> getUsers(GenericEntity object,String where) throws Exception{
        AbstractGenericOperation operation = new ReadUsersWhere(where);
        operation.execute(object);
        return ((ReadUsersWhere)operation).getList();        
    }

    public List<GenericEntity> getReservationsLeftJoinUser(GenericEntity object, String where) throws Exception {
        AbstractGenericOperation operation = new ReadReservationsByUsername(where);
        operation.execute(object);
        List<GenericEntity> list = ((ReadReservationsByUsername)operation).getList();
        
        for(GenericEntity entity : list){
            Reservation reservation = (Reservation)entity;
            String whereUser = " userID=" + reservation.getUser().getUserID();
            reservation.setUser((User)getUsers(new User(),whereUser).get(0));
            String whereTerm = " termID=" + reservation.getTerm().getTermID();
            reservation.setTerm((Term)getTerms(new Term(), whereTerm).get(0));
        }        
        
        return list;
    }

    public List<GenericEntity> getHalls(GenericEntity object, String where) throws Exception {
        AbstractGenericOperation operation = new ReadHallsWhere(where);
        operation.execute(object);
        return ((ReadHallsWhere)operation).getList();
    }

    public static void validateTerm(GenericEntity term) throws Exception{
        Term t = (Term)term;
        java.sql.Timestamp timestamp = new java.sql.Timestamp(t.getDate().getTime());
        String where = " t.hallID=" + t.getHall().getHallID() + " AND t.date <= '" + timestamp+ "' AND DATE_ADD(t.date, INTERVAL f.duration MINUTE) >= '" + timestamp + "' AND t.termID != " + t.getTermID();
        
        AbstractGenericOperation operation = new ValidateTerm(new Film(), where);
        operation.execute(new Term());
        List<GenericEntity> result = ((ValidateTerm)operation).getList();

        if(result!=null && !result.isEmpty()){
            throw new Exception(t.getHall().getName() + " already in use at: " + t.getDate());
        }
    }
    
    private void validateReservation(GenericEntity object) throws Exception{
        Reservation reservation = (Reservation)object;
        
        String select =" h.Capacity-SUM(r.Number) AS result ";
        String where = " t.termId = " + reservation.getTerm().getTermID();
        AbstractGenericOperation operation = new ValidateReservation(new Reservation(), new Hall(), select, where);
        operation.execute(new Term());
        int result = ((ValidateReservation)operation).getResult();
        
        if(result != -1 && result < reservation.getNumberOfTickets()){
            throw new Exception("Only " + result + " ticket(s) left unsold in this term.");
        }
    }
}

