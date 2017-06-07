package logic;

import javafx.util.Pair;
import models.Project;
import models.SystemData;
import models.Task;
import org.apache.logging.log4j.Logger;
import servers.LocalServer;

import java.io.IOException;
import java.util.*;

/**
 * A class that determines what strategy to call base on the constrcutor parameters.
 */
public class StrategyContext {
    private Strategy strategy;
    private static int numOfTasksInvalidForAllocation ,
            numOfUnallocatedTasks,
            numOfUnallocatedProjects,
            numOfProjectsInvalidForAllocation,
            totalProfitFromSelectedProjects;
    private List selectedEntitiesToAllocate;

    private Logger logger;
    private long begTime, endTime;

    /**
     *
     * @param allocationAlgorithm depends on the user selection.
     *                            Either EdmondsKarpStrategy or GreedyStrategy if a list of tasks was selected.
     *                            If a list of projects was selected, MaximumrProfitStrategy.
     * @param entitiesToAllocate either a list of tasks, or a list of projects.
     */
    public StrategyContext(Strategy allocationAlgorithm, List entitiesToAllocate) {
        strategy = allocationAlgorithm;
        selectedEntitiesToAllocate = new LinkedList();
        selectedEntitiesToAllocate.addAll(entitiesToAllocate);
        SystemData.getAllTasksMap().values().forEach(task -> {
            if (task.getRecommendedAssignee()!=null) {
                task.setRecommendedAssignee(null);
                System.out.println("task reset:"+task.getName());
            }
        });
        if (strategy.getClass().equals(MaximumProfitStrategy.class)) {
            logger = LocalServer.mpLogger;
            computeAllocationForProjects((List<Project>)selectedEntitiesToAllocate);
        } else if (strategy.getClass().equals(EdmondsKarpStrategy.class)) {
            logger = LocalServer.ekLogger;
            computeAllocationForTasks((List<Task>)selectedEntitiesToAllocate);
        } else if (strategy.getClass().equals(GreedyStrategy.class)) {
            logger = LocalServer.gLogger;
            computeAllocationForTasks((List<Task>)selectedEntitiesToAllocate);
        }
    }

    private void computeAllocationForTasks(List<Task> tasksToAllocate){
        numOfUnallocatedTasks = 0;
        numOfTasksInvalidForAllocation = 0;
        List<Task> highPriorityTasks = new ArrayList<>();
        List<Task> mediumPriorityTasks = new ArrayList<>();
        List<Task> lowPriorityTasks = new ArrayList<>();

        //O(t)
        Iterator it = tasksToAllocate.iterator();
        while(it.hasNext()) {
            Task task = (Task)it.next();
            try {
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
            it.remove();
            numOfTasksInvalidForAllocation++;
        }
        numOfUnallocatedTasks = numOfTasksInvalidForAllocation;
        begTime = System.currentTimeMillis();
        startAllocatingTasks(highPriorityTasks, "high");
        startAllocatingTasks(mediumPriorityTasks, "medium");
        startAllocatingTasks(lowPriorityTasks, "low");
        endTime = System.currentTimeMillis();
        logger.info(strategy.getClass().getSimpleName()+": Total time: {} ms", (endTime-begTime));
        logger.info("Total number of unallocated tasks: {}. Among them number of tasks invalid for allocation: {}\n", numOfUnallocatedTasks, numOfTasksInvalidForAllocation);
    }

    /**
     * A method that is used by all strategies.
     * @param listOfTasks lists of tasks to Allocate.
     * @param priority priority level for logging purposes.
     */
    private void startAllocatingTasks(List<Task> listOfTasks, String priority) {
        if (!listOfTasks.isEmpty()) {
            listOfTasks.forEach(task -> logger.trace(task.toString()));
            logger.info("Start allocating the list of {} priority tasks with a size of {}", priority, listOfTasks.size());
            strategy.allocate(listOfTasks);
            numOfUnallocatedTasks +=  strategy.getNumberOfUnallocatedTasks();
            logger.info("Number of unallocated tasks: {}. \n", strategy.getNumberOfUnallocatedTasks());

        }
    }

    private void computeAllocationForProjects(List<Project> projectsToAllocate){
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

        begTime = System.currentTimeMillis();
        for (Project project : projectsToAllocate) {
            try {
                numOfUnallocatedTasks = 0;
                startAllocatingTasks(project.getTasks(), "all");

                Integer profit = 0;
                Integer projectPrimeCost = 0;
                if (numOfUnallocatedTasks >0) {
                    for(Task task: project.getTasks()) {
                        SystemData.getAllTasksMap().get(task.getId()).setRecommendedAssignee(null);
                    }
                    numOfUnallocatedProjects++;
                } else {
                    for(Task t: project.getTasks()) {
                        Task task = SystemData.getAllTasksMap().get(t.getId());
                        projectPrimeCost += task.getRecommendedAssignee().getDailySalary()*task.getDuration();
                    }
                    profit = project.getPrice() - projectPrimeCost;
                    logger.trace("Max profit for the project = {}", profit);
                    project.setEstimatedCost(projectPrimeCost);
                    project.setEstimatedProfit(profit);
                    totalProfitFromSelectedProjects += project.getEstimatedProfit();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        endTime = System.currentTimeMillis();
        logger.info(strategy.getClass().getSimpleName()+": Total time: {} ms", (endTime-begTime));
        logger.info("Total number of unallocated projects: {}. Among them number of projects invalid for allocation: {}\n\n", numOfUnallocatedProjects, numOfProjectsInvalidForAllocation);
        logger.info(strategy.getClass().getSimpleName()+": Max profit = {}", totalProfitFromSelectedProjects);
    }

    /**
     * the method is used only for debugging and logging purposes by computeAllocationForProjects method.
     */
    private void getTasksInfo(List<Project> projectsToAllocate) {
        Set<Pair<Task, Task>> taskPairs = new HashSet<>();
        for (Project project : projectsToAllocate) {
            LocalServer.iLogger.info(project.getName().toString());
            try {
                for (Task task1: project.getTasks()) {
                    project.getTasks().forEach(task2 -> {
                        if (!task1.equals(task2)) {
                            Pair pair = new Pair(task1, task2);
                            if (!(taskPairs.contains(pair) || taskPairs.contains(new Pair(task2, task1))))
                                taskPairs.add(new Pair(task1, task2));
                        }
                    });
                    LocalServer.iLogger.info(task1.toString());
                }
                for (Pair<Task, Task> taskPair: taskPairs) {
                    Task task0 = taskPair.getKey();
                    Task task1 = taskPair.getValue();
                    if (task0.timeOverlapWith(task1)) {
                        LocalServer.iLogger.info("Task {} has timeOverllaping with Task {}", task0.getId(), task1.getId());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
