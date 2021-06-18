package rs.ac.bg.fon.CinemaServer.operation.terms;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class ReadTerms extends AbstractGenericOperation{
    private List<GenericEntity> list;
    
    public List<GenericEntity> getList(){
        return list;
    }
    
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Term)) {
            throw new Exception("Invalid request!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        list = repository.getAll((Term)param);
    }
}

