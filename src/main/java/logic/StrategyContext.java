package logic;

import models.Task;

import java.io.IOException;
import java.util.*;

/**
 * Created by nura on 20/11/16.
 */
public class StrategyContext {
    /**
     * Only finds the matches for the unallocated tasks.
     * @param tasksToAllocate
     * @return
     */
    private List<Task> tasksToAllocate,
            result,
            highPriorityTasks,
            mediumPriorityTasks,
            lowPriorityTasks;
    private int numberOfTasksUnvalidForAllocation,
            numberOfUnnalocatedTasks,
            numberOfUnnalocatedTasksOfHighPriority,
            numberOfUnnalocatedTasksOfMediumPriority,
            numberOfUnnalocatedTasksOfLowPriority;
    private Strategy strategy;

    public StrategyContext(Strategy allocationAlgorithm) {
        strategy = allocationAlgorithm;
    }

    public List<Task> executeStrategy(List<Task> tasksToAllocate){
        this.tasksToAllocate = tasksToAllocate;
        distributeValidTasksForAllocationByPriority();
        int numTries = 1;
        //int numTries = 1;
        long begTime = System.currentTimeMillis();
        for (int i = 0; i < numTries; ++i) {
            result.addAll(strategy.allocate(highPriorityTasks));
            numberOfUnnalocatedTasksOfHighPriority +=  strategy.numOfUnnalocatedTasks;
            result.addAll(strategy.allocate(mediumPriorityTasks));
            numberOfUnnalocatedTasksOfMediumPriority +=  strategy.numOfUnnalocatedTasks;
            result.addAll(strategy.allocate(lowPriorityTasks));
            numberOfUnnalocatedTasksOfLowPriority +=  strategy.numOfUnnalocatedTasks;
        }
        long endTime = System.currentTimeMillis();
        System.out.printf(strategy.getClass().getSimpleName()+": Total time for %10d tries: %d ms\n", numTries, (endTime-begTime));
        numberOfUnnalocatedTasks+=numberOfTasksUnvalidForAllocation
                +numberOfUnnalocatedTasksOfHighPriority
                +numberOfUnnalocatedTasksOfMediumPriority
                +numberOfUnnalocatedTasksOfLowPriority;
        result.sort(new TaskComparator());
        result.forEach(task -> System.out.println(task.toString()));
        System.out.print("Total number of unallocated tasks: "+ numberOfUnnalocatedTasks+". ");
        System.out.println("Among them number of tasks non-valid for allocation: "+ numberOfTasksUnvalidForAllocation);
        return result;
    }
    /**
     * distribute tasks of different priority into separate list to deal with them separately
     */
    private void distributeValidTasksForAllocationByPriority() {
        numberOfUnnalocatedTasks = 0;
        result = new ArrayList<>();
        highPriorityTasks = new ArrayList<>();
        mediumPriorityTasks = new ArrayList<>();
        lowPriorityTasks = new ArrayList<>();
        for (Task task: tasksToAllocate) {
            try {
                task.setRecommendedAssignee(null);
                if(task.getEmployee()==null && task.getEndTime()!=null && task.getStartTime()!=null && task.getSkills().size()>0) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.add(task);
        }
        numberOfTasksUnvalidForAllocation = result.size();

    }

    static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            if (t1.getId() > t2.getId()) return 1;
            else if (t1.getId() == t2.getId()) return 0;
            else return -1;
        }
    }
}
