package models.bipartite_matching;

import com.sun.javafx.tk.Toolkit;
import models.Employee;
import models.Skill;
import models.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by nura on 06/03/17.
 */
public class BipartiteGraphTest {
    ArrayList<Skill> taskSkills;
    ArrayList<Task> tasks = new ArrayList<>();
    Task task;
    Employee possibleEmployee;
    BipartiteGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = Mockito.mock(BipartiteGraph.class);


    }

    @After
    public void tearDown() throws Exception {

    }

    /*@Test
    public void removeCandiatesWithTimeOverlappingTasksShouldRemoveCandidates() throws IOException {
        task = Mockito.mock(Task.class);
        possibleEmployee = Mockito.mock(Employee.class);
        assertNotNull(possibleEmployee.getTasks());
        Task employeeTask = Mockito.mock(Task.class);
        possibleEmployee.get().add(employeeTask);
        employeeTask.setStartTime(new Date(5));
        employeeTask.setEndTime(new Date(10));

        System.out.println("number of tasks of employee mock object is:"+ possibleEmployee.getTasks().size() );




        task.setStartTime(new Date(3));
        task.setEndTime(new Date(4));
        ArrayList<Employee> employees =  new ArrayList<Employee>();
        employees.add(possibleEmployee);
        task.possibleAssignee = employees;
        graph.removeCandiatesWithTimeOverlappingTasks(task);
        assertEquals(1, task.possibleAssignee.get(0).getTasks().size());

        task.setStartTime(new Date(5));
        task.setEndTime(new Date(10));
        assertEquals(task.getEndTime(), employeeTask.getEndTime());
        graph.removeCandiatesWithTimeOverlappingTasks(task);
        System.out.println(possibleEmployee.getTasks().get(0));

    }*/

    @Test
    public void getIndexOfDefiningSkillShouldReturnIndexOfSkillInListWithSmallestNumberOfEmployeesPossessThatSkill() throws IOException {
        taskSkills = new ArrayList<>();
        int numberOfSkill = 5;
        int i=0;
        while (i<numberOfSkill) {
            Skill s = Mockito.mock(Skill.class);
            assertNotNull(s.getEmployees());
            taskSkills.add(s);
            i++;
        }

        Collections.sort(taskSkills, new Comparator<Skill>() {
            @Override
            public int compare(Skill o1, Skill o2) {
                int toReturn = -1;
                try {
                    toReturn = ((Integer)o1.getEmployees().size()).compareTo(o2.getEmployees().size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return toReturn;
            }
        });
        assertEquals(0, graph.getIndexOfDefiningSkill(taskSkills));
    }

}