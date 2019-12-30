<%-- 
    Document   : ChatClient
    Created on : Dec 27, 2019, 10:59:39 AM
    Author     : Prof. Matteo Palitto
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>WEB CHAT</title>
        <link rel="stylesheet" type="text/css" href="/WebChat/w3.css">
        <script type="text/javascript" src="/WebChat/chatSocket.js"></script>       
    </head>
<%
    String name=request.getParameter("name");
    String sex=request.getParameter("sex");   
%>
<body onload="onEntry('<%out.print(sex);%>', '<%out.print(name);%>')">
<div class="w3-row w3-white" style="margin-left: 22px; padding-top: 44px; margin-right: 22px">
<!--div class="w3-card-4 xtest w3-col m8"-->
<div class="w3-card-4">
    <% if ( sex.equals("lui") ) { %>
  <header class="w3-container w3-blue">
          <% } else { %>
  <header class="w3-container w3-pink">
          <% } %>
    <div id="userList" style="white-space: nowrap"> <h4>User List: </h4> </div>
  </header>
  <div class="w3-container" style="overflow:auto; height:400px;">
    <p id="chatBox"></p>
  </div>

          <% if ( sex.equals("lui") ) { %>
  <footer class="w3-container w3-blue">
     <div style="white-space: nowrap">
            <div style="display:inline-block"><img src="/WebChat/img_lui.png" alt="Avatar" height="40" width="40"><%out.print(name);%></div>
          <% } else { %>
  <footer class="w3-container w3-pink">
     <div style="white-space: nowrap">
            <div style="display:inline-block"><img src="/WebChat/img_lei.png" alt="Avatar" height="40" width="40"><%out.print(name);%></div>
          <% } %>
          <input name="usermsg" type="text" id="usermsg" size="50" style="display:inline-block" onkeypress="return onEnterSendMessage(event)"/>
          <button class="w3-button w3-green" onclick="onSendMessage('<%out.print(sex);%>', '<%out.print(name);%>')" style="display:inline-block"/>Send</button>
          <button class="w3-button w3-red" onclick="window.location.assign('/WebChat')" style="display:inline-block">Exit</button>
     </div>
  </footer>

</div>
</div>
</body>

</html>
