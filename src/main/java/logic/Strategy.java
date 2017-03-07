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
public abstract class Strategy {
    public List<Task> unallocatedTasks;
    List<Task> recommendedAllocation;
    public int numOfUnnalocatedTasks=0;
    protected long begTime;
    protected long endTime;
    public List<Task> allocate(List<Task> tasksToAllocate) {
        return null;
    }
}
