package rs.ac.bg.fon.CinemaServer.operation.terms;

import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class UpdateTerm extends AbstractGenericOperation{
    protected void preconditions(Object param) throws Exception {
    if (param == null || !(param instanceof Term)) {
        throw new Exception("Invalid term data!");
    }
}

@Override
protected void executeOperation(Object param) throws Exception {
    repository.edit((Term) param);
}
}
