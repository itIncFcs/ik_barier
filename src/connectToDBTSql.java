import java.util.ArrayList;

/**
 * Created by Вадим on 22.02.2016.
 */
public class connectToDBTSql implements connectToDBS {
    private static ArrayList<String> numbers = new ArrayList<>();
    private static DBIOsql DBIOsql;

    @Override
    public boolean add(String number, DBIOsql DBIOsql) {
        this.DBIOsql = DBIOsql;
        numbers.clear();
        numbers = DBIOsql.getInputAll();

        if (numbers.contains(number)) {
            System.out.println("This plate is already exists!");
            return false;
        }
        else {
            DBIOsql.setOutput(number);
            System.out.println("Plate successfully added!");
            return true;
        }
    }

    @Override
    public boolean delete(String number, DBIOsql DBIOsql) {
        this.DBIOsql = DBIOsql;
        boolean b = DBIOsql.deletePlate(number);
        return b;
    }

    @Override
    public ArrayList<String> selectAll() {
        numbers.clear();
        numbers = DBIOsql.getInputAll();
        return numbers;
    }

    @Override
    public boolean select(String number, DBIOsql DBIOsql) {
        this.DBIOsql = DBIOsql;
        numbers.clear();
        numbers = DBIOsql.getInputAll();

        if (numbers.contains(number))
            return true;
        else return false;
    }

    @Override
    public boolean sendQuery() {
        return false;
    }
}
