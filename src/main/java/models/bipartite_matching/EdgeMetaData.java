package models.bipartite_matching;

/**
 * Created by nura on 20/01/17.
 */
public class EdgeMetaData {
    private Integer capacity;
    private Integer flow;
    private boolean isVisited = false;

    public EdgeMetaData() {
    }

    public EdgeMetaData(Integer capacity) {
        this.capacity = capacity;
    }

    public EdgeMetaData(Integer capacity, Integer flow) {
        this.capacity = capacity;
        this.flow = flow;
    }

    public EdgeMetaData setVisited(boolean isVisited) {
        this.isVisited = isVisited;
        return this;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public EdgeMetaData setFlow(Integer flow) {
        this.flow = flow;
        return this;
    }

    public Integer getFlow() {
        return flow;
    }

    public EdgeMetaData setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "capacity="+capacity+ " and flow="+flow + " and visited "+ isVisited();
    }
}
