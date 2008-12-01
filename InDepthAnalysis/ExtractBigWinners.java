import java.io.*;
import java.util.*;

public class ExtractBigWinners  {

	public static void main(String[]  args) {
		List<String> winnerLines = new ArrayList<String>();
		try {
			BufferedReader r = new BufferedReader(new FileReader("FCSS.csv"));
			String line = "";
			line = r.readLine();
			winnerLines.add(line);
			while((line=r.readLine())!=null) {
				String[] toks = line.split(",");
				int difference = Integer.parseInt(toks[4]);
				if(difference >= 1000)
					winnerLines.add(line);
			}

			PrintWriter w = new PrintWriter(new FileWriter("FCSS_BigWinners.csv"));
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