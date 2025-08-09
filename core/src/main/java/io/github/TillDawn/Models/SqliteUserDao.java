package io.github.TillDawn.Models;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteUserDao implements UserDao {
    private static final String DB_NAME = "users.db";
    private static final String TABLE_NAME = "users";
    private Connection connection;
    private String currentUsername;

    @Override
    public void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbPath = Gdx.files.external(DB_NAME).path();
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Gdx.app.log("SqliteUserDao", "Database connection established to: " + dbPath);

            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "username TEXT PRIMARY KEY," +
                "password TEXT NOT NULL," +
                "security_question_index INTEGER NOT NULL," +
                "security_answer TEXT NOT NULL," +
                "avatar_id INTEGER NOT NULL," +
                "score INTEGER DEFAULT 0," +
                "kills INTEGER DEFAULT 0," +
                "survival_time INTEGER DEFAULT 0" +
                ");";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableSQL);
                Gdx.app.log("SqliteUserDao", "Table '" + TABLE_NAME + "' ensured to exist.");
            }
        } catch (ClassNotFoundException e) {
            Gdx.app.error("SqliteUserDao", "SQLite JDBC driver not found.", e);
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error initializing database.", e);
        }
    }

    @Override
    public boolean addUser(User user) {
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return false;
        }
        if (userExists(user.getUsername())) {
            Gdx.app.log("SqliteUserDao", "User already exists: " + user.getUsername());
            return false;
        }

        String insertSQL = "INSERT INTO " + TABLE_NAME + " (username, password, security_question_index, security_answer, avatar_id) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getSecurityQuestionIndex());
            pstmt.setString(4, user.getSecurityAnswer());
            pstmt.setInt(5, user.getAvatarId());
            pstmt.executeUpdate();
            Gdx.app.log("SqliteUserDao", "User added: " + user.getUsername());
            return true;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error adding user: " + user.getUsername(), e);
            return false;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return null;
        }


        String selectSQL = "SELECT username, password, security_question_index, security_answer, avatar_id, score, kills, survival_time FROM " + TABLE_NAME + " WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String fetchedUsername = rs.getString("username");
                String fetchedPassword = rs.getString("password");
                int fetchedQuestionIndex = rs.getInt("security_question_index");
                String fetchedAnswer = rs.getString("security_answer");
                int fetchedAvatarId = rs.getInt("avatar_id");
                int fetchedScore = rs.getInt("score");
                int fetchedKills = rs.getInt("kills");
                int fetchedSurvivalTime = rs.getInt("survival_time");
                return new User(fetchedUsername, fetchedPassword, fetchedQuestionIndex, fetchedAnswer, fetchedAvatarId, fetchedScore, fetchedKills, fetchedSurvivalTime);
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error getting user: " + username, e);
        }
        return null;
    }

    @Override
    public boolean validateUser(String username, String password) {
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return false;
        }

        String selectSQL = "SELECT password FROM " + TABLE_NAME + " WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                boolean valid = rs.getString("password").equals(password);
                if (valid) {
                    this.currentUsername = username;
                }
                return valid;
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error validating user: " + username, e);
        }
        return false;
    }

    @Override
    public boolean userExists(String username) {
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return false;
        }

        String selectSQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error checking if user exists: " + username, e);
        }
        return false;
    }

    public boolean checkSecurityAnswer(String username, int questionIndex, String answer) {
        String selectSQL = "SELECT security_question_index, security_answer FROM " + TABLE_NAME + " WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int storedIndex = rs.getInt("security_question_index");
                String storedAnswer = rs.getString("security_answer");
                return storedIndex == questionIndex && storedAnswer.equalsIgnoreCase(answer.trim());
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error checking security answer for user: " + username, e);
        }
        return false;
    }

    public boolean updatePassword(String username, String newPassword) {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET password = ? WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error updating password for user: " + username, e);
            return false;
        }
    }

    public boolean updateUsername(String oldUsername, String newUsername) {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET username = ? WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, oldUsername);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error updating username: " + oldUsername, e);
            return false;
        }
    }

    public boolean deleteUser(String username) {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error deleting user: " + username, e);
            return false;
        }
    }

    public boolean selectAvatarFromFileSystem(String username) {
        List<String> avatars = getAvailableAvatars();
        if (avatars.isEmpty()) return false;
        int avatarId = Integer.parseInt(avatars.get(0));
        return updateAvatarById(username, avatarId);
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public List<String> getAvailableAvatars() {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            ids.add(String.valueOf(i));
        }
        return ids;
    }

    public boolean updateAvatarById(String username, int avatarId) {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET avatar_id = ? WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setInt(1, avatarId);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error updating avatar_id for user: " + username, e);
            return false;
        }
    }

    public List<User> getTopUsers(String orderBy, boolean ascending, int limit) {
        List<User> users = new ArrayList<>();
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return users;
        }

        String validOrderBy;
        switch(orderBy.toLowerCase()) {
            case "score": validOrderBy = "score"; break;
            case "username": validOrderBy = "username"; break;
            case "kills": validOrderBy = "kills"; break;
            case "survivaltime": validOrderBy = "survival_time"; break;
            default: validOrderBy = "score"; break;
        }

        String ascDesc = ascending ? "ASC" : "DESC";

        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + validOrderBy + " " + ascDesc + " LIMIT ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("security_question_index"),
                    rs.getString("security_answer"),
                    rs.getInt("avatar_id"),
                    rs.getInt("score"),
                    rs.getInt("kills"),
                    rs.getInt("survival_time")
                ));
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error fetching top users.", e);
        }
        return users;
    }

    @Override
    public void dispose() {
        if (connection != null) {
            try {
                connection.close();
                Gdx.app.log("SqliteUserDao", "Database connection closed.");
            } catch (SQLException e) {
                Gdx.app.error("SqliteUserDao", "Error closing database connection.", e);
            }
        }
    }
}
