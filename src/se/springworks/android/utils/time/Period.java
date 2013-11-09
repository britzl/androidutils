package se.springworks.android.utils.time;

public class Period {

	private long days;
	private int hours;
	private int minutes;
	private int seconds;
	
	public Period(long start, long end) {
		this(end - start);
	}
	
	public Period(long diff) {
		days  = diff/1000/86400;
		hours = (int)(diff/1000 - 86400*days) / 3600;
		minutes  = (int)(diff/1000 - 86400*days - 3600*hours) / 60;
		seconds  = (int)(diff/1000 - 86400*days - 3600*hours - 60*minutes);
	}
	
	public boolean hasDays() {
		return days > 0;
	}
	
	public boolean hasHours() {
		return hours > 0 || hasDays();
	}
	
	public boolean hasMinutes() {
		return minutes > 0 || hasHours();
	}
	
	public boolean hasSeconds() {
		return seconds > 0 || hasMinutes();
	}
	
	public long getDays() {
		return days;
	}
		
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
}
