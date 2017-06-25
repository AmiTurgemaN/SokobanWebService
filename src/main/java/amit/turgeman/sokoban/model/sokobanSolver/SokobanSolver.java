package amit.turgeman.sokoban.model.sokobanSolver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import amit.turgeman.sokoban.model.utilities.Utils;
import model.data.level.GeneralLevelLoaderCreator;
import model.data.level.Level;
import model.data.level.LevelCreator;
import model.data.util.Utilities;
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
	
	public void loadLevel()
	{
		try {
			String path = Utils.Path;
			LevelCreator lc = new LevelCreator(new FileInputStream(path+"\\Hash Maps\\"+"saveHashMap.obj"),new FileInputStream(path+"\\Hash Maps\\"+"loadHashMap.obj"));
			GeneralLevelLoaderCreator gllc = lc.getLoadHashMap().get(Utilities.getExtension(levelName));
			InputStream is = new FileInputStream(path+"\\Level Files\\"+levelName);
			sokobanModel.loadLevel(gllc.create(), is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		plannable=sokobanModel.readLevel();
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
		Level level = new Level();
		level.setLevelString(levelString);
		sokobanModel.setLevel(level);
		plannable=sokobanModel.readLevel();
	}
}
