package logic

import entity_utils.{EmployeeSkillUtils, TaskSkillUtils, TaskUtils}
import models.TaskSkill

/**
  * Created by nura on 20/11/16.
  */
class GreedyAlgorithmScala {

  def allocate(): Unit = {
    /*import scala.collection.JavaConversions._
    println("Hello from scala!")
    for (t <- TaskUtils.getAllTasks) {
      System.out.println("Task with id: " + t.getId + " and name " + t.getName)
        import scala.collection.JavaConversions._
        for (s <- t.getSkills) {
          System.out.println("Skill with id: " + s.getId + " and name " + s.getName)
          import scala.collection.JavaConversions._
          for (e <- s.getEmployees) {
            System.out.println("Employee with id: " + e.getId + " and name " + e.getFirstName + " " + e.getLastName)
            if (t.possibleAssignee.containsKey(e.getId)) t.possibleAssignee.put(e.getId, t.possibleAssignee.get(e.getId) + 1)
            else t.possibleAssignee.put(e.getId, 1)
          }
        }
        t.possibleAssignee.foreach(println(_))

    }
*/
    import scala.collection.JavaConversions._
    val skillMapByTask = TaskSkillUtils.getAllTaskSkills.map(a=>(a.getTask.getName -> a.getSkill.getName)).groupBy(_._1).mapValues(_.map(_._2))//groupBy(a => a.getTask.getName)// => a.getSkill.getName -> a.getTask.getName).toMap
    println(skillMapByTask)

    val skillMapByEmployee = EmployeeSkillUtils.getAllEmployeeSkills.map(a=>(a.getEmployee.getId -> a.getSkill.getName)).groupBy(_._1).mapValues(_.map(_._2))//groupBy(a => a.getTask.getName)// => a.getSkill.getName -> a.getTask.getName).toMap
    println(skillMapByEmployee)

    //println("TaskSkill with id: " + t.ge + " and name ")
    /*    import scala.collection.JavaConversions._
      for (s <- t.getSkills) {
        System.out.println("Skill with id: " + s.getId + " and name " + s.getName)
        import scala.collection.JavaConversions._
        for (e <- s.getEmployees) {
          System.out.println("Employee with id: " + e.getId + " and name " + e.getFirstName + " " + e.getLastName)
          if (t.possibleAssignee.containsKey(e.getId)) t.possibleAssignee.put(e.getId, t.possibleAssignee.get(e.getId) + 1)
          else t.possibleAssignee.put(e.getId, 1)
        }
      }
      t.possibleAssignee.foreach(println(_))
*/
  //}
  }
}
