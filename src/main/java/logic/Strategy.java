package logic;

import entity_utils.TaskUtils;
import models.Task;

import java.util.*;

/**
 * Created by nura on 20/11/16.
 */
public class Strategy {
    /**
     * Only finds the matches for the unallocated tasks.
     * @param tasksToAllocate
     * @return
     */
    public static List<Task> getLargestMatching(List<Task> tasksToAllocate) {
        List<Task> result = new ArrayList<>();
        List<Task> highPriorityTasks = new ArrayList<>();
        List<Task> mediumPriorityTasks = new ArrayList<>();
        List<Task> lowPriorityTasks = new ArrayList<>();
        for (Task task: tasksToAllocate) {
            if(task.getEmployee()==null && task.getEndTime()!=null && task.getStartTime()!=null) {
                switch (task.getPriority()) {
                    case HIGH:
                        highPriorityTasks.add(task);
                        break;
                    case MEDIUM:
                        mediumPriorityTasks.add(task);
                        break;
                    case LOW:
                        lowPriorityTasks.add(task);
                        break;
                }
                continue;
            }
            result.add(task);
        }

        FordFulkersonAlgorithm algorithm = new FordFulkersonAlgorithm();
        result.addAll(algorithm.allocate(highPriorityTasks));

        algorithm = new FordFulkersonAlgorithm();
        result.addAll(algorithm.allocate(mediumPriorityTasks));

        algorithm = new FordFulkersonAlgorithm();
        result.addAll(algorithm.allocate(lowPriorityTasks));

        return result;
    }

    public static List<Task> getMatchingUsingGreedy(List<Task> tasksToAllocate) {
        GreedyAlgorithm greedy= new GreedyAlgorithm();
        List<Task> result = greedy.allocate(tasksToAllocate);
        result.forEach(task -> System.out.println(task.toString()));
        return result;
    }
}
