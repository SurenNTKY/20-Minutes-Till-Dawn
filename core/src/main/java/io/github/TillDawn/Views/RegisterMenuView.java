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
import io.github.TillDawn.Controllers.RegisterMenuController;


public class RegisterMenuView implements Screen {
    private Stage stage;
    private final TextButton loginLinkButton;
    private final Label titleLabel;
    private final TextField usernameField;
    private final TextField passwordField;
    private final TextField confirmPasswordField;
    private final SelectBox<String> questionSelect;
    private final TextField answerField;
    private final TextButton registerButton;
    private final TextButton guestButton;
    private final Label feedbackLabel;
    private final Skin skin;
    private final RegisterMenuController controller;

    public RegisterMenuView(RegisterMenuController controller, Skin skin, String[] questions) {
        this.controller = controller;
        this.skin = skin;
        this.loginLinkButton = new TextButton("Login", skin);
        this.titleLabel = new Label("User Registration", skin);
        this.usernameField = new TextField("", skin);
        this.passwordField = new TextField("", skin);
        this.passwordField.setPasswordMode(true);
        this.passwordField.setPasswordCharacter('*');
        this.confirmPasswordField = new TextField("", skin);
        this.confirmPasswordField.setPasswordMode(true);
        this.confirmPasswordField.setPasswordCharacter('*');
        this.questionSelect = new SelectBox<>(skin);
        String[] items = new String[questions.length + 1];
        items[0] = "🔑 Select Question";
        System.arraycopy(questions, 0, items, 1, questions.length);
        this.questionSelect.setItems(items);
        this.answerField = new TextField("", skin);
        this.registerButton = new TextButton("Register", skin);
        this.guestButton = new TextButton("Guest Login", skin);
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

        table.add(new Label("Already have an account?", skin)).padBottom(10).right();
        table.add(loginLinkButton).padBottom(10).left();
        table.row();

        table.add(titleLabel).colspan(2).padBottom(20);
        table.row();

        table.add(new Label("Username:", skin)).right();
        table.add(usernameField).width(400);
        table.row().padTop(10);

        table.add(new Label("Password:", skin)).right();
        table.add(passwordField).width(400);
        table.row().padTop(10);

        table.add(new Label("Confirm Password:", skin)).right();
        table.add(confirmPasswordField).width(400);
        table.row().padTop(10);

        table.add(new Label("Security Question:", skin)).right();
        table.add(questionSelect).width(400);
        table.row().padTop(10);

        table.add(new Label("Answer:", skin)).right();
        table.add(answerField).width(400);
        table.row().padTop(20);

        table.add(registerButton).colspan(1).padRight(20);
        table.add(guestButton).colspan(1);
        table.row().padTop(20);

        table.add(feedbackLabel).colspan(2);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        controller.update();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public String getUsername() { return usernameField.getText().trim(); }
    public String getPassword() { return passwordField.getText(); }
    public String getConfirmPassword() { return confirmPasswordField.getText(); }
    public int getSelectedQuestionIndex() { return questionSelect.getSelectedIndex(); }
    public String getAnswer() { return answerField.getText().trim(); }
    public TextButton getRegisterButton() { return registerButton; }
    public TextButton getGuestButton() { return guestButton; }
    public TextButton getLoginLinkButton() { return loginLinkButton; }
    public void setFeedback(String message) { feedbackLabel.setText(message); }

}
