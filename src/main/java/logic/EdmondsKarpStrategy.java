package logic;

import models.Employee;
import models.SystemData;
import models.Task;
import models.bipartite_matching.VertexType;
import models.bipartite_matching.*;
import models.bipartite_matching.Vertex;
import org.apache.logging.log4j.Logger;
import servers.LocalServer;

import java.util.*;

/**
 * Algorithm finds largest matching possible for a given set of tasks.
 * The algorithm based on Edmonds-Karp method (does breadth-first-search to find augmenting path).
 */
public class EdmondsKarpStrategy extends Strategy {
    protected FlowNetwork residualNetwork;
    public Map<Vertex, Vertex> matching;

    protected Class strategyClass;
    Logger logger;

    private static EdmondsKarpStrategy ourInstance = new EdmondsKarpStrategy();

    public static EdmondsKarpStrategy getInstance() {
        return ourInstance;
    }


    @Override
    public List<Task> allocate(List<Task> tasksToAllocate) {
        strategyClass = this.getClass();
        logger = LocalServer.ekLogger;

        result = new LinkedList<>();
        numOfUnnalocatedTasks=tasksToAllocate.size();

        List<Task> remainingTasksToAllocate = tasksToAllocate;
        begTime = System.currentTimeMillis();
        while(remainingTasksToAllocate.size()>0) {
            if (canAllocateMoreTasks(remainingTasksToAllocate)) {
                remainingTasksToAllocate = runAllocationRound(remainingTasksToAllocate);
            } else {
                break;
            }
        }


        remainingTasksToAllocate.forEach(task -> {
            if (!result.contains(task)) {
                result.add(task);
            }
        });
        endTime = System.currentTimeMillis();
        logger.info("Time for running algorithm: {} ms", (endTime-begTime));
        return result;
    }

    protected boolean canAllocateMoreTasks(List<Task> remainingTasksToAllocate) {
        begTime = System.currentTimeMillis();
        residualNetwork = new FlowNetwork(new BipartiteGraph(strategyClass, remainingTasksToAllocate));
        endTime = System.currentTimeMillis();
        logger.info("Time for constrcuting data structure: {} ms", (endTime-begTime));
        return residualNetwork.getSource().getValue().size()>0;
    }

    private List<Task> runAllocationRound(List<Task> remainingTasksToAllocate) {
        matching = new HashMap<>();

        //Starts constructing a path from the source;
        residualNetwork.printGraph();
        //TODO: BFS, DFS is non-deterministic!!!!!!!
        Map<Vertex, Vertex> augmentingPathBFS;
        while ((augmentingPathBFS = getAugmentingPathBFS())!=null) {
            constructResidualNetwork(augmentingPathBFS);
            residualNetwork.printGraph();
        }
        findMatching();
        matching.forEach((a, b)-> {
            Task task = SystemData.getAllTasksMap().get(a.getVertexId());
            remainingTasksToAllocate.remove(task);
            Employee employee = SystemData.getAllEmployeesMap().get(b.getVertexId());
            task.setRecommendedAssignee(employee);
            numOfUnnalocatedTasks--;
            result.add(task);
            logger.trace("{} is matched to {}", task.getName(), employee.getFirstName());
        });
        return remainingTasksToAllocate;
    }

    protected void findMatching() {
        residualNetwork.getSink().getValue().keySet().forEach(
                employeeVertex-> {
                    residualNetwork.getMapToSink().get(employeeVertex).keySet().forEach(
                            vertex -> {
                                if (vertex.getVertexType().equals(VertexType.TASK)) {
                                    matching.put(vertex, employeeVertex);
                                }
                            });
                });
    }

    /**
     * O(m), where m is the number of edges in the residual graph.
     * @return
     */
    private Map<Vertex, Vertex> getAugmentingPathBFS() {
        Map<Vertex, Vertex> augmentingPathBFS = new HashMap<>();
        Queue<Vertex> traversalQueue = new LinkedList<>();
        traversalQueue.add(FlowNetwork.SOURCE_VERTEX);
        Vertex childVertex = FlowNetwork.SOURCE_VERTEX;

        while (!traversalQueue.isEmpty()) {
            Vertex parentVertex = traversalQueue.remove();
            while((childVertex=findUnvisitedChild(parentVertex))!=null) {
                if (!traversalQueue.contains(childVertex)){
                    traversalQueue.add(childVertex);
                    augmentingPathBFS.putIfAbsent(childVertex, parentVertex);
                }
                if (childVertex.equals(FlowNetwork.SINK_VERTEX)) {
                    return augmentingPathBFS;
                }
            }
        }
        return null;
    }


    private Vertex findUnvisitedChild(Vertex parentVertex) {
        Map<Vertex, Boolean> adjacentVertices=null;
        Vertex toReturn = null;
        switch (parentVertex.getVertexType()) {
            case SOURCE:
                adjacentVertices = residualNetwork.getSource().getValue();
                break;
            case TASK:
                adjacentVertices = residualNetwork
                        .getMapFromSource()
                        .get(parentVertex);
                break;
            case EMPLOYEE:
                adjacentVertices = residualNetwork.getMapToSink().get(parentVertex);
                break;
        }

        Vertex vertexToReturn = null;
        if (adjacentVertices!=null) {
            a: for (Vertex childVertex : adjacentVertices.keySet()) {
                if (childVertex.getVertexType()==VertexType.SOURCE || adjacentVertices.get(childVertex)) {
                    continue a;
                } else if (childVertex.equals(FlowNetwork.SINK_VERTEX) || !adjacentVertices.get(childVertex)) {
                    vertexToReturn = childVertex;
                    //set the isVisited for the vertex to be true
                    adjacentVertices.put(childVertex, true);
                    break;
                }
            }
        }
        return vertexToReturn;
    }

    /**
     *
     * n-1 is length of the longest possible augmented path
     * Therfore O(n) is the running time for constructing residual network, where n is the number of nodes(employees + tasks + source a+ sink) and
     * @param
     */
    private void constructResidualNetwork(Map<Vertex, Vertex> augmentingPathBFS) {
        Vertex childVertex = FlowNetwork.SINK_VERTEX;
        Vertex parentVertex = childVertex;

        Stack path = new Stack();
        path.push(childVertex);
        while (parentVertex.getVertexType()!=VertexType.SOURCE) {
            parentVertex = (Vertex)augmentingPathBFS.get(childVertex);
            path.push(parentVertex);
            childVertex = parentVertex;
        }

        //path.forEach(vertex ->logger.trace(vertex));
        Queue<Vertex> augmentingPathQueue = new LinkedList<>();
        augmentingPathQueue.addAll(path);
        while (!augmentingPathQueue.isEmpty()) {
            childVertex = augmentingPathQueue.poll();
            parentVertex  = augmentingPathQueue.peek();
            updateNetworkEdges(parentVertex, childVertex);
            if (parentVertex.equals(FlowNetwork.SOURCE_VERTEX)) break;
        }

        residualNetwork.getSource().getValue().forEach((vertex, aBoolean) -> residualNetwork.getSource().getValue().put(vertex, false));
        residualNetwork.getMapFromSource().keySet().forEach(
                vertex-> {
                    Map<Vertex, Boolean> vertices = residualNetwork.getMapFromSource().get(vertex);
                    vertices.forEach((adVertex, isVisited)-> vertices.put(adVertex, false));
                });
        residualNetwork.getMapToSink().keySet().forEach(
                vertex-> {
                    Map<Vertex, Boolean> vertices = residualNetwork.getMapToSink().get(vertex);
                    vertices.forEach((adVertex, isVisited)-> vertices.put(adVertex, false));
                });
    }

    private void updateNetworkEdges(Vertex parentVertex, Vertex childVertex) {
        if (parentVertex.getVertexType()==VertexType.SOURCE && childVertex.getVertexType()==VertexType.TASK) {
            residualNetwork.getSource().getValue().remove(childVertex);
            residualNetwork.getMapFromSource().get(childVertex).put(parentVertex, false);
        } else if (parentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.EMPLOYEE) {
            residualNetwork.getMapFromSource().get(parentVertex).remove(childVertex);
            residualNetwork.getMapToSink().get(childVertex).put(parentVertex, false);
        } else if (parentVertex.getVertexType()==VertexType.EMPLOYEE && childVertex.getVertexType()==VertexType.TASK) {
            residualNetwork.getMapToSink().get(parentVertex).remove(childVertex);
            residualNetwork.getMapFromSource().get(childVertex).put(parentVertex, false);
        } else if (parentVertex.getVertexType()==VertexType.EMPLOYEE && childVertex.getVertexType()==VertexType.SINK) {
            residualNetwork.getMapToSink().get(parentVertex).remove(childVertex);
            residualNetwork.getSink().getValue().put(parentVertex, false);
        }//the last case will never be executed;
        else if (parentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.SOURCE) {
            residualNetwork.getMapFromSource().get(parentVertex).remove(childVertex);
            residualNetwork.getSource().getValue().put(parentVertex, false);
        }
    }
}
