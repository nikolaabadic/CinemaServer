package rs.ac.bg.fon.CinemaServer.operation.hall;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaCommon.domain.Hall;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class ReadHallsWhere extends AbstractGenericOperation{
    private List<GenericEntity> list;
    private String where;

    public ReadHallsWhere(String where) {
        this.where = where;
    }
        
    public List<GenericEntity> getList(){
        return list;
    }
    
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Hall)) {
            throw new Exception("Invalid request!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        list = repository.getAll((Hall)param,where);
    }          
}