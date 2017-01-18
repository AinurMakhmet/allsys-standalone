package models.graph_models;

import logic.Strategy;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nura on 15/01/17.
 */
public class BipartiteGraphNode {
    private Set<BipartiteGraphEdge> edges = new HashSet<>();
    private Set<Flow> incomingFlows = new HashSet<>();
    private Set<Flow> outcomingFlows = new HashSet<>();
    public BipartiteGraphNodeType nodeType;

    protected BipartiteGraphNode() {

    }

    public BipartiteGraphNode setNodeType(BipartiteGraphNodeType type) {
        nodeType = type;
        return this;
    }

    public BipartiteGraphNodeType getNodeType() {
        return nodeType;
    }

    public void addEdge(BipartiteGraphEdge edge) {
        edges.add(edge);
    }

    public Set<BipartiteGraphEdge> getEdges() {
        return edges;
    }


    public Set<Flow> getIncomingFlows() {
        return incomingFlows;
    }

    public void addIncomingFlow(Flow flow) {
        incomingFlows.add(flow);
    }

    public void setIncomingFlows(Set<Flow> flows) {
        incomingFlows = flows;
    }

    public Set<Flow> getOutcomingFlows() {
        return outcomingFlows;
    }

    public void addOutcomingFlow(Flow flow) {
        outcomingFlows.add(flow);
    }

    public void setOutcomingFlows(Set<Flow> flows) {
        outcomingFlows = flows;
    }

}
