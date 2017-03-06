package logic;

import models.DatabaseEntity;
import models.Project;
import models.Task;
import servers.LocalServer;

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
    private List<Project> projectsToAllocate,
            projectResultList;
    private int numberOfTasksUnvalidForAllocation,
            numberOfUnnalocatedTasks,
            numberOfUnnalocatedProjects,
            numberOfUnnalocatedTasksOfHighPriority,
            numberOfUnnalocatedTasksOfMediumPriority,
            numberOfUnnalocatedTasksOfLowPriority;
    private Strategy strategy;

    public StrategyContext(Strategy allocationAlgorithm) {
        strategy = allocationAlgorithm;
    }

    public List<Task> executeTaskStrategy(List<Task> tasksToAllocate){
        this.tasksToAllocate = tasksToAllocate;
        distributeValidTasksForAllocationByPriority();
        int numTries = 1;

        long begTime = System.currentTimeMillis();
        //TODO: fix Greedy returns only allocated tasks. Need to return all tasks.
        for (int i = 0; i < numTries; ++i) {
            if (!highPriorityTasks.isEmpty()) {
                result.addAll(strategy.allocate(highPriorityTasks));
                numberOfUnnalocatedTasksOfHighPriority +=  strategy.numOfUnnalocatedTasks;
            }
            if (!mediumPriorityTasks.isEmpty()) {
                result.addAll(strategy.allocate(mediumPriorityTasks));
                numberOfUnnalocatedTasksOfMediumPriority +=  strategy.numOfUnnalocatedTasks;
            }
            if (!lowPriorityTasks.isEmpty()) {
                result.addAll(strategy.allocate(lowPriorityTasks));
                numberOfUnnalocatedTasksOfLowPriority +=  strategy.numOfUnnalocatedTasks;
            }

        }
        long endTime = System.currentTimeMillis();
        LocalServer.iLogger.info(strategy.getClass().getSimpleName()+": Total time for {} tries: {} ms", numTries, (endTime-begTime));
        numberOfUnnalocatedTasks+=numberOfTasksUnvalidForAllocation
                +numberOfUnnalocatedTasksOfHighPriority
                +numberOfUnnalocatedTasksOfMediumPriority
                +numberOfUnnalocatedTasksOfLowPriority;
        result.sort(new EntityComparator());
        result.forEach(task -> LocalServer.iLogger.info(task.toString()));
        LocalServer.iLogger.info("Total number of unallocated tasks: {}. Among them number of tasks non-valid for allocation: {}\n",numberOfUnnalocatedTasks, numberOfTasksUnvalidForAllocation);
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
        tasksToAllocate = null;
        numberOfTasksUnvalidForAllocation = result.size();

    }

    static class EntityComparator implements Comparator<DatabaseEntity> {
        @Override
        public int compare(DatabaseEntity e1, DatabaseEntity e2) {
            if (e1.getId() > e2.getId()) return 1;
            else if (e1.getId() == e2.getId()) return 0;
            else return -1;
        }
    }

    public List<Project> executeProjectStrategy(List<Project> projectsToAllocate){
        numberOfUnnalocatedProjects=0;
        int totalProfitFromSelectedProjects = 0;
        projectResultList = new ArrayList<>();
        Iterator<Project> iter = projectsToAllocate.iterator();
        while (iter.hasNext()) {
            Project project = iter.next();
            try {
                project.getTasks().forEach(t -> {
                    if (t.getStartTime() == null || t.getEndTime() == null) {
                        projectResultList.add(project);
                        iter.remove();
                        LocalServer.mpLogger.info("project contains with unspecified start tate or end date, therefore will not be considered for allocation");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int numTries = 1;
        long begTime = System.currentTimeMillis();
        for (int i = 0; i < numTries; ++i) {
            for (Project project : projectsToAllocate) {
                try {
                    if (!MaximumProfitAlgorithm.getInstance().allocateByProject(project)) {
                        numberOfUnnalocatedProjects++;
                    } else {
                         totalProfitFromSelectedProjects += MaximumProfitAlgorithm.getInstance().getProfit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        long endTime = System.currentTimeMillis();
        LocalServer.iLogger.info(strategy.getClass().getSimpleName()+": Total time for {} tries: {} ms", numTries, (endTime-begTime));

        projectsToAllocate.sort(new EntityComparator());

        LocalServer.iLogger.info("Total number of unallocated projects: {}.\n",numberOfUnnalocatedProjects);
        LocalServer.iLogger.info(strategy.getClass().getSimpleName()+": Max profit = {}", totalProfitFromSelectedProjects);
        return projectsToAllocate;
    }
}
