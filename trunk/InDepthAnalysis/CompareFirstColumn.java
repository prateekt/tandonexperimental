import java.io.*;
import java.util.*;

public class CompareFirstColumn {

	public static void main(String[]  args) {
		List<String> winnerSims = new ArrayList<String>();
		try {
			BufferedReader r = new BufferedReader(new FileReader("FCSS_BigWinners.csv"));
			String line = "";
			line = r.readLine();
			while((line=r.readLine())!=null) {
				String[] toks = line.split(",");
				String fileName = toks[0];
				winnerSims.add(fileName);
				System.out.println("F: " +fileName);
			}

			List<String> simsInCommon = new ArrayList<String>();
			BufferedReader r2 = new BufferedReader(new FileReader("FSS.csv"));
			line = "";
			line = r2.readLine();
			while((line=r2.readLine())!=null) {
				String[] toks = line.split(",");
				String fileName = toks[0];
				System.out.println("BS: " + fileName);
				if(winnerSims.contains(fileName)) {
					simsInCommon.add(fileName);
				}
			}

			PrintWriter w = new PrintWriter(new FileWriter("SimsInCommon_FCSSBigWinnersByFSS.txt"));
			w.print(stringify(simsInCommon));
			w.close();

		}
		catch(Exception e) {
			e.printStackTrace();
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