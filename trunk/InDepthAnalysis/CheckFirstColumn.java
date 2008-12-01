import java.io.*;
import java.util.*;

public class CheckFirstColumn {

	public static void main(String[]  args) {
		List<String> winnerLines = new ArrayList<String>();
		try {
			int numNines =0;
			BufferedReader r = new BufferedReader(new FileReader("FSS.csv"));
			String line = "";
			line = r.readLine();
			winnerLines.add(line);
			while((line=r.readLine())!=null) {
				String[] toks = line.split(",");
				String fileName = toks[0];
				if(fileName.indexOf("T9") > -1)
					winnerLines.add(line);
			}

			PrintWriter w = new PrintWriter(new FileWriter("CheckNines_FSS.csv"));
			w.print(stringify(winnerLines));
			w.close();
		}
		catch(Exception e) {
		}
	}

	public static String stringify(List<String> list) {
		StringBuffer rtn = new StringBuffer();
		for(String line : list) {
			rtn.append(line + "\n");
		}
		return rtn.toString();
	}
}