package ui.gui;
import org.jfree.data.xy.XYSeries;
import java.util.*;

/**
 * Contains a cached list of points for local modification.
 * @author Prateek Tandon
 *
 */
public class XYSeriesCache extends XYSeries {
	
	/**
	 * The points in the cache.
	 */
	private java.util.List<Point> points;
	
	/**
	 * The XYSeries cache constructor
	 * @param title The title of the cache
	 */
	public XYSeriesCache(String title) {
		super(title);
		points = new ArrayList<Point>();
	}
	
	/**
	 * Adds a point to the cache.
	 */
	@Override
	public void add(double x, double y) {
		super.add(x, y);
		points.add(new Point(x,y));
	}
	
	/**
	 * Clears the cache.
	 */
	@Override
	public void clear() {
		super.clear();
		points.clear();
	}
	
	/**
	 * Returns true whether cache contains a certain x value.
	 * @param x x_value
	 * @return Returns true whether cache contains a certain x value.
	 */
	public boolean contains(double x) {
		for(int z=0; z < points.size(); z++) {
			if(points.get(z).getX()==x)
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the Y Values as a CSV line.
	 * @return The Y values of the points
	 */
	public String toYCSVLine() {
		StringBuffer rtn = new StringBuffer();
		for(int x=0; x < points.size(); x++) {
			if(x!=points.size()-1)
				rtn.append(points.get(x).getY() + ",");
			else
				rtn.append(points.get(x).getY());
		}
		
		return rtn.toString();
	}
}
