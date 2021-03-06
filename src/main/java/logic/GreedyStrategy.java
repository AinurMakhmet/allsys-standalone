package logic;

import javafx.util.Pair;
import models.Employee;
import models.SystemData;
import models.Task;
import models.bipartite_matching.BipartiteGraph;
import servers.LocalServer;

import java.io.IOException;
import java.util.*;

/**
 * Greedy algorithm allocates each task if possible
 * to the first available employee in the list.
 * It starts with a task that has the least number of possible assignees.
 */
public class GreedyStrategy implements Strategy {
    public int numOfUnallocatedTasks =0;

    private long begTime;
    private long endTime;

    private List<Pair<Integer, ArrayList>> listOfAdjacencyLists;

    private static GreedyStrategy ourInstance = new GreedyStrategy();

    /**
     * Singleton pattern
     * @return a single instance created of the MaximumProfitStrategy class
     */
    public static GreedyStrategy getInstance() {
        return ourInstance;
    }

    /**
     * O(et^2)
     * Compute allocation for a list of tasks provided as an a parameter
     * @param tasksToAllocate a list of tasks selected for allocation
     */
    @Override
    public void allocate(List<Task> tasksToAllocate) {
        numOfUnallocatedTasks =tasksToAllocate.size();

        begTime = System.currentTimeMillis();
        //O(ets^2)
        BipartiteGraph graph = new BipartiteGraph(GreedyStrategy.class, tasksToAllocate);
        listOfAdjacencyLists = graph.getListOfAdjacencyLists();
        endTime = System.currentTimeMillis();
        LocalServer.gLogger.info(getClass().getSimpleName()+": Time for constructing Bipartite Graph structure: {} ms", (endTime-begTime));

        listOfAdjacencyLists.forEach(pair->{
            LocalServer.gLogger.trace("Task{} has the following possible assignee: {}", pair.getKey(), pair.getValue());
        });

        begTime = System.currentTimeMillis();
        Collections.sort(listOfAdjacencyLists, new Comparator<Pair<Integer, ArrayList>>() {
            @Override
            public int compare(Pair<Integer, ArrayList> o1, Pair<Integer, ArrayList> o2) {
                if (o1.getValue().size() > o2.getValue().size()) return -1;
                else if (o1.getValue().size() == o2.getValue().size()) return 0;
                else return 1;
            }
        });
        //O(e*(t^2))
        while (!listOfAdjacencyLists.isEmpty()) {

            int indexWithMinPossibleEmployees = listOfAdjacencyLists.size() - 1;

            //Greedy algorithm allocates the task to the first available employee in the list.
            Task toAllocate = SystemData.getAllTasksMap().get(listOfAdjacencyLists.get(indexWithMinPossibleEmployees).getKey());
            //O(et)
            Employee chosenEmployee = findAvailableEmployee(toAllocate, indexWithMinPossibleEmployees);

            if (chosenEmployee!=null) {
                toAllocate.setRecommendedAssignee(chosenEmployee);
                numOfUnallocatedTasks--;
                /*if (!updateEdgesOfGreedyGraph(chosenEmployee)) {
                    LocalServer.ekLogger.error("Was unable to update appropriately the lists in greedy algorithm");
                    throw new InternalError("Was unable to update appropriately the lists in greedy algorithm");
                }*/
            }
            listOfAdjacencyLists.remove(indexWithMinPossibleEmployees);
            tasksToAllocate.remove(toAllocate);
        }
        endTime = System.currentTimeMillis();
        LocalServer.gLogger.info(getClass().getSimpleName()+": Time for running algorithm: {} ms", (endTime-begTime));
    }

    @Override
    public int getNumberOfUnallocatedTasks() {
        return numOfUnallocatedTasks;
    }

    //O(e*t)
    private Employee findAvailableEmployee(Task toAllocate, int indexWithMinPossibleEmployees) {
        choosingNextEmployee:
        for (Employee employee: (ArrayList<Employee>) listOfAdjacencyLists.get(indexWithMinPossibleEmployees).getValue()) {
            Employee chosenEmployee = SystemData.getAllEmployeesMap().get(employee.getId());
            try {
                if ((chosenEmployee.getTasks()==null || chosenEmployee.getTasks().size()==0) && (chosenEmployee.getMatchedTasks()==null || chosenEmployee.getMatchedTasks().size()==0))
                    return chosenEmployee;
                else {
                    if (!(chosenEmployee.getTasks()==null || chosenEmployee.getTasks().size()==0)) {
                        for (Task employeeTask : chosenEmployee.getTasks()) {
                            if (toAllocate.timeOverlapWith(employeeTask))
                                continue choosingNextEmployee;
                        }
                    }
                    if (!(chosenEmployee.getMatchedTasks()==null || chosenEmployee.getMatchedTasks().size()==0)) {
                        for (Task employeeMatchedTask: chosenEmployee.getMatchedTasks()) {
                            if (toAllocate.timeOverlapWith(employeeMatchedTask))
                                continue choosingNextEmployee;
                        }
                    }

                    return chosenEmployee;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
