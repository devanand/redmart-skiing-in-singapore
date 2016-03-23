
/**
 * Slope Stats is a modified more general Pair classes that used to contain data
 * pairs to help the program operation
 * 
 * @author Ignatius
 *
 */
public class SlopeStats implements Comparable<SlopeStats> {
	private Integer drop;
	private Integer length;
	
	public SlopeStats(Integer drop, Integer length) {
		this.drop = drop;
		this.length = length;
	}

	public Integer getDrop() {
		return drop;
	}

	public void setDrop(Integer drop) {
		this.drop = drop;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public int compareTo(SlopeStats ss) {
		if (this.length == ss.getLength())
			return this.drop.compareTo(ss.drop);
		else
			return this.length.compareTo(ss.length);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SlopeStats))
			return false;
		SlopeStats ss = (SlopeStats) o;
		return (this.length == ss.length) && (this.drop == ss.drop);
	}
}
