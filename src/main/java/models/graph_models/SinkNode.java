package models.graph_models;

import javafx.util.Pair;
import models.Employee;

/**
 * Created by nura on 19/01/17.
 */
public class SinkNode extends BipartiteGraphNode {
    private static SinkNode ourInstance = new SinkNode();

    public static SinkNode getInstance() {
        return ourInstance;
    }

    private SinkNode() {
        setNodeType(BipartiteGraphNodeType.SINK);
    }
}
