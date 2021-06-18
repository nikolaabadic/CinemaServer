package rs.ac.bg.fon.CinemaServer.operation.film;

import rs.ac.bg.fon.CinemaCommon.domain.Film;
import rs.ac.bg.fon.CinemaServer.operation.AbstractGenericOperation;

public class DeleteFilm extends AbstractGenericOperation {
    @Override
    protected void preconditions(Object param) throws Exception {
        if (param == null || !(param instanceof Film)) {
            throw new Exception("Invalid film!");
        }
    }

    @Override
    protected void executeOperation(Object param) throws Exception {
        repository.delete((Film) param);
    }
}
