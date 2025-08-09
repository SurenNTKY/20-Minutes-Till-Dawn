package io.github.TillDawn.Controllers;

import com.badlogic.gdx.Gdx;
import io.github.TillDawn.Controllers.Game.GameController;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.GameAssetManager;
import io.github.TillDawn.Models.User;
import io.github.TillDawn.Views.GameView;
import io.github.TillDawn.Views.PreGameMenuView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PreGameMenuController {
    private PreGameMenuView view;
    private User loggedInUser;

    private static final String HERO_DIR = "assets/Heroes";
    private static final String WEAPON_DIR = "assets/Weapons";

    public PreGameMenuController() {
        this.loggedInUser = Main.getMain().getCurrentUser();
    }

    public void setView(PreGameMenuView view) {
        this.view = view;
    }

    public void update() {
        if (view.getBackButton().isPressed()) {
            Main.getMain().initMainMenu(loggedInUser);
            Main.getMain().goToMainMenu();
        }
        if (view.getStartButton().isPressed()) {
            handleStartGame();
        }
    }

    private void handleStartGame() {
        String selectedHero = view.getSelectedHero();
        String selectedWeapon = view.getSelectedWeapon();
        int selectedTime = view.getSelectedTime();

        if (selectedHero == null || selectedHero.isEmpty()) {
            view.setFeedback("Please select a hero.");
            return;
        }
        if (selectedWeapon == null || selectedWeapon.isEmpty()) {
            view.setFeedback("Please select a weapon.");
            return;
        }
        if (selectedTime <= 0) {
            view.setFeedback("Please select a valid game duration.");
            return;
        }
            Main.getMain().getScreen().dispose();
            Main.getMain().setScreen(new GameView(new GameController(), GameAssetManager.getGameAssetManager().getSkin()));
        Gdx.app.log("PreGameMenu", "Starting game with Hero: " + selectedHero + ", Weapon: " + selectedWeapon + ", Time: " + selectedTime + " minutes");
    }

    public String[] getHeroList() {
        return listFilesInDir(HERO_DIR);
    }

    public String[] getWeaponList() {
        return listFilesInDir(WEAPON_DIR);
    }

    private String[] listFilesInDir(String dir) {
        File folder = new File(Gdx.files.internal(dir).file().getAbsolutePath());
        if (!folder.exists() || !folder.isDirectory()) {
            Gdx.app.error("PreGameMenu", "Directory not found: " + dir);
            return new String[0];
        }
        File[] files = folder.listFiles((f) -> f.isFile());
        if (files == null) return new String[0];
        List<String> names = new ArrayList<>();
        for (File file : files) {
            String name = file.getName();
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) name = name.substring(0, dotIndex);
            names.add(name);
        }
        return names.toArray(new String[0]);
    }
}
