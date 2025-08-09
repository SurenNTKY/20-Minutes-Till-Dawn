package io.github.TillDawn.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.TillDawn.Controllers.SettingsMenuController;

public class SettingsMenuView implements Screen {
    private Stage stage;
    private final Skin skin;
    private final SettingsMenuController controller;

    private final Slider volumeSlider;
    private final CheckBox muteSfxCheckBox;
    private final SelectBox<String> musicSelectBox;
    private final CheckBox grayscaleCheckBox;
    private final CheckBox autoReloadCheckBox;
    private final TextButton backButton;

    public SettingsMenuView(SettingsMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;

        volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        muteSfxCheckBox = new CheckBox("Mute SFX", skin);
        musicSelectBox = new SelectBox<>(skin);
        musicSelectBox.setItems("Track 1", "Track 2", "Track 3");
        grayscaleCheckBox = new CheckBox("Black & White Mode", skin);
        autoReloadCheckBox = new CheckBox("Auto Reload", skin);
        backButton = new TextButton("Back", skin);

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTexture = new Texture(Gdx.files.internal("MyImage/Setting.png"));
        Image bgImage = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(new Label("Settings", skin, "title")).colspan(2).padBottom(20);
        table.row();
        table.add(new Label("Volume", skin)).left();
        table.add(volumeSlider).width(300);
        table.row().padTop(10);
        table.add(muteSfxCheckBox).colspan(2);
        table.row().padTop(10);
        table.add(new Label("Music", skin)).left();
        table.add(musicSelectBox).width(200);
        table.row().padTop(10);
        table.add(autoReloadCheckBox).colspan(2);
        table.row().padTop(10);
        table.add(grayscaleCheckBox).colspan(2);
        table.row().padTop(20);
        table.add(backButton).colspan(2);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(delta, 1/30f));
        stage.draw();
        controller.update();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public Slider getVolumeSlider() { return volumeSlider; }
    public CheckBox getMuteSfxCheckBox() { return muteSfxCheckBox; }
    public SelectBox<String> getMusicSelectBox() { return musicSelectBox; }
    public CheckBox getGrayscaleCheckBox() { return grayscaleCheckBox; }
    public CheckBox getAutoReloadCheckBox() { return autoReloadCheckBox; }
    public TextButton getBackButton() { return backButton; }
}
