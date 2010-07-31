package util.sys;
import java.io.*;
import java.util.*;

public class SysUtil {

	public static int promptInt(String promptMsg, int min, int max) {
		int choice = -1;
		
		while(choice < min || choice > max) {
			System.out.println(promptMsg);
			System.out.print("Your input: ");
			
			try {
				BufferedReader r2 = new BufferedReader(new InputStreamReader(System.in));
				choice = Integer.parseInt(r2.readLine().substring(0,1));
				
				if(choice < min || choice > max) {
					System.out.println("Input choice must be between " + min + " and " + max + ".\n");
				}
			}
			catch(Exception e) {
				System.out.println("Input choice unparsable. Please input a valid integer choice between " + min + " and " + max + ".\n");
			}
		}
		
		return choice;
	}
	
	public static String promptString(String promptMsg) {
		String input = "";
		
		while(input.trim().equals("")) {
			System.out.println(promptMsg);
			
			try {
				BufferedReader r1 = new BufferedReader(new InputStreamReader(System.in));
				input = r1.readLine();
			}
			catch(Exception e) {
				e.printStackTrace();
				System.out.println("Input can't be all spaces Please try again.\n");
			}
		}
		
		return input;
	}

	public static List<Double> getArray(String file) {
		List<Double> rtn = new ArrayList<Double>();
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			String line = r.readLine();
			r.close();
			String[] toks = line.split(" ");
			for(String tok : toks) {
				double d = Double.parseDouble(tok);
				rtn.add(d);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

	public static List<Integer> dtoi(List<Double> list) {
		List<Integer> rtn = new ArrayList<Integer>();
		for(Double d : list) {
			rtn.add(d.intValue());
		}
		return rtn;		
	}
	
	public static void printList(List l) {
		for(int x=0; x < l.size(); x++) {
			System.out.println(l.get(x));
		}
	}
	
	public static void writeFile(String file, String content) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			out.print(content);
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String callSysCmd(String cmd) {
		StringBuffer rtn = new StringBuffer();	
		String s="";
		try {
			Process p = Runtime.getRuntime().exec(cmd);	            
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

	        // read the output from the command
	        System.out.println("Here is the standard output of the command:\n");
	        while ((s = stdInput.readLine()) != null) {
	            rtn.append(s + "\n");
	        }
	        
	        // read any errors from the attempted command
	        System.out.println("Here is the standard error of the command (if any):\n");
	        while ((s = stdError.readLine()) != null) {
	        	rtn.append(s + "\n");
	        }	            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return rtn.toString();
	}
}
