import java.io.*;
import java.util.ArrayList;

/**
 * Created by ����� on 19.02.2016.
 */
public class connectToDbTxt implements connectToDB{
    private static ArrayList<String> numbers;
    private static DBIO dbio = new DBIO();

    @Override
    public boolean add(String number) {
        numbers = dbio.getInput();

        if (numbers.contains(number)) {
            numbers.clear();
            System.out.println("This plate is already exists!");
            return false;
        }
        else {
            System.out.println("Plate successfully added!");
            dbio.setOutput(number);
            numbers.clear();
            return true;
        }
    }

    @Override
    public boolean delete(String number) {
        numbers = dbio.getInput();
        if (numbers.contains(number)){
            File db = new File("DB.txt");
            db.delete();
            numbers.remove(number);

            for (String out : numbers)
                dbio.setOutput(out);

            numbers.clear();
            System.out.println("Plate is deleted!");
            return true;
        }
        else {
            numbers.clear();
            return false;
        }
    }

    @Override
    public boolean select(String number) {
        numbers = dbio.getInput();
        if (numbers.contains(number)) {
            numbers.clear();
            System.out.println("BD contains this plate!");
            return true;
        }
        else {
            numbers.clear();
            System.out.println("BD doesn`t contains this plate!");
            return false;
        }
    }

    @Override
    public boolean sendQuery() {
        return false;
    }
}
