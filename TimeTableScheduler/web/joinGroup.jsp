<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="JoinGroupRequest" class="userDataPackage.JoinGroupRequest" scope="request" />
<%
    userPackage.User user = (userPackage.User)session.getAttribute("user");
    if (user != null) {
        outputPackage.Output output = new outputPackage.Output(request, (userPackage.UserType)(session.getAttribute("userType")));
        out.println(output.createHeader());

        JoinGroupRequest.setValues(request, user);
%>
        <div class="hidden" name="context" value="joinGroup" data-userId="<%= user.getUserID() %>"></div> 
        <h1 class="banner">Join Group</h1>
        <hgroup>
        	<h1 class="banner">Join Group</h1>
        </hgroup>
        <form action="joinGroup.jsp" method="GET">
            <div>
                <label for="gname">Group:</label>
        	<select name="gname" id="gname">
                <% out.println(output.createJoinGroupDropDown(user.getUserID())); %>
                </select><br>
            </div>
            <div>
        	<label for="submit">Submit:</label>
        	<input type="submit" id="submit" value="Next">
            </div>
        </form>
<% 
        if (JoinGroupRequest.numErrors() > 0) {
            out.println(output.displayErrors(JoinGroupRequest.numErrors(), JoinGroupRequest.getErrors()));
        }
        if (JoinGroupRequest.joinGroup((String)request.getParameter("gname"))) {
            out.println("You joined the group.");
        }
        out.println(output.createFooter());
    } else {
        
    %>
    <jsp:forward page="/login.jsp" />
    <%
        
    }
%>
