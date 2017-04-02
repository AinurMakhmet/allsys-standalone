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
    List<Task> result;
    public int numOfUnallocatedTasks =0;

    protected long begTime;
    protected long endTime;

    private List<Pair<Integer, ArrayList>> listOfAdjacencyLists;

    private static GreedyStrategy ourInstance = new GreedyStrategy();

    public static GreedyStrategy getInstance() {
        return ourInstance;
    }

    @Override
    public List<Task> allocate(List<Task> tasksToAllocate) {
        result = new LinkedList<>();
        numOfUnallocatedTasks =tasksToAllocate.size();

        begTime = System.currentTimeMillis();
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
        while (!listOfAdjacencyLists.isEmpty()) {

            int indexWithMinPossibleEmployees = listOfAdjacencyLists.size() - 1;

            //Greedy algorithm allocates the task to the first available employee in the list.
            Task toAllocate = SystemData.getAllTasksMap().get(listOfAdjacencyLists.get(indexWithMinPossibleEmployees).getKey());
            Employee chosenEmployee = findAvailableEmployee(toAllocate, indexWithMinPossibleEmployees);

            if (chosenEmployee!=null) {
                toAllocate.setRecommendedAssignee(chosenEmployee);
                numOfUnallocatedTasks--;
                /*if (!updateEdgesOfGreedyGraph(chosenEmployee)) {
                    LocalServer.ekLogger.error("Was unable to update appropriately the lists in greedy algorithm");
                    throw new InternalError("Was unable to update appropriately the lists in greedy algorithm");
                }*/
            }
            result.add(toAllocate);
            listOfAdjacencyLists.remove(indexWithMinPossibleEmployees);
            tasksToAllocate.remove(toAllocate);
        }
        endTime = System.currentTimeMillis();
        LocalServer.gLogger.info(getClass().getSimpleName()+": Time for running algorithm: {} ms", (endTime-begTime));

        return result;
    }

    @Override
    public int getNumberOfUnallocatedTasks() {
        return numOfUnallocatedTasks;
    }

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
