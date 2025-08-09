package io.github.TillDawn.Controllers;

import com.badlogic.gdx.Gdx;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.SqliteUserDao;
import io.github.TillDawn.Models.User;
import io.github.TillDawn.Models.UserDao;
import io.github.TillDawn.Models.GameAssetManager;
import io.github.TillDawn.Views.LoginMenuView;
import io.github.TillDawn.Views.MainMenuView;
import io.github.TillDawn.Views.RecoveryPasswordView;
import io.github.TillDawn.Views.RegisterMenuView;

import static io.github.TillDawn.Controllers.RegisterMenuController.hashPassword;

public class LoginMenuController {
    private final UserDao dao;
    private LoginMenuView view;
    private User user;

    public LoginMenuController(SqliteUserDao dao) {
        this.dao = dao;
        dao.initialize();
    }

    public void setView(LoginMenuView view) {
        this.view = view;
    }

    public void update() {
        if (view.getRegisterLinkButton().isPressed()) {
            String[] securityQuestions = {
                "🔑 Select Question",
                "What was your childhood nickname?",
                "What is the name of your favorite childhood friend?",
                "In what city did you meet your spouse?",
                "What street did you live on in third grade?"
            };

            Gdx.app.postRunnable(() -> Main.getMain().setScreen(
                new RegisterMenuView(
                    new RegisterMenuController((SqliteUserDao) dao),
                    GameAssetManager.getGameAssetManager().getSkin(), securityQuestions
                )
            ));
        }
        if (view.getLoginButton().isPressed()) {
            handleLogin();
        }
        if (view.getRecoverButton().isPressed()) {
            String[] securityQuestions = {
                "🔑 Select Question",
                "What was your childhood nickname?",
                "What is the name of your favorite childhood friend?",
                "In what city did you meet your spouse?",
                "What street did you live on in third grade?"
            };
            Gdx.app.postRunnable(() -> Main.getMain().setScreen(
                new RecoveryPasswordView(
                    new RecoveryPasswordController((SqliteUserDao) dao),
                    GameAssetManager.getGameAssetManager().getSkin(), securityQuestions
                )
            ));
        }
    }

    private void handleLogin() {
        String username = view.getUsername();
        String pass = view.getPassword();

        if (username.isEmpty() || pass.isEmpty()) {
            view.setFeedback("Please enter both username and password.");
            return;
        }
        if (!dao.userExists(username)) {
            view.setFeedback("Username not found.");
            return;
        }
        pass = hashPassword(pass);
        boolean valid = dao.validateUser(username, pass);
        if (!valid) {
            view.setFeedback("Incorrect password.");
            return;
        }
        User loggedInUser = dao.getUserByUsername(username);
        if (loggedInUser == null) {
            view.setFeedback("User data could not be loaded.");
            return;
        }
        Main.getMain().initMainMenu(loggedInUser);
        Main.getMain().goToMainMenu();
    }
}
