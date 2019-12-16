<%-- 
    Document   : index
    Created on : Dec 15, 2019, 7:30:03 PM
    Author     : matteo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="/WebChat/w3.css">
    </head>
    
    <body>
<div class="w3-card-4 w3-dark-grey">

<div class="w3-container w3-center">
  <h3>Inserisci il tuo Nome
      <input type="text" name="nameField" id="nameField"/> oppure
      <button class="w3-button w3-red" onclick="window.close()">Chiudi</button>
</h3>
  <!--img src="img_he.png" alt="Avatar" style="width:40%"-->
  <h5>
  <input type="image" src="/WebChat/img_she.png" onclick='enterChatroom("she")' style="width:33%"/>
  <input type="image" src="/WebChat/img_he.png" onclick='enterChatroom("he")' style="width:33%"/>
  
 <!--button class="w3-button w3-green"  onclick='enterChatroom("he")'>Chat</button-->
      </h5>
</div>

</div>
        
<script>
    function enterChatroom(sex){
        var xhttp = new XMLHttpRequest();
        var name= document.getElementById("nameField").value;
        if(name == "") {
            alert("assicurati di aver inserito il Nick!");
        } else {
            xhttp.open("POST", "/WebChat/ChatClient", true);
            xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xhttp.send("name="+name+"&"+"sex="+sex);
            window.location.assign("/WebChat/ChatClient");
        }
}
</script>

    </body>
    
</html>
