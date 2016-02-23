import java.util.ArrayList;

/**
 * Created by Вадим on 23.02.2016.
 */
public interface connectToDBS {
    boolean add (String number, DBIOsql DBIOsql);
    boolean delete(String number, DBIOsql DBIOsql);
    boolean select(String number, DBIOsql DBIOsql);
    boolean sendQuery();
    ArrayList<String> selectAll();
}
