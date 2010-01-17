package src.gui;
import org.jfree.data.xy.XYSeries;
import java.util.*;

/**
 * Contains a cached list of points for local modification.
 * @author Prateek Tandon
 *
 */
public class XYSeriesCache extends XYSeries {
	
	private java.util.List<Point> points;
	
	public XYSeriesCache(String title) {
		super(title);
		points = new ArrayList<Point>();
	}
	
	public void add(double x, double y) {
		super.add(x, y);
		points.add(new Point(x,y));
	}
	
	public void clear() {
		super.clear();
		points.clear();
	}
	
	public boolean contains(double x) {
		for(int z=0; z < points.size(); z++) {
			if(points.get(z).getX()==x)
				return true;
		}
		return false;
	}
}
