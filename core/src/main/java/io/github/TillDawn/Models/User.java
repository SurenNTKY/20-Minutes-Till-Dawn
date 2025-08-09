package io.github.TillDawn.Models;

public class User {
    private String username;

    private String password;
    private int securityQuestionIndex;
    private String securityAnswer;
    private int avatarId;
    private int score;
    private int kills;
    private int survivalTime;

    public User(String username, String password, int securityQuestionIndex, String securityAnswer, int avatarId, int score, int kills, int survivalTime) {
        this.username = username;
        this.password = password;
        this.securityQuestionIndex = securityQuestionIndex;
        this.securityAnswer = securityAnswer;
        this.avatarId = avatarId;
        this.score = score;
        this.kills = kills;
        this.survivalTime = survivalTime;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getSecurityQuestionIndex() {
        return securityQuestionIndex;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public int getSurvivalTime() { return survivalTime; }

    public void setSurvivalTime(int survivalTime) { this.survivalTime = survivalTime; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public void setSecurityQuestionIndex(int securityQuestionIndex) {
        this.securityQuestionIndex = securityQuestionIndex;
    }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

    public int getKills() { return kills; }
    public void setKills(int kills) { this.kills = kills; }
}
