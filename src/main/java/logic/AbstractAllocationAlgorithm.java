package logic;

import entity_utils.TaskUtils;
import models.Task;

import java.util.ArrayList;

/**
 * Created by nura on 20/11/16.
 */
public abstract class AbstractAllocationAlgorithm {

    public ArrayList<Task> unallocatedTasks = (ArrayList<Task>) TaskUtils.getAllTasks();

    public boolean allocate() {
        return true;
    }
}
