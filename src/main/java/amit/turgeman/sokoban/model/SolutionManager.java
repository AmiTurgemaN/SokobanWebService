package amit.turgeman.sokoban.model;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class SolutionManager {

	private static SessionFactory factory;
	private HashMap<String, String> solutionHash;

	public SolutionManager() {
		Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
		Configuration configuration = new Configuration();
		configuration.configure();
		factory = configuration.buildSessionFactory();
		initSolutionHash();
	}

	public void addSolution(Solution solution) {
		solution.setSolution(CompressSolution(solution.getSolution()));
		this.solutionHash.put(solution.getLevelName(), solution.getSolution());
		Transaction tx = null;
		Session session = factory.openSession();
		try {
			tx = session.beginTransaction();
			session.save(new Solution(solution));
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private void initSolutionHash() {
		solutionHash=new HashMap<>();
		Session session = factory.openSession();
		try {
			Query<Solution> query = session.createQuery("from Solutions");
			List<Solution> list = query.list();
			for (Solution solution : list)
				solutionHash.put(solution.getLevelName(), solution.getSolution());
		}
		catch (HibernateException e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
	}
	
	public HashMap<String, String> getSolutionHash() {
		return solutionHash;
	}

	public void setSolutionHash(HashMap<String, String> solutionHash) {
		this.solutionHash = solutionHash;
	}

	public String CompressSolution(String solution)
	{
		if(solution.equals("Unsolvable"))
			return "x";
		String compressedString="";
		String [] solutionArray=solution.toLowerCase().split("\n");
		for(String s : solutionArray)
		{
			switch (s) {
			case "move left":
				compressedString+="l";
				break;
			case "move right":
				compressedString+="r";
				break;
			case "move down":
				compressedString+="d";
				break;
			case "move up":
				compressedString+="u";
				break;
			default:
				break;
			}
		}
		return compressedString;
	}

	public String DecompressSolution(String solution)
	{
		if(solution.equals("x"))
			return "Unsolvable";
		String decompressedString="";
		for(char c : solution.toLowerCase().toCharArray())
		{
			switch (c) {
			case 'l':
				decompressedString+="move left\n";
				break;
			case 'r':
				decompressedString+="move right\n";
				break;
			case 'd':
				decompressedString+="move down\n";
				break;
			case 'u':
				decompressedString+="move up\n";
				break;
			default:
				break;
			}
		}
		return decompressedString;
	}
}
