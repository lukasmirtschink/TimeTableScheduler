<%@page import="userPackage.UserType"%>
<%@page import="timeTablePackage.TimeTable"%>
<%@page import="timeTablePackage.EventPriority"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="PracticalRequest" class="userDataPackage.PracticalRequest" scope="session" />
<%
    userPackage.User user = (userPackage.User)session.getAttribute("user");
    if (user != null) {
        outputPackage.Output output = new outputPackage.Output(request, (userPackage.UserType)(session.getAttribute("userType")));
        out.println(output.createHeader());
        
        PracticalRequest.setValues(request, user);
        if (!PracticalRequest.isSetup()) {
            PracticalRequest.setup((String)request.getParameter("moduleCode"),
                                   (String)request.getParameter("duration"),
                                   (String)request.getParameter("semester"));
        } else {
            PracticalRequest.setStartDate((String)request.getParameter("date"));
            PracticalRequest.setTime((String)request.getParameter("time"));
            PracticalRequest.setVenue((String)request.getParameter("venue"));
            PracticalRequest.setEndDate((String)request.getParameter("endDate"));
        }
        
        TimeTable timeTable = TimeTable.getPreSetTimeTable();
        timeTable.setDisplayWeek((String)request.getParameter("displayDate"));
        timeTable.setupTimeSlots();
        if (user.getUserType().equals(UserType.ADMIN)) {
            timeTable.intialiseAdminTimeTable();
        } else {
            timeTable.initialiseTimeTable(PracticalRequest.getUsersInvolved());
        }
        
        PracticalRequest.setTimeTable(timeTable);
        
        out.println(output.createSuggestedTimeTable(timeTable,
                                                    PracticalRequest.getDuration(),
                                                    EventPriority.MEETING.getPriority(),
                                                    true));
        out.println(output.createTimeTableNav(timeTable.getDisplayWeek(), request));
%>
        <div class="hidden" name="context" value="scheduleLab" data-userId="<%= user.getUserID() %>"></div>
        <hgroup class="animate">
        	<h1>Add Practical</h1>
        	<h2>Step 2 of 2</h2>
        </hgroup>

        <form id="createMeetingForm" action="scheduleLab.jsp" method="GET">
            <div>
                <label for="date">Start Date:</label>
                <input type="text" name="date" id="date" value="<%= PracticalRequest.getStartDate() %>" required="required"><br>
            </div>
            <div>
                <label for="time">Time:</label>
                <input type="text" name="time" id="time" value="<%= PracticalRequest.getTime() %>" required="required"><br>
            </div>
            <div>
                <label for="endDate">End Date:</label>
                <input type="date" name="endDate" id="endDate" value="<%= PracticalRequest.getEndDate() %>" required="required"><br>
            </div>
            <div>
                <label for="venue">Venue:</label>
                <input type="text" name="venue" id="venue" value="<%= PracticalRequest.getVenue() %>" required="required"><br>
            </div>
            <div>
                <label for="submit">Submit:</label>
                <input type="submit" id="submit" value="Next" class="animate">
            </div>
        </form>
<%
        if (PracticalRequest.numErrors() > 0) {
            out.println(output.displayErrors(PracticalRequest.numErrors(), PracticalRequest.getErrors()));
        } else if(PracticalRequest.isValid() && PracticalRequest.checkConflict()) {
            out.println(output.displayErrors(1, "You are trying to schedule over an existing event"));
        } else {
            if (PracticalRequest.createPractical()) {
                response.sendRedirect("index.jsp");
            } 
        }
        out.println(output.createFooter());
    } else {
        
    %>
    <jsp:forward page="/login.jsp" />
    <%
        
    }
%>