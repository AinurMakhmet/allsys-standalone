package logic;

import models.graph_models.*;

import java.util.Queue;

/**
 *
 */
public class FordFulkersonAlgorithm extends AbstractAllocationAlgorithm {

    @Override
    public boolean allocate() {
        BipartiteGraph bipartiteGraph = new BipartiteGraph();
        NetworkGraph networkGraph = new NetworkGraph(bipartiteGraph);
        networkGraph.printGraph();
        traverseBFS(SourceNode.getInstance());
        return true;
    }

    private void traverseBFS(BipartiteGraphNode origin) {
        Queue<BipartiteGraphNode> neighbors =
        node.getOutcomingEdges().forEach(inEdge -> inEdge.getNodeTo().);
    }
}
