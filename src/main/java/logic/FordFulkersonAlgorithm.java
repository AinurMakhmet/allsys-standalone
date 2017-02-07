package logic;

import entity_utils.EmployeeUtils;
import entity_utils.TaskUtils;
import models.Employee;
import models.Task;
import models.bipartite_matching.VertexType;
import models.bipartite_matching.*;
import models.bipartite_matching.Vertex;

import java.util.*;

/**
 * Algorithm that
 */
public class FordFulkersonAlgorithm extends AbstractAllocationAlgorithm {
    private FlowNetwork residualNetwork;
    private Queue<Vertex> augmentedPathQueue = new LinkedList<>();
    private Map<Vertex, Vertex> augmentedPathBFS;
    private Map<Vertex, Boolean> adjacentVertices;
    private final Vertex SOURCE_VERTEX = FlowNetwork.SOURCE_VERTEX;
    private final Vertex SINK_VERTEX = FlowNetwork.SINK_VERTEX;
    private int pathNumber = 0;
    public Map<Vertex, Vertex> matching = new HashMap<>();

    @Override
    public boolean allocate(List<Task> tasksToAllocate) {
        residualNetwork = new FlowNetwork(new BipartiteGraph(tasksToAllocate));

        //Starts constructing a path from the source;
        residualNetwork.printGraph();
        //TODO: BFS, DFS is non-deterministic!!!!!!!
        while (findAugmentingPathBFS()) {
            System.out.println("Path Number "+ ++pathNumber);
            constructResidualNetworkBFS();
            residualNetwork.printGraph();

        }
        findMatching();
        matching.forEach((a, b)-> System.out.println(a + " is matched to " + b));
        return true;
    }

    private void findMatching() {
        residualNetwork.getSink().getValue().keySet().forEach(
                employeeVertex-> {
                    residualNetwork.getEmployeeMap().get(employeeVertex).keySet().forEach(
                            taskVertex -> matching.put(taskVertex, employeeVertex));
                });
    }


    private boolean findAugmentingPathBFS() {
        augmentedPathBFS = new HashMap<>();
        Queue<Vertex> traversalQueue = new LinkedList<>();
        traversalQueue.add(SOURCE_VERTEX);
        Vertex childVertex = SOURCE_VERTEX;

        while (!traversalQueue.isEmpty()) {
            Vertex parentVertex = traversalQueue.remove();
            while((childVertex=findUnvisitedChild(parentVertex))!=null) {
                if (!traversalQueue.contains(childVertex)){
                    traversalQueue.add(childVertex);
                    augmentedPathBFS.putIfAbsent(childVertex, parentVertex);
                }
                if (childVertex.equals(SINK_VERTEX)) {
                    return true;
                }
            }
        }
        return false;
    }


    private Vertex findUnvisitedChild(Vertex parentVertex) {
        Vertex toReturn = null;
        switch (parentVertex.getVertexType()) {
            case SOURCE:
                adjacentVertices = residualNetwork.getSource().getValue();
                break;
            case TASK:
                adjacentVertices = residualNetwork
                        .getTaskMap()
                        .get(parentVertex);
                break;
            case EMPLOYEE:
                adjacentVertices = residualNetwork.getEmployeeMap().get(parentVertex);
                break;
        }

        Vertex vertexToReturn = null;
        if (adjacentVertices!=null) {
            a: for (Vertex childVertex : adjacentVertices.keySet()) {
                if (childVertex.getVertexType()==VertexType.SOURCE || adjacentVertices.get(childVertex)) {
                    continue a;
                } else if (childVertex.equals(SINK_VERTEX) || !adjacentVertices.get(childVertex)) {
                    vertexToReturn = childVertex;
                    //set the isVisited for the vertex to be true
                    adjacentVertices.put(childVertex, true);
                    break;
                }
            }
        }
        return vertexToReturn;
    }

    private void constructResidualNetworkBFS() {
        Vertex childVertex = SINK_VERTEX;
        Vertex parentVertex = childVertex;

        Stack path = new Stack();
        path.push(childVertex);
        while (parentVertex.getVertexType()!=VertexType.SOURCE) {
            parentVertex = (Vertex)augmentedPathBFS.get(childVertex);
            path.push(parentVertex);
            childVertex = parentVertex;
        }

        path.forEach(vertex ->System.out.println(vertex));

        augmentedPathQueue.clear();
        augmentedPathQueue.addAll(path);
        while (!augmentedPathQueue.isEmpty()) {
            childVertex = augmentedPathQueue.poll();
            parentVertex  = augmentedPathQueue.peek();
            doAddDeleteVertices(parentVertex, childVertex);
            if (parentVertex.equals(SOURCE_VERTEX)) break;
        }

        residualNetwork.getSource().getValue().forEach((vertex, aBoolean) -> residualNetwork.getSource().getValue().put(vertex, false));
        residualNetwork.getTaskMap().keySet().forEach(
                vertex-> {
                    Map<Vertex, Boolean> vertices = residualNetwork.getTaskMap().get(vertex);
                    vertices.forEach((adVertex, isVisited)-> vertices.put(adVertex, false));
                });
        residualNetwork.getEmployeeMap().keySet().forEach(
                vertex-> {
                    Map<Vertex, Boolean> vertices = residualNetwork.getEmployeeMap().get(vertex);
                    vertices.forEach((adVertex, isVisited)-> vertices.put(adVertex, false));
                });
    }

    private void doAddDeleteVertices(Vertex parentVertex, Vertex childVertex) {
        if (parentVertex.getVertexType()==VertexType.SOURCE && childVertex.getVertexType()==VertexType.TASK) {
            residualNetwork.getSource().getValue().remove(childVertex);
            residualNetwork.getTaskMap().get(childVertex).put(parentVertex, false);
        } else if (parentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.EMPLOYEE) {
            residualNetwork.getTaskMap().get(parentVertex).remove(childVertex);
            residualNetwork.getEmployeeMap().get(childVertex).put(parentVertex, false);
        } else if (parentVertex.getVertexType()==VertexType.EMPLOYEE && childVertex.getVertexType()==VertexType.TASK) {
            residualNetwork.getEmployeeMap().get(parentVertex).remove(childVertex);
            residualNetwork.getTaskMap().get(childVertex).put(parentVertex, false);
        } else if (parentVertex.getVertexType()==VertexType.EMPLOYEE && childVertex.getVertexType()==VertexType.SINK) {
            residualNetwork.getEmployeeMap().get(parentVertex).remove(childVertex);
            residualNetwork.getSink().getValue().put(parentVertex, false);
        }//the last case will never be executed;
        else if (parentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.SOURCE) {
            residualNetwork.getTaskMap().get(parentVertex).remove(childVertex);
            residualNetwork.getSource().getValue().put(parentVertex, false);
        }
    }
}
