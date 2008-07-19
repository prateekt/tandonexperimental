import java.util.*;
import java.io.*;

public class KnapSackGA {

	private List<KnapSackObject> objects;
	private Map<KnapSackObject, Integer> objectToIndex;
	private List<String> population;
	private static final double MUTATION_PROBABILITY = 0.3;

	public static void main(String[] args) {
		KnapSackGA ga = new KnapSackGA();
		for(int x=0; x < 50; x++) {
			System.out.println("Generation " + x + ": ");
			System.out.println(ga);
			pause();
			ga.doNextGeneration();
		}
	}

	public static void pause() {
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
			r.readLine();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public KnapSackGA() {
		objects = new ArrayList<KnapSackObject>();
		objectToIndex = new HashMap<KnapSackObject, Integer>();
		population = new ArrayList<String>();
		int index = 0;

		//get objects
		try {
			BufferedReader r = new BufferedReader(new FileReader("in.txt"));
			String line="";
			while((line=r.readLine())!=null) {
				String[] toks = line.split(" ");
				int weight = Integer.parseInt(toks[0]);
				int value = Integer.parseInt(toks[1]);
				KnapSackObject o = new KnapSackObject(value, weight);
				objects.add(o);
				objectToIndex.put(o, index);
				index++;
			}
			r.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		//initial population
		for(int y=0; y < 6; y++) {
			String str = "";
			for(int x=0; x < objects.size(); x++) {
				int rand = (int) Math.rint(Math.random());
				str = str + rand;
			}
			population.add(str);
		}

	}

	public void doNextGeneration() {

		//kill dead wood
		List<String> toDelete = new ArrayList<String>();
		for(String str : population) {
			if(stringToKnapsack(str).getWeight() < 0)
				toDelete.add(str);
		}
		for(String str : toDelete) {
			population.remove(str);
		}

		//keep the 10 best individuals
		List<KnapSack> knapsacks = new ArrayList<KnapSack>();
		for(int x=0; x < population.size(); x++)  {
			knapsacks.add(stringToKnapsack(population.get(x)));
		}
		Collections.sort(knapsacks);
		List<String> survivingPopulation = new ArrayList<String>();
		for(int x=0; x < knapsacks.size() && x < 10; x++) {
			KnapSack k = knapsacks.get(x);
			survivingPopulation.add(knapsackToString(k));
		}
		population = survivingPopulation;

		//breed the living
		List<String> toAdd = new ArrayList<String>();
		for(String p1 : population) {
			for(String p2 : population) {
				if(p1==p2)
					continue;
				String child = getChild(p1, p2);
				toAdd.add(child);
			}
		}
		population.addAll(toAdd);

	}

	public  String knapsackToString(KnapSack k) {
		String rtn = "";
		for(int x=0; x < objects.size(); x++) {
			KnapSackObject o = objects.get(x);
			if(k.contains(o))
				rtn += "1";
			else
				rtn += "0";
		}
		return rtn;
	}

	public KnapSack stringToKnapsack(String str) {
		KnapSack rtn = new KnapSack();
		for(int x=0; x < str.length(); x++) {
			if(rtn.getWeight() < 0)
				break;
			char c = str.charAt(x);
			if(c=='1') {
				rtn.putObject(objects.get(x));
			}
			if(c=='0')  {
				continue;
			}
		}
		return rtn;
	}

	public String getChild(String p1, String p2) {
		int crossoverPoint =(int) ((p1.length()-1)*Math.random());
		String child = p1.substring(0, crossoverPoint) + p2.substring(crossoverPoint, p2.length());

		//mutation
		double mutProb = Math.random();
		if(mutProb <= MUTATION_PROBABILITY) {
			System.out.println("MUTATION OCCURED");
			//find place of mutation
			int mutationPoint =(int) ((child.length()-1)*Math.random());

			//switch mutation code
			char mutCode = '0';
			if(child.charAt(mutationPoint)=='0')
				mutCode = '1';

			child = child.substring(0, mutationPoint)  + mutCode + child.substring(mutationPoint+1, child.length());
		}

		return child;
	}

	public String toString() {
		StringBuffer rtn = new StringBuffer();
		for(String str: population) {
			KnapSack ks = stringToKnapsack(str);
			rtn.append(str + " Value: " + ks.getValue() + " Weight: " + ks.getWeight() + "\n");
		}
		rtn.append("\n");
		return rtn.toString();
	}

}

class KnapSack implements Comparable<KnapSack> {

	private List<KnapSackObject> objects;
	private int weight;
	private int value;
	private static final int KNAPSACK_WEIGHT_CONSTRAINT = 2000;

	public KnapSack() {
		objects  = new ArrayList<KnapSackObject>();
		weight = 0;
		value  = 0;
	}

	public boolean putObject(KnapSackObject o) {
		if(weight + o.getWeight() > KNAPSACK_WEIGHT_CONSTRAINT) {
			weight = -100;
			return false;
		}
		else {
			objects.add(o);
			weight += o.getWeight();
			value += o .getValue();
		}
		return true;
	}

	public List<KnapSackObject> getObjects() {
		return objects;
	}

	public int getValue() {
		return value;
	}

	public int getWeight() {
		return weight;
	}

	public boolean contains(KnapSackObject o) {
		return objects.contains(o);
	}

	public int compareTo(KnapSack k) {
		return (int) Math.rint((0.5) * (k.getValue() - getValue()) + (0.5) * (getWeight() - k.getWeight()));
	}
}

class KnapSackObject {

	private int value;
	private int weight;

	public KnapSackObject(int value, int weight) {
		this.value = value;
		this.weight=  weight;
	}

	public int getWeight() {
		return weight;
	}

	public int getValue() {
		return value;
	}

}