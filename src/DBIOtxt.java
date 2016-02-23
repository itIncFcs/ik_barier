import java.io.*;
import java.util.ArrayList;

/**
 * Created by Вадим on 22.02.2016.
 */
public class DBIOtxt {
    private ArrayList<String> numbers;
    private String number;

    public ArrayList<String> getInput(){
        return input();
    }

    public boolean setOutput(String number){
        this.number = number + "\n";
        return output();
    }

    private ArrayList<String> input(){
        File db = new File("DB.txt");
        numbers = new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream(db);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            while (br.ready())
                numbers.add(br.readLine());

            br.close();
            fis.close();

        } catch (FileNotFoundException e) {
            System.out.println("Can`t read file!");
            numbers.add("File doesn`t exists!");
            return numbers;
        } catch (IOException e) {
            e.printStackTrace();
            numbers.add("Can`t read file!");
            return numbers;
        }
        return numbers;
    }

    private boolean output(){
        File db = new File("DB.txt");

        try {
            FileWriter write = new FileWriter(db, true);
            write.append(number);
            write.flush();
            write.close();
        } catch (IOException e) {
            System.out.println("Can`t write to file!");
            return false;
        }

        return true;
    }
}
