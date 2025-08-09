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
import io.github.TillDawn.Controllers.RecoveryPasswordController;

public class RecoveryPasswordView implements Screen {
    private final Skin skin;
    private final RecoveryPasswordController controller;
    private Stage stage;

    private final TextField usernameField;
    private final SelectBox<String> questionSelect;
    private final TextField answerField;
    private final TextField newPasswordField;
    private final TextButton confirmButton;
    private final TextButton backButton;
    private final Label feedbackLabel;

    public RecoveryPasswordView(RecoveryPasswordController controller, Skin skin, String[] questions) {
        this.controller = controller;
        this.skin = skin;

        this.usernameField = new TextField("", skin);
        this.questionSelect = new SelectBox<>(skin);
        this.questionSelect.setItems(questions);
        this.answerField = new TextField("", skin);
        this.newPasswordField = new TextField("", skin);
        this.newPasswordField.setPasswordMode(true);
        this.newPasswordField.setPasswordCharacter('*');
        this.confirmButton = new TextButton("Reset Password", skin);
        this.backButton = new TextButton("Back to Login", skin);
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

        table.add(new Label("Username:", skin)).right();
        table.add(usernameField).width(400);
        table.row().padTop(10);

        table.add(new Label("Security Question:", skin)).right();
        table.add(questionSelect).width(400);
        table.row().padTop(10);

        table.add(new Label("Answer:", skin)).right();
        table.add(answerField).width(400);
        table.row().padTop(10);

        table.add(new Label("New Password:", skin)).right();
        table.add(newPasswordField).width(400);
        table.row().padTop(20);

        table.add(confirmButton).colspan(2).padBottom(10);
        table.row();

        table.add(backButton).colspan(2).padBottom(10);
        table.row();

        table.add(feedbackLabel).colspan(2);

        stage.addActor(table);
    }

    @Override public void render(float delta) {
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
    public int getSelectedQuestionIndex() { return questionSelect.getSelectedIndex(); }
    public String getAnswer() { return answerField.getText().trim(); }
    public String getNewPassword() { return newPasswordField.getText(); }
    public TextButton getConfirmButton() { return confirmButton; }
    public TextButton getBackButton() { return backButton; }
    public void setFeedback(String message) { feedbackLabel.setText(message); }
}
