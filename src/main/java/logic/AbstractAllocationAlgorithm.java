package logic;

import entity_utils.TaskUtils;
import javafx.util.Pair;
import models.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nura on 20/11/16.
 */
public abstract class AbstractAllocationAlgorithm {

    public ArrayList<Task> unallocatedTasks = (ArrayList<Task>) TaskUtils.getAllTasksValidForAllocation();
    List<Pair<Integer, ArrayList>> listOfAdjacencyLists;
    List<Task> recommendedAllocation = new LinkedList<>();

    public List<Task> allocate(Task.Priority priority, List<Task> tasksToAllocate) {
        return null;
    }

    public List<Task> allocate(List<Task> tasksToAllocate) {
        return null;
    }
}
