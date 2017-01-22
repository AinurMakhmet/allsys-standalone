package logic;

import models.bipartite_matching.VertexType;
import models.bipartite_matching.*;
import models.bipartite_matching.Vertex;

import java.util.*;

/**
 *
 */
public class FordFulkersonAlgorithm extends AbstractAllocationAlgorithm {
    FlowNetwork residualNetwork = new FlowNetwork(new BipartiteGraph());
    FlowNetwork originalNetwork;
    //LinkedList<Pair<Integer, EdgeMetaData>> augmentingPath;
    Queue<Vertex> augmentedPath = new LinkedList<>();
    Set<Vertex> adjacentVertices;
    private final Integer SOURCE_ID = -1;
    private final Integer SINK_ID = -2;
    private Vertex sinkNode = new Vertex(SINK_ID, VertexType.SINK);
    private Vertex sourceNode = new Vertex(SOURCE_ID, VertexType.SOURCE);
    private int pathNumber = 0;
    //Vertex currentVertex;

    @Override
    public boolean allocate(FlowNetwork network) {

        originalNetwork = network;
        //Starts constructing a path from the source;
        residualNetwork.printGraph();
        //TODO: BFS, DFS is non-deterministic!!!!!!!
        while (findAugmentingPathDFS()) {
            System.out.println("Path Number "+ ++pathNumber);
            augmentedPath.forEach(vertex -> System.out.println(vertex.toString()));
            constructResidualNetwork();
            residualNetwork.printGraph();

        }
        return true;
    }

    private boolean findAugmentingPathDFS() {
        augmentedPath = new LinkedList<>();
        //augmentedPath.add(sourceNode);
        Stack<Vertex> traversalStack = new Stack<>();
        traversalStack.push(sourceNode);
        sourceNode.setVisited(true);

        while (!traversalStack.empty() && traversalStack.peek().getVertexType()!=VertexType.SINK) {
            Vertex currentVertex = traversalStack.peek();
            Vertex nextVertex = findUnvisitedChild(currentVertex);
            if (nextVertex==null || nextVertex.getVertexId()==null||nextVertex.getVertexType()==null) {
                traversalStack.pop();
            } else {
                nextVertex.setVisited(true);
                traversalStack.push(nextVertex);
            }
            if ((nextVertex==null || nextVertex.getVertexId()==null||nextVertex.getVertexType()==null ) && currentVertex.getVertexType()!=VertexType.SINK) {
                return false;
            }
        }
        augmentedPath.addAll(traversalStack);
        return true;
    }



    private Vertex findUnvisitedChild(Vertex currentVertex) {
        Vertex toReturn = null;
        switch (currentVertex.getVertexType()) {
            case SOURCE:
                adjacentVertices = residualNetwork.getSource().getValue();
                break;
            case TASK:
                adjacentVertices = residualNetwork.getTaskMap().get(currentVertex.getVertexId());
                break;
            case EMPLOYEE:
                adjacentVertices = residualNetwork.getEmployeeMap().get(currentVertex.getVertexId());
                break;
        }

        Vertex vertexToReturn = null;
        //Integer childVertexId = null;
        a: for (Vertex childVertex : adjacentVertices) {
            if (childVertex.getVertexType()==VertexType.SOURCE || childVertex.isVisited()) {
                continue a;
            } else if (childVertex.getVertexType()==VertexType.SINK) {
                vertexToReturn = childVertex;
                break;
            } else if (!childVertex.isVisited()) {
                vertexToReturn = childVertex;
                break;
            }
        }
        return vertexToReturn;
    }

    public void constructResidualNetwork() {
        while (!augmentedPath.isEmpty()) {
            Vertex currentVertex = augmentedPath.poll();
            currentVertex.setVisited(false);
            Vertex childVertex  = augmentedPath.peek();
            if (currentVertex.getVertexType()==VertexType.SOURCE && childVertex.getVertexType()==VertexType.TASK) {
                residualNetwork.getSource().getValue().remove(childVertex);
                residualNetwork.getTaskMap().get(childVertex.getVertexId()).add(currentVertex);
            } else if (currentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.EMPLOYEE) {
                residualNetwork.getTaskMap().get(currentVertex.getVertexId()).remove(childVertex);
                residualNetwork.getEmployeeMap().get(childVertex.getVertexId()).add(currentVertex);
            } else if (currentVertex.getVertexType()==VertexType.EMPLOYEE && childVertex.getVertexType()==VertexType.TASK) {
                residualNetwork.getEmployeeMap().get(currentVertex.getVertexId()).remove(childVertex);
                residualNetwork.getTaskMap().get(childVertex.getVertexId()).add(currentVertex);
            } else if (currentVertex.getVertexType()==VertexType.EMPLOYEE && childVertex.getVertexType()==VertexType.SINK) {
                residualNetwork.getEmployeeMap().get(currentVertex.getVertexId()).remove(childVertex);
                residualNetwork.getSink().getValue().add(currentVertex);
            }//the last case will never be executed;
            else if (currentVertex.getVertexType()==VertexType.TASK && childVertex.getVertexType()==VertexType.SOURCE) {
                residualNetwork.getTaskMap().get(currentVertex.getVertexId()).remove(childVertex);
                residualNetwork.getSource().getValue().add(currentVertex);
            }
        }
    }





    /*private Pair<Integer,EdgeMetaData> findUnvisitedChild() {
        if (currentVertexType==VertexType.SOURCE) {
            adjacentVertices = residualNetwork.getSource().getValue().getEdges();
        } else if (currentVertexType==VertexType.TASK) {
            adjacentVertices = residualNetwork.getTaskMap().get(currentVertexId).getEdges();
        } else if (currentVertexType==VertexType.EMPLOYEE) {
            adjacentVertices = residualNetwork.getEmployeeMap().get(currentVertexId).getEdges();
        } else if (currentVertexType==VertexType.SINK) {
            adjacentVertices = null;
        }
        int counter=0;
        int size = 0;
        if (adjacentVertices!=null) {
            size = adjacentVertices.keySet().size();
        }
        if(counter<size) {
            for (Integer childVertexId: adjacentVertices.keySet()) {
                this.childVertexId = childVertexId;
                EdgeMetaData edgeData = adjacentVertices.get(childVertexId);
                Integer edgeFlow = edgeData.getFlow();
                if (!edgeData.isVisited() && edgeFlow<edgeData.getCapacity()) {
                    //edgeData.setFlow(edgeFlow++);
                    if (currentVertexType==VertexType.SOURCE) {
                        edgeData.setFlow(edgeFlow++).setVisited(true);
                        edgeData.setCapacity(edgeData.getCapacity()-edgeFlow);
//                        residualNetwork.getTaskMap().get(this.childVertexId).getEdge(SOURCE_ID).setVisited(true);
                        currentVertexType = VertexType.TASK;
                        break;
                    } else if (currentVertexType==VertexType.TASK) {
                        residualNetwork.getTaskMap().get(currentVertexId).setDestinationVertexId(childVertexId);
                        edgeData.setFlow(edgeFlow++).setVisited(true);
                        edgeData.setCapacity(edgeData.getCapacity());
                        residualNetwork.getEmployeeMap().get(this.childVertexId).getEdge(currentVertexId).setVisited(true).setFlow(-edgeFlow);

                        currentVertexType = VertexType.EMPLOYEE;
                    } else if (currentVertexType==VertexType.EMPLOYEE) {
                        residualNetwork.getEmployeeMap().get(currentVertexId).setDestinationVertexId(childVertexId);
                        if (childVertexId==SINK_ID) {
                            edgeData.setFlow(edgeFlow++).setVisited(true);
                            edgeData.setCapacity(edgeData.getCapacity()-edgeFlow);
                            //residualNetwork.getSink().get(this.childVertexId).getEdge(currentVertexId).setVisited(true);

                            currentVertexType = VertexType.SINK;
                        } else {
                            edgeData.setFlow(edgeFlow--).setVisited(true);
                            edgeData.setCapacity(edgeData.getCapacity());
                            residualNetwork.getTaskMap().get(this.childVertexId).getEdge(currentVertexId).setVisited(true).setFlow(-edgeFlow);

                            currentVertexType = VertexType.TASK;
                        }

                    }
                    return new Pair<>(childVertexId, edgeData);
                }
                //counter++;
            }
        }
        return null;
    }
*//*
    private void setFlows(Pair<Integer, EdgeMetaData> vertex, Pair<Integer, EdgeMetaData> nextVertex) {
        if (currentVertexType==VertexType.SOURCE) {
            adjacentVertices = residualNetwork.getSource().getValue().getEdges();
            EdgeMetaData edgeData = adjacentVertices.get(childVertexId);
            Integer edgeFlow = edgeData.getFlow();
            edgeData.setFlow(edgeFlow++).setVisited(true);
            edgeData.setCapacity(edgeData.getCapacity()-edgeFlow);

            residualNetwork.getSource().getValue().setDestinationVertexId(childVertexId);
            currentVertexType = VertexType.TASK;

        } else if (currentVertexType==VertexType.TASK) {
            //System.out.print("root vertex id  = " + residualNetwork.getTaskMap().get(currentVertexId));
            adjacentVertices = residualNetwork
                    .getTaskMap()
                    .get(currentVertexId)
                    .getEdges();
            //currentVertexType = VertexType.EMPLOYEE;
            EdgeMetaData edgeData = adjacentVertices.get(childVertexId);
            Integer edgeFlow = edgeData.getFlow();
            edgeData.setFlow(edgeFlow++).setVisited(true);
            edgeData.setCapacity(edgeData.getCapacity()-edgeFlow);

            residualNetwork.getEmployeeMap().get(childVertexId).getEdges().get(currentVertexId).setFlow(-edgeFlow);

            residualNetwork.getTaskMap().get(currentVertexId).setDestinationVertexId(childVertexId);
            currentVertexType = VertexType.EMPLOYEE;
        } else if (currentVertexType==VertexType.EMPLOYEE) {
            //currentVertexType = VertexType.SINK;
            adjacentVertices = residualNetwork.getEmployeeMap().get(currentVertexId).getEdges();
            EdgeMetaData edgeData = adjacentVertices.get(childVertexId);
            Integer edgeFlow = edgeData.getFlow();
            residualNetwork.getEmployeeMap().get(currentVertexId).setDestinationVertexId(childVertexId);
            if (childVertexId==SINK_ID) {
                edgeData.setFlow(edgeFlow++).setVisited(true);
                edgeData.setCapacity(edgeData.getCapacity()-edgeFlow);
                //residualNetwork.getSink().get(this.childVertexId).getEdge(currentVertexId).setVisited(true);

                currentVertexType = VertexType.SINK;
            } else {
                edgeData.setFlow(edgeFlow--).setVisited(true);
                edgeData.setCapacity(edgeData.getCapacity());
                residualNetwork.getTaskMap().get(this.childVertexId).getEdge(currentVertexId).setVisited(true).setFlow(-edgeFlow);

                currentVertexType = VertexType.TASK;
            }

        } else if (currentVertexType==VertexType.SINK) {
            adjacentVertices = null;
*//*
            EdgeMetaData edgeData = adjacentVertices.get(childVertexId);
            Integer edgeFlow = edgeData.getFlow();
*//*

        }






        //nextVertex.getValue().setFlow(nextVertex.getValue().getFlow()+1);
        if (currentVertexType==VertexType.SOURCE) {
            //adjacentVertices.get(childVertexId).setFlow();

//                        residualNetwork.getTaskMap().get(this.childVertexId).getEdge(SOURCE_ID).setVisited(true);


        } else if (currentVertexType==VertexType.TASK) {
            edgeData.setFlow(edgeFlow++).setVisited(true);
            edgeData.setCapacity(edgeData.getCapacity());
            residualNetwork.getEmployeeMap().get(this.childVertexId).getEdge(currentVertexId).setVisited(true).setFlow(-edgeFlow);


        } else if (currentVertexType==VertexType.EMPLOYEE) {
            if (childVertexId == SINK_ID) {
                edgeData.setFlow(edgeFlow++).setVisited(true);
                edgeData.setCapacity(edgeData.getCapacity() - edgeFlow);
                //residualNetwork.getSink().get(this.childVertexId).getEdge(currentVertexId).setVisited(true);

                currentVertexType = VertexType.SINK;
            } else {
                edgeData.setFlow(edgeFlow--).setVisited(true);
                edgeData.setCapacity(edgeData.getCapacity());
                residualNetwork.getTaskMap().get(this.childVertexId).getEdge(currentVertexId).setVisited(true).setFlow(-edgeFlow);

                currentVertexType = VertexType.TASK;
            }

        }
    }
*/
/*
    private  void clearVisited() {
        residualNetwork.getTaskMap().values()
                .forEach(payload-> payload.getEdges().values()
                        .forEach(edgeData-> edgeData.setVisited(false)));

        residualNetwork.getEmployeeMap().values()
                .forEach(payload->payload.getEdges().values()
                        .forEach(edgeData-> edgeData.setVisited(false)));

        residualNetwork.getSource().getValue().getEdges().values()
                .forEach(edgeData-> edgeData.setVisited(false));
    }
*/
    /*public void printTraversalStack() {
        traversalStack.forEach(
                pair -> {
                    System.out.println("STACK STARTS");
                    System.out.println(currentVertex.getVertexType()+ "vertexId = "+currentVertex.getVertexId());
                    System.out.println("STACK ENDS");
                });
    }*/

    /* private LinkedList findAugmentingPathBFS() {
        System.out.println("Augementing path is:");
        augmentingPath = new LinkedList<>();
        augmentedPath.add(sourceNode);
        currentVertexType = VertexType.SOURCE;
        sourceNode.getValue().setVisited(true);
        augmentingPath.add(sourceNode);
        System.out.println("source");

        adjacentVertices = residualNetwork.getSource().getValue().getEdges();
        while (!augmentedPath.isEmpty()) {
            Pair<Integer, EdgeMetaData> edge = augmentedPath.poll();
            Pair<Integer, EdgeMetaData> nextEdge;
            if ((nextEdge=findUnvisitedChild(edge.getKey(), currentVertexType))!=null) {
                nextEdge.getValue().setVisited(true);
                augmentingPath.add(nextEdge);
                System.out.println("printing node" + nextEdge.getKey());
                augmentedPath.add(nextEdge);
            }
        }
        return augmentingPath;
    }*/


}
