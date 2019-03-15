package cn.edu.ruc.domain;


public class FeatureKey {
	private int entityId;
	private int relationId;
	private int direction;
	
	public FeatureKey(int entityId, int relationId, int direction){
		setEntityId(entityId);
		setRelationId(relationId);
		setDirection(direction);
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof FeatureKey))
			return false;
		if(object == this)
			return true;
		return (entityId == ((FeatureKey) object).getEntityId()) && (relationId == ((FeatureKey) object).getRelationId()) && (direction == ((FeatureKey) object).getDirection());
	}
	
	public int hashCode(){
		return new Integer((entityId + relationId) * direction).hashCode();
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public int getRelationId() {
		return relationId;
	}

	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
