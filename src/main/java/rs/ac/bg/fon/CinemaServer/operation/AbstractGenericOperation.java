package rs.ac.bg.fon.CinemaServer.operation;

import rs.ac.bg.fon.CinemaServer.repository.db.impl.RepositoryDBGeneric;

public abstract class AbstractGenericOperation {
    protected final RepositoryDBGeneric repository;

    public AbstractGenericOperation() {
        this.repository = new RepositoryDBGeneric();
    }

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

    private void startTransaction() throws Exception {
        repository.connect();
    }

    // Operation-specific method
    protected abstract void preconditions(Object param) throws Exception;

    // Operation-specific method
    protected abstract void executeOperation(Object param) throws Exception;

    private void commitTransaction() throws Exception {
        repository.commit();
    }

    private void rollbackTransaction() throws Exception {
        repository.rollback();
    }

    private void disconnect() throws Exception {
        repository.disconnect();
    }
}

