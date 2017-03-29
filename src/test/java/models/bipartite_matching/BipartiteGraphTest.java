package models.bipartite_matching;

import models.Employee;
import models.Skill;
import models.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.*;

/**
 * Created by nura on 06/03/17.
 */
public class BipartiteGraphTest {
    ArrayList<Skill> taskSkills;
    BipartiteGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = Mockito.mock(BipartiteGraph.class);


    }

    @After
    public void tearDown() throws Exception {

    }

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