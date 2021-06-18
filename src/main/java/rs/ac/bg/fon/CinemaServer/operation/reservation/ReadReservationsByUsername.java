package rs.ac.bg.fon.CinemaServer.operation.reservation;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.User;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class ReadReservationsByUsername extends AbstractGenericOperation{
    private List<GenericEntity> list;
    private String where;

    public ReadReservationsByUsername(String where) {
        this.where = where;
    }
       
    public List<GenericEntity> getList(){
        return list;
    }
    
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Reservation)) {
            throw new Exception("Invalid request!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        list = repository.getAllLeftJoin((Reservation)param,new User(),where);
    }     
}
