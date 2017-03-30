package logic;

import models.Task;
import java.util.List;

/**
 * Created by nura on 20/11/16.
 */
public abstract class Strategy {
    List<Task> result;
    public int numOfUnnalocatedTasks=0;

    protected long begTime;
    protected long endTime;

    public List<Task> allocate(List<Task> tasksToAllocate) {
        return null;
    }
}
