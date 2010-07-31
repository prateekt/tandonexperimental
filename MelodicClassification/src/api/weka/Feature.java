package api.weka;
import java.util.*;

public class Feature<E> {
	
	private String name;
	private Type type;
	private List<String> options;
	private E value;
	
	public Feature(String name, Type type, E value) {
		this.name = name;
		this.type = type;
		this.value = value;
		
		if(type==Type.REAL) {
			options = null;
		}
	}
	
	public String genWekaAttrString() {
		StringBuffer rtn = new StringBuffer();
		rtn.append("@attribute " + name + " ");
		
		if(type==Type.REAL) {
			rtn.append("real");
		}
		else if(type==Type.DISCRETE) {
			rtn.append("{");
			for(int x=0; x < options.size(); x++) {
				rtn.append(options.get(x));
				if(x!=options.size()-1) 
					rtn.append(", ");
			}
			rtn.append("}");
		}
		
		return rtn.toString();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	/**
	 * @return the options
	 */
	public List<String> getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}

	/**
	 * @return the value
	 */
	public E getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(E value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return ""+value;
	}
}