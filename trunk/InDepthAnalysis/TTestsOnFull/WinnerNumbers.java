import java.io.*;
import java.util.*;

public class WinnerNumbers {

	public static void main(String[] args) {
		try {
			BufferedReader r =new BufferedReader(new FileReader("winners.csv"));
			String line="";
			int numRobots = 10;
			int numRounds = 10;
			int winnerNumbers = 0;
			line=r.readLine();
			while((line=r.readLine())!=null)  {
				String[] toks = line.split(",");
				int currentNumRobots= Integer.parseInt(toks[0]);
				int currentNumRounds = Integer.parseInt(toks[1]);

				if(numRobots!=currentNumRobots || numRounds!=currentNumRounds) {
					System.out.println(winnerNumbers);
					numRobots = currentNumRobots;
					numRounds = currentNumRounds;
					winnerNumbers = 0;
				}

				winnerNumbers++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}