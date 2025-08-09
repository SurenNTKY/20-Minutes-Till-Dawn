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
import io.github.TillDawn.Controllers.PreGameMenuController;

public class PreGameMenuView implements Screen {
    private Stage stage;
    private final Skin skin;
    private final PreGameMenuController controller;

    private SelectBox<String> heroSelectBox;
    private SelectBox<String> weaponSelectBox;
    private SelectBox<String> timeSelectBox;
    private TextButton startButton;
    private Label feedbackLabel;
    private final TextButton backButton;

    public PreGameMenuView(PreGameMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        controller.setView(this);
        backButton = new TextButton("Back", skin);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTexture = new Texture(Gdx.files.internal("MyImage/PreGame.png"));
        Image bgImage = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("Pre-Game Menu", skin);
        title.setFontScale(1.5f);

        heroSelectBox = new SelectBox<>(skin);
        heroSelectBox.setItems(controller.getHeroList());

        weaponSelectBox = new SelectBox<>(skin);
        weaponSelectBox.setItems(controller.getWeaponList());

        timeSelectBox = new SelectBox<>(skin);
        timeSelectBox.setItems("2", "5", "10", "20");

        startButton = new TextButton("Start Game", skin);
        feedbackLabel = new Label("", skin);

        table.add(title).colspan(2).padBottom(30);
        table.row();

        table.add(new Label("Select Hero:", skin)).right().padRight(10);
        table.add(heroSelectBox).width(200);
        table.row().padTop(15);

        table.add(new Label("Select Weapon:", skin)).right().padRight(10);
        table.add(weaponSelectBox).width(200);
        table.row().padTop(15);

        table.add(new Label("Select Time (minutes):", skin)).right().padRight(10);
        table.add(timeSelectBox).width(200);
        table.row().padTop(30);

        table.add(startButton).colspan(2).width(200).height(40);
        table.row().padTop(20);

        table.add(feedbackLabel).colspan(2);

        table.row().padTop(20);
        table.add(backButton).colspan(2);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
        controller.update();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public String getSelectedHero() {
        return heroSelectBox.getSelected();
    }

    public String getSelectedWeapon() {
        return weaponSelectBox.getSelected();
    }

    public int getSelectedTime() {
        try {
            return Integer.parseInt(timeSelectBox.getSelected());
        } catch (NumberFormatException e) {
            return 5;
        }
    }

    public TextButton getStartButton() {
        return startButton;
    }

    public void setFeedback(String msg) {
        feedbackLabel.setText(msg);
    }

    public TextButton getBackButton() { return backButton; }
}
