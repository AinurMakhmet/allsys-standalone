package models.graph_models;

import java.util.LinkedList;

/**
 * Created by nura on 15/01/17.
 */
public class Flow extends LinkedList<BipartiteGraphNode> {
    private BipartiteGraphNode flowSource;
    private BipartiteGraphNode flowSink;

    public Flow(BipartiteGraphNode flowSource, BipartiteGraphNode flowSink) {
        this.flowSource = flowSource;
        addFirst(flowSource);
        this.flowSink = flowSink;
        add(flowSink);
    }

    public BipartiteGraphNode getFlowSource() {
        return flowSource;
    }

    public void setFlowSource(BipartiteGraphNode flowSource) {
        flowSource = getFirst();
        this.flowSource = flowSource;
    }

    public BipartiteGraphNode getFlowSink() {
        flowSink = getLast();
        return flowSink;
    }

    public void setFlowSink(BipartiteGraphNode flowSink) {
        this.flowSink = flowSink;
    }

    public void addFlowSink(BipartiteGraphNode node) {
        add(node);
        this.flowSink = node;
    }

}

