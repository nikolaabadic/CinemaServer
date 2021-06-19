package rs.ac.bg.fon.CinemaServer.operation.film;

import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

/**
 * System operation that deletes a Film record from the database.
 * @author Nikola Abadic
 *
 */
public class DeleteFilm extends AbstractGenericOperation {
    /**
     * Verifies if the given parameter is not null and is a Film class object.
     * @param param Object that is sent as request argument.
     * @throws Exception If the given parameter is null or isn't a Film class object.
     */
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Film)) {
            throw new Exception("Invalid film!");
        }
    }

    /**
     * Executes the operation by calling the delete method on the repository attribute.
     * @param param Object that is sent as request argument.
     * @throws Exception Thrown by the repository method if there were any errors.
     */
    @Override
    protected void executeOperation(Object param) throws Exception {
        repository.delete((Film) param);
    }
}
