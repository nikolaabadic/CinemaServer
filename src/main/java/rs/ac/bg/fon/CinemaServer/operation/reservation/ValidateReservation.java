package rs.ac.bg.fon.CinemaServer.operation.reservation;

import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaCommon.domain.Reservation;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;
/**
 * System operation that validates if the given reservation can be created - if there are any unreserved seats for the given term.
 * @author Nikola Abadic
 *
 */
public class ValidateReservation extends AbstractGenericOperation{
	/**
	 * Number of unreserved seats for the given term.
	 */
    private int result;
    /**
     * Reservation class object.
     */
    private final GenericEntity param2;
    /**
     * Hall class object.
     */
    private final GenericEntity param3;
    /**
     * String that represents the SQL query result.
     */
    private String select;
    /**
     * String which represents the SQL where condition.
     */
    private String where;

    /**
     * Parameterized constructor that initializes a ValidateReservation object and sets the fields.
     * @param param2 Reservation class object.
     * @param param3 Hall class object.
     * @param select String that represents the SQL query result.
     * @param where String which represents the SQL where condition.
     */
    public ValidateReservation(GenericEntity param2, GenericEntity param3, String select, String where) {
        this.param2 = param2;
        this.param3 = param3;
        this.select = select;
        this.where = where;
    }
    
    /**
     * Returns the number of unreserved seats as an integer.
     * @return Number of unreserved seats.
     */
    public int getResult(){
        return result;
    }
    
    /**
     * Verifies if the given parameter is not null and is a Term class object.
     * @param param Object that is sent as request argument.
     * @throws Exception If the given parameter is null or isn't a Term class object.
     */
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Term)) {
            throw new Exception("Invalid request!");
        }
    }
    
    /**
     * Executes the operation by calling the getTwoJoinsGroupBy method on the repository attribute.
     * @param param Object that is sent as request argument.
     * @throws Exception Thrown by the repository method if there were any errors.
     */
    @Override
    protected void executeOperation(Object param) throws Exception {
        result = repository.getTwoJoinsGroupBy((Term)param, (Reservation) param2, (Hall)param3, select, where);
    }  
}

