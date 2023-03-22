package carsharing.DAO;

import java.util.List;

public interface AbstractDao <E>{
    List<E> getList();
}
