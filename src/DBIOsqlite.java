import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Вадим on 25.02.2016.
 */
public class DBIOsqlite implements DBIO {
    private Connection conn;
    private Statement statmt;
    private ResultSet resSet;

    @Override
    public boolean connect(String connectionDataBase) throws Exception {// params[0] = "org.sqlite.JDBC", params[1] = "jdbc:sqlite: [databasename].s3db"
        String[] params = connectionDataBase.split("|");
        conn = null;
        Class.forName(params[0]);
        conn = DriverManager.getConnection(params[1]);
        return true;
    }

    @Override
    public boolean create(String tablename, String coloumns) throws Exception{
        statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists '" + tablename + "' (" + coloumns +");");
        return true;
    }

    @Override
    public boolean close(ArrayList<String> numbers) throws Exception{
        conn.close();
        statmt.close();
        resSet.close();
        return true;
    }

    @Override
    public boolean add(String number, String name) throws Exception{
        statmt.execute("INSERT INTO 'plate' ('Number') VALUES ('" + number + "','" + name + "');");
        return true;
    }

    @Override
    public boolean delete(String number) throws Exception{
        statmt.execute("DELETE FROM plate WHERE Number = '" + number + "');");
        return true;
    }

    @Override
    public ArrayList<String> select() throws Exception{
        resSet = statmt.executeQuery("SELECT * FROM plate");
        ArrayList<String> numbers = new ArrayList<>();
        while (resSet.next())
            numbers.add(resSet.getString("Number"));

        return numbers;
    }
}
