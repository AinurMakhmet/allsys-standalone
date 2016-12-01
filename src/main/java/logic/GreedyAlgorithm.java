package logic;

import entity_utils.TaskUtils;
import models.Employee;
import models.Task;

import java.util.*;

/**
 * Created by nura on 20/11/16.
 */
public class GreedyAlgorithm extends AbstractAllocationAlgorithm implements Comparator<Map.Entry<Integer, ArrayList>>{

    @Override
    public boolean allocate() {
        //considers task of different priority separately
        for (Task.Priority priority: Task.Priority.values()) {
            Collections.sort(unallocatedTasks, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getEarliestStartTime().compareTo(o2.getEarliestStartTime());
                }
            });
            //TODO: consider task dependecy on time
            ArrayList<Map.Entry<Integer, ArrayList>> adjacencyList = new BipartiteGraph(priority, this).getAdjacencyList();
            while (!adjacencyList.isEmpty()) {
                Collections.sort(adjacencyList, this);

                int indexWithMinPossibleEmployees = adjacencyList.size() - 1;

                //System.out.println("looking for scala task: "+ adjacencyList.get(indexWithMinPossibleEmployees).getKey().equals(1));


                //If task doesn't have any matching, remove from the adjacency list and considers another task;
                if (adjacencyList.get(indexWithMinPossibleEmployees).getValue().isEmpty()) {
                    adjacencyList.remove(indexWithMinPossibleEmployees); continue;
                }

                //Greedy algorithm allocates the task to the first employee in the list.
                Employee chosenEmployee = (Employee) adjacencyList.get(indexWithMinPossibleEmployees).getValue().get(0);
                Task allocated = TaskUtils.getTask(adjacencyList.get(indexWithMinPossibleEmployees).getKey());
                allocated.setEmployee(chosenEmployee);
                TaskUtils.updateEntity(allocated);
                adjacencyList.remove(indexWithMinPossibleEmployees);
                System.out.println(removeTask(unallocatedTasks, allocated));

                if (!updateEdges(chosenEmployee, adjacencyList)) {
                    System.out.println("Was unable to update appropriately the lists in greedy algorithm");
                    return false;
                }
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

    private boolean removeTask(List<Task> tasks, Task taskToRemove) {
        for (Task task: tasks) {
            if (taskToRemove.getId()==task.getId()) {
                 return tasks.remove(task);
            }
        }
        return false;
    }
}
