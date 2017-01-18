package logic;

import models.bipartite_matching.BipartiteGraph;
import models.bipartite_matching.FlowNetwork;

/**
 * Created by nura on 20/11/16.
 */
public class Strategy {
    private static Strategy ourInstance = new Strategy();

    public static Strategy getInstance() {
        return ourInstance;
    }

    private Strategy() {
    }
    public static void allocate() {
        BipartiteGraph bipartiteGraph = new BipartiteGraph();
        bipartiteGraph.printGraph();
        FlowNetwork networkGraph = new FlowNetwork(bipartiteGraph);
        networkGraph.printGraph();
        FordFulkersonAlgorithm algorithm = new FordFulkersonAlgorithm();
        algorithm.allocate();
        //GreedyAlgorithm greedy= new GreedyAlgorithm();
        /*if (greedy.allocate()) {
            TaskUtils.getAllocatedTask().forEach(task -> System.out.println(task.toString()));
        }*/
    }
}
