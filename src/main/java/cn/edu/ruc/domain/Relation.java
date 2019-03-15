package cn.edu.ruc.domain;

import java.text.DecimalFormat;

public class Relation {
	private int id;
	private int direction;
	private String name;
	private double score;

	public Relation(int id, int direction){
		setId(id);
		setDirection(direction);
		setScore(1);
	}

	public Relation(int id, int direction, double score){
		setId(id);
		setDirection(direction);
		setScore(score);
	}

	public Relation(int id, int direction, String name){
		setId(id);
		setDirection(direction);
		setName(name);
		setScore(1);
	}

	public Relation(int id, int direction, String name, double score){
		setId(id);
		setDirection(direction);
		setName(name);
		setScore(score);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
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

	@Override
	public String toString() {
		return "Relation{" +
				/*"id=" + id +*/
				"direction=" + direction +
				", name='" + name + '\'' +
				", score=" + new DecimalFormat("0.000").format(score) +
				'}';
	}
}
