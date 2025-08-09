package io.github.TillDawn.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.SqliteUserDao;
import io.github.TillDawn.Models.User;
import io.github.TillDawn.Views.ProfileMenuView;

public class ProfileMenuController {
    private final SqliteUserDao dao;
    private ProfileMenuView view;
    private User loggedInUser;


    public ProfileMenuController(SqliteUserDao dao) {
        this.dao = dao;
        dao.initialize();
        this.loggedInUser = Main.getMain().getCurrentUser();
    }

    public void setView(ProfileMenuView view) {
        this.view = view;
        view.setAvatarOptions(dao.getAvailableAvatars());
        view.updateCurrentUsername(dao.getCurrentUsername());
    }

    public ClickListener usernameListener() {
        return new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                String newName = view.getNewUsername();
                String current = dao.getCurrentUsername();
                if (newName.isEmpty()) {
                    view.setUsernameFeedback("Username cannot be empty.");
                } else if (dao.userExists(newName)) {
                    view.setUsernameFeedback("Username is already taken.");
                } else if (dao.updateUsername(current, newName)) {
                    view.setUsernameFeedback("Username changed.");
                    view.updateCurrentUsername(newName);
                } else {
                    view.setUsernameFeedback("Error changing username.");
                }
            }
        };
    }

    public ClickListener passwordListener() {
        return new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                String user = dao.getCurrentUsername();
                if (!dao.validateUser(user, hash(view.getCurrentPassword()))) {
                    view.setPasswordFeedback("Incorrect current password.");
                } else if (!isStrong(view.getNewPassword())) {
                    view.setPasswordFeedback("Password too simple.");
                } else if (dao.updatePassword(user, hash(view.getNewPassword()))) {
                    view.setPasswordFeedback("Password updated.");
                } else {
                    view.setPasswordFeedback("Error updating password.");
                }
            }
        };
    }

    public ClickListener deleteListener() {
        return new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                String user = dao.getCurrentUsername();
                if (dao.deleteUser(user)) {
                    view.setDeleteFeedback("Account deleted.");
                    com.badlogic.gdx.Gdx.app.postRunnable(() ->
                        io.github.TillDawn.Main.getMain().setScreen(
                            new io.github.TillDawn.Views.LoginMenuView(
                                new io.github.TillDawn.Controllers.LoginMenuController(dao),
                                io.github.TillDawn.Models.GameAssetManager.getGameAssetManager().getSkin()
                            )
                        )
                    );
                } else {
                    view.setDeleteFeedback("Error deleting account.");
                }
            }
        };
    }

    public ClickListener fileChooserListener() {
        return new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                String user = dao.getCurrentUsername();
                if (dao.selectAvatarFromFileSystem(user)) {
                    view.setAvatarFeedback("Avatar updated from file.");
                } else {
                    view.setAvatarFeedback("Error selecting file.");
                }
            }
        };
    }

    public ChangeListener avatarListListener() {
        return new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String user = dao.getCurrentUsername();
                int selectedId = Integer.parseInt(view.getSelectedAvatarName());
                if (dao.updateAvatarById(user, selectedId)) {
                    view.setAvatarFeedback("Avatar set to #" + selectedId);
                    view.updateAvatarImage(selectedId);
                } else {
                    view.setAvatarFeedback("Error setting avatar.");
                }
            }
        };
    }

    public ClickListener backListener() {
        return new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {

                Gdx.app.log("ProfileMenu", "Back button clicked");
                Main.getMain().initMainMenu(loggedInUser);
                Main.getMain().goToMainMenu();
            }
        };
    }

    public DragAndDrop.Payload createDragPayload() {
        DragAndDrop.Payload payload = new DragAndDrop.Payload();
        payload.setObject(view.getSelectedAvatarName());
        return payload;
    }

    public void handleAvatarDrop(DragAndDrop.Payload payload) {
        String user = dao.getCurrentUsername();
        int avatarId = Integer.parseInt(payload.getObject().toString());
        if (dao.updateAvatarById(user, avatarId)) {
            view.setAvatarFeedback("Avatar updated via drag & drop.");
            view.updateAvatarImage(avatarId);
        } else {
            view.setAvatarFeedback("Error updating via drag & drop.");
        }
    }

    private boolean isStrong(String pwd) {
        return pwd.length() >= 8 && pwd.matches(".*\\d.*") && pwd.matches(".*[A-Z].*");
    }
    private String hash(String in) {
        return io.github.TillDawn.Controllers.RegisterMenuController.hashPassword(in);
    }
}
