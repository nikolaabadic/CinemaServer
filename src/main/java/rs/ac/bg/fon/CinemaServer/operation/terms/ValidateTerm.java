package rs.ac.bg.fon.CinemaServer.operation.terms;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Term;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class ValidateTerm extends AbstractGenericOperation{
    private Object param2;
    private String where;
    private List<GenericEntity> result;
    
    public ValidateTerm(Object param2, String where) {
        this.param2 = param2;
        this.where = where;
    }
       
    
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Term)) {
            throw new Exception("Invalid term data!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        result = repository.getAllLeftJoin((Term)param, (Film)param2, where);
    }
   
    public List<GenericEntity> getList(){
        return result;
    }
    
}

