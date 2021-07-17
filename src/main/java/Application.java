import DataClasses.Location;
import DataClasses.Problem;
import DataClasses.Route;
import DataClasses.Solution;
import ShortestPath.ShortestPath;
import db.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void run(){
        try {
            Connection connection = ConnectionSetup.getConnection();
            DbWorker dbWorker = new DbWorker(connection);
            List<Location> locations = dbWorker.readLocations();
            List<Problem> problems = dbWorker.readProblems();
            List<Route> routes = dbWorker.readRoutes();

            List<Solution> solutions = new ArrayList<>();
            for(Problem item : problems){
                Solution newSolution = ShortestPath.findShortestPath(routes, locations, item);
                solutions.add(newSolution);
            }

            dbWorker.writeSolutions(connection, solutions);
            connection.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}