package amit.turgeman.sokoban.model.sokobanSolver;
import java.io.PrintWriter;
import java.util.List;

public interface View {

	public void showSolution(List<String> list, String fileName);
	
	public void showSolution(List<String> list, PrintWriter pw);
}
