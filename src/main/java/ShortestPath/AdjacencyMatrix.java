package ShortestPath;

import DataClasses.Location;
import DataClasses.Route;
import java.util.List;

public class AdjacencyMatrix {

    public static int[][] buildMatrix(List<Route> routes, List<Location> cities){
        int [][] adjacencyMatrix = new int[cities.size()][cities.size()];

        for (Route item : routes){
            adjacencyMatrix[item.getId_from() - 1][item.getId_to() - 1] = item.getCost();
        }

        for(int i = 0; i < cities.size(); i++){
            for(int j = 0; j < cities.size(); j++){
                if(adjacencyMatrix[i][j] == 0){
                    adjacencyMatrix[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        return adjacencyMatrix;
    }
}
