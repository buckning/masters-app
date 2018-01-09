package dateUtils;

import java.util.Calendar;

public class DateUtils {
	
	/**
	 * Get the current time
	 * Element 0 is the hours, Element 1 is minutes, Element 2 is seconds
	 * Time will be returned in 12hour format
	 */
	public static int[] get24HourTime(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		return new int[]{hour, min, second};
	}
	/**
	 * Get the current time
	 * Element 0 is the hours, Element 1 is minutes, Element 2 is seconds
	 * Time will be returned in 12hour format
	 */
	public static int[] getTime(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		return new int[]{hour, min, second};
	}
	
	/**
	 * Get the current date
	 * Element 0 is day, element 1 is month, element 2 is the year
	 * @return the date accurate to the day
	 */
	public static int[] getDate(){
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH)+1;	//month starts from 0 and not 1
		int year = c.get(Calendar.YEAR)%100;
		return new int[]{day, month, year};
	}
	
	/**
	 * Get the time as a string in the format HHMMSS
	 * @return the time string
	 */
	public static String getTimeString(){
		int[] time = getTime();
		String s = "";
		for(int i = 0; i < time.length; i++){
			if(time[i]<10){
				s += "0";
			}
			s += time[i];
		}
		return s;
	}
	
	/**
	 * Get the time as a string in the format HHMMSS
	 * @return the time string
	 */
	public static String get24HourTimeString(){
		int[] time = get24HourTime();
		String s = "";
		for(int i = 0; i < time.length; i++){
			if(time[i]<10){
				s += "0";
			}
			s += time[i];
		}
		return s;
	}
	
	/**
	 * Get the current date in a string with the format DDMMYY 
	 * @return
	 */
	public static String getDateString(){
		int[] date = getDate();
		String s = "";
		
		for(int i = 0; i < date.length; i++){
			if(date[i] < 10){
				s+="0";
			}
			s += date[i];
		}
		return s;
	}
}
