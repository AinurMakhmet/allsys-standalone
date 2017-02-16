package scalability;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by nura on 16/02/17.
 */
public class LargeDatasetGenerator {
    public static final int numberOfEmployees = 5;
    public static final int numberOfTasks = numberOfEmployees * 2;
    public static final int numberOfSkillLevels = 20;
    public static final int totalNumberOfSkills = numberOfSkillLevels*12;
    public static final int maxNumberOfSkillsPerEmployee = 15;
    public static final int maxNumberOfSkillsPerTask = 7;
    public static int numberOfEmployeeSkillsRows = 0;
    public static int numberOfTaskSkillsRows = 0;

    private static String value1="", value2="", value3="", value4="";

    public static void generateTestFiles() {
        try{
            PrintWriter writer = new PrintWriter("src/main/resources/large_dataset/Employees.txt", "UTF-8");
            generateEmployees(writer);
            writer.close();

            writer = new PrintWriter("src/main/resources/large_dataset/Skills.txt", "UTF-8");
            generateSkills(writer);
            writer.close();


            writer = new PrintWriter("src/main/resources/large_dataset/EmployeeSkills.txt", "UTF-8");
            generateEmployeeSkills(writer);
            writer.close();

            writer = new PrintWriter("src/main/resources/large_dataset/TaskSkills.txt", "UTF-8");
            generateTaskSkills(writer);
            writer.close();

            writer = new PrintWriter("src/main/resources/large_dataset/Tasks.txt", "UTF-8");
            generateTasks(writer);
            writer.close();

        } catch (IOException e) {
            // do something
        }
    }

    private static void generateTasks(PrintWriter writer) {
        StringBuilder builder = new StringBuilder();
        String taskLine = "INSERT INTO `task` (`name`, `priority`, `start_time`, `end_time`) VALUES ('";
        //+value1+"', '"+value2+"', '"+value3+"', '"+value4+"');";
        Date startDate;
        Date endDate;
        Random rand = new Random();
        String[] priorities = {"HIGH", "MEDIUM", "LOW"};

        int i=0;
        while(i<numberOfTasks) {
            value1 = new StringBuilder().append("Task").append(((Integer)i).toString()).toString();
            value2 = priorities[rand.nextInt(3)];
            startDate = getRandomDate();
            endDate = getRandomDate();
            while (endDate.before(startDate)) {
                endDate = getRandomDate();
            }
            value3 = getDateValueInString(startDate);
            value4 = getDateValueInString(endDate);
            builder.append(taskLine);
            builder.append(value1);
            builder.append("', '");
            builder.append(value2);
            builder.append("', '");
            builder.append(value3);
            builder.append("', '");
            builder.append(value4);
            builder.append("');");
            writer.println(builder.toString());
            builder.delete(0, builder.length());
            i++;
        }
    }

    private static String getDateValueInString(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        StringBuilder builder =  new StringBuilder().append(year).append("-").append(month).append("-").append(day);
        return builder.toString();
    }

    /*
     * http://stackoverflow.com/a/3985467
     */
    private static Date getRandomDate() {
        long ms;

        // Get a new random instance, seeded from the clock
        Random rnd = new Random();

        // Get an Epoch value roughly between 1940 and 2010
        // -946771200000L = January 1, 1940
        // Add up to 70 years to it (using modulus on the next long)
        ms = -946771200000L + (Math.abs(rnd.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));

        // Construct a date

        return new Date(ms);
    }

    private static void generateEmployees(PrintWriter writer) throws FileNotFoundException {
        StringBuilder builder = new StringBuilder();
        String employeeLineStart = "INSERT INTO `employee` (`first_name`, `last_name`) VALUES ('";

        String firstNamesFile = "src/main/resources/large_dataset/Database_of_First_Names.csv";
        String lastNamesFile = "src/main/resources/large_dataset/Database_of_Last_Names.csv";

        Scanner scannerFN = new Scanner(new File(firstNamesFile));
        Scanner scannerLN = new Scanner(new File(lastNamesFile));
        int i=0;
        while(i<numberOfEmployees) {
            value1= scannerFN.nextLine();
            value2= scannerLN.nextLine();

            builder.append(employeeLineStart);
            builder.append(value1);
            builder.append("', '");
            builder.append(value2);
            builder.append("');");
            writer.println(builder.toString());
            builder.delete(0, builder.length());
            i++;
        }
        scannerFN.close();
        scannerLN.close();


    }

    private static void generateSkills(PrintWriter writer) {
        StringBuilder builder = new StringBuilder();
        String skillLineStart = "INSERT INTO `skill` (`name`, `level`) VALUES ('";//+value1+"', '"+value2+"');";
        int i=0;
        String[] names = { "C++", "Java", "Python", "Ruby", "Linux Shell", "Functional Programming", "Artificial Intelligence", "SQL", "Databases", "Distributed Systems", "Regex", "Security" };
        int[] levels = new int[numberOfSkillLevels];
        while (i<levels.length) {
            levels[i] = i+1;
            i++;
        }
        for (int nameIndex=0; nameIndex<names.length; nameIndex++) {
            value1= names[nameIndex];
            for (int levelIndex=0; levelIndex<levels.length; levelIndex++) {
                value2= ((Integer)levels[levelIndex]).toString();
                builder.append(skillLineStart);
                builder.append(value1);
                builder.append("', '");
                builder.append(value2);
                builder.append("');");
                writer.println(builder.toString());
                builder.delete(0, builder.length());
            }
        }


    }

    private static void generateEmployeeSkills(PrintWriter writer) {
        StringBuilder builder = new StringBuilder();
        String employeeSkillLineStart = "INSERT INTO `employee_skill` (`employee_id`, `skill_id`) VALUES ('";
        //+value1+"', '"+value2+"');";
        int i=0;
        int[] employeeIds = new int[numberOfEmployees];
        int[] skillIds = new int[totalNumberOfSkills];
        while (i<employeeIds.length) {
            employeeIds[i] = i+1;
            i++;
        }
        i=0;
        while (i<skillIds.length) {
            skillIds[i] = i+1;
            i++;
        }
        int numberOfSkills=0;
        Random rand = new Random();
        for (int eIndex=0; eIndex<employeeIds.length; eIndex++) {
            value1= ((Integer)employeeIds[eIndex]).toString();
            numberOfSkills = rand.nextInt(maxNumberOfSkillsPerEmployee) + 1;
            numberOfEmployeeSkillsRows+=numberOfSkills;
            int sCounter=0;
            int[] employeeSkills = pickNRandom(skillIds, numberOfSkills);
            while (sCounter<numberOfSkills){
                value2= ((Integer)employeeSkills[sCounter++]).toString();
                builder.append(employeeSkillLineStart);
                builder.append(value1);
                builder.append("', '");
                builder.append(value2);
                builder.append("');");
                writer.println(builder.toString());
                builder.delete(0, builder.length());
            }
        }

    }

    /**
     * http://stackoverflow.com/a/8409831
     * @param array
     * @param n
     * @return
     */
    public static int[] pickNRandom(int[] array, int n) {

        List<Integer> list = new ArrayList<Integer>(array.length);
        for (int i : array)
            list.add(i);
        Collections.shuffle(list);

        int[] answer = new int[n];
        for (int i = 0; i < n; i++)
            answer[i] = list.get(i);
        Arrays.sort(answer);

        return answer;

    }


    private static void generateTaskSkills(PrintWriter writer) {
        StringBuilder builder = new StringBuilder();
        String taskSkillLine = "INSERT INTO `task_skill` (`task_id`, `skill_id`) VALUES ('";
        //+value1+"', '"+value2+"');";
        int i=0;
        int[] taskIds = new int[numberOfTasks];
        int[] skillIds = new int[totalNumberOfSkills];
        while (i<taskIds.length) {
            taskIds[i] = i+1;
            i++;
        }
        i=0;
        while (i<skillIds.length) {
            skillIds[i] = i+1;
            i++;
        }
        int numberOfSkills=0;
        Random rand = new Random();
        for (int tIndex=0; tIndex<taskIds.length; tIndex++) {
            value1= ((Integer)taskIds[tIndex]).toString();
            numberOfSkills = rand.nextInt(maxNumberOfSkillsPerTask) + 1;
            numberOfTaskSkillsRows+=numberOfSkills;
            int sCounter=0;
            int[] taskSkills = pickNRandom(skillIds, numberOfSkills);
            while (sCounter<numberOfSkills){
                value2= ((Integer)taskSkills[sCounter++]).toString();
                builder.append(taskSkillLine);
                builder.append(value1);
                builder.append("', '");
                builder.append(value2);
                builder.append("');");
                writer.println(builder.toString());
                builder.delete(0, builder.length());
            }
        }
    }

}
