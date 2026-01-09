package amoba.io;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Adatbazis {
    private static final Logger logger = LoggerFactory.getLogger(Adatbazis.class);
    private static final String DB_URL = "jdbc:h2:./highscore_db";
    private static final String USER = "sa";
    private static final String PASS = "";

    public Adatbazis() {
        initTable();
    }

    private void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS highscore (name VARCHAR(255) PRIMARY KEY, wins INT DEFAULT 0)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error("Hiba az adatbázis inicializálásakor", e);
        }
    }

    public void addWin(String playerName) {
        String mergeSql = "MERGE INTO highscore (name, wins) KEY (name) VALUES (?, COALESCE((SELECT wins FROM highscore WHERE name = ?), 0) + 1)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(mergeSql)) {
            pstmt.setString(1, playerName);
            pstmt.setString(2, playerName);
            pstmt.executeUpdate();
            logger.info("Győzelem rögzítve az adatbázisban: {}", playerName);
        } catch (SQLException e) {
            logger.error("Nem sikerült menteni a győzelmet", e);
        }
    }

    public List<String> getHighScores() {
        List<String> results = new ArrayList<>();
        String sql = "SELECT name, wins FROM highscore ORDER BY wins DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                int wins = rs.getInt("wins");
                results.add(name + ": " + wins + " győzelem");
            }
        } catch (SQLException e) {
            logger.error("Hiba a ranglista lekérdezésekor", e);
        }
        return results;
    }

    public void exportToHtml() {
        String htmlFile = "highscore.html";
        List<String> scores = getHighScores();

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Amőba Bajnokság</title>");
        html.append("<style>");
        html.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f9; display: flex; justify-content: center; padding-top: 50px; }");
        html.append(".container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 500px; text-align: center; }");
        html.append("h1 { color: #2c3e50; margin-bottom: 20px; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("th { background-color: #3498db; color: white; padding: 12px; text-align: left; }");
        html.append("td { padding: 12px; border-bottom: 1px solid #ddd; text-align: left; }");
        html.append("tr:nth-child(even) { background-color: #f2f2f2; }");
        html.append("tr:hover { background-color: #e1f5fe; }");
        html.append(".footer { margin-top: 20px; font-size: 12px; color: #888; }");
        html.append("</style>");
        html.append("</head><body>");

        html.append("<div class='container'>");
        html.append("<h1> Ranglista </h1>");
        html.append("<table><thead><tr><th>Helyezés</th><th>Játékos</th><th>Győzelmek</th></tr></thead><tbody>");

        int rank = 1;
        for (String line : scores) {
            try {
                String[] parts = line.split(": ");
                String name = parts[0];
                String wins = parts[1].replace(" győzelem", "");

                html.append("<tr>");

                html.append("<td>").append(rank).append(".</td>");

                html.append("<td>").append(name).append("</td>");

                html.append("<td>").append(wins).append("</td>");

                html.append("</tr>");
                rank++;
            } catch (Exception e) {
                logger.warn("Hiba a HTML sor generálásakor: " + line);
            }
        }

        html.append("</tbody></table>");
        html.append("<div class='footer'>Utolsó frissítés: " + new java.util.Date() + "</div>");
        html.append("</div></body></html>");

        try {
            java.nio.file.Files.writeString(java.nio.file.Path.of(htmlFile), html.toString());
            logger.info("Webes ranglista sikeresen frissítve: {}", htmlFile);
        } catch (Exception e) {
            logger.error("Nem sikerült a HTML fájl mentése", e);
        }
    }
}