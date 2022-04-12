// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;



@WebServlet("/statsquary")   // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class statrsQueryServlet extends HttpServlet {

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
         String[] questionID = request.getParameterValues("questionID");

         if (questionID != null) {
         //out.println("title:" +searchtitle+ "genre:" + searchgenre+ "price:"+searchprice);
         // Step 3: Execute a SQL SELECT query
         String sqlStr;
            int count;
            double cost = 0;
            ResultSet rset;
            out.println("<p>your stats:</p>");
            for (int x=0; x<questionID.length; ++x){//get's game data and adds to cart 
               sqlStr = "select * from qna where id = " + questionID[x];
               //out.println("<p>" + sqlStr + "</p>");  // for debugging
               rset =  stmt.executeQuery(sqlStr);
               rset.next();
               out.println("<p>(" +rset.getString("id")+ ")" +rset.getString("question")+ "</p>");

               sqlStr = "select * from Stats where id = " + questionID[x];
               //out.println("<p>" + sqlStr + "</p>");  // for debugging
               rset =  stmt.executeQuery(sqlStr);
               rset.next();
               out.println("<p>"+"option 1 times selected = "+ rset.getString("choice1")+"</p><p>"
               +"option 2 times selected = "+ rset.getString("choice2")+"</p><p>"
               +"option 3 times selected = "+ rset.getString("choice3")+"</p><p>"
               +"option 4 times selected = "+ rset.getString("choice4")+"</p><p>"
               +"times correct = "+ rset.getString("numCorrect")+"</p><p>"
               +"times asked = "+ rset.getString("timesAsked")+ "</p>");
            }
      
          
        
         
         out.println("<form method='get' action='http://localhost:9999/clicker/triviaquary'>");//rstart search
         out.println("<p><input type='submit' value='search for more questions' />");
         out.println("</form>");
         }else {
            out.println("<h3>Please go back and select questions...</h3>");
            out.println("<form method='get' action='http://localhost:9999/clicker/triviaquary'>");//rstart search
            out.println("<p><input type='submit' value='continue searching' />");
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