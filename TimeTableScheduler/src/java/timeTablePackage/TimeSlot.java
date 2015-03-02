/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeTablePackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific time slot in timetable
 *
 * @author John O Riordan
 */
public class TimeSlot {
    private int[] eventPrioritys;
    private int totalPriority;
    private boolean suggested;
    private List<Event> events;
    private String date;
    private String time;
    
    public TimeSlot(String date, String time) {
        this.date = date;
        this.time = time;
        this.eventPrioritys = new int[EventPriority.highestPriority + 1];
        this.totalPriority = 0;
        events = new ArrayList<Event>();
    }

    /**
     * Checks if there are events in this timeslot
     * @return True if an event exists
     */
    public boolean hasEvents() {
        return totalPriority > 0 || !events.isEmpty();
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    /**
     * Adds an event to this timeslot
     * @param event The event
     */
    public void addEvent(Event event) {
        events.add(event);
    }
    
    /**
     * Set if this timeslot is a suggested timeslot for a meeting
     * 
     * @param suggested True if suggested
     */
    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }
    
    /**
     * Returns if the timeslot was suggested to hold a meeting
     * 
     * @return True if suggested
     */
    public boolean isSuggested() {
        return suggested;
    }
    
    /**
     * Total priority for all eventPrioritys scheduled during this timeslot
     * 
     * @return total priority
     */
    public int getTotalPriority() {
        return totalPriority;
    }
    
    /**
     * Increments the number of eventPrioritys with this priority value
     * 
     * @param priority Priority of the event scheduled
     */
    public void addPriority(int priority) {
        if (priority >= 0 && priority < eventPrioritys.length) {
            eventPrioritys[priority] += 1;
            totalPriority += priority;
        }
    }
    
    /**
     * Returns the number of lectures in this timeslot
     * 
     * @return Number of lectures
     */
    public int numLectures() {
        return eventPrioritys[EventPriority.LECTURE.getPriority()];
    }
    
    /**
     * Returns the number of practicals in this timeslot
     * 
     * @return Number of practicals
     */
    public int numPracticals() {
        return eventPrioritys[EventPriority.PRACTICAL.getPriority()];
    }
    
    /**
     * Returns the number of meetings in this timeslot
     * 
     * @return Number of meeting
     */
    public int numMeetings() {
        return eventPrioritys[EventPriority.MEETING.getPriority()];
    }
    
    /**
     * Returns the HTML representation of this timeslot that would display in
     * an HTML table
     * 
     * @return HTML td element
     */
    public String printTableCell() {
        if (totalPriority > 0) {
            return "<td class=\"animate selectable priority-" + totalPriority + " "
                   + (isSuggested() ? "suggested-timeslot\"" : "\"") + ">"
                    
                   + "<div class=\"hidden\""
                   + " data-date=\"" + date + "\""
                   + " data-time=\"" + time + "\"></div>"
                    
                   + "Lecture: " + numLectures() + "<br />"
                   + "Practical: " + numPracticals() + "<br />"
                   + "Meeting: " + numMeetings() + "</td>";
        } else {
            return "<td class=\"animate selectable priority-" + totalPriority + " "
                   + (isSuggested() ? "suggested-timeslot\"" : "\"") + ">"
                   + "<div class=\"hidden\""
                   + " data-date=\"" + date + "\""
                   + " data-time=\"" + time + "\"></div>"
                   + "</td>";
        }
    }
    
    /**
     * Prints a detailed version of the events details
     * @param filter Filter the events displayed
     * @return HTML to print into a table cell
     */
    public String printDetailedTableCell(EventType filter) {
        EventPriority highPriority = EventPriority.MEETING;
        String eventList = "";
        String description = "";
        for (Event event : events) {
            if (filterEvent(event.getEventType(), filter)) {
                eventList += event.toString() + "<br />";
                description += getEventDescription(event);
                highPriority = (event.getEventPriority().getPriority() > highPriority.getPriority() 
                                            ? event.getEventPriority() : highPriority);
            }
        }
        String html = "<td class=\"animate selectable ";
        if (eventList.equals("")) {
            html += ">";
        } else {
            html += highPriority.getPriorityName() + " hoverable\"" +
                    " data-description=\"" + description + "\">" ;
        }
        html += "<div class=\"hidden\""
                   + " data-date=\"" + date + "\""
                   + " data-time=\"" + time + "\"></div>";
        html += eventList + "</td>";
        return html;
    }
    
    private String getEventDescription(Event event) {
        String description = "";
        if (event.getEventType().equals(EventType.MEETING)) {
            Meeting meeting = ((Meeting)event);
            description += "Description: " + meeting.getDescription() + "<br />"
                           + "Organiser: " + meeting.retrieveOrganiserDetails() + "<br />";
        } else if (event.getEventType().equals(EventType.LECTURE)) {
            Lecture lecture = ((Lecture)event);
            description += "Module: " + lecture.getEventID() + "<br />"
                           + "Semester: " + lecture.getSemester() + "<br />";
        } else {
            Practical practical = ((Practical)event);
            description += "Module: " + practical.getEventID() + "<br />"
                           + "Semester: " + practical.getSemester() + "<br />";
        }
        description += "Time: " + event.getTime() + "<br />Location: " + event.getLocation() + "<br />";
        return description;
    }
    
    /**
     * Filters the events to be displayed from the timeslot
     * 
     * @param eventType The type of the event
     * @param filterType The filter type 
     * @return True if the event should be displayed
     */
    private boolean filterEvent(EventType eventType, EventType filterType) {
        return (eventType.equals(filterType) || filterType.equals(EventType.ALL_EVENTS));
    }
    
    /**
     * Returns a string that represents the event scheduled in this timeslot with
     * the highest priority.
     * This is used when displaying the event in the HTML table
     * 
     * @return String to represent the highest priority event
     */
    private String highestPriorityEvent() {
        return ((numLectures() > 0) ? EventPriority.LECTURE.getPriorityName() : 
               ((numPracticals() > 0) ? EventPriority.PRACTICAL.getPriorityName() : 
               ((numMeetings() > 0) ? EventPriority.MEETING.getPriorityName() : "")));
    }
     
    @Override
    public String toString() {
        return "Lecture: " + numLectures() + "\n"
               + "Practical: " + numPracticals() + "\n"
               + "Meeting: " + numMeetings();
    }
}
