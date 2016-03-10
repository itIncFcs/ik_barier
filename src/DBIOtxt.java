import java.io.*;
import java.util.ArrayList;

/**
 * Created by Вадим on 25.02.2016.
 */
public class DBIOtxt implements DBIO{
    private ArrayList<String> bufNums;
    private File database;
    private BufferedReader br;
    private BufferedWriter bw;

    @Override
    public boolean connect(String connectionDataBase) throws Exception {
        database = new File(connectionDataBase);
        bufNums = new ArrayList<>();
        return database.exists();
    }

    @Override
    public boolean create(String connectionDataBase, String coloumns) throws Exception {
        database = new File(connectionDataBase);
        return database.createNewFile();
    }

    @Override
    public boolean close(ArrayList<String> numbers) throws Exception {
        bw = new BufferedWriter(new FileWriter(database));
        bufNums = numbers;
        database.delete();
        database.createNewFile();

        for (String s : bufNums)
            bw.append(s + "\n");

        bufNums.clear();
        bw.flush();
        br.close();
        bw.close();
        return true;
    }

    @Override
    public boolean add(String number, String name) throws Exception {
        if (!bufNums.contains(number)) {
            bufNums.add(number);
            return true;
        }else return false;
    }

    @Override
    public boolean delete(String number) throws Exception {
        if (bufNums.contains(number)) {
            bufNums.remove(number);
            return true;
        } else {return false;}
    }

    @Override
    public ArrayList<String> select() throws Exception {
        br = new BufferedReader(new FileReader(database));

        String s;
        while ((s = br.readLine()) != null)
            bufNums.add(s);

        return bufNums;
    }
}
