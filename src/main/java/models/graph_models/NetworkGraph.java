package models.graph_models;

import models.Employee;
import models.Task;

import java.util.*;

/**
 * Created by nura on 15/01/17.
 */
public class NetworkGraph {
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Map<Integer, Employee> employeeMap = new HashMap<>();
    private Set<DirectedEdge> directedEdges = new HashSet<>();
    private BipartiteGraph bipartiteGraph;

    private SinkNode sink = SinkNode.getInstance();
    private SourceNode source = SourceNode.getInstance();

    public NetworkGraph(BipartiteGraph bipartiteGraph) {
        this.bipartiteGraph = bipartiteGraph;
        taskMap = bipartiteGraph.getTaskMap();
        employeeMap = bipartiteGraph.getEmployeeMap();
        convertEdgesToFlows();
        for (Task t: taskMap.values()) {
            DirectedEdge fromSource = new DirectedEdge(source, t);
            source.addOutcomingEdge(fromSource);
            t.addIncomingEdge(fromSource);
        }
        for (Employee e: employeeMap.values()) {
            DirectedEdge toSink = new DirectedEdge(e, sink);
            sink.addIncomingEdge(toSink);
            e.addOutcomingEdge(toSink);
        }
    }

    private void convertEdgesToFlows() {
        for (BipartiteGraphEdge e: bipartiteGraph.getAllMatchings()) {
            directedEdges.add(e.convertToDirectedEdge());
        }
    }

    private static void printList(BipartiteGraphNode node, Set<DirectedEdge> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            DirectedEdge directedEdge = (DirectedEdge)it.next();
            BipartiteGraphNode nodeToPrint = directedEdge.getNodeTo();
            if (nodeToPrint.getNodeType().equals(node.getNodeType())) {
                nodeToPrint = directedEdge.getNodeTo();
            }
            if (nodeToPrint.getNodeType().equals(BipartiteGraphNodeType.EMPLOYEE)) {
                Employee employee = (Employee) nodeToPrint;
                System.out.print(" employee "+ employee.getId() + ", ");
            } else if (nodeToPrint.getNodeType().equals(BipartiteGraphNodeType.TASK)) {
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
            BipartiteGraphNode nodeToPrint = directedEdge.getNodeFrom();
            if (nodeToPrint.getNodeType().equals(BipartiteGraphNodeType.EMPLOYEE)) {
                Employee employee = (Employee) nodeToPrint;
                System.out.print(" employee "+ employee.getId() + ", ");
            } else if (nodeToPrint.getNodeType().equals(BipartiteGraphNodeType.TASK)) {
                Task task = (Task) nodeToPrint;
                System.out.print(" task " + task.getId()+", ");
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    private static void printFlowSinks(Set<DirectedEdge> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            DirectedEdge directedEdge = (DirectedEdge)it.next();
            BipartiteGraphNode nodeToPrint = directedEdge.getNodeTo();
            if (nodeToPrint.getNodeType().equals(BipartiteGraphNodeType.EMPLOYEE)) {
                Employee employee = (Employee) nodeToPrint;
                System.out.print(" employee "+ employee.getId() + ", ");
            } else if (nodeToPrint.getNodeType().equals(BipartiteGraphNodeType.TASK)) {
                Task task = (Task) nodeToPrint;
                System.out.print(" task " + task.getId()+", ");
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void printGraph() {
        System.out.println("==============NETWORK START==============");
        System.out.println("-------Flows from SOURCE to---------: ");
        source.getOutcomingEdges().forEach(directedEdge -> System.out.println(" task " + ((Task) directedEdge.getNodeTo()).getId()));
        System.out.println("---------Flows from TASK---------: ");
        /*for (Task task: taskMap.values()) {
            System.out.print("Flows from task " + task.getId() + " to: ");
            printFlowSinks(task.getOutcomingEdges());
            System.out.println();
        }*/

        taskMap.values().forEach(
              task -> {
                  System.out.print("Flows from task " + task.getId() + " to: ");
                  task.getOutcomingEdges().forEach(
                          directedEdge -> System.out.print(" employee " + ((Employee) directedEdge.getNodeTo()).getId() + ", "));
                  System.out.println();});

        System.out.println("---------Flows to SINK---------: ");
        sink.getIncomingEdges().forEach(directedEdge -> System.out.println(" employee " + ((Employee) directedEdge.getNodeFrom()).getId()));
        System.out.println("==============NETWORK END==============");
    }
}
