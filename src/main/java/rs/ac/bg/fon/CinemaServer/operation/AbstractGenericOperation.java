package rs.ac.bg.fon.CinemaServer.operation;

import rs.ac.bg.fon.CinemaServer.repository.db.impl.RepositoryDBGeneric;

/**
 * Generic system operation abstract class that all system operations extend.
 * @author Nikola Abadic
 *
 */
public abstract class AbstractGenericOperation {
	/**
	 * Database repository that is used from manipulating data.
	 */
    protected final RepositoryDBGeneric repository;

    /**
     * Non-parameterized constructor used for initialization of AbstractGenericOperation type objects. Initializes repository attribute.
     */
    public AbstractGenericOperation() {
        this.repository = new RepositoryDBGeneric();
    }

    /**
     * Executes the system operation after stating a transaction a checking the preconditions. 
     * @param param Object that is sent as request argument.
     * @throws Exception java.lang.Exception when the preconditions are not pleased or when there is an Exception thrown by the repository method.	
     */
    public final void execute(Object param) throws Exception {
        try {
            startTransaction();
            preconditions(param);
            executeOperation(param);
            commitTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
            rollbackTransaction();
            throw ex;
        } finally {
            disconnect();
        }
    }

    /**
     * Starts a transaction before the system operation is executed.
     * @throws Exception
     */
    private void startTransaction() throws Exception {
        repository.connect();
    }

    // Operation-specific method
    /**
     * Verifies if all preconditions are pleased and the system operation can be executed.
     * @param param Object that is sent as request argument.
     * @throws Exception java.lang.Exception thrown during the preconditions verification.
     */
    protected abstract void preconditions(Object param) throws Exception;

    // Operation-specific method
    /**
     * Executes the system operation.
     * @param param Object that is sent as request argument.
     * @throws Exception java.lang.Exception thrown during the system operation execution or by the repository method.
     */
    protected abstract void executeOperation(Object param) throws Exception;
    
    /**
     * Commits the transaction.
     * @throws Exception java.lang.Exception if there were any connection errors.
     */
    private void commitTransaction() throws Exception {
        repository.commit();
    }

    /**
     * Annuls the operation results if there were any errors.
     * @throws Exception java.lang.Exception if there were any connection errors.
     */
    private void rollbackTransaction() throws Exception {
        repository.rollback();
    }

    /**
     * Disconnects the database connection.
     * @throws Exception
     */
    private void disconnect() throws Exception {
        repository.disconnect();
    }
}

