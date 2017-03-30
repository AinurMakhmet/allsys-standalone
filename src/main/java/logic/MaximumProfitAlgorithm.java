package logic;

import javafx.util.Pair;
import models.Employee;
import models.Project;
import models.SystemData;
import models.Task;
import models.bipartite_matching.*;
import servers.LocalServer;

import java.io.IOException;
import java.util.*;

/**
 * Created by nura on 27/02/17.
 */
public class MaximumProfitAlgorithm extends EdmondsKarpStrategy {
    //private FlowNetwork residualNetwork;
    //private Queue<Vertex> augmentedPathQueue;
    private Map<Vertex, Pair<Vertex, Integer>> shortestPathMap;
    private Map<Vertex, Boolean> adjacentVertices;
    //public Map<Vertex, Vertex> matching;
    private int profit;
    private int projectPrimeCost;
    private Project projectToAllocate;

    private static MaximumProfitAlgorithm ourInstance = new MaximumProfitAlgorithm();

    public static MaximumProfitAlgorithm getInstance() {
        return ourInstance;
    }

    public boolean allocateByProject(Project projectToAllocate) throws IOException {
        strategyClass = this.getClass();
        logger = LocalServer.mpLogger;
        boolean isFullyAllocated = false;
        this.projectToAllocate = projectToAllocate;
        List<Task> remainingTasksToAllocate = null;
        remainingTasksToAllocate = projectToAllocate.getTasks();
        numOfUnnalocatedTasks=remainingTasksToAllocate.size();

        begTime = System.currentTimeMillis();
        while(remainingTasksToAllocate.size()>0) {
            if (canAllocateMoreTasks(remainingTasksToAllocate)) {
                remainingTasksToAllocate = doNextAllocationRound(remainingTasksToAllocate);
            } else {
                break;
            }
        }

        endTime = System.currentTimeMillis();
        LocalServer.mpLogger.info(getClass().getSimpleName()+": Time for running algorithm: {} ms", (endTime-begTime));
        if (remainingTasksToAllocate.size()>0) {
            isFullyAllocated=false;
            for(Task task: projectToAllocate.getTasks()) {
                task.setRecommendedAssignee(null);
            }
        } else {
            isFullyAllocated=true;
            for(Task t: projectToAllocate.getTasks()) {
                Task task = SystemData.getAllTasksMap().get(t.getId());
                assert(task.getRecommendedAssignee()!=null);
                projectPrimeCost += task.getRecommendedAssignee().getDailySalary();
            }
            profit = projectToAllocate.getPrice() - projectPrimeCost;
            LocalServer.mpLogger.trace("Max profit for the project = {}", profit);

        }
        projectToAllocate.setEstimatedCost(projectPrimeCost);
        projectToAllocate.setEstimatedProfit(profit);
        return isFullyAllocated;
    }

    /*private boolean canAllocateMoreTasks(List<Task> remainingTasksToAllocate) {
        begTime = System.currentTimeMillis();
        residualNetwork = new FlowNetwork(new BipartiteGraph(this.getClass(), remainingTasksToAllocate));
        endTime = System.currentTimeMillis();
        LocalServer.mpLogger.info(getClass().getSimpleName()+": Time for constrcuting data structure: {} ms", (endTime-begTime));
        return residualNetwork.getSource().getValue().size()>0;
    }*/

    private List<Task> doNextAllocationRound(List<Task> remainingTasksToAllocate) {
        matching = new HashMap<>();
        augmentedPathQueue = new LinkedList<>();
        //Starts constructing a path from the source;
        residualNetwork.printGraph();
        //TODO: compute a shortest-path tree T in G from v to all nodes reachable from v;
        while (findShortestAugmentingPath()) {
            constructResidualNetwork();
            residualNetwork.printGraph();
            findMatching();
        }
        residualNetwork.printGraph();

        matching.forEach((a, b)-> {
            Task task = SystemData.getAllTasksMap().get(a.getVertexId());
            remainingTasksToAllocate.remove(task);
            Employee employee = SystemData.getAllEmployeesMap().get(b.getVertexId());
            task.setRecommendedAssignee(employee);
            numOfUnnalocatedTasks--;
            LocalServer.mpLogger.trace("{} is matched to {}", task.getName(), employee.getFirstName());
        });
        return remainingTasksToAllocate;
    }

    /**
     * uses Bellman-Ford algorithm to find the shortest-paths algorithm
     * @return
     */
    private boolean findShortestAugmentingPath() {
        shortestPathMap = new HashMap<>();
        bellmanFordInitialization();
        Queue<Vertex> traversalQueue = new LinkedList<>();
        traversalQueue.add(FlowNetwork.SOURCE_VERTEX);
        Vertex childVertex = FlowNetwork.SOURCE_VERTEX;

        while (!traversalQueue.isEmpty()) {
            Vertex parentVertex = traversalQueue.remove();
            while((childVertex=findUnvisitedChild(parentVertex))!=null) {
                traversalQueue = doBellmanFordRelax(parentVertex, childVertex, traversalQueue);
            }
        }
        if (shortestPathMap.containsKey(FlowNetwork.SINK_VERTEX) && shortestPathMap.get(FlowNetwork.SINK_VERTEX).getKey()!=null) return true;
        return false;
    }

    private Queue<Vertex> doBellmanFordRelax(Vertex parentVertex, Vertex childVertex, Queue<Vertex> traversalQueue) {
        Integer shortestPathWeightChild = shortestPathMap.get(childVertex).getValue();
        Integer shortestPathWeightParent = shortestPathMap.get(parentVertex).getValue();
        int cost = 0;
        if (parentVertex.getVertexType()==VertexType.SOURCE && childVertex.getVertexType()==VertexType.TASK) {
            cost =1;
        } else if (parentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.EMPLOYEE) {
            cost = childVertex.getCost();
        } else if (parentVertex.getVertexType()==VertexType.EMPLOYEE && childVertex.getVertexType()==VertexType.TASK) {
            cost = -parentVertex.getCost();
        } else if (parentVertex.getVertexType()==VertexType.EMPLOYEE && childVertex.getVertexType()==VertexType.SINK) {
            cost =1;
        }//the last case will never be executed;
        else if (parentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.SOURCE) {
            cost =1;
        }
        if (shortestPathWeightChild>shortestPathWeightParent+cost) {
            shortestPathWeightChild = shortestPathWeightParent+cost;
            shortestPathMap.put(childVertex, new Pair(parentVertex, shortestPathWeightChild));
            if (!traversalQueue.contains(childVertex)) {
                traversalQueue.add(childVertex);
            }
        }
        return traversalQueue;
    }

    private void bellmanFordInitialization() {
        shortestPathMap.put(FlowNetwork.SOURCE_VERTEX, new Pair(null, 0));
        residualNetwork.getMapFromSource().keySet().forEach(vertex-> {
            shortestPathMap.put(vertex, new Pair(null, Integer.MAX_VALUE));
        });
        residualNetwork.getMapToSink().keySet().forEach(vertex-> {
            shortestPathMap.put(vertex, new Pair(null, Integer.MAX_VALUE));
        });
        shortestPathMap.put(FlowNetwork.SINK_VERTEX, new Pair(null, Integer.MAX_VALUE));
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
                } else if (parentVertex.getVertexType().equals(VertexType.TASK)
                        && childVertex.getVertexType().equals(VertexType.EMPLOYEE)) {
                    Task taskToMatch = SystemData.getAllTasksMap().get(parentVertex.getVertexId());
                    /*for (Task matchedTask: SystemData.getAllEmployeesMap().get(childVertex.getVertexId()).getMatchedTasks()) {
                        if (taskToMatch.timeOverlapWith(matchedTask)) {
                            adjacentVertices.put(childVertex, true);
                            continue;
                        }
                    }*/
                    for (Vertex vertex : matching.keySet()) {
                        Task matchedTask = SystemData.getAllTasksMap().get(vertex.getVertexId());
                        if (taskToMatch.timeOverlapWith(matchedTask)) {
                            adjacentVertices.put(childVertex, true);
                            continue;
                        }
                    }
                }
                if (childVertex.equals(FlowNetwork.SINK_VERTEX) || !adjacentVertices.get(childVertex)) {
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
        Vertex childVertex = FlowNetwork.SINK_VERTEX;
        Vertex parentVertex = childVertex;

        Stack path = new Stack();
        path.push(childVertex);
        while (parentVertex.getVertexType()!=VertexType.SOURCE) {
            parentVertex = (Vertex)shortestPathMap.get(childVertex).getKey();
            path.push(parentVertex);
            childVertex = parentVertex;
        }

        //path.forEach(vertex ->LocalServer.ekLogger.trace(vertex));

        augmentedPathQueue.clear();
        augmentedPathQueue.addAll(path);
        while (!augmentedPathQueue.isEmpty()) {
            childVertex = augmentedPathQueue.poll();
            parentVertex  = augmentedPathQueue.peek();
            doAddDeleteVertices(parentVertex, childVertex);
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
            residualNetwork.getMapToSink().get(parentVertex).put(childVertex, false);
            residualNetwork.getSink().getValue().put(parentVertex, false);
        } else if (parentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.SOURCE) {
            residualNetwork.getMapFromSource().get(parentVertex).remove(childVertex);
            residualNetwork.getSource().getValue().put(parentVertex, false);
        }
    }

    public int getProfit() {
        return profit;
    }

}
