/**
 * Javascript handling the WebSocket client code and chat/userlist display
 * 
 */
var connection;
function onEntry(sex, name){
    //alert(sex+" "+name);
    // stabilisci una nuova connessione con il server via WebSocket
    var loc = window.location;
    connection = new WebSocket("ws://"+loc.host+"/WebChat/WebSocket");
    // set onopen WebSocket action to send the login name to the Server
    connection.onopen = function (e){ connection.send("newUser::"+sex+"::"+name); };

    // Quando ricevo un messaggio determina il tipo di messaggio e scrivilo in
    // modo appropriato
    connection.onmessage = function(e){
        //alert(e.data.substring(0,9));
        if (e.data.substring(0,10).localeCompare("userList::")===0){  //nel caso sia la lista degli utenti
            var UL = document.getElementById("userList")
            var newUL = e.data.substring(10);
            //alert(newUL);
            UL.innerHTML = '<font size="+2" style="display:inline-block">User List: </font>' + newUL;
        } else if (e.data.substring(0,8).localeCompare("rmUser::")===0){ //nel caso un utente si sia disconnesso
            var MSG=document.getElementById("chatBox");
            var sex = e.data.substring(8,11);
            var name = e.data.substring(13);
            var msg = "DISCONNESSO";
            if(sex == "lei") { msg = "DISCONNESSA"; }
            var msg = "<div style='background-color:orange;display:inline-block;box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);border-radius: 5px'>" + name + " si e' "+msg+"</div>";
            MSG.innerHTML = MSG.innerHTML + msg + "<br>";
            overflowFix();
        } else if (e.data.substring(0,9).localeCompare("newUser::")===0){ //nel caso un nuovo utente si sia connesso
            var MSG=document.getElementById("chatBox");
            var sex = e.data.substring(9,12);
            var name = e.data.substring(14);
            var msg = "ENTRATO";
            if(sex == "lei") { msg = "ENTRATA"; }
            var msg = "<div style='background-color:orange;display:inline-block;box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);border-radius: 5px'>" + name + " e' "+msg+"</div>";
            MSG.innerHTML = MSG.innerHTML + msg + "<br>";
            overflowFix();
        }else { //in tutti gli altri casi lo considero un messaggio tra utenti
            var MSG=document.getElementById("chatBox");
            MSG.innerHTML = MSG.innerHTML + e.data.substring(7) + "<br>";
            overflowFix();
        }
    };
}

// Usa WebSocket per inviare il messaggio al server
function onSendMessage(sex, name){
  var msg = document.getElementById("usermsg").value;
      if(msg == "") alert("assicurati di inserire il testo da inviare");
      else {
        if(sex == "lui")
          msg = "newMsg " + name + ": <div style='background-color:lightblue;display:inline-block;box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);border-radius: 5px'>" + document.getElementById("usermsg").value + "</div>";
	else
          msg = "newMsg " + name + ": <div style='background-color:pink;display:inline-block;box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);border-radius: 5px'>" + document.getElementById("usermsg").value + "</div>";
        //alert(msg);
        connection.send(msg);
        document.getElementById("usermsg").value="";
      }
}

// Automatically indent the Chat Window
function overflowFix(){
    var element = document.getElementById("chatBox");
    element.scrollTop = element.scrollHeight;
}

//quando viene premuto il tasto ENTER/INVIO su tastiera, invia messaggio
function onEnterSendMessage(e) {
    if (e.keyCode === 13) {
        onSendMessage('<%out.print(sex);%>', '<%out.print(name);%>');
        return false;
    }
}