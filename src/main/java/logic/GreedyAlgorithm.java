package logic;

import entity_utils.TaskUtils;
import models.Employee;
import models.Task;

import java.util.*;

/**
 * Created by nura on 20/11/16.
 */
public class GreedyAlgorithm implements AbstractAllocationAlgorithm, Comparator<Map.Entry<Integer, ArrayList>>{

    @Override
    public boolean allocate() {
        ArrayList<Map.Entry<Integer, ArrayList>> adjacencyList = BipartiteGraph.getInstance().getAdjacencyList();
        while(!adjacencyList.isEmpty()) {
            Collections.sort(adjacencyList, this);

            int indexWithMinPossibleEmployees = adjacencyList.size() - 1;

            //Greedy algorithm allocates the task to the first employee in the list.
            Employee chosenEmployee = (Employee) adjacencyList.get(indexWithMinPossibleEmployees).getValue().get(0);
            Task allocated = TaskUtils.getTask(adjacencyList.get(indexWithMinPossibleEmployees).getKey());
            allocated.setEmployee(chosenEmployee);
            TaskUtils.updateEntity(allocated);
            adjacencyList.remove(indexWithMinPossibleEmployees);

            if (!updateEdges(chosenEmployee, adjacencyList)) {
                System.out.println("Was unable to update appropriately the lists in greedy algorithm");
                return false;
            }
        }
        return true;
    }

    private boolean updateEdges(Employee toRemove, ArrayList<Map.Entry<Integer, ArrayList>> adjacencyList) {
        boolean updated = true;
        outerLoop:
        for ( int i = 0; i<adjacencyList.size(); i++) {
            ArrayList<Employee> listToUpdate = adjacencyList.get(i).getValue();
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

    @Override
    public int compare(Map.Entry<Integer, ArrayList> o1, Map.Entry<Integer, ArrayList> o2) {
        if (o1.getValue().size() > o2.getValue().size()) return -1;
        else if (o1.getValue().size() == o2.getValue().size()) return 0;
        else return 1;
    }

    public static void printList(ArrayList<Employee> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            Employee e = (Employee)it.next();
            System.out.println(e.getId());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
