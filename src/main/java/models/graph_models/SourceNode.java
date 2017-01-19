package models.graph_models;

/**
 * Created by nura on 19/01/17.
 */
public class SourceNode extends BipartiteGraphNode {
    private static SourceNode ourInstance = new SourceNode();

    public static SourceNode getInstance() {
        return ourInstance;
    }

    private SourceNode() {
        setNodeType(BipartiteGraphNodeType.SOURCE);
    }
}
