package rs.ac.bg.fon.CinemaServer.operation.terms;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

/**
 * System operation that validates if the given term can be created - if the hall is already used at that time.
 * @author Nikola Abadic
 *
 */
public class ValidateTerm extends AbstractGenericOperation{
    /**
     * Film class object.
     */
    private Object param2;
    /**
     * String which represents the SQL where condition.
     */
    private String where;
    /**
     * Operation result - List of terms shown at the same time and hall.
     */
    private List<GenericEntity> result;
    
    /**
     * Parameterized constructor that initializes a ValidateTerm object and sets the fields.
     * @param param2 Film class object.
     * @param where String which represents the SQL where condition.
     */
    public ValidateTerm(Object param2, String where) {
        this.param2 = param2;
        this.where = where;
    }
       
    /**
     * Verifies if the given parameter is not null and is a Term class object.
     * @param param Object that is sent as request argument.
     * @throws Exception If the given parameter is null or isn't a Term class object.
     */
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Term)) {
            throw new Exception("Invalid term data!");
        }
    }

    /**
     * Executes the operation by calling the getAllLeftJoin method on the repository attribute.
     * @param param Object that is sent as request argument.
     * @throws Exception Thrown by the repository method if there were any errors.
     */
    @Override
    protected void executeOperation(Object param) throws Exception {
        result = repository.getAllLeftJoin((Term)param, (Film)param2, where);
    }
   
    /**
     * Returns the list of terms.
     * @return List of terms.
     */
    public List<GenericEntity> getList(){
        return result;
    }
    
}

