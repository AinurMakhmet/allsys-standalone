package logic;

import entity_utils.TaskUtils;
import models.Employee;
import models.Task;
import models.graph_models.GreedyGraph;

import java.io.IOException;
import java.util.*;

/**
 * Greedy algorithm works on high-priority tasks first and allocates each task if possible
 * to the first available employee in the list.
 * It starts with a task that has the least number of possible assignees.
 * Algorithm takes into consideration the priorities of the task.
 */
public class GreedyAlgorithm extends AbstractAllocationAlgorithm implements Comparator<Map.Entry<Integer, ArrayList>>{

    @Override
    public boolean allocate() {
        //considers task of different priority separately
        for (Task.Priority priority: Task.Priority.values()) {
            Collections.sort(unallocatedTasks, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getStartTime().compareTo(o2.getStartTime());
                }
            });
            //TODO: consider task dependecy on time
            adjacencyList = new GreedyGraph(priority, this).getAdjacencyList();
            Collections.sort(adjacencyList, this);
            while (!adjacencyList.isEmpty()) {
                
                //If task doesn't have any matching, remove from the adjacency list and considers another task;
                while (adjacencyList.get(adjacencyList.size() - 1).getValue().isEmpty()) {
                    adjacencyList.remove(adjacencyList.size() - 1);
                }
                int indexWithMinPossibleEmployees = adjacencyList.size() - 1;

                //Greedy algorithm allocates the task to the first employee in the list.
                Task toAllocate = TaskUtils.getTask(adjacencyList.get(indexWithMinPossibleEmployees).getKey());
                Employee chosenEmployee = findAvailableEmployee(toAllocate, indexWithMinPossibleEmployees);

                toAllocate.setEmployee(chosenEmployee);
                TaskUtils.updateEntity(toAllocate);
                adjacencyList.remove(indexWithMinPossibleEmployees);
                System.out.println(removeTask(unallocatedTasks, toAllocate));

                /*if (!updateEdges(chosenEmployee, adjacencyList)) {
                    System.out.println("Was unable to update appropriately the lists in greedy algorithm");
                    return false;
                }*/
            }
        }
        return true;
    }

    private boolean updateEdges(Employee toRemove) {
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

    private Employee findAvailableEmployee(Task toAllocate, int indexWithMinPossibleEmployees) {
        choosingNextEmployee:
        for (Employee chosenEmployee: (ArrayList<Employee>) adjacencyList.get(indexWithMinPossibleEmployees).getValue()) {
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
