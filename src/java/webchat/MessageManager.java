package webchat;

import java.io.*; 
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Future;
import javax.servlet.*; 

//Per ogni utente memorizzo anche il worker associato
class User {
    String user;  //memorizza il nome e il sesso dell'utente
    ChatWorker worker;  //memorizza il worker associato all'utente
    
    User(ChatWorker worker, String user) {
        this.user = user;
        this.worker = worker;
    }
}

public class MessageManager { 
    int count;
    
    //ultimo messaggio inviato dai Clients
    private String messaggio;
    //lista degli utenti e dei workers associati, uno per ogni Client connesso
    private LinkedList<User> users = new LinkedList<>();
    
    void MessageManager() {}
    
    //Invia la lista degli utenti connessi
    void sendUserList(){
        if(! users.isEmpty()) {
            String ul = "userList::";

            for (User user: this.users) {
                System.out.println("UL: " + user.user);
                ul += "<div style='display:inline-block'><img src='/WebChat/img_"+user.user.substring(0,3)+".png' alt='Avatar' height=30' width='30'>"+user.user.substring(5)+"</div>";
            }    
            sendNewMessaggio(ul);
        }
    }
    
    //aggiungo utente e il suo worker alla lista
    void addClient(ChatWorker worker, String user) {
        User newUser = new User(worker, user);
        this.users.add(newUser);
        sendUserList();
    }
    
    //rimuovo utente dalla lista
    void removeClient(String user2remove) {
        
        for(Iterator<User> iter = users.iterator(); iter.hasNext();) {
            User data = iter.next();
            if (data.user == user2remove) {
                iter.remove();
            }
        }        
        sendUserList();
    }
    
    //chiamata dai vari workers quando ricevono un messaggio dal proprio client.
    //questo metodo e' sycronized per evitare conflitti tra workers
    //che desiderano accedere alla stessa risorsa (cioe' nel caso in cui
    // vengono ricevuti simultaneamente i messaggi da piu' clients)
    void sendNewMessaggio(String m) {
        //aggiorna l'ultimo messaggio nella variabile dell'oggetto
        this.messaggio = m;
        //chiedi ad ogni worker di inviare il messaggio ricevuto
        for (User user: this.users) {
            user.worker.sendMessaggio(this.messaggio);
        }
    }

}

//questa interfaccia deve essere implementata da tutti i threads che vogliono
//inviare il nuovo messaggio
interface InviaMessaggio {
    //questo metodo conterra' il codice da eseguire da ogni worker per inviare
    //il messaggio al proprio client
    public void sendMessaggio(String m);
}

//questa interfaccia deve essere implementata da tutti i threads che vogliono
//notificare la ricezione di un messaggio dal proprio client per poi poterlo  
//inviare a tutti i clients tramite i relativi workers
interface RiceviMessaggio { 

    //La notifica viene generata chiamando il suguente metodo          
    public void messaggioReceived(String m);

}    
