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
    private List<Task> taskResultList,
            highPriorityTasks,
            mediumPriorityTasks,
            lowPriorityTasks;
    private List<Project> projectResultList;
    private static int numOfTasksInvalidForAllocation,
            numOfUnnalocatedTasks,
            numOfUnnalocatedProjects,
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

    public List<Task> maxAllocationAlgorithmNoPriotity(List<Task> tasksToAllocate){
        numOfUnnalocatedTasks = 0;
        taskResultList = new ArrayList<>();
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
            taskResultList.add(task);
        }
        numOfTasksInvalidForAllocation = taskResultList.size();

        long begTime = System.currentTimeMillis();
        if (!tasksToAllocate.isEmpty()) {
            allocateTasks(tasksToAllocate, "all");
        }
        long endTime = System.currentTimeMillis();
        logger.info(strategy.getClass().getSimpleName()+": Total time: {} ms", (endTime-begTime));
        logger.info("Total number of unallocated tasks: {}. Among them number of tasks non-valid for allocation: {}\n", numOfUnnalocatedTasks, numOfTasksInvalidForAllocation);

        taskResultList.sort(new EntityComparator());
        //taskResultList.forEach(task -> logger.info(task.toString()));
        return taskResultList;
    }


    public List<Task> maxAllocationAlgorithm(List<Task> tasksToAllocate){
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
        logger.info("Total number of unallocated tasks: {}. Among them number of tasks non-valid for allocation: {}\n\n", numOfUnnalocatedTasks, numOfTasksInvalidForAllocation);

        taskResultList.sort(new EntityComparator());
        //taskResultList.forEach(task -> logger.info(task.toString()));
        return taskResultList;
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
            taskResultList.addAll(strategy.allocate(listOfTasks));
            numOfUnnalocatedTasks +=  strategy.numOfUnnalocatedTasks;
            logger.info("Number of unallocated tasks: {}. \n", strategy.numOfUnnalocatedTasks);

        }
    }

    /**
     * distribute tasks of different priority into separate list to deal with them separately.
     * Is used internally by maxAllocationAlgorithm.
     */
    private void distributeValidForAllocationTasksByPriority(List<Task> tasksToAllocate) {
        numOfUnnalocatedTasks = 0;
        taskResultList = new ArrayList<>();
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
            taskResultList.add(task);
        }
        numOfTasksInvalidForAllocation = taskResultList.size();

    }

    public List<Project> maxProfitAlgorithm(List<Project> projectsToAllocate){
        numOfUnnalocatedProjects =0;
        totalProfitFromSelectedProjects = 0;
        projectResultList = new ArrayList<>();
        getTasksInfo(projectsToAllocate);

        Iterator<Project> iter = projectsToAllocate.iterator();
        while (iter.hasNext()) {
            Project project = iter.next();
            try {
                project.getTasks().forEach(t -> {
                    Task task = SystemData.getAllTasksMap().get(t.getId());
                    task.setRecommendedAssignee(null);
                    if (t.getStartTime() == null || t.getEndTime() == null) {
                        projectResultList.add(project);
                        iter.remove();
                        logger.info("project contains with unspecified start tate or end date, therefore will not be considered for allocation");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long begTime = System.currentTimeMillis();
        for (Project project : projectsToAllocate) {
            try {
                if (!MaximumProfitAlgorithm.getInstance().allocateByProject(project)) {
                    numOfUnnalocatedProjects++;
                } else {
                     totalProfitFromSelectedProjects += MaximumProfitAlgorithm.getInstance().getProfit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info(strategy.getClass().getSimpleName()+": Total time: {} ms", (endTime-begTime));
        logger.info("Total number of unallocated projects: {}.\n", numOfUnnalocatedProjects);
        logger.info(strategy.getClass().getSimpleName()+": Max profit = {}", totalProfitFromSelectedProjects);

        projectsToAllocate.sort(new EntityComparator());
        return projectsToAllocate;
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

    public static int getNumOfUnnalocatedTasks() {
        return numOfUnnalocatedTasks;
    }

    public static int getNumOfUnnalocatedProjects() {
        return numOfUnnalocatedProjects;
    }

    public static int getTotalProfitFromSelectedProjects() {
        return totalProfitFromSelectedProjects;
    }
}
