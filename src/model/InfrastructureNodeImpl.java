package model;

import java.util.Set;

import model.interfaces.ICoordinates;
import model.interfaces.IInfrastructureNodeImpl;

public class InfrastructureNodeImpl implements IInfrastructureNodeImpl{

  private String nodeID;
  private ICoordinates coordinates;
  private Set<IInfrastructureNodeImpl> nearNodes;
  
  public InfrastructureNodeImpl(String nodeID, Set<IInfrastructureNodeImpl> nearNodes) {
    super();
    this.nodeID = nodeID;
    this.nearNodes = nearNodes;
  }

  public InfrastructureNodeImpl(String nodeID, ICoordinates coordinates, Set<IInfrastructureNodeImpl> nearNodes) {
    super();
    this.nodeID = nodeID;
    this.coordinates = coordinates;
    this.nearNodes = nearNodes;
  }

  @Override
  public String getNodeID() {
    return this.nodeID;
  }

  @Override
  public ICoordinates getCoordinates() {
    return this.coordinates;
  }

  @Override
  public Set<IInfrastructureNodeImpl> getNearNodes() {
    return this.nearNodes;
  }

  @Override
  public void setNearNodes(IInfrastructureNodeImpl node) {
    this.nearNodes.add(node);
  }

  @Override
  public void setCoordinates(ICoordinates coordinates) {
    this.coordinates = coordinates;
  }

}