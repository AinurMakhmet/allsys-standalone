package logic;

import javafx.util.Pair;
import models.Task;
import models.bipartite_matching.BipartiteGraph;
import models.bipartite_matching.FlowNetwork;
import models.bipartite_matching.Vertex;
import models.bipartite_matching.VertexType;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by nura on 01/04/17.
 */
public abstract class MaximumFlowAlgorithm implements Strategy{
    long begTime;
    long endTime;
    int numOfUnallocatedTasks =0;
    FlowNetwork residualNetwork;
    Map<Vertex, Vertex> matching;

    Class strategyClass;
    Logger logger;

    Map<Vertex, Pair<Vertex, Integer>> shortestPathMap;
    Map<Vertex, Vertex> augmentingPathBFS;

    /**
     * Compute allocation for a list of tasks provided as an a parameter
     * @param tasksToAllocate a list of tasks selected for allocation
     */
    @Override
    public void allocate(List<Task> tasksToAllocate) {
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
        //logger.info("Time for running algorithm: {} ms", (endTime-begTime));
    }

    boolean canAllocateMoreTasks(List<Task> remainingTasksToAllocate) {
        begTime = System.currentTimeMillis();
        residualNetwork = new FlowNetwork(new BipartiteGraph(strategyClass, remainingTasksToAllocate));
        endTime = System.currentTimeMillis();
        //logger.info("Time for constrcuting data structure: {} ms", (endTime-begTime));
        return residualNetwork.getSource().getValue().size()>0;
    }

    void findMatching() {
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

    List<Task> runAllocationRound(List<Task> remainingTasksToAllocate){
        return null;
    };

    Vertex findUnvisitedChild(Vertex parentVertex){
        return null;
    }

    /**
     *
     * n-1 is length of the longest possible augmented path
     * Therfore O(n) is the running time for constructing residual network, where n is the number of nodes(employees + tasks + source a+ sink) and
     * @param
     */
    void constructResidualNetwork() {
        Vertex childVertex = FlowNetwork.SINK_VERTEX;
        Vertex parentVertex = childVertex;

        Stack path = new Stack();
        path.push(childVertex);
        while (parentVertex.getVertexType()!=VertexType.SOURCE) {
            if (strategyClass.equals(EdmondsKarpStrategy.class)) {
                parentVertex = (Vertex) augmentingPathBFS.get(childVertex);
            } else if (strategyClass.equals(MaximumProfitStrategy.class)) {
                parentVertex = (Vertex) shortestPathMap.get(childVertex).getKey();
            }
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

    void updateNetworkEdges(Vertex parentVertex, Vertex childVertex) {
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
            if (strategyClass.equals(EdmondsKarpStrategy.class)) {
                residualNetwork.getMapToSink().get(parentVertex).remove(childVertex);
            } else if (strategyClass.equals(MaximumProfitStrategy.class)){
                residualNetwork.getMapToSink().get(parentVertex).put(childVertex, false);
            }
            residualNetwork.getSink().getValue().put(parentVertex, false);
        } else if (parentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.SOURCE) {
            residualNetwork.getMapFromSource().get(parentVertex).remove(childVertex);
            residualNetwork.getSource().getValue().put(parentVertex, false);
        }
    }

    /**
     *
     * @return number of unallocated tasks
     */
    @Override
    public int getNumberOfUnallocatedTasks() {
        return numOfUnallocatedTasks;
    }
}
