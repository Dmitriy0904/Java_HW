import controller.Controller;
import entity.*;
import logic.NearestLessonFinder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void run(){
        Controller controller = new Controller();
        NearestLessonFinder lessonFinder = new NearestLessonFinder();

        log.info("Getting session factory");
        Configuration configuration = new Configuration().configure();

        try(SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            try {
                log.info("Beginning transaction");
                session.beginTransaction();

                log.info("Reading entity id");
                Long studentId = controller.readStudentId();

                log.info("Finding student by id");
                Student student = session.find(Student.class, studentId);

                log.info("Finding nearest lesson for student id:" + student.getId());
                Lesson nearestLesson = lessonFinder.findNearestLesson(student);

                log.info("Printing results");
                controller.printResult(student, nearestLesson);

                session.getTransaction().commit();
                log.info("Transaction was completed successfully");

            } catch (Exception exception){
                log.error("Transaction error!. Reason:" + exception.getMessage());
                session.getTransaction().rollback();
                throw new RuntimeException(exception);
            }
        }
    }
}