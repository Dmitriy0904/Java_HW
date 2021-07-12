package db;

import DataClasses.Route;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RouteDao {
    public static List<Route> readRoutes(Connection connection){
        String SELECT_ALL_ROUTES = "SELECT * FROM route";
        List<Route> routes;

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
            throw new RuntimeException(exception);
        }

        return routes;
    }
}
