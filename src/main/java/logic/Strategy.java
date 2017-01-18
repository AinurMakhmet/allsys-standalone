package logic;

import entity_utils.TaskUtils;
import models.graph_models.BipartiteGraph;
import models.graph_models.NetworkGraph;

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
        //NetworkGraph networkGraph = new NetworkGraph(bipartiteGraph);
        //networkGraph.printGraph();
        /*GreedyAlgorithm greedy= new GreedyAlgorithm();
        if (greedy.allocate()) {
            TaskUtils.getAllocatedTask().forEach(task -> System.out.println(task.toString()));
        }*/
    }
}
