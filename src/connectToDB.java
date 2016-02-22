/**
 * Created by Вадим on 19.02.2016.
 */
public interface connectToDB {
    boolean add (String number);
    boolean delete (String number);
    boolean select(String number);
    boolean sendQuery();
}
