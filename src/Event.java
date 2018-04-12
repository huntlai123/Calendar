import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

/**
 * 
 * Class that holds all information an event would need, like title, date,
 * start time, and end time
 * 
 * @author HunterLai
 *
 */
public class Event implements Comparable<Event>
{
    private String title;
    private GregorianCalendar date;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    
    /**
     * 
     * Puts all event information into the class
     * 
     * post-condition: event is created with proper information
     * 
     * @param title
     * @param date
     * @param start
     * @param end
     */
    Event(String title, GregorianCalendar date, GregorianCalendar start, GregorianCalendar end)
    {
            this.title = title;
            this.date = date;
            startTime = start;
            endTime = end;
    }
    
    /**
     * 
     * Checks to see if two GregorianCalendar objects are on the same day.
     * 
     * @param day
     * @return true or false, depending on if they are the same or not
     */
    public boolean sameDay(GregorianCalendar day)
    {
        if(date.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH)
                & date.get(Calendar.MONTH) == day.get(Calendar.MONTH)
                & date.get(Calendar.YEAR) == day.get(Calendar.YEAR))
            return true;
        return false;
    }
    
    /**
     * 
     * Checks to see if the two objects are the same
     * 
     * @param e
     * @return true or false depending on if they are the same or not
     */
    public boolean equals(Event e)
    {
        return title.equals(e.title) && date.equals(e.date) &&
                startTime.equals(e.startTime) && endTime.equals(e.endTime);
    }
    
    /**
     * 
     * Compares two events by time, and at the same time checks to see if the event
     * conflicts with any other events. Checks events to the minute, meaning that
     * if 12:30 - 13:30 and an event 13:30 to 14:30 will not be allowed.
     * 
     * @return positive number if this object is after 3, and negative if vice versa,
     * returns 0 if they conflict
     * 
     */
    public int compareTo(Event e)
    {
        int dateDiff = date.compareTo(e.date);
        if(dateDiff == 0)
        {
            int startTimeDiff = startTime.compareTo(e.startTime);
            if(startTimeDiff > 0)
            {
                if(!startTime.getTime().after(e.endTime.getTime()))
                    return 0;
                return startTimeDiff; 
            }
            else if(startTimeDiff < 0)
            {
                if(!endTime.getTime().before(e.startTime.getTime()))
                    return 0;
                return startTimeDiff; 
            }
            else
                return 0;
        }
        else 
            return dateDiff;
    }
    
    /**
     * 
     * Displays the title of the event and the time
     * 
     * @return string
     * 
     */
    public String toString()
    {
        return title + " " + getTime();
    }
    
    /**
     * 
     * Retrieves the title
     * 
     * @return title
     */
    public String getTitle()
    {
        return title;
    }
    
    /**
     * 
     * retrieves date
     * 
     * @return date
     */
    public GregorianCalendar getDate()
    {
        return date;
    }
    
    /**
     * 
     * Gets start time in the form of a string
     * 
     * @return startTime as string
     */
    public String getStartTime()
    {
        if(startTime.get(Calendar.HOUR_OF_DAY) < 10)
        {
            SimpleDateFormat shortTime = new SimpleDateFormat("H:mm");
            return shortTime.format(startTime.getTime());
        }
        else
        {
            SimpleDateFormat longTime = new SimpleDateFormat("HH:mm");
            return longTime.format(startTime.getTime());
        }
    }
    
    /**
     * 
     * gets end time as a string
     * 
     * @return endTime as a string
     */
    public String getEndTime()
    {
        if(endTime.get(Calendar.HOUR_OF_DAY) < 10)
        {
            SimpleDateFormat shortTime = new SimpleDateFormat("H:mm");
            return shortTime.format(endTime.getTime());
        }
        else
        {
            SimpleDateFormat longTime = new SimpleDateFormat("HH:mm");
            return longTime.format(endTime.getTime());
        }

    }
    
    /**
     * Displays the start and end time of an event
     * 
     * @return displays time as a string
     */
    public String getTime()
    {
        if(startTime.equals(endTime))
            return getStartTime();
        return getStartTime() + " - " + getEndTime();
    }
}
