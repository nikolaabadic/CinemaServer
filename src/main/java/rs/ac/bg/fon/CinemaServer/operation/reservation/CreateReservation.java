package rs.ac.bg.fon.CinemaServer.operation.reservation;

import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class CreateReservation extends AbstractGenericOperation{

    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Reservation)) {
            throw new Exception("Invalid film data!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        repository.add((Reservation) param);
    }
    
}
