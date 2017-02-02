package models.bipartite_matching;

/**
 * Created by nura on 15/01/17.
 */
public class Vertex {
    private Integer vertexId;
    private VertexType vertexType;

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

        if (!getVertexId().equals(vertex.getVertexId())) return false;
        return getVertexType() == vertex.getVertexType();

    }

    @Override
    public int hashCode() {
        int result = getVertexId().hashCode();
        result = 31 * result + getVertexType().hashCode();
        return result;
    }
}

