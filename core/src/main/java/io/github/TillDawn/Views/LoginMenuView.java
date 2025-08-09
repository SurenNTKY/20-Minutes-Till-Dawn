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
import io.github.TillDawn.Controllers.LoginMenuController;

public class LoginMenuView implements Screen {
    private Stage stage;
    private final TextButton registerLinkButton;
    private final TextField usernameField;
    private final TextField passwordField;
    private final TextButton loginButton;
    private final TextButton recoverButton;
    private final Label feedbackLabel;
    private final Skin skin;
    private final LoginMenuController controller;

    public LoginMenuView(LoginMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.registerLinkButton = new TextButton("Register", skin);
        this.usernameField = new TextField("", skin);
        this.passwordField = new TextField("", skin);
        this.passwordField.setPasswordMode(true);
        this.passwordField.setPasswordCharacter('*');
        this.loginButton = new TextButton("Login", skin);
        this.recoverButton = new TextButton("Recover Password", skin);
        this.feedbackLabel = new Label("", skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTexture = new Texture(Gdx.files.internal("MyImage/Background.jpg"));
        Image bgImage = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(new Label("Create New Account", skin)).padBottom(10).right();
        table.add(registerLinkButton).padBottom(10).left();
        table.row();

        table.add(new Label("Login", skin)).colspan(2).padBottom(20);
        table.row();

        table.add(new Label("Username:", skin)).right();
        table.add(usernameField).width(300);
        table.row().padTop(10);

        table.add(new Label("Password:", skin)).right();
        table.add(passwordField).width(300);
        table.row().padTop(20);

        table.add(loginButton).padRight(20);
        table.add(recoverButton);
        table.row().padTop(20);

        table.add(feedbackLabel).colspan(2);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
        controller.update();
    }

    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public String getUsername() { return usernameField.getText().trim(); }
    public String getPassword() { return passwordField.getText(); }
    public TextButton getLoginButton() { return loginButton; }
    public TextButton getRecoverButton() { return recoverButton; }
    public TextButton getRegisterLinkButton() { return registerLinkButton; }
    public void setFeedback(String msg) { feedbackLabel.setText(msg); }
}
