package models.bipartite_matching;

import javafx.util.Pair;
import servers.LocalServer;

import java.util.*;

/*
 *Created by nura on 15/01/17.
*/
public class FlowNetwork {
    private Map<Vertex, Map<Vertex, Boolean>> taskMap = new HashMap<>();
    private Map<Vertex, Map<Vertex, Boolean>> employeeMap = new HashMap<>();
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

    public FlowNetwork(BipartiteGraph bipartiteGraph) {
        this.bipartiteGraph = bipartiteGraph;
        //initialises taskMap for Network flow
        taskMap = bipartiteGraph.getTaskMap();
        //initialises employeeMap for Network flow
        employeeMap = new HashMap<>();
        bipartiteGraph.getEmployeeMap().keySet().forEach(employeeVertex -> {
                    Map<Vertex, Boolean> adjacentVertices = new HashMap();
                    adjacentVertices.put(SINK_VERTEX, false);
                    employeeMap.put(employeeVertex, adjacentVertices);
                    //LocalServer.ffLogger.trace("employee "+ employeeId);
                });


        //initialises source
        taskMap.keySet().forEach(taskVertex ->  SOURCE.getValue().put(taskVertex, false));
    }

    public Map<Vertex, Map<Vertex, Boolean>> getTaskMap() {
        return taskMap;
    }

    public Map<Vertex, Map<Vertex, Boolean>> getEmployeeMap() {
        return employeeMap;
    }

    public Pair<Vertex, Map<Vertex, Boolean>> getSink() {
        return SINK;
    }

    public Pair<Vertex, Map<Vertex, Boolean>> getSource() {
        return SOURCE;
    }

    public void printGraph() {
        LocalServer.ffLogger.trace("==========================NETWORK START==============");
        LocalServer.ffLogger.trace("------------------------Edges from SOURCE to---------: ");
        Map<Vertex, Boolean> sourceEdges = SOURCE.getValue();
        sourceEdges.forEach((vertex, isVisited) -> LocalServer.ffLogger.trace("     source: t{}", vertex.getVertexId()));
        LocalServer.ffLogger.trace("-------------------------Edges from TASK---------: ");
        taskMap.keySet().forEach(
              taskVertex -> {
                  LocalServer.ffLogger.trace("     t{}    : ", taskVertex.getVertexId());
                  Map<Vertex, Boolean> taskEdges = taskMap.get(taskVertex);
                  taskEdges.forEach(
                          (vertex, isVisited)-> {
                              if (vertex.equals(SOURCE_VERTEX)) {
                                  LocalServer.ffLogger.trace("                 source, ");
                              } else {
                                LocalServer.ffLogger.trace("                 e{}, ", vertex.getVertexId());
                              }
                          });
              });

        LocalServer.ffLogger.trace("------------------------Edges from EMPLOYEE---------: ");
        employeeMap.keySet().forEach(
                employeeVertex -> {
                    LocalServer.ffLogger.trace("     e{}    :",employeeVertex.getVertexId());
                    Map<Vertex, Boolean> employeeEdges = employeeMap.get(employeeVertex);
                    employeeEdges.forEach(
                            (vertex, isVisited) -> {
                                if (vertex.equals(SINK_VERTEX)) {
                                    LocalServer.ffLogger.trace("                 sink, ");
                                } else {
                                    LocalServer.ffLogger.trace("                 t{}, ", vertex.getVertexId());
                                }
                            });
                    });

        LocalServer.ffLogger.trace("------------------------Edges to SINK---------: ");

        Map<Vertex, Boolean> sinkEdges = SINK.getValue();
        if (sinkEdges !=null){
            sinkEdges.forEach((vertex, isVisited) -> LocalServer.ffLogger.trace("     sink: e{},  ", vertex.getVertexId()));
        }
        LocalServer.ffLogger.trace("===========================NETWORK END==============\n");
    }
}
