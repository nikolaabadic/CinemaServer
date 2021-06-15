package rs.ac.bg.fon.CinemaServer.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll(T param) throws Exception;
    List<T> getAll(T param, String where) throws Exception;
    List<T> getAllLeftJoin(T first, T second, String where) throws Exception;
    int addReturnKey(T param) throws Exception;
    void add(T param) throws Exception;
    void edit(T param) throws Exception;
    void delete(T param)throws Exception;
    int getTwoJoinsGroupBy(T first, T second, T third,String select, String where) throws Exception;
}
