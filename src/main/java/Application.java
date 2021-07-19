import model.Student;
import reflection.AppProperties;

public class Application {
    public static void run(){
        AppProperties appProperties = new AppProperties();
        Student student = appProperties.createObject(Student.class);
        System.out.println(student.toString());
    }
}
