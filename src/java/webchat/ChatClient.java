/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webchat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author matteo
 */
@WebServlet(name = "ChatWindow", urlPatterns = {"/ChatClient"})
public class ChatClient extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String user; //nome dell'utente
    String sesso; //il sesso dell'utente
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                                        throws ServletException, IOException {
        
    
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            String headerValue = request.getHeader(headerName);
//            System.out.println(headerName +": "+headerValue);
//        }
        
      
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            if(user == null) {
                System.out.println("sending REDIRECT");
                response.sendRedirect("http://localhost:8080/WebChat");
                out.close();
           }
           
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            htmlHead(out,new String[]{"/WebChat/w3.css"},"Chat Window");
            javascriptAddons(out,new String[]{"/WebChat/chatVisibility.js","/WebChat/chatSocket.js"});
            //request.getRequestDispatcher("/WebChat/loginForm.html").include(request, response);
            String relativeWebPath = "chatBoard.html";
String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
System.out.println(absoluteDiskPath);

            request.getRequestDispatcher("chatBoard.html").include(request, response);

            chatCore(out,"https://www.iispeano.edu.it/", user, sesso);
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("GET request received");
        System.out.println("USER: "+user);
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        user=null;
        System.out.println("POST request received");
        //System.out.println(request);
        user = request.getParameter("name");
        sesso = request.getParameter("sex");
        System.out.println(user);
        //processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


    /**
     * Loads up listed Javascript's from pre-written array
     * @param printWriter PrintWriter to use when calling the method
     * @param scriptLocations String[] containing links (local or external) to JS files
     */
    public static void javascriptAddons(PrintWriter printWriter, String[] scriptLocations){
        for (String script : scriptLocations) {  printWriter.println("<script type=\"text/javascript\" src = \"" + script +"\"></script>");  }
    }

    /**
     * Print out the HTML head including title and css content
     * @param printWriter PrintWriter to use when calling the method
     * @param cssLinks Any CSS to be included
     * @param title Title of the site
     */
    public static void htmlHead(PrintWriter printWriter,String[] cssLinks,String title){
        printWriter.println("<head>");

        // Set the title
        printWriter.println("<title>"+ title + "</title>");

        // Add any additional stylesheets necessary
        for (String cssLink : cssLinks){
            printWriter.println("<link rel=\"stylesheet\" type=\"text/css\" href=\""+ cssLink + "\" >");
        }
        printWriter.println("</head>");

    }

    /**
     * Core HTML for the Chat Window
     * @param printWriter PrintWriter to use when calling the method
     * @param exitLink Link to direct user to upon closing chat
     * @param user
     * @param sesso
     */
    public static void chatCore (PrintWriter printWriter, String exitLink, String user, String sesso){
        System.out.println("CORE-user: "+user);
        printWriter.println("<body onload='onEntry(\""+user+"\");'>");
        printWriter.println("<div id=\"user\">"+user+"</div>");

        
//        printWriter.println("<form name=\"loginName\" action=\"javascript:enterChatroom()\" id=\"loginForm\">");
//        printWriter.println("<label for=\"nameField\">Name: </label>");
//        printWriter.println("<input type=\"text\" name=\"nameField\" id=\"nameField\"/>");
//        printWriter.println("<input type=\"submit\" name=\"Enter Chat\" id=\"enterChat\"   value=\"Enter Chat\"/>");
//        printWriter.println("</form>");
        printWriter.println("<div id=\"backdrop\"> ");
        printWriter.println("<div class=\"w3-card\" id=\"menu\">");
        if(sesso.equals("she")) {
           printWriter.println("<p class=\"welcome\"> Welcome, my lady <b id=\""+ user + "\"><script>document.write(document.getElementById(\"user\").innerHTML);</script></b> </p>");
        } else {
           printWriter.println("<p class=\"welcome\"> Welcome, my lord <b id=\""+ user + "\"><script>document.write(document.getElementById(\"user\").innerHTML);</script></b> </p>");
        }
        printWriter.println("</div>");
        printWriter.println("<div class=\"w3-card\" id=\"userList\">---Users---</div>");
        printWriter.println("<div class=\"w3-card\" id=\"chatBox\">------------------------------ Chat ------------------------------<br></div>");
        printWriter.println("<br style=\"clear:both;\"/>");
        printWriter.println("<form class=\"w3-card\" name=\"message\" action='javascript:onSendMessage(\""+user+"\")' id=\"chatmsg\">");
        printWriter.println("<input name=\"usermsg\" type=\"text\" id=\"usermsg\" size=\"100\"/>");
        printWriter.println("<input class=\"w3-button w3-green\" name=\"sendmsg\" type=\"submit\" id=\"sendmsg\" value=\"Send\"/>");
        printWriter.println("<button class=\"w3-button w3-red\" onclick=\"window.close()\">Close</button>");
        printWriter.println("</form>");
        printWriter.println("</div>");
        printWriter.println("</body>");
    }    
}
