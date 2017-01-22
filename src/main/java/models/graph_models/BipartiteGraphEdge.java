/*
package models.graph_models;

import models.Employee;
import models.Task;
import models.bipartite_matching.Vertex;
import models.bipartite_matching.VertexType;

*/
/**
 * Created by nura on 18/01/17.
 *//*

public class BipartiteGraphEdge {
    protected Vertex end1;
    protected Vertex end2;

    public BipartiteGraphEdge(Vertex node1, Vertex node2) {
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
        if (end1.getNodeType()== VertexType.TASK) {
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
        if (end1.getNodeType()== VertexType.EMPLOYEE) {
            return (Employee) end1;
        }
        return (Employee) end2;
    }

    public Task getTask() {
        if (end1.getNodeType()== VertexType.TASK) {
            return (Task) end1;
        }
        return (Task) end2;
    }
}
*/
