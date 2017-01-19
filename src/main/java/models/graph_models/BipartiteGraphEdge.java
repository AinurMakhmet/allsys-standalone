package models.graph_models;

import models.Employee;
import models.Task;

/**
 * Created by nura on 18/01/17.
 */
public class BipartiteGraphEdge {
    protected BipartiteGraphNode end1;
    protected BipartiteGraphNode end2;

    public BipartiteGraphEdge(BipartiteGraphNode node1, BipartiteGraphNode node2) {
        this.end1 = node1;
        this.end2 = node2;
        setEdgestoNodes();
    }

    private void setEdgestoNodes() {
        end1.addEdge(this);
        end2.addEdge(this);
    }

    public DirectedEdge convertToDirectedEdge() {
        DirectedEdge directedEdge;
        if (end1.getNodeType()==BipartiteGraphNodeType.TASK) {
            directedEdge = new DirectedEdge(end1, end2);
            end1.addOutcomingEdge(directedEdge);
            end2.addIncomingEdge(directedEdge);
        }
        directedEdge = new DirectedEdge(end2, end1);
        end2.addOutcomingEdge(directedEdge);
        end1.addIncomingEdge(directedEdge);
        return directedEdge;

    }

    public Employee getEmployee() {
        if (end1.getNodeType()==BipartiteGraphNodeType.EMPLOYEE) {
            return (Employee) end1;
        }
        return (Employee) end2;
    }

    public Task getTask() {
        if (end1.getNodeType()==BipartiteGraphNodeType.TASK) {
            return (Task) end1;
        }
        return (Task) end2;
    }
}
