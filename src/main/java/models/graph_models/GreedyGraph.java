package models.graph_models;

import logic.AbstractAllocationAlgorithm;
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
public class GreedyGraph {
    //list that stores the edges
    private ArrayList<Map.Entry<Integer, ArrayList>> adjacencyList = new ArrayList<>();
    //private ArrayList<Vertex> adListBipartite = new ArrayList<>();
    //public static Vertex sink = new Vertex();
    //public static Vertex source = new Vertex();

    /*public GreedyGraph(AbstractAllocationAlgorithm algorithm) {
        for (Task task: algorithm.unallocatedTasks) {
            //creates a temporary list of skills and adds list of skills in a task to that list, for future opearations
            ArrayList<Skill> taskSkills = null;
            try {
                taskSkills = new ArrayList<>(task.getSkills());

            // finds the index of the skill that is possessed by smallest amount of employees.
            int indexSmallestSize = getIndexOfDefiningSkill(taskSkills);

            task.possibleAssignee.addAll(taskSkills.get(indexSmallestSize).getEmployees());
            taskSkills.remove(indexSmallestSize);

            ArrayList<Employee> definingSkillEmployees = (ArrayList<Employee>) task.getSkills().get(indexSmallestSize).getEmployees();
            filterPossibleAssignee(task, taskSkills, definingSkillEmployees);
            //adds new entry to the adjacency list
            adListBipartite.add(task);
            source.addOutcomingEdge(task);
            task.addIncomingEdge(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
    public GreedyGraph(Task.Priority priority, AbstractAllocationAlgorithm algorithm) {
        for (Task task: algorithm.unallocatedTasks) {
            if (task.getPriority()!=priority) continue;
            //creates a temporary list of skills and adds list of skills in a task to that list, for future opearations
            try {
                ArrayList<Skill> taskSkills = new ArrayList<>(task.getSkills());

                // finds the index of the skill that is possessed by smallest amount of employees.
                int indexSmallestSize = getIndexOfDefiningSkill(taskSkills);

                task.possibleAssignee = (ArrayList<Employee>) taskSkills.get(indexSmallestSize).getEmployees();
                taskSkills.remove(indexSmallestSize);

                ArrayList<Employee> definingSkillEmployees = (ArrayList<Employee>) task.getSkills().get(indexSmallestSize).getEmployees();
                filterPossibleAssignee(task, taskSkills, definingSkillEmployees);

                System.out.println("possible assignee for task "+ task.getName()+ "are ");
                printList(task.possibleAssignee);

                //adds new entry to the adjacency list
                adjacencyList.add(new AbstractMap.SimpleEntry<Integer, ArrayList>(task.getId(), task.possibleAssignee));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void filterPossibleAssignee(Task task, ArrayList<Skill> taskSkills, ArrayList<Employee> definingSkillEmployees) throws IOException {
        for (Skill s : taskSkills){
            for (int i=0; i<definingSkillEmployees.size(); i++) {
                ArrayList<Skill> employeeSkills = (ArrayList<Skill>) definingSkillEmployees.get(i).getSkills();
                for (int j = 0; j< employeeSkills.size(); j++) {
                    if (s.getId()==employeeSkills.get(j).getId())  {
                        break;
                    }
                    else if (j == employeeSkills.size()-1) {
                        //System.out.println("Employee " + employees.get(i) + " doesn'task have skill " + s.getName());
                        //System.out.println("Employee skill: " + employees.get(i).getSkills());

                        //the employee doesn'task have all the required taskSkills to complete the task,
                        //so it will be removed from the list of candidates
                        task.possibleAssignee.remove(definingSkillEmployees.get(i));
                    }
                }
            }
        }
    }

    private int getIndexOfDefiningSkill(ArrayList<Skill> taskSkills) throws IOException {
        //index of the skill in the taskSkills that have a minimum number of Employees that posses this skill
        int indexSmallestSize = 0;

        int minSize = taskSkills.get(0).getEmployees().size();
        for (int i = 0; i<taskSkills.size(); i++) {
            if (minSize > taskSkills.get(i).getEmployees().size()) {
                indexSmallestSize = i;
                minSize = taskSkills.get(i).getEmployees().size();
            }
        }
        return indexSmallestSize;

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
