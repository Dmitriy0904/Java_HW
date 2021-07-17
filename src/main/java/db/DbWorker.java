package db;
import DataClasses.Location;
import DataClasses.Problem;
import DataClasses.Route;
import DataClasses.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DbWorker {
    private final Logger infoLog = LoggerFactory.getLogger("info");
    private final Logger errorLog = LoggerFactory.getLogger("error");
    private Connection connection;


    public DbWorker(Connection connection) {
        this.connection = connection;
    }


    public List<Location> readLocations(){
        String SELECT_ALL_LOCATIONS = "SELECT * FROM location";
        List<Location> locations;

        infoLog.info("Try to read locations from db");

        try (Statement statement = connection.createStatement()) {
            locations = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_LOCATIONS);

            while (resultSet.next()) {
                Location newLocation = new Location();
                newLocation.setId(resultSet.getInt(1));
                newLocation.setName(resultSet.getString(2));
                locations.add(newLocation);
            }

        } catch (SQLException exception) {
            errorLog.error("SQLException in method read locations");
            throw new RuntimeException(exception);
        }

        infoLog.info("Locations were read successfully");
        return locations;
    }



    public List<Problem> readProblems(){
        String SELECT_PROBLEMS = "SELECT problem.id, problem.from_id, problem.to_id, solution.price  " +
                "FROM problem LEFT JOIN solution ON solution.problem_id = problem.id " +
                "WHERE solution.price is NULL";
        List<Problem> problems;

        infoLog.info("Try to read problems from db");

        try (Statement statement = connection.createStatement()) {
            problems = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(SELECT_PROBLEMS);

            while (resultSet.next()) {
                Problem newProblem = new Problem();
                newProblem.setId(resultSet.getInt(1));
                newProblem.setId_from(resultSet.getInt(2));
                newProblem.setId_to(resultSet.getInt(3));
                problems.add(newProblem);
            }

        } catch (SQLException exception) {
            errorLog.error("SQLException in method read problems");
            throw new RuntimeException(exception);
        }

        infoLog.info("Problems were read successfully");
        return problems;
    }



    public List<Route> readRoutes(){
        String SELECT_ALL_ROUTES = "SELECT * FROM route";
        List<Route> routes;

        infoLog.info("Try to read routes from db");

        try (Statement statement = connection.createStatement()) {
            routes = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_ROUTES);

            while (resultSet.next()) {
                Route newRoute = new Route();
                newRoute.setId(resultSet.getInt(1));
                newRoute.setId_from(resultSet.getInt(2));
                newRoute.setId_to(resultSet.getInt(3));
                newRoute.setCost(resultSet.getInt(4));
                routes.add(newRoute);
            }

        } catch (SQLException exception) {
            errorLog.error("SQLException in method read routes");
            throw new RuntimeException(exception);
        }

        infoLog.info("Routes were read successfully");
        return routes;
    }



    public void writeSolutions(Connection connection, List<Solution> solutions) {
        String INSERT_SOLUTION = "INSERT INTO solution(problem_id, price) VALUES(?, ?)";

        infoLog.info("Try to write solutions to db");

        try (PreparedStatement insertSolution = connection.prepareStatement(INSERT_SOLUTION)) {
            for (Solution item : solutions) {
                insertSolution.setInt(1, item.getProblem_id());
                insertSolution.setInt(2, item.getCost());
                insertSolution.addBatch();
            }
            insertSolution.executeBatch();

        } catch (SQLException exception) {
            errorLog.error("SQLException in method write solutions");
            throw new RuntimeException(exception);
        }

        infoLog.info("Solutions were written successfully");
    }


    public void setConnection(Connection connection) {
        infoLog.info("Updating connection. Previous one: {}", this.connection);
        this.connection = connection;
        infoLog.info("Connection was updated successfully. New connection: {}", this.connection);
    }
}
