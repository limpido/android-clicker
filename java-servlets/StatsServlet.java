import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.google.gson.*;

@WebServlet("/stats")
public class StatsServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        System.out.println("post request received!!!!!");
        Database db = new Database();
        Helper helper = new Helper();
        try (
            Connection conn = db.connect();         
            ) {
            helper.addHeader(res);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            PrintWriter out = res.getWriter();

            String reqBody = helper.getBody(req);

            JsonObject answerState = new Gson().fromJson(reqBody, JsonObject.class);
            System.out.println(answerState);
            int id = answerState.get("id").getAsInt();
            int selectedOption = answerState.get("selectedOption").getAsInt();
            boolean isCorrect = answerState.get("isCorrect").getAsBoolean();

            JsonObject stats = db.getStats(id);
            switch (selectedOption) {
                case 1:
                    stats.addProperty("choice1", stats.get("choice1").getAsInt()+1);
                    break;
                case 2:
                    stats.addProperty("choice2", stats.get("choice2").getAsInt()+1);
                    break;
                case 3:
                    stats.addProperty("choice3", stats.get("choice3").getAsInt()+1);
                    break;
                case 4:
                    stats.addProperty("choice4", stats.get("choice4").getAsInt()+1);
                    break;
            }

            if (isCorrect) {
                stats.addProperty("numCorrect", stats.get("numCorrect").getAsInt()+1);
            }
            stats.addProperty("timesAsked", stats.get("timesAsked").getAsInt()+1);

            db.updateStats(stats);

            out.close();                      
            res.setStatus(HttpServletResponse.SC_OK);
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }


    }
}