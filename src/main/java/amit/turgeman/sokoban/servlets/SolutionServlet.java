package amit.turgeman.sokoban.servlets;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import amit.turgeman.sokoban.model.Solution;
import amit.turgeman.sokoban.model.SolutionManager;
import amit.turgeman.sokoban.model.sokobanSolver.SokobanSolver;

@Path("/solutions")
public class SolutionServlet {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getSolutions()
	{
		String solutionsString="";
		SolutionManager sm = new SolutionManager();
		if(sm.getSolutionHash().size()!=0)
		{
			for(HashMap.Entry<String, String> entry : sm.getSolutionHash().entrySet())
				solutionsString+=entry.getKey()+":\n"+sm.DecompressSolution(entry.getValue())+"\n\n";
			return Response.status(200).entity(solutionsString).build();
		}
		else
		{
			return Response.status(500).entity("No solutions exist").build();
		}
		
	}
	
	@GET
	@Path("/{levelName}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getSolution(@PathParam("levelName") String levelName)
	{
		levelName=levelName.toLowerCase();
		SolutionManager sm = new SolutionManager();
		String compressedSolution = sm.getSolutionHash().get(levelName); 
		if(compressedSolution!=null)
			return Response.status(200).entity(sm.DecompressSolution(compressedSolution)).build();
		return Response.status(500).entity("Level not found").build();
	}

	@POST
	@Path("/{levelName}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response getSolution(@PathParam("levelName") String levelName, String levelString)
	{
		SolutionManager sm = new SolutionManager();
		String compressedSolution = sm.getSolutionHash().get(levelName); 
		if(compressedSolution!=null)
			return Response.status(200).entity(sm.DecompressSolution(compressedSolution)).build();
		else
		{
			SokobanSolver sokobanSolver = new SokobanSolver(levelName);
			sokobanSolver.loadLevel(levelString);
			String uncompressedSolution=sokobanSolver.solve();
			sm.addSolution(new Solution(levelName,uncompressedSolution));
			return Response.status(201).entity(uncompressedSolution).build();
		}
	}

}
