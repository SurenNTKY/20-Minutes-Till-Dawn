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
import io.github.TillDawn.Controllers.MainMenuController;
import io.github.TillDawn.Models.User;

public class MainMenuView implements Screen {
    private final MainMenuController controller;
    private final Skin skin;
    private Stage stage;
    private User currentUser;


    private final TextButton settingsButton;
    private final TextButton profileButton;
    private final TextButton gamePreButton;
    private final TextButton scoreboardButton;
    private final TextButton hintButton;
    private final TextButton continueGameButton;
    private final TextButton logoutButton;
    private final Label usernameLabel;
    private final Label scoreLabel;
    private final Image avatarImage;

    public MainMenuView(MainMenuController controller, Skin skin, User user) {
        this.controller = controller;
        this.skin = skin;
        this.currentUser = user;

        settingsButton = new TextButton("Settings", skin);
        profileButton = new TextButton("Profile", skin);
        gamePreButton = new TextButton("Game Menu", skin);
        scoreboardButton = new TextButton("Scoreboard", skin);
        hintButton = new TextButton("Hint Menu", skin);
        continueGameButton = new TextButton("Continue", skin);
        logoutButton = new TextButton("Logout", skin);

        usernameLabel = new Label("Username: " + user.getUsername(), skin);
        scoreLabel = new Label("Score: " + user.getScore(), skin);
        avatarImage = new Image(new Texture(Gdx.files.internal("Avatar/avatar" + user.getAvatarId() + ".png")));

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTexture = new Texture(Gdx.files.internal("MyImage/MainMenuBackground.png"));
        Image bgImage = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().pad(20);

        Table topTable = new Table();
        Table userInfoTable = new Table();
        userInfoTable.left();
        userInfoTable.add(usernameLabel).left().padBottom(5).row();
        userInfoTable.add(scoreLabel).left();

        topTable.add(userInfoTable).expandX().left().padLeft(20);
        topTable.add(avatarImage).size(300).expandX().right().padRight(20);

        Table buttonLeftTable = new Table();
        buttonLeftTable.add(settingsButton).width(350).pad(5).row();
        buttonLeftTable.add(profileButton).width(350).pad(5).row();
        buttonLeftTable.add(gamePreButton).width(350).pad(5).row();
        buttonLeftTable.add(scoreboardButton).width(350).pad(5).row();

        Table buttonRightTable = new Table();
        buttonRightTable.add(hintButton).width(350).pad(5).row();
        buttonRightTable.add(continueGameButton).width(350).pad(5).row();
        buttonRightTable.add(logoutButton).width(350).pad(5).row();

        Table centerButtonsTable = new Table();
        centerButtonsTable.center().padTop(100);
        centerButtonsTable.add(buttonLeftTable).padRight(50);
        centerButtonsTable.add(buttonRightTable);

        mainTable.add(topTable).expandX().fillX().padBottom(30).colspan(2).row();
        mainTable.add(centerButtonsTable).colspan(2);

        stage.addActor(mainTable);
    }

    @Override public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
        controller.update();
    }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public TextButton getSettingsButton() { return settingsButton; }
    public TextButton getProfileButton() { return profileButton; }
    public TextButton getGamePreButton() { return gamePreButton; }
    public TextButton getScoreboardButton() { return scoreboardButton; }
    public TextButton getHintButton() { return hintButton; }
    public TextButton getContinueGameButton() { return continueGameButton; }
    public TextButton getLogoutButton() { return logoutButton; }
}
