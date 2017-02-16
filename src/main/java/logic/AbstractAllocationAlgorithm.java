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
    public List<Task> unallocatedTasks;
    static List<Pair<Integer, ArrayList>> listOfAdjacencyLists;
    static List<Task> recommendedAllocation;
}
