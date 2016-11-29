package logic;

import entity_utils.TaskUtils;
import javafx.util.Pair;
import models.Employee;
import models.Skill;
import models.Task;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Class that creates edges between task and employees for the graph.
 */
public class BipartiteGraph {
    private static BipartiteGraph ourInstance = new BipartiteGraph();

    public static BipartiteGraph getInstance() {
        return ourInstance;
    }

    private BipartiteGraph() {
        createMatchingEdges();
    }
    //list that stores the edges
    private ArrayList<Map.Entry<Integer, ArrayList>> adjacencyList = new ArrayList<>();

    private void createMatchingEdges() {
        for (Task task: TaskUtils.getAllTasks()) {
            System.out.println("Task with id: "+ task.getId()+ " and name "+ task.getName());
            try {
                //creates a temporary list of skills and adds list of skills in a task to that list, for future opearations
                ArrayList<Skill> taskSkills = new ArrayList<>();
                taskSkills.addAll(task.getSkills());

                //index of the skill in the taskSkills that have a minimum number of Employees that posses this skill
                int indexSmallestSize = 0;

                int minSize = taskSkills.get(0).getEmployees().size();
                for (int i = 0; i<taskSkills.size(); i++) {
                    if (minSize > taskSkills.get(i).getEmployees().size()) {
                        indexSmallestSize = i;
                        minSize = taskSkills.get(i).getEmployees().size();
                    }
                }
                task.possibleAssignee.addAll(taskSkills.get(indexSmallestSize).getEmployees());
                taskSkills.remove(indexSmallestSize);

                ArrayList<Employee> employees = (ArrayList<Employee>) task.getSkills().get(indexSmallestSize).getEmployees();
                for (Skill s : taskSkills){
                    for (int i=0; i<employees.size(); i++) {
                        //System.out.println("Employee with id: "+ e.getId() + " and name " +e.getFirstName()+ " "+ e.getLastName() );
                        //if (!s.getEmployees().contains(employees.get(i))) {
                        ArrayList<Skill> employeeSkills = (ArrayList<Skill>) employees.get(i).getSkills();
                        for (int j = 0; j< employeeSkills.size(); j++) {
                            if (s.getId()==employeeSkills.get(j).getId()) break;
                            else if (j == employeeSkills.size()-1) {
                                System.out.println("Employee " + employees.get(i) + " doesn'task have skill " + s.getName());
                                System.out.println("Employee skill: " + employees.get(i).getSkills());
                                //the employee doesn'task have all the required taskSkills to complete the task,
                                //so it will be removed from the list of candidates
                                task.possibleAssignee.remove(employees.get(i));
                            }
                        }
                    }
                }
                System.out.println("possible assignee for task "+ task.getName()+ "are ");
                //printList(task.possibleAssignee);
            } catch (IOException e) {
                e.printStackTrace();
            }
            adjacencyList.add(new AbstractMap.SimpleEntry<Integer, ArrayList>(task.getId(), task.possibleAssignee));
        }
    }

    public static void printList(ArrayList<Employee> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            Employee e = (Employee)it.next();
            System.out.println(e.getId());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    /**
     *
     * @return the adjacency list that represents the bipartite graph of employees and tasks
     */
    public ArrayList<Map.Entry<Integer, ArrayList>> getAdjacencyList() {
        return adjacencyList;
    }
}
