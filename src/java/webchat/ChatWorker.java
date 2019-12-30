package webchat;

/**
 * @author Prof-Matteo-Palitto-Peano
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

//Il websocket viene raggiunto usando <indirizzoIP:8080>/WebChat/WebSocket
@ServerEndpoint("/WebSocket") //con questa sintassi esprimo il fatto che questa e' la classe che ha a che fare con il websocket
public class ChatWorker implements InviaMessaggio, RiceviMessaggio {
    //istanzia il gestore dei messaggi condiviso tra tutti i ChatWorker (static)
    protected final static MessageManager gestoreMessaggi = new MessageManager();
    private RemoteEndpoint.Basic myClient; //connessione al mio client
    private String myUser;  //nome + sesso dell'utente che sto servendo
     
    //Questo metodo e' invocato dal metodo setNewMessaggio nella 
    //classe MessageManager. Ogni volta che un worker riceve messaggio da proprio
    //client, MessageManager richiede ad ogni worker di inviare il messaggio  
    //che e' stato appena ricevuto
    @Override  //dalla interfaccia InviaMessaggio
    synchronized public void sendMessaggio(String messaggio) {
        try {
            myClient.sendText(messaggio);
        } catch (IOException ex) {
            Logger.getLogger(ChatWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override //dalla interfaccia RiceviMessaggio
    public void messaggioReceived(String m) {
        this.gestoreMessaggi.sendNewMessaggio(m);
    }

    @OnOpen
    public void onOpen (Session session){
        //memorizzo connessione al mio(worker) client
        this.myClient = session.getBasicRemote();
        System.out.println(session.getId() + " opened a connection");
    }

    @OnMessage
    public void onMessage(String message, Session session){
        //nel caso di nuovo utente, lo aggiungo alla lista
        if((message.length()>7)&&message.substring(0,7).equals("newUser")) {
            this.myUser = message.substring(9);
            System.out.println("Adding new user: "+this.myUser);            
            //registro il worker con il gestore di messaggi
            gestoreMessaggi.addClient(this, myUser);
            messaggioReceived(message);
        }else{
            //il nuovo messaggio e' stato ricevuto dal client connesso al worker
            //e lo andiamo ad inserire
            //nella variabile "messaggio" della classe MessageManager
            //il quale aggiornera' la variabile e richiedera' l'invio a ogni
            //Worker, ognuno al proprio client
            messaggioReceived(message);
        }
    }

    @OnClose
    public void onClose (Session session){
        System.out.println("Removing user: "+this.myUser);            
        
        gestoreMessaggi.removeClient(myUser);
        messaggioReceived("rmUser::"+myUser);
    }

}
