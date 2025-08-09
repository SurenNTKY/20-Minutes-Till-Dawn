package io.github.TillDawn.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.GameAssetManager;
import io.github.TillDawn.Models.SqliteUserDao;
import io.github.TillDawn.Models.User;
import io.github.TillDawn.Views.*;

public class MainMenuController {
    private final SqliteUserDao dao;
    private MainMenuView view;
    private User user;

    public MainMenuController(SqliteUserDao dao) {
        this.dao = dao;
        dao.initialize();
        this.user = Main.getMain().getCurrentUser();
    }

    public void setView(MainMenuView view) {
        this.view = view;
    }

    public void update() {
        if (view.getSettingsButton().isPressed()) {
            Gdx.app.postRunnable(() -> {
                Main.getMain().setScreen(
                    new SettingsMenuView(
                        new SettingsMenuController(),
                        GameAssetManager.getGameAssetManager().getSkin()
                    )
                );
            });
        }


        if (view.getProfileButton().isPressed()) {
            Gdx.app.postRunnable(() -> {
                Main.getMain().setScreen(
                    new ProfileMenuView(
                        new ProfileMenuController(dao),
                        GameAssetManager.getGameAssetManager().getSkin()
                    )
                );
            });
        }

        if (view.getGamePreButton().isPressed()) {
            Gdx.app.postRunnable(() -> {
                Main.getMain().setScreen(
                    new PreGameMenuView(
                        new PreGameMenuController(),
                        GameAssetManager.getGameAssetManager().getSkin()
                    )
                );
            });
        }

        if (view.getScoreboardButton().isPressed()) {
            Gdx.app.postRunnable(() -> {
                Main.getMain().setScreen(
                    new ScoreboardView(
                        new ScoreboardController(dao, user.getUsername()),
                        GameAssetManager.getGameAssetManager().getSkin()
                    )
                );
            });
        }

        if (view.getHintButton().isPressed()) {
            Gdx.app.postRunnable(() -> {
                Main.getMain().setScreen(
                    new HintMenuView(
                        new HintMenuController(),
                        GameAssetManager.getGameAssetManager().getSkin()
                    )
                );
            });
        }

        if (view.getContinueGameButton().isPressed()) {
            Gdx.app.postRunnable(() -> {

            });
        }

        if (view.getLogoutButton().isPressed()) {
            Gdx.app.postRunnable(() -> {

            });
        }
    }

    public MainMenuView getView() {
        return view;
    }

}
