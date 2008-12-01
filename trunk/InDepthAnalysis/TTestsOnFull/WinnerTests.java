import java.io.*;
import java.util.*;

public class WinnerTests {

	public static void main(String[] args) {
		try {
			BufferedReader r =new BufferedReader(new FileReader("newseeds_together.csv"));
			String line="";
			line=r.readLine();
			while((line=r.readLine())!=null)  {
				String[] toks = line.split(",");
				int mpgm= Integer.parseInt(toks[3]);
				int rebid = Integer.parseInt(toks[4]);

				if(mpgm > rebid)  {
					System.out.println(line);
				}

			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}