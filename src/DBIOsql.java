import java.sql.*;
import java.util.ArrayList;

/**
 * Created by ����� on 22.02.2016.
 */
public class DBIOsql {
    private ArrayList<String> numbers = new ArrayList<>();
    private String number;
    private Connection conn;
    private Statement statmt;
    private ResultSet resSet;

    public ArrayList<String> getInputAll(){
        inputAll();
        return numbers;
    }

    public boolean setOutput(String number){
        this.number = number;
        return output();
    }
    public boolean deletePlate(String number){
        this.number = number;
        boolean b = delete();

        return b;
    }

    public boolean connection() {
        conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:DB.s3db");
            System.out.println("DataBase connected!");
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("DBIOsql - ClassNotFoundException");
            return false;
        } catch (SQLException e) {
            System.out.println("DBIOsql - SQLException");
            return false;
        }
    }

    public void createDB(){
        try {
            statmt = conn.createStatement();
            statmt.execute("CREATE TABLE if not exists 'plate' ('Number' text);");

            System.out.println("Table created or exists!");
        } catch (SQLException e) {
            System.out.println("OOPS...");
        }
    }

    private boolean output(){
        try {
            statmt.execute("INSERT INTO 'plate' ('Number') VALUES ('" + number + "');");
            System.out.println("Plate is successfully added!");
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR, Can`t add a plate!");
            return false;
        }
    }

    private void inputAll(){
        numbers.clear();

        try {
            resSet = statmt.executeQuery("SELECT * FROM plate");

            while (resSet.next())
                numbers.add(resSet.getString("Number"));

            System.out.println("DataBase was read!");
        } catch (SQLException e) {
            System.out.println("DataBase wasn`t read!");
        } catch (Exception e) {
            System.out.println("DataBase is empty!");
        }
    }

    private boolean delete(){
        try {
            statmt.execute("DELETE FROM plate WHERE Number = '" + number + "';");
            System.out.println("Plate was deleted!");
            return true;
        } catch (SQLException e) {
            System.out.println("Can`t delete a plate!");
            return false;
        }
    }

    public boolean closeDB(){
        try {
            conn.close();
            statmt.close();
            resSet.close();

            System.out.println("Connection closed!");
            return true;
        } catch (SQLException e) {
            System.out.println("Can`t close DataBase!");
            return false;
        }
    }

}
