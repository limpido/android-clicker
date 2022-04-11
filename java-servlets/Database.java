import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.sql.*;
import com.google.gson.*;
import org.apache.commons.text.StringSubstitutor;
import java.util.*;


public class Database {
	private Connection conn;
	private Statement stmt;

	public Connection connect() throws SQLException {
		try {
			this.conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/TriviaLib?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
				"myuser", "xxxx");
			this.stmt = conn.createStatement();
			return this.conn;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public JsonArray getQuestions() throws SQLException {
		try {
			String query = "SELECT * FROM qna;";
			ResultSet rset = this.stmt.executeQuery(query);
			JsonArray questions = new JsonArray();
			while (rset.next()) {
				JsonObject questionObj = new JsonObject();
				questionObj.addProperty("questionId", rset.getInt("id"));
				questionObj.addProperty("description", rset.getString("question"));
				questionObj.addProperty("correctAns", rset.getInt("correctAns"));

				JsonArray options = new JsonArray();
				for (int i=1; i<5; i++) {
					JsonObject option = new JsonObject();
					option.addProperty("index", i);
					option.addProperty("description", rset.getString("answer"+ i));
					options.add(option);
				}
				questionObj.add("options", options);
				
				questions.add(questionObj);
			}
			return questions;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public void updateStats(JsonObject stats) throws SQLException {
		try {
			Map<String, String> values = new HashMap<>();
			values.put("id", stats.get("id").getAsString());
			values.put("choice1", stats.get("choice1").getAsString());
			values.put("choice2", stats.get("choice2").getAsString());
			values.put("choice3", stats.get("choice3").getAsString());
			values.put("choice4", stats.get("choice4").getAsString());
			values.put("numCorrect", stats.get("numCorrect").getAsString());
			values.put("timesAsked", stats.get("timesAsked").getAsString());
			StringSubstitutor sub = new StringSubstitutor(values);
			String query = sub.replace("INSERT INTO Stats (id, choice1, choice2, choice3, choice4, numCorrect, timesAsked) VALUES (${id}, ${choice1}, ${choice2}, ${choice3}, ${choice3}, ${numCorrect}, ${timesAsked}) ON DUPLICATE KEY UPDATE choice1=${choice1}, choice2=${choice2}, choice3=${choice3}, choice4=${choice4}, numCorrect=${numCorrect}, timesAsked=${timesAsked};");
			System.out.println(query);
			this.stmt.executeUpdate(query);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public JsonObject getStats(int id) throws SQLException {
		try {
			Map<String, String> values = new HashMap<>();
			values.put("id", Integer.toString(id));
			StringSubstitutor sub = new StringSubstitutor(values);
			String query = sub.replace("SELECT * FROM Stats WHERE id=${id};");
			ResultSet rset = this.stmt.executeQuery(query);			
			rset.next();

			JsonObject stats = new JsonObject();
			stats.addProperty("id", rset.getInt("id"));
			stats.addProperty("choice1", rset.getInt("choice1"));
			stats.addProperty("choice2", rset.getInt("choice2"));
			stats.addProperty("choice3", rset.getInt("choice3"));
			stats.addProperty("choice4", rset.getInt("choice4"));
			stats.addProperty("numCorrect", rset.getInt("numCorrect"));
			stats.addProperty("timesAsked", rset.getInt("timesAsked"));
			return stats;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}