package amit.turgeman.sokoban.model;

import javax.persistence.*;

@Entity(name="Solutions")
public class Solution {
	@Id
	private String levelName;
	private String solution;

	
	public Solution() {
	}

	public Solution(String levelName, String solution) {
		super();
		this.levelName = levelName;
		this.solution = solution;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Solution(String levelName) {
		this.levelName = levelName;
	}

	public Solution(Solution solution) {
		this.levelName=solution.levelName;
		this.solution=solution.solution;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	
}
