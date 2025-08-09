package io.github.TillDawn.Controllers;

import com.badlogic.gdx.Gdx;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.SqliteUserDao;
import io.github.TillDawn.Views.LoginMenuView;
import io.github.TillDawn.Views.RecoveryPasswordView;
import io.github.TillDawn.Models.GameAssetManager;

import static io.github.TillDawn.Controllers.RegisterMenuController.hashPassword;

public class RecoveryPasswordController {
    private final SqliteUserDao dao;
    private RecoveryPasswordView view;

    public RecoveryPasswordController(SqliteUserDao dao) {
        this.dao = dao;
        dao.initialize();
    }

    public void setView(RecoveryPasswordView view) {
        this.view = view;
    }

    public void update() {
        if (view.getConfirmButton().isPressed()) {
            String username = view.getUsername();
            int questionIndex = view.getSelectedQuestionIndex();
            String answer = view.getAnswer();
            String newPassword = view.getNewPassword();

            if (!dao.userExists(username)) {
                view.setFeedback("Username not found.");
                return;
            }
            if (!dao.checkSecurityAnswer(username, questionIndex, answer)) {
                view.setFeedback("Incorrect answer to security question.");
                return;
            }
            if (newPassword.length() < 6) {
                view.setFeedback("Password must be at least 6 characters.");
                return;
            }
            String hashed = hashPassword(newPassword);
            boolean updated = dao.updatePassword(username, hashed);
            if (updated) {
                view.setFeedback("Password updated successfully.");
                Gdx.app.postRunnable(() -> Main.getMain().setScreen(
                    new LoginMenuView(new LoginMenuController(dao), GameAssetManager.getGameAssetManager().getSkin())
                ));
            } else {
                view.setFeedback("Failed to update password. Try again.");
            }
        }

        if (view.getBackButton().isPressed()) {
            Gdx.app.postRunnable(() -> Main.getMain().setScreen(
                new LoginMenuView(new LoginMenuController(dao), GameAssetManager.getGameAssetManager().getSkin())
            ));
        }
    }
}
