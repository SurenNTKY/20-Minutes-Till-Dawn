package io.github.TillDawn.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.User;
import io.github.TillDawn.Views.SettingsMenuView;

public class SettingsMenuController {
    private SettingsMenuView view;
    private final Music music;
    private User loggedInUser;

    public SettingsMenuController() {
        this.music = Main.getMain().getBackgroundMusic();
        this.loggedInUser = Main.getMain().getCurrentUser();
    }

    public void setView(SettingsMenuView view) {
        this.view = view;
    }

    public void update() {
        if (view.getVolumeSlider().isDragging()) {
            music.setVolume(view.getVolumeSlider().getValue());
        }
        if (view.getMuteSfxCheckBox().isChecked()) {
        }
        String selected = view.getMusicSelectBox().getSelected();
        if (selected != null) {
            switchMusic(selected);
        }
        if (view.getGrayscaleCheckBox().isChecked()) {
        }
        if (view.getAutoReloadCheckBox().isChecked()) {
        }
        if (view.getBackButton().isPressed()) {
            Main.getMain().initMainMenu(loggedInUser);
            Main.getMain().goToMainMenu();
        }
    }

    private void switchMusic(String track) {
        Music newMusic = Gdx.audio.newMusic(
            Gdx.files.internal("Audio/" + track + ".mp3")
        );
        newMusic.setLooping(true);
        newMusic.setVolume(view.getVolumeSlider().getValue());
        newMusic.play();
        Main.getMain().setBackgroundMusic(newMusic);
    }
}
