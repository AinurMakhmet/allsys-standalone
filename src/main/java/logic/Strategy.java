package logic;

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
        GreedyAlgorithm greedy= new GreedyAlgorithm();
        //GreedyAlgorithmScala greedy = new GreedyAlgorithmScala();
        greedy.allocate();
    }
}
