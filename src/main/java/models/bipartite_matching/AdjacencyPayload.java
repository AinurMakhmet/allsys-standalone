package models.bipartite_matching;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nura on 20/01/17.
 */
public class AdjacencyPayload {
    Integer destinationVertexId;
    Map<Integer, EdgeMetaData> edges = new HashMap<>();

    public Integer getDestinationVertexId() {
        return destinationVertexId;
    }

    public void setDestinationVertexId(Integer destinationVertexId) {
        this.destinationVertexId = destinationVertexId;
    }

    public Map<Integer, EdgeMetaData> getEdges() {
        return edges;
    }

    public EdgeMetaData getEdge(Integer nodeId) {
        return edges.get(nodeId);
    }

    public void addEdge(Integer nodeId, EdgeMetaData edgeMetaData) {
        edges.put(nodeId, edgeMetaData);
    }

    public void setEdges(EdgeMetaData edgeMetaData) {
        edges.keySet().forEach(key -> edges.put(key, edgeMetaData));
    }

    public String toString() {
        return "";//"destination vertex id is "+ destinationVertexId + " and has edges "+ (edges!=null);
    }
}
