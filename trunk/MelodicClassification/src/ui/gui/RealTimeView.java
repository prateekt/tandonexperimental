package ui.gui;

/**
 * Drawing of the real time view of the hammer and nail. Also
 * drawn are screens for noise and bend graphs.
 * @author Prateek Tandon
 */
public class RealTimeView extends View {

	XYSeriesCache signalSeries, freqSeries;
	/**
	 * Constructor
	 * @param arm The hammering arm
	 * @param nail The nail model
	 */
	public RealTimeView() {
		super();
		screens = new Screen[2];
		screens[0] = new GraphScreen(this, "Signal", "time", "f(x)");
		screens[1] = new GraphScreen(this, "FREQ_DECOMP", "Freq", "FFT");
		signalSeries = new XYSeriesCache("Signal");
		freqSeries = new XYSeriesCache("Freq");
		((GraphScreen)screens[0]).addSeries(signalSeries);
		((GraphScreen)screens[1]).addSeries(freqSeries);
		doLayout2();
	}
	
	public void addSignalSeriesPoint(double x, double y) {
		signalSeries.add(x, y);
		screens[0].repaint();
	}

	public void addFreqSeriesPoint(double x, double y) {
		freqSeries.add(x, y);
		screens[1].repaint();
	}
}