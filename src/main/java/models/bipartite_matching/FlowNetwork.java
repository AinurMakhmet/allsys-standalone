package models.bipartite_matching;

import javafx.util.Pair;
import logic.EdmondsKarpStrategy;
import logic.MaximumProfitAlgorithm;
import org.apache.logging.log4j.Logger;
import servers.LocalServer;

import java.util.*;

/*
 *Created by nura on 15/01/17.
*/
public class FlowNetwork {
    private Map<Vertex, Map<Vertex, Boolean>> mapFromSource = new HashMap<>();
    private Map<Vertex, Map<Vertex, Boolean>> mapToSink = new HashMap<>();
    private static final Integer SOURCE_ID = -1;
    private static final Integer SINK_ID = -2;
    public static final Vertex SOURCE_VERTEX = new Vertex(SOURCE_ID, VertexType.SOURCE);
    public static final Vertex SINK_VERTEX = new Vertex(SINK_ID, VertexType.SINK);

    private final Pair<Vertex, Map<Vertex, Boolean>> SOURCE = new Pair(SOURCE_VERTEX, new HashMap<>());
    private final Pair<Vertex, Map<Vertex, Boolean>> SINK = new Pair(SINK_VERTEX, new HashMap<>());

    private Class strategyClass;
    private Logger logger;
    public FlowNetwork() {

    }
    public FlowNetwork(BipartiteGraph bipartiteGraph) {
        strategyClass = bipartiteGraph.strategyClass;
        if (strategyClass.equals(MaximumProfitAlgorithm.class)) {
            logger = LocalServer.mpLogger;
        } else if (strategyClass.equals(EdmondsKarpStrategy.class)) {
            logger = LocalServer.ekLogger;
        }
        mapToSink = new HashMap<>();

        //initialises mapFromSource for Network flow
        mapFromSource = bipartiteGraph.getTaskMap();
        //initialises mapToSink for Network flow
        mapToSink = new HashMap<>();
        bipartiteGraph.getEmployeeMap().keySet().forEach(employeeVertex -> {
            Map<Vertex, Boolean> adjacentVertices = new HashMap();
            adjacentVertices.put(SINK_VERTEX, false);
            mapToSink.put(employeeVertex, adjacentVertices);
            //LocalServer.ekLogger.trace("employee "+ employeeId);
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
        logger.trace("\n==========================NETWORK START==============");
        logger.trace("------------------------Edges from SOURCE to---------: ");
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
