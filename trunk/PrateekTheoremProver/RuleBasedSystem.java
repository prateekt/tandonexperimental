import java.util.*;
import java.io.*;

public class RuleBasedSystem {

	private List<Rule> rules;
	private List<Statement> statements;

	public RuleBasedSystem(String defFile) {
		statements = new ArrayList<Statement>();
		rules = new ArrayList<Rule>();
		load(defFile);

	}

	public void load(String file) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			String line = "";
			while((line=r.readLine())!=null) {
				if(line.indexOf(":-") > -1) {
					rules.add(parseRule(line));
				}
				else {
					statements.add(parseStatement(line));
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Rule parseRule(String line) {
		String[] tok = line.split(":-");
		String conc = tok[0].trim();
		String predStr = tok[1].trim();
		String[] preds = predStr.split(",");

		//Create conclusion
		Predicate conclusion = new Predicate(conc);

		//Create conditions
		List<Predicate> predicates = new ArrayList<Predicate>();
		for(String pred: preds) {
			pred = pred.trim();
			String[] predParts = pred.split("(");
			String predName = predParts[0].trim();
			if(predName.endsWith("."))
				predName = predName.substring(0, predName.length()-1);
			Predicate p = new Predicate(predName);
			predicates.add(p);
		}

		return new Rule(predicates, conclusion);
	}

	public Statement parseStatement(String line) {
		String[] predParts = line.split("(");
		String predname = predParts[0].trim();
		String[] predParts2 = predParts[1].split(")");
		String varName = predParts2[0].trim();
		return new Statement(varName, new Predicate(predname));
	}

}

class Rule {

	private List<Predicate> req;
	private Predicate conclusion;

	public Rule(List<Predicate> req, Predicate conclusion) {
		this.req = req;
		this.conclusion = conclusion;
	}

	public List<Predicate> getReq() {
		return req;
	}

	public Predicate getConclusion() {
		return conclusion;
	}

}

class Predicate {

	private String name;

	public Predicate(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

class Statement {

	private String name;
	private Predicate predicate

	public Statement(String name, Predicate p) {
		this.name = name;
		this.predicate = p;
	}

	public String getName() {
		return name;
	}

	public Predicate getPredicate() {
		return predicate;
	}
}

class PredicateComparator implements Comparator<Predicate> {

	public int compareTo(Predicate p1, Predicate p2) {
		return p1.getName().compareTo(p2.getName());
	}
}
