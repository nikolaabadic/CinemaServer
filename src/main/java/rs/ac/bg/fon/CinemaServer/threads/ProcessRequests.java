package rs.ac.bg.fon.CinemaServer.threads;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rs.ac.bg.fon.CinemaCommon.communication.Receiver;
import rs.ac.bg.fon.CinemaCommon.communication.Request;
import rs.ac.bg.fon.CinemaCommon.communication.Response;
import rs.ac.bg.fon.CinemaCommon.communication.Sender;
import rs.ac.bg.fon.CinemaCommon.domain.Admin;
import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaCommon.domain.User;
import rs.ac.bg.fon.CinemaServer.controller.Controller;

public class ProcessRequests extends Thread {
    Socket socket;
    Sender sender;
    Receiver receiver;
    boolean end = false;
    Admin admin;
    Server server;
    
    public ProcessRequests(Server server, Socket socket) {
        this.socket = socket;
        this.server = server;
        sender=new Sender(socket);
        receiver=new Receiver(socket);
    }

    @Override
    public void run() {
        while(!Server.getEnd()){
            try{
                Request request=(Request)receiver.receive();
                Response response=new Response();
                try{
                    switch(request.getOperation()){
                        case LOGIN:
                            User user=(User)request.getArgument();
                            response.setResult(Controller.getInstance().login(user.getUsername(), user.getPassword()));
                            break;
                        case ADD_FILM:
                            Film film = (Film)request.getArgument();
                            Controller.getInstance().addFilm(film);
                            response.setResult(true);
                            break;
                        case GET_FILMS:
                            List<GenericEntity> films = Controller.getInstance().getFilms(new Film());
                            response.setResult(films);
                            break;
                        case GET_HALLS:
                            List<GenericEntity> halls = Controller.getInstance().getHalls(new Hall());
                            response.setResult(halls);
                            break;
                        case ADD_TERM:
                            Term term = (Term)request.getArgument();
                            Controller.getInstance().addTerm(term);
                            response.setResult(true);
                            break;
                        case GET_TERMS:
                            List<GenericEntity> terms = Controller.getInstance().getTerms(new Term());
                            response.setResult(terms);
                            break;
                        case DELETE_FILM:
                            Controller.getInstance().deleteFilm((Film)request.getArgument());
                            break;
                        case GET_USERS:
                            List<GenericEntity> users = Controller.getInstance().getUsers(new User());
                            response.setResult(users);
                            break;  
                        case ADD_RESERVATION:
                            Reservation reservation = (Reservation) request.getArgument();
                            Controller.getInstance().addReservation(reservation);
                            response.setResult(true);
                            break;
                        case GET_RESERVATIONS:
                            List<GenericEntity> reservations = Controller.getInstance().getReservations(new Reservation());
                            response.setResult(reservations);
                            break;
                        case DELETE_TERM:
                            Controller.getInstance().deleteTerm((Term)request.getArgument());
                            break;
                        case EDIT_TERM:
                            Controller.getInstance().editTerm((Term)request.getArgument());
                            break;
                        case DELETE_RESERVATION:
                            Controller.getInstance().deleteReservation((Reservation)request.getArgument());
                            break;
                        case GET_FILMS_WHERE:
                            String title = (String) request.getArgument();
                            String where = " LOWER(name) LIKE '%" + title.toLowerCase() +"%' ";
                            List<GenericEntity> filmsSearch = Controller.getInstance().getFilms(new Film(), where);
                            response.setResult(filmsSearch);
                            break;
                        case GET_TERMS_BY_FILM_ID:
                            int id = (int) request.getArgument();
                            where = " filmID =" +id;
                            List<GenericEntity> termsSearch = Controller.getInstance().getTerms(new Term(), where);
                            response.setResult(termsSearch);
                            break;
                        case GET_TERMS_BY_HALL:
                            id = (int)request.getArgument();
                            where = " hallID =" +id;
                            List<GenericEntity> hallsSearch = Controller.getInstance().getTerms(new Term(), where);
                            response.setResult(hallsSearch);
                            break;
                        case GET_TERMS_BY_DATE:
                            String date = (String) request.getArgument();
                            
                            where = " CONVERT(DATE, Date)='" +date +"'";
                            List<GenericEntity> dateSearch = Controller.getInstance().getTerms(new Term(), where);
                            response.setResult(dateSearch);
                            break;
                        case GET_RESERVATIONS_BY_TERM_ID:
                            id = (int)request.getArgument();
                            where = " termID =" +id;
                            List<GenericEntity> reservationsByID = Controller.getInstance().getReservations(new Reservation(), where);
                            response.setResult(reservationsByID);
                            break;
                        case GET_RESERVATIONS_BY_USERNAME:
                            String username = (String)request.getArgument();
                            where = "LOWER(u.username) LIKE '%" + username.toLowerCase() +"%' ";
                            List<GenericEntity> reservationsByUsername = Controller.getInstance().getReservationsLeftJoinUser(new Reservation(), where);
                            response.setResult(reservationsByUsername);
                            break;
                        case GET_RESERVATIONS_BY_DATE:
                            String dateRes = (String)request.getArgument();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                            Date dateOld = sdf1.parse(dateRes);
                            String dateNew = sdf2.format(dateOld);
                            where = " CONVERT(DATE, Date)='" +dateNew +"'";
                            List<GenericEntity> reservationsByDate = Controller.getInstance().getReservations(new Reservation(), where);
                            response.setResult(reservationsByDate);
                            break;  
                        case GET_FILM_BY_ID:
                            int filmId = (int) request.getArgument();
                            where = " filmId = " + filmId;
                            GenericEntity filmById = Controller.getInstance().getFilms(new Film(), where).get(0);
                            response.setResult(filmById);
                            break;
                        case GET_TERM_BY_ID:
                            int termId = (int) request.getArgument();
                            where = " termId = " + termId;
                            GenericEntity termById = Controller.getInstance().getTerms(new Term(), where).get(0);
                            response.setResult(termById);
                            break;
                        case GET_RESERVATION_BY_ID:
                            int[] array = (int[]) request.getArgument();
                            where = " termId = " + array[0] + " AND userId = " + array[1];
                            GenericEntity resById = Controller.getInstance().getReservations(new Reservation(), where).get(0);
                            response.setResult(resById);
                            break;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    response.setException(e);
                }
                sender.send(response);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
}

