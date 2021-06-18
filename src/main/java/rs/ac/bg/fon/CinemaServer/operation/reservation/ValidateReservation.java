package rs.ac.bg.fon.CinemaServer.operation.reservation;

import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class ValidateReservation extends AbstractGenericOperation{
    private int result;
    private final GenericEntity param2;
    private final GenericEntity param3;
    private String select;
    private String where;

    public ValidateReservation(GenericEntity param2, GenericEntity param3, String select, String where) {
        this.param2 = param2;
        this.param3 = param3;
        this.select = select;
        this.where = where;
    }
    
    
    public int getResult(){
        return result;
    }
    
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Term)) {
            throw new Exception("Invalid request!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        result = repository.getTwoJoinsGroupBy((Term)param, (Reservation) param2, (Hall)param3, select, where);
    }  
}

