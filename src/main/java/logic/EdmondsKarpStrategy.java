package logic;

import models.Employee;
import models.SystemData;
import models.Task;
import models.bipartite_matching.VertexType;
import models.bipartite_matching.*;
import models.bipartite_matching.Vertex;
import servers.LocalServer;

import java.util.*;

/**
 * Algorithm finds largest matching possible for a given set of tasks.
 * The algorithm based on Edmonds-Karp method (does breadth-first-search to find augmenting path).
 */
public class EdmondsKarpStrategy extends MaximumFlowAlgorithm {
    private static EdmondsKarpStrategy ourInstance = new EdmondsKarpStrategy();

    public static EdmondsKarpStrategy getInstance() {
        return ourInstance;
    }

    @Override
    public void allocate(List<Task> tasksToAllocate) {
        strategyClass = this.getClass();
        logger = LocalServer.ekLogger;
        super.allocate(tasksToAllocate);
    }

    @Override
    List<Task> runAllocationRound(List<Task> remainingTasksToAllocate) {
        matching = new HashMap<>();
        //Starts constructing a path from the source;
        residualNetwork.printGraph();
        //TODO: BFS, DFS is non-deterministic!!!!!!!
        while (findAugmentingPathBFS()) {
            constructResidualNetwork();
            residualNetwork.printGraph();
        }
        findMatching();
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
     * O(m), where m is the number of edges in the residual graph.
     * @return
     */
    private boolean findAugmentingPathBFS() {
        augmentingPathBFS = new HashMap<>();
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
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    Vertex findUnvisitedChild(Vertex parentVertex) {
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


}
