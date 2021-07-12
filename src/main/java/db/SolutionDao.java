package db;

import DataClasses.Solution;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SolutionDao {
    public static List<Solution> readSolutions(Connection connection){
        String SELECT_ALL_PROBLEMS = "SELECT * FROM solution";
        List<Solution> solutions;

        try (Statement statement = connection.createStatement()) {
            solutions = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_PROBLEMS);
            while (resultSet.next()) {
                Solution newSolution = new Solution();
                newSolution.setProblem_id(resultSet.getInt(1));
                newSolution.setCost(resultSet.getInt(2));
                solutions.add(newSolution);
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }

        return solutions;
    }



    public static void writeSolution(Connection connection, Solution solution){

    }
}
