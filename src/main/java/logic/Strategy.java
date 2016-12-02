package logic;

import entity_utils.TaskUtils;
import models.Task;

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
        GreedyAlgorithm greedy= new GreedyAlgorithm();
        if (greedy.allocate()) {
            TaskUtils.getAllocatedTask().forEach(task -> System.out.println(task.toString()));
        }
    }
}
