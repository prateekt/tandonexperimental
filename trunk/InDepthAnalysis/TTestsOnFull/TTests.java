import java.io.*;
import java.util.*;

public class TTests {

	public static void main(String[] args) {
		try {
			BufferedReader r =new BufferedReader(new FileReader("newseeds_together.csv"));
			String line="";
			int numRobots = 10;
			int numRounds = 10;
			int colStart= 2;
			int currentCol=2;
			line=r.readLine();
			while((line=r.readLine())!=null)  {
				String[] toks = line.split(",");
				int currentNumRobots= Integer.parseInt(toks[0]);
				int currentNumRounds = Integer.parseInt(toks[1]);

				if(numRobots!=currentNumRobots || numRounds!=currentNumRounds) {
					System.out.println(numRobots + " " + numRounds /*+ " " + "=TTEST(D"+colStart+":D"+currentCol+",E"+colStart+":E"+currentCol+",2,1)"*/);
					colStart = currentCol;
					numRobots = currentNumRobots;
					numRounds = currentNumRounds;
				}

				currentCol++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}