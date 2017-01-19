package models.graph_models;

import models.Employee;
import models.Task;

import java.util.*;

/**
 * Created by nura on 15/01/17.
 */
public class NetworkGraph {
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Map<Integer, Employee> employeeMap = new HashMap<>();private Set<Flow> flows = new HashSet<>();
    private BipartiteGraph bipartiteGraph;

    private BipartiteGraphNode sink = new BipartiteGraphNode().setNodeType(BipartiteGraphNodeType.SINK);
    private BipartiteGraphNode source = new BipartiteGraphNode().setNodeType(BipartiteGraphNodeType.SOURCE);

    public NetworkGraph(BipartiteGraph bipartiteGraph) {
        this.bipartiteGraph = bipartiteGraph;
        taskMap = bipartiteGraph.getTaskMap();
        employeeMap = bipartiteGraph.getEmployeeMap();
        convertEdgesToFlows();
        for (Task t: taskMap.values()) {
            Flow fromSource = new Flow(source, t);
            source.addOutcomingFlow(fromSource);
            t.addIncomingFlow(fromSource);
        }
        for (Employee e: employeeMap.values()) {
            Flow toSink = new Flow(e, sink);
            sink.addIncomingFlow(toSink);
            e.addOutcomingFlow(toSink);
        }
        buildNetwork();

    }

    private void convertEdgesToFlows() {
        for (BipartiteGraphEdge e: bipartiteGraph.getMatching()) {
            flows.add(e.convertToFlow());
        }
    }

    public NetworkGraph buildNetwork() {
        return this;
    }

    private static void printList(BipartiteGraphNode node, Set<Flow> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            Flow flow = (Flow)it.next();
            BipartiteGraphNode nodeToPrint = flow.getFlowSink();
            if (nodeToPrint.getNodeType().equals(node.getNodeType())) {
                nodeToPrint = flow.getFlowSink();
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

    private static void printFlowSources(Set<Flow> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            Flow flow = (Flow)it.next();
            BipartiteGraphNode nodeToPrint = flow.getFlowSource();
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

    private static void printFlowSinks(Set<Flow> mp) {
        Iterator it = mp.iterator();
        while (it.hasNext()) {
            Flow flow = (Flow)it.next();
            BipartiteGraphNode nodeToPrint = flow.getFlowSink();
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
        source.getOutcomingFlows().forEach(flow -> System.out.println(" task " + ((Task)flow.getFlowSink()).getId()));
        System.out.println("---------Flows from TASK---------: ");
        /*for (Task task: taskMap.values()) {
            System.out.print("Flows from task " + task.getId() + " to: ");
            printFlowSinks(task.getOutcomingFlows());
            System.out.println();
        }*/

        taskMap.values().forEach(
              task -> {
                  System.out.print("Flows from task " + task.getId() + " to: ");
                  task.getOutcomingFlows().forEach(
                          flow -> System.out.print(" employee " + ((Employee) flow.getFlowSink()).getId() + ", "));
                  System.out.println();});

        System.out.println("---------Flows to SINK---------: ");
        sink.getIncomingFlows().forEach(flow -> System.out.println(" employee " + ((Employee)flow.getFlowSource()).getId()));
        System.out.println("==============NETWORK END==============");
    }

    public void addFlow() {

    }
}
