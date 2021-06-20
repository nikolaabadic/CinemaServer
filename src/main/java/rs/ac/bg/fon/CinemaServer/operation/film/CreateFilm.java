package rs.ac.bg.fon.CinemaServer.operation.film;

import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.controller.Controller;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

/**
 * System operation that creates a Film record in the database.
 * @author Nikola Abadic
 *
 */
public class CreateFilm extends AbstractGenericOperation{
    /**
     * Verifies if the given parameter is not null and is a Film class object.
     * @param param Object that is sent as request argument.
     * @throws Exception If the given parameter is null or isn't a Film class object.
     */
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Film)) {
            throw new Exception("Invalid film data!");
        }
    }

    /**
     * Executes the operation by calling the addReturnKey method on the repository attribute for saving the film and the add method for saving related terms.
     * @param param Object that is sent as request argument.
     * @throws Exception Thrown by the repository method if there were any errors.
     */
    @Override
    protected void executeOperation(Object param) throws Exception {
        int id = repository.addReturnKey((Film) param);
        
        Film film = (Film) param;
        film.setId(id);
        if(film.getTerms() != null && film.getTerms().size() > 0){
            for(Term t : film.getTerms()){
                t.setFilm(film);
                Controller.validateTerm(t);
                
                repository.add(t); 
            }
        }
    }
}
