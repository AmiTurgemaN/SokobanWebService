package amit.turgeman.sokoban.model.sokobanSolver;

import java.io.PrintWriter;
import strips.Plannable;
import strips.Strips;

public class SokobanSolver {
	
	private SolverModel sokobanModel;
	private View sokobanView;
	private Plannable plannable;
	private Strips plan;
	private String levelName;

	public SokobanSolver(String levelName) {
		sokobanModel = new SolverModel();
		sokobanView = new SolverView();
		plan = new Strips();
		this.levelName=levelName;
	}
	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public SokobanSolver(Level level) {
		sokobanModel = new SolverModel();
		sokobanView = new SolverView();
		sokobanModel.setLevel(level);
		plannable=sokobanModel.readLevel();
		plan = new Strips();
	}
	
	public void solve(String destinationFile)
	{
		plan.plan(plannable);
		sokobanView.showSolution(plan.getActions(),"Level Files\\"+destinationFile);
	}
	
	public void solve(PrintWriter pw)
	{
		plan.plan(plannable);
		sokobanView.showSolution(plan.getActions(),pw);
	}
	
	public String solve()
	{
		plan.plan(plannable);
		String solution="";
		for(String s : plan.getActions())
			if(s.lastIndexOf("\n")!=-1)
				solution+=(s);
			else
				solution+=(s+"\n");
		if(solution.contains("Dead end"))
			return "Unsolvable";
		return solution;
	}
	
	public Strips getPlan() {
		return plan;
	}

	public void setPlan(Strips plan) {
		this.plan = plan;
	}
	
	public SolverModel getSokobanModel() {
		return sokobanModel;
	}

	public void setSokobanModel(SolverModel sokobanModel) {
		this.sokobanModel = sokobanModel;
	}

	public View getSokobanView() {
		return sokobanView;
	}

	public void setSokobanView(View sokobanView) {
		this.sokobanView = sokobanView;
	}

	public Plannable getPlannable() {
		return plannable;
	}

	public void setPlannable(Plannable plannable) {
		this.plannable = plannable;
	}

	public void loadLevel(String levelString) {
		sokobanModel.setLevel(new Level(levelString));
		plannable=sokobanModel.readLevel();
	}
}
