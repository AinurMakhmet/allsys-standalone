package logic;

import javafx.util.Pair;
import models.DatabaseEntity;
import models.Project;
import models.SystemData;
import models.Task;
import org.apache.logging.log4j.Logger;
import servers.LocalServer;

import java.io.IOException;
import java.util.*;

/**
 * Created by nura on 20/11/16.
 */
public class StrategyContext {
    private Strategy strategy;
    private List<Task> highPriorityTasks,
            mediumPriorityTasks,
            lowPriorityTasks;
    private static int numOfTasksInvalidForAllocation ,
            numOfUnallocatedTasks,
            numOfUnallocatedProjects,
            numOfProjectsInvalidForAllocation,
            totalProfitFromSelectedProjects;

    private Logger logger;

    public StrategyContext(Strategy allocationAlgorithm) {
        strategy = allocationAlgorithm;
        if (strategy.getClass().equals(MaximumProfitAlgorithm.class)) {
            logger = LocalServer.mpLogger;
        } else if (strategy.getClass().equals(EdmondsKarpStrategy.class)) {
            logger = LocalServer.ekLogger;
        } else if (strategy.getClass().equals(GreedyStrategy.class)) {
            logger = LocalServer.gLogger;
        }
    }

    public void maxAllocationAlgorithmNoPriotity(List<Task> tasksToAllocate){
        numOfUnallocatedTasks = 0;
        numOfTasksInvalidForAllocation = 0;
        Iterator it = tasksToAllocate.iterator();
        while(it.hasNext()) {
            Task task = (Task)it.next();
            try {
                task.setRecommendedAssignee(null);
                if(task.getEmployee()==null && task.getEndTime()!=null && task.getStartTime()!=null && task.getSkills().size()>0) {
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            it.remove();
            numOfTasksInvalidForAllocation++;
        }
        numOfUnallocatedTasks = numOfTasksInvalidForAllocation;
        long begTime = System.currentTimeMillis();
        if (!tasksToAllocate.isEmpty()) {
            allocateTasks(tasksToAllocate, "all");
        }
        long endTime = System.currentTimeMillis();
        logger.info(strategy.getClass().getSimpleName()+": Total time: {} ms", (endTime-begTime));
        logger.info("Total number of unallocated tasks: {}. Among them number of tasks invalid for allocation: {}\n", numOfUnallocatedTasks, numOfTasksInvalidForAllocation);
    }


    public void maxAllocationAlgorithm(List<Task> tasksToAllocate){
        distributeValidForAllocationTasksByPriority(tasksToAllocate);

        long begTime = System.currentTimeMillis();
        //TODO: fix Greedy returns only allocated tasks. Need to return all tasks.
        if (!highPriorityTasks.isEmpty()) {
            allocateTasks(highPriorityTasks, "high");
        }
        if (!mediumPriorityTasks.isEmpty()) {
            allocateTasks(mediumPriorityTasks, "medium");
        }
        if (!lowPriorityTasks.isEmpty()) {
            allocateTasks(lowPriorityTasks, "low");
        }
        long endTime = System.currentTimeMillis();
        logger.info(strategy.getClass().getSimpleName()+": Total time: {} ms", (endTime-begTime));
        logger.info("Total number of unallocated tasks: {}. Among them number of tasks invalid for allocation: {}\n\n", numOfUnallocatedTasks, numOfTasksInvalidForAllocation);
    }

    /**
     *
     * @param listOfTasks lists of tasks to Allocate. All the tasks in the list are of one priority level.
     * @param priority priority level for logging purposes.
     */
    private void allocateTasks(List<Task> listOfTasks, String priority) {
        if (!listOfTasks.isEmpty()) {
            listOfTasks.forEach(task -> logger.trace(task.toString()));
            logger.info("Start allocating the list of {} priority tasks with a size of {}", priority, listOfTasks.size());
            strategy.allocate(listOfTasks);
            //taskResultList.addAll(strategy.allocate(listOfTasks));
            numOfUnallocatedTasks +=  strategy.getNumberOfUnallocatedTasks();
            logger.info("Number of unallocated tasks: {}. \n", strategy.getNumberOfUnallocatedTasks());

        }
    }

    /**
     * distribute tasks of different priority into separate list to deal with them separately.
     * Is used internally by maxAllocationAlgorithm.
     */
    private void distributeValidForAllocationTasksByPriority(List<Task> tasksToAllocate) {
        numOfUnallocatedTasks = 0;
        numOfTasksInvalidForAllocation = 0;
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
            numOfTasksInvalidForAllocation++;
        }
        numOfUnallocatedTasks = numOfTasksInvalidForAllocation;
    }

    public void maxProfit(List<Project> projectsToAllocate){
        numOfUnallocatedProjects =0;
        numOfProjectsInvalidForAllocation=0;
        totalProfitFromSelectedProjects = 0;
        getTasksInfo(projectsToAllocate);

        Iterator<Project> iter = projectsToAllocate.iterator();
        while (iter.hasNext()) {
            Project project = iter.next();
            try {
                if (project.getTasks().isEmpty()) {
                    numOfProjectsInvalidForAllocation++;
                    iter.remove();
                } else {
                    for (Task t : project.getTasks()) {
                        Task task = SystemData.getAllTasksMap().get(t.getId());
                        task.setRecommendedAssignee(null);
                        if (t.getStartTime() == null || t.getEndTime() == null || task.getSkills().size() <= 0) {
                            numOfProjectsInvalidForAllocation++;
                            iter.remove();
                            logger.info("project contains tasks with unspecified startdate or end date, therefore will not be considered for allocation");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        numOfUnallocatedProjects = numOfProjectsInvalidForAllocation;

        long begTime = System.currentTimeMillis();
        for (Project project : projectsToAllocate) {
            try {
                numOfUnallocatedTasks = 0;
                boolean isFullyAllocated = false;

                allocateTasks(project.getTasks(), "all");

                Integer profit = 0;
                Integer projectPrimeCost = 0;
                if (numOfUnallocatedTasks >0) {
                    isFullyAllocated=false;
                    for(Task task: project.getTasks()) {
                        task.setRecommendedAssignee(null);
                    }
                } else {
                    isFullyAllocated=true;
                    for(Task t: project.getTasks()) {
                        Task task = SystemData.getAllTasksMap().get(t.getId());
                        projectPrimeCost += task.getRecommendedAssignee().getDailySalary()*task.getDuration();
                    }
                    profit = project.getPrice() - projectPrimeCost;
                    logger.trace("Max profit for the project = {}", profit);

                }
                project.setEstimatedCost(projectPrimeCost);
                project.setEstimatedProfit(profit);
                if (!isFullyAllocated) {
                    numOfUnallocatedProjects++;
                } else {
                    totalProfitFromSelectedProjects += project.getEstimatedProfit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info(strategy.getClass().getSimpleName()+": Total time: {} ms", (endTime-begTime));
        logger.info("Total number of unallocated projects: {}. Among them number of projects invalid for allocation: {}\n\n", numOfUnallocatedProjects, numOfProjectsInvalidForAllocation);
        logger.info(strategy.getClass().getSimpleName()+": Max profit = {}", totalProfitFromSelectedProjects);
    }

    /**
     * the method is used only for debugging purposes by maxProfitAlgorithm method.
     */
    private void getTasksInfo(List<Project> projectsToAllocate) {
        Set<Pair<Task, Task>> taskPairs = new HashSet<>();
        for (Project project : projectsToAllocate) {
            logger.info(project.getName().toString());
            try {
                for (Task task1: project.getTasks()) {
                    project.getTasks().forEach(task2 -> {
                        if (!task1.equals(task2)) {
                            Pair pair = new Pair(task1, task2);
                            if (!(taskPairs.contains(pair) || taskPairs.contains(new Pair(task2, task1))))
                                taskPairs.add(new Pair(task1, task2));
                        }
                    });
                    logger.info(task1.toString());
                }
                for (Pair<Task, Task> taskPair: taskPairs) {
                    Task task0 = taskPair.getKey();
                    Task task1 = taskPair.getValue();
                    if (task0.timeOverlapWith(task1)) {
                        logger.info("Task {} has timeOverllaping with Task {}", task0.getId(), task1.getId());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static class EntityComparator implements Comparator<DatabaseEntity> {
        @Override
        public int compare(DatabaseEntity e1, DatabaseEntity e2) {
            if (e1.getId() > e2.getId()) return 1;
            else if (e1.getId() == e2.getId()) return 0;
            else return -1;
        }
    }

    public static int getNumOfTasksInvalidForAllocation() {
        return numOfTasksInvalidForAllocation;
    }

    public static int getNumOfProjectsInvalidForAllocation() {
        return numOfProjectsInvalidForAllocation;
    }

    public static int getNumOfUnallocatedTasks() {
        return numOfUnallocatedTasks;
    }

    public static int getNumOfUnallocatedProjects() {
        return numOfUnallocatedProjects;
    }

    public static int getTotalProfitFromSelectedProjects() {
        return totalProfitFromSelectedProjects;
    }
}
