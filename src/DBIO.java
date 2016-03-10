import java.util.ArrayList;

/**
 * Created by Вадим on 25.02.2016.
 */
public interface DBIO {
    boolean connect(String connectionDataBase) throws Exception;
    boolean create(String tablename, String coloumns) throws Exception;
    boolean close(ArrayList<String> numbers) throws Exception;
    boolean add(String number, String name) throws Exception;
    boolean delete(String number) throws Exception;
    ArrayList<String> select() throws Exception;
}
