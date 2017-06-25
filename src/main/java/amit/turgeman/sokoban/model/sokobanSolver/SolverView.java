package amit.turgeman.sokoban.model.sokobanSolver;

import java.io.PrintWriter;
import java.util.List;

public class SolverView implements View {

	@Override
	public void showSolution(List<String> solution,String fileName) {
		
		try {
			PrintWriter w = new PrintWriter(fileName);
			for(String s : solution)
				if(s.lastIndexOf("\n")!=-1)
					w.write(s);
				else
					w.write(s+"\n");
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void showSolution(List<String> solution, PrintWriter w) {
		try {
			for(String s : solution)
				if(s.lastIndexOf("\n")!=-1)
					w.write(s);
				else
					w.write(s+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		w.flush();
	}
}
