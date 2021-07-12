package db;

import DataClasses.Problem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProblemDao{
    public static List<Problem> readProblems(Connection connection){
        String SELECT_ALL_PROBLEMS = "SELECT * FROM route";
        List<Problem> problems;

        try (Statement statement = connection.createStatement()) {
            problems = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_PROBLEMS);
            while (resultSet.next()) {
                Problem newProblem = new Problem();
                newProblem.setId(resultSet.getInt(1));
                newProblem.setId_from(resultSet.getInt(2));
                newProblem.setId_to(resultSet.getInt(3));
                problems.add(newProblem);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return problems;
    }
}
