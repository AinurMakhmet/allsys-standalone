package logic;

import entity_utils.TaskUtils;
import models.Task;
import models.TaskSkill;

/**
 * Created by nura on 20/11/16.
 */
public class Strategy {
    private static Strategy ourInstance = new Strategy();

    public static Strategy getInstance() {
        return ourInstance;
    }

    private Strategy() {
    }
    public static void allocate() {
        BipartiteGraph.getInstance();
        GreedyAlgorithm greedy= new GreedyAlgorithm();
        if (greedy.allocate()) {
            for (Task task: TaskUtils.getAllTasks()) {
                if (task.getEmployee()!=null) {
                    System.out.println("Task "+ task.getName()+ " is allocated to employee "+ task.getEmployee().getFirstName() + " " + task.getEmployee().getLastName());
                }
            }
        }
    }
}
