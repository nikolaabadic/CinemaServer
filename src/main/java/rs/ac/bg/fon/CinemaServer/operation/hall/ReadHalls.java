package rs.ac.bg.fon.CinemaServer.operation.hall;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

/**
 * System operation that returns all halls from the database.
 * @author Nikola Abadic
 *
 */
public class ReadHalls extends AbstractGenericOperation{
	    /**
	    * List of halls that will store the operation result.
	    */
	    private List<GenericEntity> list;
	    
	    /**
	     * Returns the list of halls.
	     * @return List of halls.
	     */
	    public List<GenericEntity> getList(){
	        return list;
	    }
	    
	    /**
	     * Verifies if the given parameter is not null and is a Hall class object.
	     * @param param Object that is sent as request argument.
	     * @throws Exception If the given parameter is null or isn't a Hall class object.
	     */
	    @Override
	    protected void preconditions(Object param) throws Exception {
	        if (param == null || !(param instanceof Hall)) {
	            throw new Exception("Invalid request!");
	        }
	    }

	    /**
	     * Executes the operation by calling the getAll method on the repository attribute.
	     * @param param Object that is sent as request argument.
	     * @throws Exception Thrown by the repository method if there were any errors.
	     */
	    @Override
	    protected void executeOperation(Object param) throws Exception {
	        list = repository.getAll((Hall)param);
	    }
}
