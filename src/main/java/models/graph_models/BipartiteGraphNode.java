package models.graph_models;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nura on 15/01/17.
 */
public abstract class BipartiteGraphNode {
    private Set<BipartiteGraphEdge> edges = new HashSet<>();
    private Set<DirectedEdge> incomingEdges = new HashSet<>();
    private Set<DirectedEdge> outcomingEdges = new HashSet<>();
    private Set<BipartiteGraphEdge> matchings = new HashSet<>();
    private Pair<BipartiteGraphNode, Integer> inFlow;
    private Pair<BipartiteGraphNode, Integer> outFlow;

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


    public Set<DirectedEdge> getIncomingEdges() {
        return incomingEdges;
    }

    public void addIncomingEdge(DirectedEdge directedEdge) {
        incomingEdges.add(directedEdge);
    }

    public void setIncomingEdges(Set<DirectedEdge> directedEdges) {
        incomingEdges = directedEdges;
    }

    public Set<DirectedEdge> getOutcomingEdges() {
        return outcomingEdges;
    }

    public void addOutcomingEdge(DirectedEdge directedEdge) {
        outcomingEdges.add(directedEdge);
    }

    public void setOutcomingEdges(Set<DirectedEdge> directedEdges) {
        outcomingEdges = directedEdges;
    }

    public void setInFlow(Pair<BipartiteGraphNode, Integer> flow) {
        inFlow = flow;
    }

    public void setOutFlow(Pair<BipartiteGraphNode, Integer> flow) {
        outFlow = flow;
    }

}
