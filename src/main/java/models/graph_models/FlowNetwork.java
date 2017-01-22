/*
package models.graph_models;

import javafx.util.Pair;
import models.Employee;
import models.Task;

import java.util.*;

*/
/*
 *Created by nura on 15/01/17.
*//*

public class FlowNetwork {
    private Map<Integer, Pair<Integer, Collection>> taskMap = new HashMap<>();
    private Map<Integer, Pair<Integer, Collection>> employeeMap = new HashMap<>();
    private Set<DirectedEdge> directedEdges = new HashSet<>();
    private BipartiteGraph bipartiteGraph;
    private final Integer sourceId = -1;
    private final Integer sinkId = -2;
    private Pair<Integer, Pair<Integer, Collection>> sink;
    private Pair<Integer, Pair<Integer, Collection>> source;

    public FlowNetwork(BipartiteGraph bipartiteGraph) {
        this.bipartiteGraph = bipartiteGraph;
        taskMap = bipartiteGraph.getTaskMap();
        employeeMap = bipartiteGraph.getEmployeeMap();
        source = new Pair<>(-1, new Pair<>(null, taskMap.keySet()));
        sink = new Pair<>(-2, new Pair(null, employeeMap.keySet()));
        for (Integer id: employeeMap.keySet()) {
            Collection outNodes =  new ArrayList();
            outNodes.add(-2);
            employeeMap.put(id, new Pair<>(null, outNodes));
        }
        for (Pair<Integer, Collection> pair: employeeMap.values()) {
            //DirectedEdge toSink = new DirectedEdge(e, sink);
            //sink.addIncomingEdge(toSink);
            //e.addOutcomingEdge(toSink);
        }
    }
*/
/*
    private void convertEdgesToFlows() {
        for (BipartiteGraphEdge e: bipartiteGraph.getAllMatchings()) {
            directedEdges.add(e.convertToDirectedEdge());
        }
    }*//*

*/
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
    }*//*

*/
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
*//*


  */
/*  public void printGraph() {
        System.out.println("==============NETWORK START==============");
        System.out.println("-------Flows from SOURCE to---------: ");
        source.getOutcomingEdges().forEach(directedEdge -> System.out.println(" task " + ((Task) directedEdge.getNodeTo()).getId()));
        System.out.println("---------Flows from TASK---------: ");
        for (Task task: taskMap.values()) {
            System.out.print("Flows from task " + task.getId() + " to: ");
            printFlowSinks(task.getOutcomingEdges());
            System.out.println();
        }


        taskMap.values().forEach(
              task -> {
                  System.out.print("Flows from task " + task.getId() + " to: ");
                  task.getOutcomingEdges().forEach(
                          directedEdge -> System.out.print(" employee " + ((Employee) directedEdge.getNodeTo()).getId() + ", "));
                  System.out.println();});

        System.out.println("---------Flows to SINK---------: ");
        sink.getIncomingEdges().forEach(directedEdge -> System.out.println(" employee " + ((Employee) directedEdge.getNodeFrom()).getId()));
        System.out.println("==============NETWORK END==============");
    }*//*

}
*/
