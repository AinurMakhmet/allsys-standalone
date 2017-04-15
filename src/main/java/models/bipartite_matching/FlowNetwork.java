package models.bipartite_matching;

import javafx.util.Pair;
import logic.EdmondsKarpStrategy;
import logic.MaximumProfitStrategy;
import models.SystemData;
import org.apache.logging.log4j.Logger;
import models.Task;
import servers.LocalServer;

import java.util.*;

/*
 * A network used to model maximum flow  and minimum cost flow problems
*/
public class FlowNetwork {
    private Map<Vertex, Map<Vertex, Boolean>> mapFromSource;
    private Map<Vertex, Map<Vertex, Boolean>> mapToSink;
    private static final Integer SOURCE_ID = -1;
    private static final Integer SINK_ID = -2;
    public static final Vertex SOURCE_VERTEX = new Vertex(SOURCE_ID, VertexType.SOURCE);
    public static final Vertex SINK_VERTEX = new Vertex(SINK_ID, VertexType.SINK);
    private Pair<Vertex, Map<Vertex, Boolean>> SOURCE = new Pair(SOURCE_VERTEX, new HashMap<>());
    private final Pair<Vertex, Map<Vertex, Boolean>> SINK = new Pair(SINK_VERTEX, new HashMap<>());
    private Class strategyClass;
    private Logger logger;

    public FlowNetwork(BipartiteGraph bipartiteGraph) {
        strategyClass = bipartiteGraph.strategyClass;
        if (strategyClass.equals(MaximumProfitStrategy.class)) {
            logger = LocalServer.mpLogger;
        } else if (strategyClass.equals(EdmondsKarpStrategy.class)) {
            logger = LocalServer.ekLogger;
        }

        //initialises mapFromSource for flow network
        mapFromSource = bipartiteGraph.getTaskMap();
        //initialises mapToSink for flow network
        mapToSink = new HashMap<>();
        bipartiteGraph.getEmployeeMap().keySet().forEach(employeeVertex -> {
            Map<Vertex, Boolean> adjacentVertices = new HashMap();
            adjacentVertices.put(SINK_VERTEX, false);
            mapToSink.put(employeeVertex, adjacentVertices);
            //LocalServer.ekLogger.trace("employee "+ employeeId);
        });

        //initialises source
        mapFromSource.keySet().forEach(taskVertex ->  SOURCE.getValue().put(taskVertex, false));

        printGraph();
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
        logger.trace("\n==========================NETWORK START==============");
        logger.trace("------------------------Edges from SOURCE ---------: ");
        Map<Vertex, Boolean> sourceEdges = SOURCE.getValue();
        sourceEdges.forEach((vertex, isVisited) -> logger.trace("\tsource: {}", vertex.toString()));
        logger.trace("-------------------------Edges from SET 1---------: ");
        mapFromSource.keySet().forEach(
                vertex -> {
                    logger.trace("\t{}    : ", vertex.toString());
                    Map<Vertex, Boolean> edges = mapFromSource.get(vertex);
                    edges.forEach(
                            (adjacentVertex, isVisited)-> {
                                if (adjacentVertex.equals(SOURCE_VERTEX)) {
                                    logger.trace("\t\t\t\tsource, ");
                                } else {
                                    logger.trace("\t\t\t\t{}, ", adjacentVertex.toString());
                                }
                            });
                });

        logger.trace("------------------------Edges from SET 2---------: ");
        mapToSink.keySet().forEach(
                vertex -> {
                    logger.trace("\t{}    :",vertex.toString());
                    Map<Vertex, Boolean> edges = mapToSink.get(vertex);
                    edges.forEach(
                            (adjacentVertex, isVisited) -> {
                                if (adjacentVertex.equals(SINK_VERTEX)) {
                                    LocalServer.mpLogger.trace("\t\t\t\tsink, ");
                                } else {
                                    logger.trace("\t\t\t\t{}, ", adjacentVertex.toString());
                                }
                            });
                });

        logger.trace("------------------------Edges to SINK---------: ");

        Map<Vertex, Boolean> sinkEdges = SINK.getValue();
        if (sinkEdges !=null){
            sinkEdges.forEach((vertex, isVisited) -> logger.trace("\tsink: {},  ", vertex.toString()));
        }
        logger.trace("===========================NETWORK END==============\n");
    }
}
