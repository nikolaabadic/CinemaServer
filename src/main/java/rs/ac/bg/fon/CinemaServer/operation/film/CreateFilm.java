package rs.ac.bg.fon.CinemaServer.operation.film;

import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.controller.Controller;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class CreateFilm extends AbstractGenericOperation{
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Film)) {
            throw new Exception("Invalid film data!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        int id = repository.addReturnKey((Film) param);
        
        Film film = (Film) param;
        film.setId(id);
        if(film.getTerms().size() > 0){
            for(Term t : film.getTerms()){
                t.setFilm(film);
                Controller.validateTerm(t);
                
                repository.add(t); 
            }
        }
    }
}
