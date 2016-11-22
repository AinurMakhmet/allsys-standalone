package logic;

import entity_utils.SkillUtils;
import entity_utils.TaskUtils;
import models.Employee;
import models.Skill;
import models.Task;
import models.TaskSkill;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nura on 20/11/16.
 */
public class GreedyAlgorithm implements AbstractAllocationAlgorithm{
    @Override
    public void allocate() {

        /*for (Skill skill: SkillUtils.getAllSkills()) {
            System.out.println("Skill: "+ skill.getName());
            try {
                for (Employee e: skill.getEmployees()) {
                    System.out.println("Employee with id: "+ e.getId() + " and name " +e.getFirstName()+ " "+ e.getLastName() );
                }
                for (Task t: skill.getTasks()) {
                    System.out.println("Task with id: "+ t.getId()+ " and name "+ t.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/



        for (Task t: TaskUtils.getAllTasks()) {
            System.out.println("Task with id: "+ t.getId()+ " and name "+ t.getName());
            try {
                /*int indexSmallestSize = 0;
                int minSize = 0;
                for (int i = 0; i<t.getSkills().size(); i++) {
                    if (minSize > t.getSkills().get(i).getEmployees().size()) {
                        indexSmallestSize = i;
                    }
                }*/
                for (Skill s: t.getSkills()) {
                    //System.out.println("Skill with id: "+ s.getId() + " and name " +s.getName());
                    for (Employee e: s.getEmployees()) {
                        //System.out.println("Employee with id: "+ e.getId() + " and name " +e.getFirstName()+ " "+ e.getLastName() );
                        if (t.possibleAssignee.containsKey(e.getId())) {
                            t.possibleAssignee.put(e.getId(), t.possibleAssignee.get(e.getId())+1);
                        } else {
                            t.possibleAssignee.put(e.getId(), 1);
                        }
                    }
                }
                Iterator it = t.possibleAssignee.entrySet().iterator();
               /* while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    //System.out.println(pair.getKey() + " = " + pair.getValue());
                    //it.remove();
                    if (pair.getValue()!=(Integer)t.getSkills().size()) {

                        t.possibleAssignee.remove(pair.getKey()); // avoids a ConcurrentModificationException
                    }
                }*/
                printMap(t.possibleAssignee);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
