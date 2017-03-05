package models.bipartite_matching;

import javafx.util.Pair;
import servers.LocalServer;

import java.util.*;

/*
 *Created by nura on 15/01/17.
*/
public class FlowNetwork {
    private Map<Vertex, Map<Vertex, Boolean>> mapFromSource = new HashMap<>();
    private Map<Vertex, Map<Vertex, Boolean>> mapToSink = new HashMap<>();
    private int maximumFlow;
    private int totalTaskMatches = 0;
    private int totalEmployeeMatches = 0;
    private BipartiteGraph bipartiteGraph;
    private static final Integer SOURCE_ID = -1;
    private static final Integer SINK_ID = -2;
    public static final Vertex SOURCE_VERTEX = new Vertex(SOURCE_ID, VertexType.SOURCE);
    public static final Vertex SINK_VERTEX = new Vertex(SINK_ID, VertexType.SINK);

    private final Pair<Vertex, Map<Vertex, Boolean>> SOURCE = new Pair(SOURCE_VERTEX, new HashMap<>());
    private final Pair<Vertex, Map<Vertex, Boolean>> SINK = new Pair(SINK_VERTEX, new HashMap<>());

    public FlowNetwork() {}
    public FlowNetwork(BipartiteGraph bipartiteGraph) {
        this.bipartiteGraph = bipartiteGraph;
        //initialises mapFromSource for Network flow
        mapFromSource = bipartiteGraph.getTaskMap();
        //initialises mapToSink for Network flow
        mapToSink = new HashMap<>();
        bipartiteGraph.getEmployeeMap().keySet().forEach(employeeVertex -> {
                    Map<Vertex, Boolean> adjacentVertices = new HashMap();
                    adjacentVertices.put(SINK_VERTEX, false);
                    mapToSink.put(employeeVertex, adjacentVertices);
                    //LocalServer.ffLogger.trace("employee "+ employeeId);
                });


        //initialises source
        mapFromSource.keySet().forEach(taskVertex ->  SOURCE.getValue().put(taskVertex, false));
    }

    public Map<Vertex, Map<Vertex, Boolean>> getMapFromSource() {
        return mapFromSource;
    }

    public Map<Vertex, Map<Vertex, Boolean>> getMapToSink() {
        return mapToSink;
    }

    public Pair<Vertex, Map<Vertex, Boolean>> getSink() {
        return SINK;
    }

    public Pair<Vertex, Map<Vertex, Boolean>> getSource() {
        return SOURCE;
    }

    public void printGraph() {
        LocalServer.ffLogger.trace("\n==========================NETWORK START==============");
        LocalServer.ffLogger.trace("------------------------Edges from SOURCE to---------: ");
        Map<Vertex, Boolean> sourceEdges = SOURCE.getValue();
        sourceEdges.forEach((vertex, isVisited) -> LocalServer.ffLogger.trace("\tsource: t{}", vertex.getVertexId()));
        LocalServer.ffLogger.trace("-------------------------Edges from TASK---------: ");
        mapFromSource.keySet().forEach(
              taskVertex -> {
                  LocalServer.ffLogger.trace("\tt{}    : ", taskVertex.getVertexId());
                  Map<Vertex, Boolean> taskEdges = mapFromSource.get(taskVertex);
                  taskEdges.forEach(
                          (vertex, isVisited)-> {
                              if (vertex.equals(SOURCE_VERTEX)) {
                                  LocalServer.ffLogger.trace("\t\tsource, ");
                              } else {
                                LocalServer.ffLogger.trace("\t\te{}, ", vertex.getVertexId());
                              }
                          });
              });

        LocalServer.ffLogger.trace("------------------------Edges from EMPLOYEE---------: ");
        mapToSink.keySet().forEach(
                employeeVertex -> {
                    LocalServer.ffLogger.trace("\te{}    :",employeeVertex.getVertexId());
                    Map<Vertex, Boolean> employeeEdges = mapToSink.get(employeeVertex);
                    employeeEdges.forEach(
                            (vertex, isVisited) -> {
                                if (vertex.equals(SINK_VERTEX)) {
                                    LocalServer.ffLogger.trace("\t\tsink, ");
                                } else {
                                    LocalServer.ffLogger.trace("\t\tt{}, ", vertex.getVertexId());
                                }
                            });
                    });

        LocalServer.ffLogger.trace("------------------------Edges to SINK---------: ");

        Map<Vertex, Boolean> sinkEdges = SINK.getValue();
        if (sinkEdges !=null){
            sinkEdges.forEach((vertex, isVisited) -> LocalServer.ffLogger.trace("\tsink: e{},  ", vertex.getVertexId()));
        }
        LocalServer.ffLogger.trace("===========================NETWORK END==============\n");
    }
}
