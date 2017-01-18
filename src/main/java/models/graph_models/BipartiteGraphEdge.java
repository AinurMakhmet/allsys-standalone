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

    public Flow convertToFlow() {
        Flow flow;
        if (end1.getNodeType()==BipartiteGraphNodeType.TASK) {
            flow = new Flow(end1, end2);
            end1.addOutcomingFlow(flow);
            end2.addIncomingFlow(flow);
        }
        flow = new Flow(end2, end1);
        end2.addOutcomingFlow(flow);
        end1.addIncomingFlow(flow);
        return flow;

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
