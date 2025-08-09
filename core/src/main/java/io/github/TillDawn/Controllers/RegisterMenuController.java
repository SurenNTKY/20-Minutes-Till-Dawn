package io.github.TillDawn.Controllers;

import com.badlogic.gdx.Gdx;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.GameAssetManager;
import io.github.TillDawn.Models.SqliteUserDao;
import io.github.TillDawn.Models.User;
import io.github.TillDawn.Views.LoginMenuView;
import io.github.TillDawn.Views.RegisterMenuView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Pattern;

public class RegisterMenuController {
    private final SqliteUserDao dao;
    private RegisterMenuView view;
    private final Random random = new Random();

    public RegisterMenuController(SqliteUserDao dao) {
        this.dao = dao;
        dao.initialize();
    }

    public void setView(RegisterMenuView view) {
        this.view = view;
    }

    public void update() {
        if (view.getLoginLinkButton().isPressed()) {
            Gdx.app.postRunnable(() -> Main.getMain().setScreen(
                new LoginMenuView(
                    new LoginMenuController(dao),
                    GameAssetManager.getGameAssetManager().getSkin()
                )
            ));
        }
        if (view.getRegisterButton().isPressed()) {
            handleRegister();
        }
        if (view.getGuestButton().isPressed()) {
            Gdx.app.postRunnable(() -> {
                Gdx.app.log("RegisterMenu", "Logged in as guest");
                Main.getMain().setScreen(
                    new LoginMenuView(
                        new LoginMenuController(dao),
                        GameAssetManager.getGameAssetManager().getSkin()
                    )
                );
            });
        }
    }

    private void handleRegister() {
        String username = view.getUsername();
        String pass = view.getPassword();

        String confirm = view.getConfirmPassword();
        int qIndex = view.getSelectedQuestionIndex();
        String ans = view.getAnswer();

        if (username.isEmpty() || pass.isEmpty() || confirm.isEmpty() || ans.isEmpty()) {
            view.setFeedback("All fields are required.");
            return;
        }
        if (!pass.equals(confirm)) {
            view.setFeedback("Password and confirmation do not match.");
            return;
        }
        String strengthRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_])[A-Za-z\\d!@#$%^&*()_]{8,}$";
        if (!Pattern.matches(strengthRegex, pass)) {
            view.setFeedback("Password must be at least 8 characters, include an uppercase letter, a number, and a special character.");
            return;
        }
        if (dao.userExists(username)) {
            view.setFeedback("Username already exists.");
            return;
        }
        if (pass != null && !pass.isEmpty()) {
            pass = hashPassword(pass);
        }
        int avatarId = random.nextInt(12);

        User user = new User(username, pass, qIndex, ans, avatarId,0,0,0);
        boolean added = dao.addUser(user);
        if (added) {
            view.setFeedback("Registration successful! Redirecting to login...");
            Gdx.app.postRunnable(() -> {
                Main.getMain().setScreen(
                    new LoginMenuView(
                        new LoginMenuController(dao),
                        GameAssetManager.getGameAssetManager().getSkin()
                    )
                );
            });
        } else {
            view.setFeedback("Registration failed. Please try again.");
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
