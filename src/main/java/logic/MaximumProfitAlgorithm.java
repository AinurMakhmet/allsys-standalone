package logic;

import javafx.util.Pair;
import models.Employee;
import models.Project;
import models.SystemData;
import models.Task;
import models.bipartite_matching.*;
import org.junit.Assert;
import servers.LocalServer;

import java.io.IOException;
import java.util.*;

/**
 * Created by nura on 27/02/17.
 */
public class MaximumProfitAlgorithm extends MaximumFlowAlgorithm implements Strategy{
    List<Task> result;
    private static MaximumProfitAlgorithm ourInstance = new MaximumProfitAlgorithm();

    public static MaximumProfitAlgorithm getInstance() {
        return ourInstance;
    }

    /*public boolean allocateByProject(Project projectToAllocate) throws IOException {
        strategyClass = this.getClass();
        logger = LocalServer.mpLogger;
        boolean isFullyAllocated = false;
        List<Task> remainingTasksToAllocate = projectToAllocate.getTasks();
        numOfUnallocatedTasks =remainingTasksToAllocate.size();

        begTime = System.currentTimeMillis();
        while(remainingTasksToAllocate.size()>0) {
            if (canAllocateMoreTasks(remainingTasksToAllocate)) {
                remainingTasksToAllocate = runAllocationRound(remainingTasksToAllocate);
            } else {
                break;
            }
        }
        endTime = System.currentTimeMillis();
        logger.info(getClass().getSimpleName()+": Time for running algorithm: {} ms", (endTime-begTime));

        Integer profit = 0;
        Integer projectPrimeCost = 0;
        if (remainingTasksToAllocate.size()>0) {
            isFullyAllocated=false;
            for(Task task: projectToAllocate.getTasks()) {
                task.setRecommendedAssignee(null);
            }
        } else {
            isFullyAllocated=true;
            for(Task t: projectToAllocate.getTasks()) {
                Task task = SystemData.getAllTasksMap().get(t.getId());
                Assert.assertNotNull(task);
                Assert.assertNotNull(task.getRecommendedAssignee());
                projectPrimeCost += task.getRecommendedAssignee().getDailySalary()*task.getDuration();
            }
            profit = projectToAllocate.getPrice() - projectPrimeCost;
            logger.trace("Max profit for the project = {}", profit);

        }
        projectToAllocate.setEstimatedCost(projectPrimeCost);
        projectToAllocate.setEstimatedProfit(profit);
        return isFullyAllocated;
    }*/

    @Override
    public List<Task> allocate(List<Task> tasksToAllocate) {
        strategyClass = this.getClass();
        logger = LocalServer.mpLogger;
        result = new LinkedList<>();
        List<Task> remainingTasksToAllocate = tasksToAllocate;
        numOfUnallocatedTasks=remainingTasksToAllocate.size();

        begTime = System.currentTimeMillis();
        while(remainingTasksToAllocate.size()>0) {
            if (canAllocateMoreTasks(remainingTasksToAllocate)) {
                remainingTasksToAllocate = runAllocationRound(remainingTasksToAllocate);
            } else {
                break;
            }
        }
        endTime = System.currentTimeMillis();
        logger.info(getClass().getSimpleName()+": Time for running algorithm: {} ms", (endTime-begTime));
        return result;

    }

    @Override
    public int getNumberOfUnallocatedTasks() {
        return numOfUnallocatedTasks;
    }

    @Override
    List<Task> runAllocationRound(List<Task> remainingTasksToAllocate) {
        matching = new HashMap<>();
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
            numOfUnallocatedTasks--;
            logger.trace("{} is matched to {} {}", task.getName(), employee.getFirstName(), employee.getLastName());
        });
        return remainingTasksToAllocate;
    }

    /**
     * uses Bellman-Ford algorithm to find the shortest-paths algorithm
     * @return
     */
    private boolean findShortestAugmentingPath() {
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
        shortestPathMap = new HashMap<>();
        shortestPathMap.put(FlowNetwork.SOURCE_VERTEX, new Pair(null, 0));
        residualNetwork.getMapFromSource().keySet().forEach(vertex-> {
            shortestPathMap.put(vertex, new Pair(null, Integer.MAX_VALUE));
        });
        residualNetwork.getMapToSink().keySet().forEach(vertex-> {
            shortestPathMap.put(vertex, new Pair(null, Integer.MAX_VALUE));
        });
        shortestPathMap.put(FlowNetwork.SINK_VERTEX, new Pair(null, Integer.MAX_VALUE));
    }

    @Override
    Vertex findUnvisitedChild(Vertex parentVertex) {
        Map<Vertex, Boolean> adjacentVertices = null;
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
}
