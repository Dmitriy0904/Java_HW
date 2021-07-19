import models.Location;
import models.Problem;
import models.Route;
import models.Solution;
import logic.ShortestPath;
import db.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void run(){
        try {
            ConnectionSetup connectionSetup = new ConnectionSetup();
            Connection connection = connectionSetup.getConnection();
            DbWorker dbWorker = new DbWorker(connection);
            List<Location> locations = dbWorker.readLocations();
            List<Problem> problems = dbWorker.readProblems();
            List<Route> routes = dbWorker.readRoutes();

            List<Solution> solutions = new ArrayList<>();
            for(Problem item : problems){
                Solution newSolution = ShortestPath.findShortestPath(routes, locations, item);
                solutions.add(newSolution);
            }

            dbWorker.writeSolutions(solutions);
            connection.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}