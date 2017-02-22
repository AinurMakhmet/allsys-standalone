package logic;

import entity_utils.TaskUtils;
import javafx.util.Pair;
import models.Employee;
import models.SystemData;
import models.Task;
import models.bipartite_matching.BipartiteGraph;
import java.io.IOException;
import java.util.*;

/**
 * Greedy algorithm allocates each task if possible
 * to the first available employee in the list.
 * It starts with a task that has the least number of possible assignees.
 */
public class GreedyAlgorithm extends Strategy {
    //private List<Pair<Integer, ArrayList>> listOfAdjacencyLists;
    private List<Pair<Integer, ArrayList>> listOfAdjacencyLists;
    private long begTime;
    private long endTime;

    private static GreedyAlgorithm ourInstance = new GreedyAlgorithm();

    public static GreedyAlgorithm getInstance() {
        return ourInstance;
    }

    @Override
    public List<Task> allocate(List<Task> tasksToAllocate) {
        recommendedAllocation = new LinkedList<>();

        /*begTime = System.currentTimeMillis();
        listOfAdjacencyLists = new GreedyGraph(tasksToAllocate).getListOfAdjacencyLists();
        endTime = System.currentTimeMillis();
        System.out.printf(getClass().getSimpleName()+": Total time for constructing Greedy Graph structure: %d ms\n", (endTime-begTime));
        */
        begTime = System.currentTimeMillis();
        BipartiteGraph graph = new BipartiteGraph(GreedyAlgorithm.class, tasksToAllocate);
        listOfAdjacencyLists = graph.getListOfAdjacencyLists();
        endTime = System.currentTimeMillis();
        System.out.printf(getClass().getSimpleName()+": Total time for constructing Bipartite Graph structure: %d ms\n", (endTime-begTime));

        //graph.printGraph();

        begTime = System.currentTimeMillis();
        numOfUnnalocatedTasks=0;
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

            //Greedy algorithm allocates the task to the first employee in the list.
            //Get the new task
            //TODO: why accessing a task from the database?
            Task toAllocate = SystemData.getAllTasksMap().get(listOfAdjacencyLists.get(indexWithMinPossibleEmployees).getKey());
            Employee chosenEmployee = findAvailableEmployee(toAllocate, indexWithMinPossibleEmployees);

            if (chosenEmployee!=null) {
                toAllocate.setRecommendedAssignee(chosenEmployee);
                /*if (!updateEdgesOfGreedyGraph(chosenEmployee)) {
                    System.out.println("Was unable to update appropriately the lists in greedy algorithm");
                    throw new InternalError("Was unable to update appropriately the lists in greedy algorithm");
                }*/
            } else {
                numOfUnnalocatedTasks++;
            }
            recommendedAllocation.add(toAllocate);
            listOfAdjacencyLists.remove(indexWithMinPossibleEmployees);
            tasksToAllocate.remove(toAllocate);
        }
        endTime = System.currentTimeMillis();
        System.out.printf(getClass().getSimpleName()+": Total time for running algorithm: %d ms\n", (endTime-begTime));

        return recommendedAllocation;
    }

    private boolean updateEdgesOfGreedyGraph(Employee toRemove) {
        boolean updated = true;
        outerLoop:
        for ( int i = 0; i<listOfAdjacencyLists.size(); i++) {
            ArrayList<Employee> listToUpdate = listOfAdjacencyLists.get(i).getValue();
            for (Employee e: listToUpdate) {
                if (toRemove.getId()==e.getId()) {
                    updated = listToUpdate.remove(e);
                    if (updated==true) break;
                    else break outerLoop;
                }
            }
        }
        return updated;
    }


    private Employee findAvailableEmployee(Task toAllocate, int indexWithMinPossibleEmployees) {
        choosingNextEmployee:
        for (Employee chosenEmployee: (ArrayList<Employee>) listOfAdjacencyLists.get(indexWithMinPossibleEmployees).getValue()) {
            try {
                if (chosenEmployee.getTasks()==null || chosenEmployee.getTasks().size()==0)
                    return chosenEmployee;
                else {
                    for (Task employeeTask : chosenEmployee.getTasks()) {
                        if (toAllocate.timeOverlapWith(employeeTask))
                            continue choosingNextEmployee;
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
