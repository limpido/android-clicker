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


}