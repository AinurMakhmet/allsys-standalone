package models.bipartite_matching;

import models.SystemData;

import java.util.Comparator;

/**
 * Created by nura on 15/01/17.
 */
public class Vertex {
    private Integer vertexId;
    private VertexType vertexType;
    private int shortestPathWeightFromSource;
    private int cost;

    public Vertex() {

    }

    public Vertex(Integer vertexId, VertexType vertexType) {
        this.vertexId = vertexId;
        this.vertexType = vertexType;
    }

    public Integer getVertexId() {
        return vertexId;
    }

    public void setVertexId(Integer vertexId) {
        this.vertexId = vertexId;
    }

    public VertexType getVertexType() {
        return vertexType;
    }

    public Vertex setVertexType(VertexType type) {
        vertexType = type;
        return this;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getShortestPathWeightFromSource() {
        return shortestPathWeightFromSource;
    }

    public void setShortestPathWeightFromSource(int shortestPathWeightFromSource) {
        this.shortestPathWeightFromSource = shortestPathWeightFromSource;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "vertexType=" + vertexType+
                ", vertexId=" + vertexId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (!getVertexId().equals(vertex.getVertexId())) return false;
        return getVertexType() == vertex.getVertexType();

    }

    @Override
    public int hashCode() {
        int result = getVertexId().hashCode();
        result = 31 * result + getVertexType().hashCode();
        return result;
    }

    public static class VertexComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex v1, Vertex v2) {
            Integer priorityT1= SystemData.getAllTasksMap().get(v1.getVertexId()).getPriorityNumber();
            Integer priorityT2=SystemData.getAllTasksMap().get(v2.getVertexId()).getPriorityNumber();
            return priorityT1.compareTo(priorityT2);
        }
    }
}

