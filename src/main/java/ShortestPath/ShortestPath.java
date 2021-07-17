package ShortestPath;

import DataClasses.Location;
import DataClasses.Problem;
import DataClasses.Route;
import DataClasses.Solution;
import java.util.List;
import static java.lang.Math.min;
import static java.util.Arrays.fill;

public class ShortestPath {

    public static Solution findShortestPath(List<Route> routes, List<Location> locations, Problem problem){
        int[][]adjacencyMatrix = AdjacencyMatrix.buildMatrix(routes, locations);

        Solution solution = new Solution();
        solution.setProblem_id(problem.getId());

        int start = problem.getId_from() - 1;
        int end = problem.getId_to() - 1;

        int []shortestPaths = dijkstraAlgorithm(adjacencyMatrix, start);
        int resultPath = shortestPaths[end];

        solution.setCost(resultPath > 200_000 ? null : resultPath);
        return solution;
    }


    private static int[] dijkstraAlgorithm(int[][] adjacencyMatrix, int start) {
        int INF = Integer.MAX_VALUE / 2;
        int vNum = adjacencyMatrix.length;
        boolean[] used = new boolean [vNum];
        int[] dist = new int [vNum];

        fill(dist, INF);
        dist[start] = 0;

        for (;;) {
            int v = -1;
            for (int nv = 0; nv < vNum; nv++)
                if (!used[nv] && dist[nv] < INF && (v == -1 || dist[v] > dist[nv]))
                    v = nv;
            if (v == -1) break;
            used[v] = true;
            for (int nv = 0; nv < vNum; nv++)
                if (!used[nv] && adjacencyMatrix[v][nv] < INF)
                    dist[nv] = min(dist[nv], dist[v] + adjacencyMatrix[v][nv]);
        }

        return dist;
    }

}