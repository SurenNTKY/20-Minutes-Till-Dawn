package io.github.TillDawn.Controllers;

import io.github.TillDawn.Main;
import io.github.TillDawn.Models.SqliteUserDao;
import io.github.TillDawn.Models.User;
import io.github.TillDawn.Models.UserDao;
import io.github.TillDawn.Views.ScoreboardView;

import java.util.List;

public class ScoreboardController {
    private final UserDao dao;
    private ScoreboardView view;
    private String currentUsername;
    private User loggedInUser;

    private String orderBy = "score";
    private boolean ascending = false;

    public ScoreboardController(SqliteUserDao dao, String currentUsername) {
        this.dao = dao;
        this.currentUsername = currentUsername;
        dao.initialize();
        this.loggedInUser = Main.getMain().getCurrentUser();
    }

    public void setView(ScoreboardView view) {
        this.view = view;
    }

    public void update() {
        if (view.isSortByScorePressed()) {
            orderBy = "score";
            ascending = !ascending;
            updateView();
        } else if (view.isSortByUsernamePressed()) {
            orderBy = "username";
            ascending = !ascending;
            updateView();
        } else if (view.isSortByKillsPressed()) {
            orderBy = "kills";
            ascending = !ascending;
            updateView();
        } else if (view.isSortBySurvivalTimePressed()) {
            orderBy = "survivaltime";
            ascending = !ascending;
            updateView();
        }
        if (view.getBackButton().isPressed()) {
            Main.getMain().initMainMenu(loggedInUser);
            Main.getMain().goToMainMenu();
        }
    }

    public void updateView() {
        List<User> topUsers = ((SqliteUserDao)dao).getTopUsers(orderBy, ascending, 10);
        view.updateScoreboard(topUsers, currentUsername);
    }
}
