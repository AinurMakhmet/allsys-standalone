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
    static List<Task> result;
    static List<Task> highPriorityTasks;
    static List<Task> mediumPriorityTasks;
    static List<Task> lowPriorityTasks;

    /**
     * distribute tasks of different priority into separate list to deal with them separately
     * @param tasksToAllocate
     */
    public static void distributeByPriority(List<Task> tasksToAllocate) {
        result = new ArrayList<>();
        highPriorityTasks = new ArrayList<>();
        mediumPriorityTasks = new ArrayList<>();
        lowPriorityTasks = new ArrayList<>();
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

    }
    public static List<Task> getLargestMatching(List<Task> tasksToAllocate) {
        distributeByPriority(tasksToAllocate);
        int numTries = 1;
        //int numTries = 1;
        long begTime = System.currentTimeMillis();
        for (int i = 0; i < numTries; ++i) {
            result.addAll(FordFulkersonAlgorithm.allocate(highPriorityTasks));
            result.addAll(FordFulkersonAlgorithm.allocate(mediumPriorityTasks));
            result.addAll(FordFulkersonAlgorithm.allocate(lowPriorityTasks));
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("FF: Total time for %10d tries: %d ms\n", numTries, (endTime-begTime));

        return result;
    }

    public static List<Task> getMatchingUsingGreedy(List<Task> tasksToAllocate) {
        distributeByPriority(tasksToAllocate);

        int numTries = 1;
        long begTime = System.currentTimeMillis();
        for (int i = 0; i < numTries; ++i) {
            result.addAll(GreedyAlgorithm.allocate(highPriorityTasks));
            result.addAll(GreedyAlgorithm.allocate(mediumPriorityTasks));
            result.addAll(GreedyAlgorithm.allocate(lowPriorityTasks));
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("GREEDY: Total time for %10d tries: %d ms\n", numTries, (endTime-begTime));

        result.forEach(task -> System.out.println(task.toString()));
        return result;
    }
}
