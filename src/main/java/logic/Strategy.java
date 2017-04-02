package logic;

import models.Task;
import java.util.List;

/**
 * Created by nura on 20/11/16.
 */
public interface Strategy {

    void allocate(List<Task> tasksToAllocate);
    int getNumberOfUnallocatedTasks();
}
