import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        if(args.length == 1){
            Application.run(args[0]);
        } else {
            try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
                System.out.println("Enter path to the csv file:");
                String csvPath = bf.readLine();
                Application.run(csvPath);
            } catch (IOException exception){
                throw new RuntimeException(exception);
            }
        }
    }
}