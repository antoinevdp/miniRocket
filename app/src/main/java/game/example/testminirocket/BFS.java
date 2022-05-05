package game.example.testminirocket;
// class pour connaître le chemin le plus rapide pour aller d'un point A à un point B
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class BFS {

    public static ArrayList<Integer> calculateShortestPath(int nb_connections, int source, int destination, ArrayList<ArrayList<Integer>> list_connections)
    {

        // Liste pour sauvegarder quels planètes sont connectées
        ArrayList<ArrayList<Integer>> adj =
                new ArrayList<ArrayList<Integer>>(nb_connections);
        for (int i = 0; i < nb_connections; i++) {
            adj.add(new ArrayList<Integer>());
        }

        // Boucle for pour ajouter les connections entre les planètes à partir de la liste des connections en paramètre
        for (int i = 0; i < list_connections.size(); i++) {
            addEdge(adj, list_connections.get(i).get(0), list_connections.get(i).get(1));
        }
        // Résultat de la recherche
        LinkedList<Integer> linked_list_best_path = printShortestDistance(adj, source, destination, nb_connections);
        if (linked_list_best_path == null){ // Si on n'a pas trouvé un chemin
            return null; // on return null
        }else { // Si on a trouvé un chemin
            ArrayList<Integer> list_best_path = new ArrayList<>(linked_list_best_path); // On switch d'une linkedList à un ArrayList
            Collections.reverse(list_best_path); // On reverse la liste pour avoir le résultat dans le bon sens
            return list_best_path; // on return la liste du chemin trouvé
        }

    }

    // Ajouter une connection
    private static void addEdge(ArrayList<ArrayList<Integer>> adj, int i, int j)
    {
        adj.get(i).add(j);
        adj.get(j).add(i);
    }

    // function to print the shortest distance and path
    // between source vertex and destination vertex
    private static LinkedList<Integer> printShortestDistance(
            ArrayList<ArrayList<Integer>> adj,
            int s, int dest, int v)
    {
        // predecessor[i] array stores predecessor of
        // i and distance array stores distance of i
        // from s
        int pred[] = new int[v];
        int dist[] = new int[v];

        if (BFS(adj, s, dest, v, pred, dist) == false) {
            System.out.println("Given source and destination" +
                    "are not connected");
            return null;
        }

        // LinkedList to store path
        LinkedList<Integer> path = new LinkedList<Integer>();
        int crawl = dest;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }

        // Print distance
        System.out.println("Shortest path length is: " + dist[dest]);

        // Print path
        System.out.println("Path is ::");
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i) + " ");
        }
        return path;
    }

    // a modified version of BFS that stores predecessor
    // of each vertex in array pred
    // and its distance from source in array dist
    private static boolean BFS(ArrayList<ArrayList<Integer>> adj, int src,
                               int dest, int v, int pred[], int dist[])
    {
        // a queue to maintain queue of vertices whose
        // adjacency list is to be scanned as per normal
        // BFS algorithm using LinkedList of Integer type
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // boolean array visited[] which stores the
        // information whether ith vertex is reached
        // at least once in the Breadth first search
        boolean visited[] = new boolean[v];

        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        // now source is first to be visited and
        // distance from source to itself should be 0
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        // bfs Algorithm
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj.get(u).size(); i++) {
                if (visited[adj.get(u).get(i)] == false) {
                    visited[adj.get(u).get(i)] = true;
                    dist[adj.get(u).get(i)] = dist[u] + 1;
                    pred[adj.get(u).get(i)] = u;
                    queue.add(adj.get(u).get(i));

                    // stopping condition (when we find
                    // our destination)
                    if (adj.get(u).get(i) == dest)
                        return true;
                }
            }
        }
        return false;
    }
}
// This code is contributed by Sahil Vaid
