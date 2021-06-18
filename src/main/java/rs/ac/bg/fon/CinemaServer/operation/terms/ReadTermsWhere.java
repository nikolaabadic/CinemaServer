package rs.ac.bg.fon.CinemaServer.operation.terms;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class ReadTermsWhere extends AbstractGenericOperation {
    private List<GenericEntity> list;
    private String where;

    public ReadTermsWhere(String where) {
        this.where = where;
    } 
    
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
        list = repository.getAll((Term)param,where);
    }    
}

