package cn.edu.ruc.domain;

import java.text.DecimalFormat;
import java.util.Set;

public class Entity {
	private int id;
	private String name;
	private double score;
	private int rank;
	private Description description;
	private Set<Feature> featureSet;

	public Entity(int id){
		setId(id);
		setScore(1);
	}

	public Entity(int id, double score){
		setId(id);
		setScore(score);
	}

	public Entity(int id, String name){
		setId(id);
		setName(name);
		setScore(1);
	}

	public Entity(int id, String name, double score){
		setId(id);
		setName(name);
		setScore(score);
	}

	@Override
	public boolean equals(Object object){
		if(!(object instanceof Entity))
			return false;
		if(object == this)
			return true;

		if(getId() != ((Entity) object).getId())
			return false;

		return true;
	}

	public int hashCode(){
		int value = getId();

		return value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	public Set<Feature> getFeatureSet() {
		return featureSet;
	}

	public void setFeatureSet(Set<Feature> featureSet) {
		this.featureSet = featureSet;
	}

	public void addFeature(Feature feature) {
		featureSet.add(feature);
	}

	@Override
	public String toString() {
		String s = "Entity{" +
				/*"id=" + id +*/
				"name='" + name + '\'' +
				", score=" + new DecimalFormat("0.000").format(score) +
				(rank == 0 ? "" : ", rank=" + rank);

		if(featureSet != null) {
			for(Feature feature : featureSet)
				s += "\n\t\t" + feature;
		}

		if(description != null) {
			s += "\n\t\t" + description;
		}

		s += '}';

		return s;
	}
}
