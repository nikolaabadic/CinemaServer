package rs.ac.bg.fon.CinemaServer.operation.reservation;

import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

/**
 * System operation that deletes a Reservation record from the database.
 * @author Nikola Abadic
 *
 */
public class DeleteReservation extends AbstractGenericOperation{

    /**
     * Verifies if the given parameter is not null and is a Reservation class object.
     * @param param Object that is sent as request argument.
     * @throws Exception If the given parameter is null or isn't a Reservation class object.
     */
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Reservation)) {
            throw new Exception("Invalid reservation!");
        }
    }

    /**
     * Executes the operation by calling the delete method on the repository attribute.
     * @param param Object that is sent as request argument.
     * @throws Exception Thrown by the repository method if there were any errors.
     */
    @Override
    protected void executeOperation(Object param) throws Exception {
        repository.delete((Reservation) param);
    }
    
}
