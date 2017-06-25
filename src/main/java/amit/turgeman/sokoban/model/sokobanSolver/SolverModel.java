package amit.turgeman.sokoban.model.sokobanSolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import strips.Action;
import strips.CheckPath;
import strips.Clause;
import strips.Plannable;
import strips.Predicate;
import strips.SokPredicate;

public class SolverModel implements Model{
	
	private Clause goal;
	private Clause kb;
	private Level level;
	
	public SolverModel() {
		super();
		goal=new Clause();
		kb=new Clause();
	}

	public Clause getKB(String levelString){
		int boxCount=0;
		String [] levelRows = levelString.split("\r\n");
		for(int i=0;i<levelRows.length;i++)
			for(int j=0;j<levelRows[i].length();j++)
			{
				switch(levelRows[i].charAt(j))
				{
				case '#':kb.add(new SokPredicate("wallAt", "", i+","+j));break;
				case ' ':kb.add(new SokPredicate("clearAt", "", i+","+j));break;
				case 'A':kb.add(new SokPredicate("characterAt","", i+","+j));break;
				case '@':boxCount++;kb.add(new SokPredicate("boxAt", "b"+boxCount, i+","+j));break;
				case '$':boxCount++;kb.add(new SokPredicate("boxAt", "b"+boxCount, i+","+j));
						goal.add(new SokPredicate("boxAt", "?", i+","+j));
				case 'B':kb.add(new SokPredicate("characterAt","", i+","+j));
						goal.add(new SokPredicate("boxAt", "?", i+","+j));
				case 'o':goal.add(new SokPredicate("boxAt", "?", i+","+j));
						kb.add(new SokPredicate("clearAt","", i+","+j));break;
				}
			}
		return kb;		
	}
	
	public Plannable readLevel(){
		try{
			ArrayList<char[]> level=new ArrayList<>();
			String [] LevelRows = this.level.getLevelString().split("\n");
			for(String row : LevelRows)
				level.add(row.toCharArray());
			final Clause kb=getKB(this.level.getLevelString());
			
			Plannable plannable=new Plannable() {
				
				@Override
				public Set<Action> getsatisfyingActions(Predicate top) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Action getsatisfyingAction(Predicate top) {
					
					String id=top.getId();
					String value=top.getValue();

					
					switch(top.getType()){
					case "boxAt":
						Action chosen=null;
						String[] xy=value.split(",");
						int x=Integer.parseInt(xy[0]);
						int y=Integer.parseInt(xy[1]);
						String valueRight=x+","+(y-1);
						String valueLeft=x+","+(1+y);
						String valueUp=(x+1)+","+y;
						String valueDown=(x-1)+","+y;
						 List<Action> list=new ArrayList<>();
						Action right=new Action("pushRight", id, valueRight);
						if((getKnowledgebase().satisfies(new Predicate("clearAt", "", value))
								|| getKnowledgebase().satisfies(new Predicate("characterAt", "", x+","+(y-2))))
								&&((getKnowledgebase().satisfies(new Predicate("characterAt", "", value)) ||
										getKnowledgebase().satisfies(new Predicate("clearAt", "", x+","+(y-2))))))
							list.add(right);
						Action left=new Action("pushLeft", id, valueLeft);
						if((getKnowledgebase().satisfies(new Predicate("clearAt", "", value))
								|| getKnowledgebase().satisfies(new Predicate("characterAt", "", x+","+(y+2))))
								&&((getKnowledgebase().satisfies(new Predicate("characterAt", "", value)) ||
										getKnowledgebase().satisfies(new Predicate("clearAt", "", x+","+(y+2))))))
							list.add(left);
						Action up=new Action("pushUp", id, valueUp);
						if((getKnowledgebase().satisfies(new Predicate("clearAt", "", value))
								|| getKnowledgebase().satisfies(new Predicate("characterAt","", (x+2)+","+y)))
								&&((getKnowledgebase().satisfies(new Predicate("characterAt","", value)) ||
										getKnowledgebase().satisfies(new Predicate("clearAt","", (x+2)+","+y)))))
							list.add(up);
						Action down=new Action("pushDown", id, valueDown);
						if((getKnowledgebase().satisfies(new Predicate("clearAt","", value))
								|| getKnowledgebase().satisfies(new Predicate("characterAt","", (x-2)+","+y)))
								&&((getKnowledgebase().satisfies(new Predicate("characterAt","", value)) ||
										getKnowledgebase().satisfies(new Predicate("clearAt","", (x-2)+","+y)))))
							list.add(down);
						Collections.sort(list,new Comparator<Action>() {
							@Override
							public int compare(Action a2,Action a1){
							
								return a1.getPreconditions().numOfSatisfied(getKnowledgebase())-a2.getPreconditions().numOfSatisfied(getKnowledgebase());
								
							}
						});
						if(!list.isEmpty()){
						chosen=list.get(0);
						}

						return chosen;
						
					case "characterAt":
						//getting character location
						Predicate p=getKnowledgebase().searchCharacter();
						return new Action("move",id,p.getValue()+" "+value);
					case "path":
						return new CheckPath("checkPath","", value,getKnowledgebase());
					case "clearAt":
						System.out.println("clearAt deadend"+"value "+value);
						return new Action("deadEnd", id, value);
						//return null;
					}
					
					
					return null;
				}
				
				@Override
				public Clause getKnowledgebase() {
					return kb;
				}
				
				@Override
				public Clause getGoal() {
					return goal;
				}
			};			
			return plannable;
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Clause getGoal() {
		return goal;
	}

	public void setGoal(Clause goal) {
		this.goal = goal;
	}

	@Override
	public Level getLevel() {
		return level;
	}

	@Override
	public void setLevel(Level level) {
		this.level=level;
	}
	
}
