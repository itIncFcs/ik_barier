import java.io.*;
import java.util.ArrayList;

/**
 * Created by ����� on 19.02.2016.
 */
public class connectToDBTTxt implements connectToDBT {
    private static ArrayList<String> numbers;
    private static DBIOtxt DBIOtxt = new DBIOtxt();

    @Override
    public boolean add(String number) {
        numbers.clear();
        numbers = DBIOtxt.getInput();

        if (numbers.contains(number)) {
            System.out.println("This plate is already exists!");
            return false;
        }
        else {
            System.out.println("Plate successfully added!");
            DBIOtxt.setOutput(number);
            return true;
        }
    }

    @Override
    public boolean delete(String number) {
        numbers.clear();
        numbers = DBIOtxt.getInput();
        if (numbers.contains(number)){
            File db = new File("DB.txt");
            db.delete();
            numbers.remove(number);

            for (String out : numbers)
                DBIOtxt.setOutput(out);

            System.out.println("Plate is deleted!");
            return true;
        }
        else {return false;}
    }

    @Override
    public boolean select(String number) {
        numbers.clear();
        numbers = DBIOtxt.getInput();
        if (numbers.contains(number)) {
            System.out.println("BD contains this plate!");
            return true;
        }
        else {
            System.out.println("BD doesn`t contains this plate!");
            return false;
        }
    }
}
