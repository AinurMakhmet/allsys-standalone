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
 * Ford-Fulkerson algorithm finds largest matching possible for a given set of tasks.
 * The algorithm uses BFS to find the augmented path.
 */
public class FordFulkersonAlgorithm extends Strategy {
    private Map<Vertex, Map<Vertex, Boolean>> taskMap = new HashMap<>();
    private Map<Vertex, Map<Vertex, Boolean>> employeeMap = new HashMap<>();

    protected FlowNetwork residualNetwork;
    protected Queue<Vertex> augmentedPathQueue;
    protected Map<Vertex, Vertex> augmentedPathBFS;
    private Map<Vertex, Boolean> adjacentVertices;
    protected static final Vertex SOURCE_VERTEX = FlowNetwork.SOURCE_VERTEX;
    protected static final Vertex SINK_VERTEX = FlowNetwork.SINK_VERTEX;
    private int pathNumber;
    public Map<Vertex, Vertex> matching;
    protected Class strategyClass;
    Logger logger;

    private static FordFulkersonAlgorithm ourInstance = new FordFulkersonAlgorithm();

    public static FordFulkersonAlgorithm getInstance() {
        return ourInstance;
    }


    @Override
    public List<Task> allocate(List<Task> tasksToAllocate) {
        strategyClass = this.getClass();
        if (strategyClass.equals(FordFulkersonAlgorithm.class)) {
            logger = LocalServer.ffLogger;
        } else if (strategyClass.equals(MaximumProfitAlgorithm.class)) {
            logger = LocalServer.mpLogger;
        }
        recommendedAllocation = new LinkedList<>();
        numOfUnnalocatedTasks=tasksToAllocate.size();
        List<Task> remainingTasksToAllocate = tasksToAllocate;
        begTime = System.currentTimeMillis();
        while(remainingTasksToAllocate.size()>0) {
            if (canAllocateMoreTasks(remainingTasksToAllocate)) {
                remainingTasksToAllocate = doNextAllocationRound(remainingTasksToAllocate);
            } else {
                break;
            }
        }


        remainingTasksToAllocate.forEach(task -> {
            if (!recommendedAllocation.contains(task)) {
                recommendedAllocation.add(task);
            }
        });
        endTime = System.currentTimeMillis();
        logger.info(getClass().getSimpleName()+": Time for running algorithm: {} ms", (endTime-begTime));
        return recommendedAllocation;
    }

    protected boolean canAllocateMoreTasks(List<Task> remainingTasksToAllocate) {
        begTime = System.currentTimeMillis();
        residualNetwork = new FlowNetwork(new BipartiteGraph(strategyClass, remainingTasksToAllocate));
        endTime = System.currentTimeMillis();
        logger.info(getClass().getSimpleName()+": Time for constrcuting data structure: {} ms", (endTime-begTime));

        taskMap = residualNetwork.getMapFromSource();
        employeeMap = residualNetwork.getMapToSink();
        return residualNetwork.getSource().getValue().size()>0;
    }

    private List<Task> doNextAllocationRound(List<Task> remainingTasksToAllocate) {
        matching = new HashMap<>();
        augmentedPathQueue = new LinkedList<>();
        pathNumber = 0;
        //Starts constructing a path from the source;
        residualNetwork.printGraph();
        //TODO: BFS, DFS is non-deterministic!!!!!!!
        while (findAugmentingPathBFS()) {
            //logger.trace("Path Number "+ ++pathNumber);
            constructResidualNetwork();
            residualNetwork.printGraph();
        }
        findMatching();
        matching.forEach((a, b)-> {
            Task task = SystemData.getAllTasksMap().get(a.getVertexId());
            remainingTasksToAllocate.remove(task);
            Employee employee = SystemData.getAllEmployeesMap().get(b.getVertexId());
            task.setRecommendedAssignee(employee);
            numOfUnnalocatedTasks--;
            recommendedAllocation.add(task);
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

    /**
     *
     * n-1 is length of the longest possible augmented path
     * Therfore O(n) is the running time for constructing residual network, where n is the number of nodes(employees + tasks + source a+ sink) and
     * @param
     */
    private void constructResidualNetwork() {
        Vertex childVertex = SINK_VERTEX;
        Vertex parentVertex = childVertex;

        Stack path = new Stack();
        path.push(childVertex);
        while (parentVertex.getVertexType()!=VertexType.SOURCE) {
            parentVertex = (Vertex)augmentedPathBFS.get(childVertex);
            path.push(parentVertex);
            childVertex = parentVertex;
        }

        //path.forEach(vertex ->logger.trace(vertex));

        augmentedPathQueue.clear();
        augmentedPathQueue.addAll(path);
        while (!augmentedPathQueue.isEmpty()) {
            childVertex = augmentedPathQueue.poll();
            parentVertex  = augmentedPathQueue.peek();
            doAddDeleteVertices(parentVertex, childVertex);
            if (parentVertex.equals(SOURCE_VERTEX)) break;
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

    private void doAddDeleteVertices(Vertex parentVertex, Vertex childVertex) {
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
