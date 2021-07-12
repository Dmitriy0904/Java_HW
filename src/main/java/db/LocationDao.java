package db;

import DataClasses.Location;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocationDao {
    public static List<Location> readLocations(Connection connection){
        String SELECT_ALL_LOCATIONS = "SELECT * FROM location";
        List<Location> locations;

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
            throw new RuntimeException(exception);
        }

        return locations;
    }
}
