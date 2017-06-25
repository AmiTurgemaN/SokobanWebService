package amit.turgeman.sokoban.servlets;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import amit.turgeman.sokoban.model.Solution;
import amit.turgeman.sokoban.model.SolutionManager;
import amit.turgeman.sokoban.model.sokobanSolver.SokobanSolver;

@Path("/solutions")
public class SolutionServlet {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getSolutions()
	{
		String solutionsString="";
		SolutionManager sm = new SolutionManager();
		for(HashMap.Entry<String, String> entry : sm.getSolutionHash().entrySet())
			solutionsString+=entry.getKey()+":\n"+sm.DecompressSolution(entry.getValue())+"\n\n";
		return solutionsString;
	}
	
	@GET
	@Path("/{levelName}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSolution(@PathParam("levelName") String levelName)
	{
		levelName=levelName.toLowerCase();
		SolutionManager sm = new SolutionManager();
		String compressedSolution = sm.getSolutionHash().get(levelName); 
		if(compressedSolution!=null)
			return sm.DecompressSolution(compressedSolution);
		return "Level Does Not Exist";
	}

	@POST
	@Path("/{levelName}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public String getSolution(@PathParam("levelName") String levelName, String levelString)
	{
		SolutionManager sm = new SolutionManager();
		String compressedSolution = sm.getSolutionHash().get(levelName); 
		if(compressedSolution!=null)
			return sm.DecompressSolution(compressedSolution);
		else
		{
			SokobanSolver sokobanSolver = new SokobanSolver(levelName);
			sokobanSolver.loadLevel(levelString);
			String uncompressedSolution=sokobanSolver.solve();
			sm.addSolution(new Solution(levelName,uncompressedSolution));
			return uncompressedSolution;
		}
	}

}
