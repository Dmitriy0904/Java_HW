import config.Path;
import mapper.CsvMapper;
import model.Student;
import table.CsvTable;
import java.util.List;

public class Application {
    public static void run(){
        CsvMapper csvMapper = new CsvMapper(Path.CSV_PATH.getPath());
        CsvTable csvTable = csvMapper.mapObjects();
        printResult(csvTable);
    }


    private static void printResult(CsvTable csvTable){
        List<Student> students = csvTable.getModels();
        List<String> header = csvTable.getHeader();

        System.out.println();
        //Проверка получения хедеров
        for (String item : header) {
            System.out.print(item + "  ");
        }

        System.out.println();
        //Проверка получения сущностей
        for (Student student : students) {
            System.out.println(student.toString());
        }

        System.out.println();
        //Проверка получения данных из таблциы по двум индексам
        for(int i = 0; i < csvTable.getData().size(); i++){
            for(int j = 0; j < csvTable.getData().get(i).length; j++){
                System.out.print(csvTable.get(i, j) + " ");
            }
            System.out.println();
        }

        System.out.println();
        //Проверка получения данных из таблциы по индексу и имени колонки
        for(int i = 0; i < csvTable.getData().size(); i++){
            for(int j = 0; j < csvTable.getData().get(i).length; j++){
                System.out.print(csvTable.get(i, header.get(j)) + " ");
            }
            System.out.println();
        }
    }
}