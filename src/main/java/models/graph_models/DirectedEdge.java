package models.graph_models;

import java.util.LinkedList;

/**
 * Created by nura on 15/01/17.
 */
public class DirectedEdge extends LinkedList<BipartiteGraphNode> {
    private BipartiteGraphNode nodeFrom;
    private BipartiteGraphNode nodeTo;

    public DirectedEdge(BipartiteGraphNode nodeFrom, BipartiteGraphNode nodeTo) {
        this.nodeFrom = nodeFrom;
        addFirst(nodeFrom);
        this.nodeTo = nodeTo;
        add(nodeTo);
    }

    public BipartiteGraphNode getNodeFrom() {
        return nodeFrom;
    }

    public void setNodeFrom(BipartiteGraphNode nodeFrom) {
        nodeFrom = getFirst();
        this.nodeFrom = nodeFrom;
    }

    public BipartiteGraphNode getNodeTo() {
        nodeTo = getLast();
        return nodeTo;
    }

    public void setNodeTo(BipartiteGraphNode nodeTo) {
        this.nodeTo = nodeTo;
    }

    public void addFlowSink(BipartiteGraphNode node) {
        add(node);
        this.nodeTo = node;
    }

}

