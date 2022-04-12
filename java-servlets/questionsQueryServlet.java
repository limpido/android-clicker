// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;



@WebServlet("/triviaquary")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class questionsQueryServlet extends HttpServlet {

   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      // Print an HTML page as the output of the query
      out.println("<html>");
      out.println("<head><title>Query Response</title></head>");
      out.println("<body>");

      try (
         // Step 1: Allocate a database 'Connection' object
         Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/triviaLib?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         Statement stmt = conn.createStatement();
      ) {
         String QuestionID = request.getParameter("searchtitle");
         //out.println("title:" +searchtitle+ "genre:" + searchgenre+ "price:"+searchprice);
         // Step 3: Execute a SQL SELECT query
         String sqlStr = null;

      
         sqlStr = "select * from qna ";

          if(sqlStr == null){
            out.println("<form method='get' action='http://localhost:9999/gameshop/gamesearch.html'>");
            out.println("<p><input type='submit' value='try again' />");
            out.println("</form>");
         }
         else{
            ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
               
            // Step 4: Process the query result set
            out.println("<form method='get' action='statsquary'>");   
            int count = 0;

             //print order button
            while(rset.next()) {
               // Print a paragraph <p>...</p> for each record
               out.println("<p><input type='checkbox' name='questionID' value="
                     + "'" + rset.getString("id") + "' />(Q" 
                     + rset.getString("id")+ ")"
                     + ":" + rset.getString("question")+ "<p>"
                     + "option 1: "+ rset.getString("answer1")+ "</p><p>" 
                     + "option 2: "+ rset.getString("answer2")+"</p><p>"
                     + "option 3: "+ rset.getString("answer3")+"</p><p>"
                     + "option 4: "+ rset.getString("answer4")+"</p><p>"
                     + "correctAnscorrect answer is "+ rset.getString("correctAns")+ "</p>");
               count++;
            }


            out.println("<p><input type='submit' value='Check Stats' />");
            
            out.println("</form>");
         }

         
      } catch(Exception ex) {
         out.println("<p>Error: " + ex.getMessage() + "</p>");
         out.println("<p>Check Tomcat console for details.</p>");
         ex.printStackTrace();
      }  // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK 7)
 
      out.println("</body></html>");
      out.close();
   }
}