package logic;

import entity_utils.TaskUtils;
import models.Task;

import java.util.List;

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

    public static void allocate(List<Task> tasksToAllocate) {
        FordFulkersonAlgorithm algorithm = new FordFulkersonAlgorithm();
        algorithm.allocate(tasksToAllocate);
        //GreedyAlgorithm greedy= new GreedyAlgorithm();
        //greedy.allocate(tasksToAllocate).forEach(task -> System.out.println(task.toString()));

    }
}
