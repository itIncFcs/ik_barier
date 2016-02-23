import java.util.ArrayList;

/**
 * Created by Вадим on 16.02.2016.
 */
public class Main {
    public static void main(String[] args){
        ArrayList<String> numbers = new ArrayList<>();
        DBIOsql db = new DBIOsql();
        connectToDBTSql cd = new connectToDBTSql();
        db.connection();
        db.createDB();

        cd.add("P080YB55", db);
        cd.add("C090HT77", db);
        cd.add("C099HT54", db);
        numbers = cd.selectAll();

        for (String s : numbers)
            System.out.println(s);
        cd.delete("C099HT54", db);
        numbers.clear();

        numbers = cd.selectAll();
        for (String s : numbers)
            System.out.println(s);

        boolean b = cd.select("C099HT54", db);
        System.out.println(b);
        db.closeDB();

    }
}
