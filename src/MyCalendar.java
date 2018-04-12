
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;

/**
 * 
 * Class that holds the current day the user is looking at and all the events
 * 
 * @author HunterLai
 *
 */
public class MyCalendar
{
    private ArrayList<Event> events;
    private GregorianCalendar calendar;
    
    /**
     * 
     * sets the calendar and initializes the events array list
     * 
     * @param cal
     */
    MyCalendar(GregorianCalendar cal)
    {
        events = new ArrayList<Event>();
        calendar = cal;
    }
   
    /**
     * 
     * Adds one more of the field to the calendar and returns the calendar
     * 
     * @param field
     * @return calendar
     */
    public GregorianCalendar getNext(int field)
    {
        calendar.add(field, 1);
        return calendar;
    }
    
    /**
     * 
     * Subtracts one of the particular field of the calendar and returns the calendar
     * 
     * @param field
     * @return calendar
     */
    public GregorianCalendar getPrev(int field)
    {
        calendar.add(field, -1);
        return calendar;
    }
    
    /**
     * 
     * adds Event to the array list and makes sure it is in the correct place so
     *  that the list is properly organized
     * 
     * @param newEvent
     * @return true if the add was successful, false if not
     */
    public boolean add(Event newEvent)
    {
        int compVal = 1;
        events.add(newEvent);
        if(events.size() > 1)
        {
            int counter = events.size() - 1;
            while(counter > 0 && (compVal = events.get(counter - 1).compareTo(events.get(counter))) > 0)
            {
                Event temp = events.get(counter - 1);
                events.set(counter - 1, events.get(counter));
                events.set(counter, temp);
                counter--;
            }
            if(compVal == 0)
            {
                events.remove(newEvent);
                return false;
            }
            
        }
        return true;
    }
    
    /**
     * 
     * Returns the events on that particular day
     * 
     * @param day
     * @return array list of events
     */
    public ArrayList<Event> dayEvents(GregorianCalendar day)
    {
        ArrayList<Event> dayEvents = new ArrayList<Event>();
        for(int i = 0; i < events.size(); i++)
            if(events.get(i).sameDay(day))
                dayEvents.add(events.get(i));
        return dayEvents;
    }
    
    /**
     * 
     * displays all events in the arraylist
     * 
     * @return output (all events as a string)
     */
    public String displayEvents()
    {
        String output = "\n Event List \n";
        MONTHS[] monthsArray = MONTHS.values();
        DAYS[] daysArray = DAYS.values();
        if(events.size() == 0)
            return "No Events.";
        int currentYear = events.get(0).getDate().get(Calendar.YEAR);
        output += currentYear;
        for(int i = 0; i < events.size(); i++)
        {
            Event currentEvent = events.get(i);
            if(currentEvent.getDate().get(Calendar.YEAR) != currentYear)
            {
                currentYear = currentEvent.getDate().get(Calendar.YEAR);
                output += "\n" + currentYear;
            }
            output += "\n   " + daysArray[currentEvent.getDate().get(Calendar.DAY_OF_WEEK) - 1] + " " +  
                    monthsArray[currentEvent.getDate().get(Calendar.MONTH)] 
                    + " " + currentEvent.getDate().get(Calendar.DATE)
                    + " " + currentEvent.getTime()
                    + " " + currentEvent.getTitle();
        }
        return output;
    }
    
    /**
     * 
     * displays all the events on a particular day
     * 
     * @param day
     * @return string of all events on that day
     */
    public String dayDisplay(GregorianCalendar day)
    {
        MONTHS[] monthsArray = MONTHS.values();
        DAYS[] daysArray = DAYS.values();
        
        ArrayList<Event> events = dayEvents(day);
        String output = daysArray[day.get(GregorianCalendar.DAY_OF_WEEK) - 1] + ", " 
                + monthsArray[day.get(GregorianCalendar.MONTH)] + " " 
                + day.get(GregorianCalendar.DAY_OF_MONTH) + ", " + day.get(GregorianCalendar.YEAR);
        
        if(events.size() == 0)
        {
            output += "\n   No Events.";
            return output;
        }
        
        for(int i = 0; i < events.size(); i++)
            output += "\n   " + events.get(i).toString();
        
        return output;
    }
    
    /**
     * 
     * returns size of the events list
     * 
     * @return
     */
    public int size()
    {
        return events.size();
    }
    
    /**
     * 
     * removes all events on a particular day
     * 
     * @param gc
     * @return true or false if it was successful
     */
    public boolean removeDay(GregorianCalendar gc)
    {
        boolean removed = true;
        ArrayList<Event> day = dayEvents(gc);
        for(int i = 0; i < day.size(); i++)
            removed = removed && remove(day.get(i));
        return removed;
    }
    
    /**
     * 
     * removes a specific event from the list
     * 
     * @param event
     * @return true or false if it worked or not
     */
    public boolean remove(Event event)
    {
        return events.remove(event);
    }
    
    /**
     * 
     * sets the calendar
     * 
     * @param cal
     */
    public void setCal(GregorianCalendar cal)
    {
        calendar = cal;
    }
    
    /**
     * 
     * retrieves an event
     * 
     * @param i
     * @return event
     */
    public Event get(int i)
    {
        return events.get(i);
    }
}
