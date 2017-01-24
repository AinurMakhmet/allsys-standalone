package models.bipartite_matching;

/**
 * Created by nura on 15/01/17.
 */
public class Vertex {
    private Integer vertexId;
    private VertexType vertexType;
    private boolean visited;

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

    public boolean isVisited() {
        return visited;
    }

    public Vertex setVisited(boolean visited) {
        this.visited = visited;
        return this;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "vertexId=" + vertexId +
                ", vertexType=" + vertexType+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (isVisited() != vertex.isVisited()) return false;
        if (!getVertexId().equals(vertex.getVertexId())) return false;
        return getVertexType() == vertex.getVertexType();

    }

    @Override
    public int hashCode() {
        int result = getVertexId().hashCode();
        result = 31 * result + getVertexType().hashCode();
        result = 31 * result + (isVisited() ? 1 : 0);
        return result;
    }
}

