import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * 
 * Class used to display the names of the months
 * 
 * @author HunterLai
 *
 */
enum MONTHS
{
    January, February, March, April, May, June, July, August, September, October, November, December;
}

/**
 * 
 * Class used to display the names of each day
 * 
 * @author HunterLai
 *
 */
enum DAYS
{
    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;
}

/**
 * 
 * class used to display the abbreviated form of each day
 * 
 * @author HunterLai
 *
 */
enum DAYS_ABBREVIATED
{
    Sun, Mon, Tue, Wed, Thu, Fri, Sat ;
}

/**
 * 
 * Manages and organizes the user input and MyCalendar class
 * 
 * @author HunterLai
 *
 */
public class MyCalendarTester
{
    private final static int DISPLAY_CURRENT_DAY = 0;
    private final static int DONT_DISPLAY_CURRENT_DAY = 1;

    private static GregorianCalendar today;
    private static Scanner scanner;
    private static MyCalendar myCalendar;
    
    /**
     * 
     * Main that loops through the main menu and calls all necessary methods
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        today = new GregorianCalendar();
        myCalendar = new MyCalendar(today);
        scanner = new Scanner(System.in);
        System.out.println(printCalendar(today, DISPLAY_CURRENT_DAY));
        String request;
        do
        {
            System.out.println("Select one of the following:\n"
                    + "   [L]oad  [V]iew by  [C]reate  [G]o to  [E]vent list  [D]elete  [Q]uit");
            request = scanner.nextLine().toUpperCase();
            
            switch (request)
            {
                case "L":
                    load("events.txt");
                    break;
                case "V":
                    myCalendar.setCal((GregorianCalendar) today.clone());
                    System.out.println("[D]ay view  [M]onth view");
                    request = scanner.nextLine().toUpperCase();
                    switch (request)
                    {
                        case "D":
                            System.out.println(myCalendar.dayDisplay(today));
                            view(Calendar.DAY_OF_MONTH);
                            break;
                        case "M":
                            System.out.println(printCalendar(today, DONT_DISPLAY_CURRENT_DAY));
                            view(Calendar.MONTH);
                            break;
                    }
                    break;
                case "C":
                    createEvent();
                    break;
                case "G":
                    System.out.println(myCalendar.dayDisplay(goTo()));
                    done();
                    break;
                case "E":
                    System.out.println(myCalendar.displayEvents());
                    done();
                    break;
                case "D":
                    GregorianCalendar day = goTo();
                    ArrayList<Event> events = myCalendar.dayEvents(day);
                    System.out.println(myCalendar.dayDisplay(day));
                    if(events.size() != 0)
                    {
                        System.out.println("[S]elected  [A]ll");
                        request = scanner.nextLine().toUpperCase();
                        switch(request)
                        {
                            case "A":
                                if(myCalendar.removeDay(day))
                                    System.out.println("Events removed.");
                                break;
                            case "S":
                                deleteSelected(events);
                                break;
                        }
                    }
                    break;
                case "Q":
                    saveEvents();
                    return;
                default: 
                    System.out.println("Invalid input");
                    done();
            }
        }
        while(true);
    }
    
    /**
     * 
     * Loads the file to myCalendar if there are any events
     * 
     * @param filename
     */
    public static void load(String filename)
    {
        try
        {
            File eventsFile = new File(filename);
            Scanner scanFile = new Scanner(eventsFile);
            scanFile.hasNextLine();
            while(scanFile.hasNextLine())
            {
                String line = scanFile.nextLine();
                String[] event = line.split(",");
                String title = event[0];
                String[] dateString = event[1].split("/");
                GregorianCalendar date = new GregorianCalendar(Integer.parseInt(dateString[2]),
                        Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]));
                
                int[] startTimeSplit = splitTime(event[2]);     
                GregorianCalendar startTime = new GregorianCalendar(Integer.parseInt(dateString[2]),
                        Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), startTimeSplit[0], startTimeSplit[1]);
                
                int[] endTimeSplit = splitTime(event[3]);
                GregorianCalendar endTime = new GregorianCalendar(Integer.parseInt(dateString[2]),
                        Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), endTimeSplit[0], endTimeSplit[1]);
                
                myCalendar.add(new Event(title, date, startTime, endTime));
            }
            scanFile.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("First run, there are no events.");
        } 
    }
    
    /**
     * 
     * Calls the logic for deleting a selected event
     * 
     * @param events
     */
    public static void deleteSelected(ArrayList<Event> events)
    {
        String request;
        int eventNumber = 0;
        do
        {
            String output = "";
            if(events.size() == 0)
            {
                System.out.println("There are no more events to delete.");
                break;
            }
            
            for(int i = 0; i < events.size(); i++)
                if(i == eventNumber)
                    output += "\n[" + events.get(i).toString() + "]";
                else
                    output += "\n " + events.get(i).toString();
            
            System.out.println(output);
            System.out.println("[F]orward  [B]ack  [D]elete  [M]ain Menu");
            request = scanner.nextLine().toUpperCase();
     
            switch(request)
            {
                case "F": 
                    if(eventNumber < events.size() - 1)
                        eventNumber++;
                    break;
                case "B":
                    if(eventNumber > 0)
                        eventNumber--;

                    break;
                case "D":
                    Event removedEvent = events.remove(eventNumber);
                    if(myCalendar.remove(removedEvent))
                        System.out.println("Event deleted.");
                    else
                        System.out.println("Event could not be deleted.");
                    eventNumber = 0;
                    break;
            }
            
        }
        while(!request.equals("M"));
    }
    
    /**
     * 
     * Calls logic for viewing either a specific day or month, depending on the field
     * 
     * @param field
     */
    public static void view(int field)
    {
        String request;
        do
        {
            GregorianCalendar cal;
            System.out.println("[N]ext  [P]revious  [M]ain Menu");
            request = scanner.nextLine().toUpperCase();
            switch (request)
            {
                case "N":
                    cal = myCalendar.getNext(field);
                    if(field == Calendar.MONTH)
                        System.out.println(printCalendar(cal, DONT_DISPLAY_CURRENT_DAY));
                    else
                        System.out.println(myCalendar.dayDisplay(cal));
                    break;
                case "P":
                    cal  = myCalendar.getPrev(field);
                    if(field == Calendar.MONTH)
                        System.out.println(printCalendar(cal, DONT_DISPLAY_CURRENT_DAY));
                    else
                        System.out.println(myCalendar.dayDisplay(cal));
                    break;
            }
        }
        while(!request.equals("M"));
    }
    
    /**
     * 
     * Calls logic for going to a specific day and viewing its events
     * 
     * @return
     */
    public static GregorianCalendar goTo()
    {
        String date;
        do
        {
        System.out.println("Please enter a date to go to in the form MM/DD/YYYY");
        date = scanner.nextLine();
        }while(!checkDateFormat(date));
        
        String[] dateSplit = date.split("/");
        GregorianCalendar newDate = new GregorianCalendar(Integer.parseInt(dateSplit[2]), 
                Integer.parseInt(dateSplit[0]) - 1, Integer.parseInt(dateSplit[1]));
        return newDate;
    }
    
    /**
     * 
     * Calls the logic for creating an event and adding it to the list of events
     * 
     */
    public static void createEvent()
    {
        System.out.println("Please enter a title for your date");
        String title = scanner.nextLine();

        GregorianCalendar newDate = goTo();
        
        String start;
        do
        {
        System.out.println("Please enter a start time in the form HH:MM (24 hour clock)");
        start = scanner.nextLine();
        }while(!checkTimeFormat(start));
        
        int[] startTime = splitTime(start);
        GregorianCalendar newStartTime = new GregorianCalendar(newDate.get(Calendar.YEAR), 
                newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH), startTime[0], startTime[1]);
        
        String end;
        do
        {
        System.out.println("Please enter an end time in the form HH:MM (24 hour "
                + "clock). If there is no end time enter / and the end time will be set to the same"
                + "as the start time.");
        end = scanner.nextLine();
        } while(!checkTimeFormat(end));
        
        GregorianCalendar newEndTime;
        if(end.equals("/"))
            newEndTime = new GregorianCalendar(newDate.get(Calendar.YEAR), 
                    newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH), startTime[0], startTime[1]);
        else
        {
            int[] endTime = splitTime(end);
            newEndTime = new GregorianCalendar(newDate.get(Calendar.YEAR), 
                    newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH), endTime[0], endTime[1]);
        }
        
        if(!newStartTime.after(newEndTime))
        {
            Event newEvent = new Event(title, newDate, newStartTime, newEndTime);
            if(!myCalendar.add(newEvent))
                System.out.println("Sorry, this event could not be added, because "
                        + "it conflicts with another event.");
            else 
                System.out.println("Event has been added.");
        }
        else
            System.out.println("Sorry, the event could not be created because the end time"
                    + " is before it starts");
    }
    
    /**
     * 
     * Helper method to split the time that the user inputs
     * 
     * @param timeString
     * @return returns the time as an int[]
     */
    public static int[] splitTime(String timeString)
    {
        int[] time = new int[2];
        String[] splitTime = timeString.split(":");
        time[0] = Integer.parseInt(splitTime[0]);
        time[1] = Integer.parseInt(splitTime[1]);
        return time;
    }
    
    /**
     * 
     * prints the calendar and displays events or not depending on the field
     * 
     * @param calendar
     * @param field
     * @return calendarOutput (calendar as a string with events or not)
     */
    public static String printCalendar(GregorianCalendar calendar, int field)
    {
        String calendarOutput = "\n";
        MONTHS[] monthsArray = MONTHS.values();
        DAYS_ABBREVIATED[] daysArray = DAYS_ABBREVIATED.values();
        
        MONTHS month = monthsArray[calendar.get(Calendar.MONTH)];
        int year = calendar.get(Calendar.YEAR);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        
        String monthYear = month + " " + year;
        
        for(int i = 0; i < (28 - monthYear.length())/2; i++)
            calendarOutput += " ";
        calendarOutput += monthYear + "\n";
        
        for(int i = 0; i < 7; i++)
            calendarOutput += daysArray[i] + " ";
        
        GregorianCalendar firstDay = new GregorianCalendar(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        int firstDayOfMonth = firstDay.get(Calendar.DAY_OF_WEEK) - 1;
        calendarOutput += "\n";
        for(int i = 0; i < firstDayOfMonth; i++)
            calendarOutput += "    ";
        
        for(int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++)
        {
            if(i == date && today.get(Calendar.YEAR) == year && field == DISPLAY_CURRENT_DAY
                    && today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                calendarOutput += "[" + i + "]"; 
            else if(myCalendar.dayEvents(new GregorianCalendar(year, calendar.get(Calendar.MONTH), i)).size() != 0)
                calendarOutput += "{" + i + "}";
            else 
                calendarOutput += " " + i + " ";
            
            if(i < 10)
                calendarOutput += " ";
            
            if((i + firstDayOfMonth)%7 == 0)
                calendarOutput += "\n";
        }
        return calendarOutput;
    }
    
    /**
     * 
     * Makes the user enter something to continue
     * 
     */
    public static void done()
    {
        System.out.println("Type anything to continue.");
        scanner.nextLine();            
    }
    
    /**
     * 
     * User overwrites events in the events to the Events.txt and creates it if it doesnt
     * exists
     * 
     */
    public static void saveEvents()
    {
        String toWrite = "";
        try
        {
            File file = new File("events.txt");
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            
            for(int i = 0; i < myCalendar.size(); i++)
            {
                Event current = myCalendar.get(i);
                toWrite += current.getTitle() + "," + current.getDate().get(Calendar.MONTH)
                        + "/" + current.getDate().get(Calendar.DAY_OF_MONTH) 
                        + "/" + current.getDate().get(Calendar.YEAR) + ","
                        + current.getStartTime() + ","
                        + current.getEndTime() + "\n";
            }
            writer.print(toWrite);
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("Could not write to file.");
        }

    }

    /**
     * 
     * Checks to make sure input of date is in correct format. Note: It only accepts
     * dates after the year 999
     * 
     * @param date
     * @return true or false depending on if the format is correct
     */
    public static boolean checkDateFormat(String date)
    {
        return date.matches("\\d{1,2}/\\d{1,2}/\\d{4,}+");
    }
    
    /**
     * 
     * Checks to make sure that the input of time is in the proper format
     * 
     * @param time
     * @return true or false depending on if the format is correct
     */
    public static boolean checkTimeFormat(String time)
    {
        return time.matches("\\d{1,2}:\\d{1,2}") || time.matches("/");
    }
}
