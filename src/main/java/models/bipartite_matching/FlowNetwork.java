package models.bipartite_matching;

import javafx.util.Pair;

import java.util.*;

/*
 *Created by nura on 15/01/17.
*/
public class FlowNetwork {
    private Map<Integer, Set<Vertex>> taskMap = new HashMap<>();
    private Map<Integer, Set<Vertex>> employeeMap = new HashMap<>();
    private int maximumFlow;
    private int totalTaskMatches = 0;
    private int totalEmployeeMatches = 0;
    private BipartiteGraph bipartiteGraph;
    private final Integer SOURCE_ID = -1;
    private final Integer SINK_ID = -2;
    private Pair<Integer, Set<Vertex>> sink = new Pair<>(SINK_ID, new HashSet<>());
    private Pair<Integer, Set<Vertex>> source = new Pair<>(SOURCE_ID, new HashSet<>());

    public FlowNetwork(BipartiteGraph bipartiteGraph) {
        this.bipartiteGraph = bipartiteGraph;
        //initialises taskMap for Network flow
        taskMap = bipartiteGraph.getTaskMap();
        //initialises employeeMap for Network flow
        employeeMap = new HashMap<>();
        //bipartiteGraph.getEmployeeMap();
        //employeeMap.values().forEach(payload -> payload.setEdges(new EdgeMetaData(Integer.MAX_VALUE, 0)));
        bipartiteGraph.getEmployeeMap().keySet().forEach(employeeId -> {
                    Set<Vertex> adjacentVerices = new HashSet();
                    adjacentVerices.add( new Vertex(SINK_ID, VertexType.SINK));
                    employeeMap.put(employeeId, adjacentVerices);
                    //System.out.println("employee "+ employeeId);
                });


        //initialises source
        Set<Vertex> adjacentVericesOfSource = new HashSet();
        taskMap.keySet().forEach(taskId -> adjacentVericesOfSource.add( new Vertex(taskId, VertexType.TASK)));
        source = new Pair(SOURCE_ID, adjacentVericesOfSource);

    }
/*
    public FlowNetwork(BipartiteGraph bipartiteGraph, int version) {
        this.bipartiteGraph = bipartiteGraph;
        //initialises taskMap for Network flow
        taskMap = bipartiteGraph.getTaskMap();
        taskMap.values().forEach(payload -> payload.setEdges(new EdgeMetaData(Integer.MAX_VALUE)));

        //initialises employeeMap for Network flow
        employeeMap = new HashMap<>();
        AdjacencyPayload employeePayload = new AdjacencyPayload();
        employeePayload.addEdge(SINK_ID, new EdgeMetaData(1, 0));
        bipartiteGraph.getEmployeeMap().keySet().forEach(employeeId -> employeeMap.put(employeeId, employeePayload));
        taskMap.values().forEach(payload -> payload.setEdges(new EdgeMetaData(Integer.MAX_VALUE)));

        //initialises source
        AdjacencyPayload sourcePayload = new AdjacencyPayload();
        taskMap.keySet().forEach(taskId -> sourcePayload.addEdge(taskId, new EdgeMetaData(1, 0)));
        source = new Pair(SOURCE_ID, sourcePayload);

        //initialises sink
        AdjacencyPayload sinkPayload = new AdjacencyPayload();
        employeeMap.keySet().forEach(employeeId -> sinkPayload.addEdge(employeeId, new EdgeMetaData(1, 0)));
        sink = new Pair(SINK_ID, sinkPayload);

    }

/*
    private void convertEdgesToFlows() {
        for (BipartiteGraphEdge e: bipartiteGraph.getAllMatchings()) {
            directedEdges.add(e.convertToDirectedEdge());
        }
    }*/
/*
    private static void printList(Vertex node, Set<DirectedEdge> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            DirectedEdge directedEdge = (DirectedEdge)it.next();
            Vertex nodeToPrint = directedEdge.getNodeTo();
            if (nodeToPrint.getNodeType().equals(node.getNodeType())) {
                nodeToPrint = directedEdge.getNodeTo();
            }
            if (nodeToPrint.getNodeType().equals(VertexType.EMPLOYEE)) {
                Employee employee = (Employee) nodeToPrint;
                System.out.print(" employee "+ employee.getId() + ", ");
            } else if (nodeToPrint.getNodeType().equals(VertexType.TASK)) {
                Task task = (Task) nodeToPrint;
                System.out.print(" task " + task.getId()+", ");
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    private static void printFlowSources(Set<DirectedEdge> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            DirectedEdge directedEdge = (DirectedEdge)it.next();
            Vertex nodeToPrint = directedEdge.getNodeFrom();
            if (nodeToPrint.getNodeType().equals(VertexType.EMPLOYEE)) {
                Employee employee = (Employee) nodeToPrint;
                System.out.print(" employee "+ employee.getId() + ", ");
            } else if (nodeToPrint.getNodeType().equals(VertexType.TASK)) {
                Task task = (Task) nodeToPrint;
                System.out.print(" task " + task.getId()+", ");
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }*/
/*

    private static void printFlowSinks(Set<DirectedEdge> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            DirectedEdge directedEdge = (DirectedEdge)it.next();
            Vertex nodeToPrint = directedEdge.getNodeTo();
            if (nodeToPrint.getNodeType().equals(VertexType.EMPLOYEE)) {
                Employee employee = (Employee) nodeToPrint;
                System.out.print(" employee "+ employee.getId() + ", ");
            } else if (nodeToPrint.getNodeType().equals(VertexType.TASK)) {
                Task task = (Task) nodeToPrint;
                System.out.print(" task " + task.getId()+", ");
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
*/

    public Map<Integer, Set<Vertex>> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, Set<Vertex>> getEmployeeMap() {
        return employeeMap;
    }

    public Pair<Integer, Set<Vertex>> getSink() {
        return sink;
    }

    public Pair<Integer, Set<Vertex>> getSource() {
        return source;
    }

    public void printGraph() {
        System.out.println();
        System.out.println();
        System.out.println("==============NETWORK START==============");
        System.out.println("-------Flows from SOURCE to---------: ");
        Set<Vertex> sourceEdges = source.getValue();
        sourceEdges.forEach(vertex -> System.out.println("source: t" + vertex.toString()));
        System.out.println("---------Flows from TASK---------: ");
        taskMap.keySet().forEach(
              taskId -> {
                  System.out.print("t" + taskId + "    : ");// +taskMap.get(taskId).toString());
                  Set<Vertex> taskEdges = taskMap.get(taskId);
                  taskEdges.forEach(
                          vertex-> {
                              if (vertex.getVertexType()==VertexType.SOURCE) {
                                  System.out.print("source, ");
                              } else {
                                System.out.print("e" + vertex.toString() + ", ");//+ " "+ edgeData);
                              }
                          });
                  System.out.println();});

        System.out.println("---------Flows from EMPLOYEE---------: ");
        employeeMap.keySet().forEach(
                employeeId -> {
                    System.out.print("e" + employeeId + "    : ");// +yeeMap.get(employeeId).toString());
                    Set<Vertex> employeeEdges = employeeMap.get(employeeId);
                    employeeEdges.forEach(
                            vertex -> {
                                String edgeData = "";//employeeEdges.get(nodeId).toString();
                                if (vertex.getVertexType()==VertexType.SINK) {
                                    System.out.print("sink, ");// + edgeData);
                                } else {
                                    System.out.print("t" + vertex.toString() + ", ");//+ " "+ edgeData);
                                }
                            });
                    System.out.println();});

        System.out.println("---------Flows to SINK---------: ");

        Set<Vertex> sinkEdges = sink.getValue();
        if (sinkEdges !=null){
            sinkEdges.forEach(vertex -> System.out.println("sink  : e" + vertex.toString() + ",  "));// + sinkEdges.get(employeeId).toString()));
        }
        System.out.println("==============NETWORK END==============");
        System.out.println();
    }
}
