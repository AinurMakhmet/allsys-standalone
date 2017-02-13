package logic;

import entity_utils.TaskUtils;
import javafx.util.Pair;
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
public class GreedyAlgorithm extends AbstractAllocationAlgorithm implements Comparator<Pair<Integer, ArrayList>>{

    @Override
    public List<Task> allocate(List<Task> tasksToAllocate) {
        //considers task of different priority separately
        unallocatedTasks = (ArrayList<Task>) tasksToAllocate;
        for (Task.Priority priority: Task.Priority.values()) {
            Collections.sort(unallocatedTasks, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getStartTime().compareTo(o2.getStartTime());
                }
            });

            //TODO: consider task dependecy on time
            listOfAdjacencyLists = new GreedyGraph(priority, this).getListOfAdjacencyLists();
            Collections.sort(listOfAdjacencyLists, this);
            while (!listOfAdjacencyLists.isEmpty()) {
                
                int indexWithMinPossibleEmployees = listOfAdjacencyLists.size() - 1;

                //Greedy algorithm allocates the task to the first employee in the list.
                //Get the new task
                Task toAllocate = TaskUtils.getTask(listOfAdjacencyLists.get(indexWithMinPossibleEmployees).getKey());
                Employee chosenEmployee = findAvailableEmployee(toAllocate, indexWithMinPossibleEmployees);

                if (chosenEmployee!=null) {
                    toAllocate.setRecommendedAssignee(chosenEmployee);
                    //TODO: the entity is not yet updated, ask the user if to persist the allocation;
                    //TaskUtils.updateEntity(toAllocate);
                    if (!updateEdges(chosenEmployee)) {
                        System.out.println("Was unable to update appropriately the lists in greedy algorithm");
                        throw new InternalError("Was unable to update appropriately the lists in greedy algorithm");
                    }
                }
                recommendedAllocation.add(toAllocate);
                listOfAdjacencyLists.remove(indexWithMinPossibleEmployees);
                System.out.println(removeTask(unallocatedTasks, toAllocate));
            }
        }
        return recommendedAllocation;
    }

    @Override
    public int compare(Pair<Integer, ArrayList> o1, Pair<Integer, ArrayList> o2) {
        if (o1.getValue().size() > o2.getValue().size()) return -1;
        else if (o1.getValue().size() == o2.getValue().size()) return 0;
        else return 1;
    }

    private boolean removeTask(List<Task> tasks, Task taskToRemove) {
        for (Task task: tasks) {
            if (taskToRemove.getId()==task.getId()) {
                 return tasks.remove(task);
            }
        }
        return false;
    }

    private boolean updateEdges(Employee toRemove) {
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
