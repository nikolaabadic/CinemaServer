package rs.ac.bg.fon.CinemaServer.operation.film;

import java.util.List;

import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaCommon.domain.GenericEntity;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class ReadFilmsWhere extends AbstractGenericOperation {
    private List<GenericEntity> list;
    private String where;

    public ReadFilmsWhere(String where) {
        this.where = where;
    }
    
    public List<GenericEntity> getList(){
        return list;
    }
    
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Film)) {
            throw new Exception("Invalid request!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        list = repository.getAll((GenericEntity)param,where);
    }
}
