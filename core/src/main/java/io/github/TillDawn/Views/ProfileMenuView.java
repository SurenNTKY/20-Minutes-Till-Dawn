package io.github.TillDawn.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.TillDawn.Controllers.ProfileMenuController;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.User;

public class ProfileMenuView implements Screen {
    private final ProfileMenuController controller;
    private Stage stage;
    private final Label currentUserLabel;
    private final TextField usernameField;
    private final TextButton changeUsernameButton;
    private final Label usernameFeedback;
    private final TextButton backButton;

    private final TextField currentPasswordField;
    private final TextField newPasswordField;
    private final TextButton changePasswordButton;
    private final Label passwordFeedback;

    private final TextButton deleteAccountButton;
    private final Label deleteFeedback;

    private final List<String> avatarList;
    private final ScrollPane avatarScroll;
    private final TextButton chooseFileButton;
    private final Image dropZone;
    private final Label avatarFeedback;
    private final Skin skin;
    private User user;

    public ProfileMenuView(ProfileMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.user = Main.getMain().getCurrentUser();

        currentUserLabel = new Label("Current: ", skin);
        usernameField = new TextField("", skin);
        changeUsernameButton = new TextButton("Change Username", skin);
        usernameFeedback = new Label("", skin);
        backButton             = new TextButton("Back", skin);

        currentPasswordField = new TextField("", skin);
        currentPasswordField.setPasswordMode(true);
        currentPasswordField.setPasswordCharacter('*');
        newPasswordField = new TextField("", skin);
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');
        changePasswordButton = new TextButton("Change Password", skin);
        passwordFeedback = new Label("", skin);

        deleteAccountButton = new TextButton("Delete Account", skin);
        deleteFeedback = new Label("", skin);

        avatarList = new List<>(skin);
        avatarScroll = new ScrollPane(avatarList, skin);
        chooseFileButton = new TextButton("Choose Avatar File", skin);

        Texture dropTex = new Texture(Gdx.files.internal("Avatar/avatar"+user.getAvatarId()+".png"));
        dropZone = new Image(new TextureRegionDrawable(new TextureRegion(dropTex)));
        dropZone.setSize(200,200);
        avatarFeedback = new Label("", skin);

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTexture = new Texture(Gdx.files.internal("MyImage/Profile.png"));
        Image bgImage = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        Table table = new Table(skin);
        table.setFillParent(true);
        table.top().pad(20);

        table.add(currentUserLabel).colspan(3).left();
        table.row().padTop(10);
        table.add(new Label("New Username:", skin)).left();
        table.add(usernameField).width(300);
        table.add(changeUsernameButton).padLeft(10);
        table.row().padTop(5);
        table.add(usernameFeedback).colspan(3).left();
        table.row().padTop(20);

        table.add(new Label("Current Password:", skin)).left();
        table.add(currentPasswordField).width(300);
        table.row().padTop(5);
        table.add(new Label("New Password:", skin)).left();
        table.add(newPasswordField).width(300);
        table.add(changePasswordButton).padLeft(10);
        table.row().padTop(5);
        table.add(passwordFeedback).colspan(3).left();
        table.row().padTop(20);

        table.add(deleteAccountButton).colspan(3).left();
        table.row().padTop(5);
        table.add(deleteFeedback).colspan(3).left();
        table.row().padTop(20);

        table.add(new Label("Select Avatar:", skin)).left();
        table.add(avatarScroll).size(200,200);
        table.add(chooseFileButton).padLeft(10);
        table.row().padTop(5);
        table.add(dropZone).size(200,200);
        table.add(avatarFeedback).colspan(2).left();

        table.add(backButton).colspan(3).center();

        stage.addActor(table);

        DragAndDrop dd = new DragAndDrop();
        dd.addSource(new DragAndDrop.Source(dropZone) {
            public DragAndDrop.Payload dragStart(com.badlogic.gdx.scenes.scene2d.InputEvent e, float x, float y, int p) {
                return controller.createDragPayload();
            }
        });
        dd.addTarget(new DragAndDrop.Target(dropZone) {
            public boolean drag(DragAndDrop.Source s, DragAndDrop.Payload pl, float x, float y, int p) { return true; }
            public void drop(DragAndDrop.Source s, DragAndDrop.Payload pl, float x, float y, int p) {
                controller.handleAvatarDrop(pl);
            }
        });

        changeUsernameButton.addListener(controller.usernameListener());
        changePasswordButton.addListener(controller.passwordListener());
        deleteAccountButton.addListener(controller.deleteListener());
        chooseFileButton.addListener(controller.fileChooserListener());
        avatarList.addListener(controller.avatarListListener());
        backButton.addListener(controller.backListener());
    }

    @Override public void render(float delta) {
        ScreenUtils.clear(0.1f,0.1f,0.1f,1);
        stage.act(Math.min(delta,1/30f));
        stage.draw();
    }

    @Override public void resize(int w,int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public String getNewUsername() { return usernameField.getText().trim(); }
    public String getCurrentPassword() { return currentPasswordField.getText(); }
    public String getNewPassword() { return newPasswordField.getText(); }
    public String getSelectedAvatarName() { return avatarList.getItems().get(avatarList.getSelectedIndex()); }

    public void setUsernameFeedback(String msg) { usernameFeedback.setText(msg); }
    public void setPasswordFeedback(String msg) { passwordFeedback.setText(msg); }
    public void setDeleteFeedback(String msg) { deleteFeedback.setText(msg); }
    public void setAvatarOptions(java.util.List<String> names) { avatarList.setItems(names.toArray(new String[0])); }
    public void setAvatarFeedback(String msg) { avatarFeedback.setText(msg); }
    public void updateCurrentUsername(String name) { currentUserLabel.setText("Current: " + name); }
    public void updateAvatarImage(int avatarId) {
        Texture newTex = new Texture(Gdx.files.internal("Avatar/avatar" + avatarId + ".png"));
        dropZone.setDrawable(new TextureRegionDrawable(new TextureRegion(newTex)));
    }
}
