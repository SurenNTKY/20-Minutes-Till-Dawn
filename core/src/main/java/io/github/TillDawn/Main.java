package io.github.TillDawn;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.TillDawn.Controllers.MainMenuController;
import io.github.TillDawn.Controllers.RegisterMenuController;
import io.github.TillDawn.Models.GameAssetManager;
import io.github.TillDawn.Models.SqliteUserDao;
import io.github.TillDawn.Models.User;
import io.github.TillDawn.Models.UserDao;
import io.github.TillDawn.Views.MainMenuView;
import io.github.TillDawn.Views.RegisterMenuView;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;
    private UserDao userDao;
    private Music backgroundMusic;
    private Screen mainMenuScreen;
    private User currentUser;


    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        userDao = new SqliteUserDao();
        userDao.initialize();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Audio/Track 3.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        String[] questions = new String[] {
            "What was your childhood nickname?",
            "What is the name of your favorite childhood friend?",
            "In what city did you meet your spouse?",
            "What street did you live on in third grade?"
        };
        RegisterMenuController controller = new RegisterMenuController((SqliteUserDao) userDao);
        Skin skin = GameAssetManager.getGameAssetManager().getSkin();
        setScreen(new RegisterMenuView(controller, skin, questions));
    }

    public void initMainMenu(User user) {
        this.currentUser = user;

        MainMenuController mainMenuController = new MainMenuController((SqliteUserDao) userDao);
        MainMenuView mainMenuView = new MainMenuView(mainMenuController,
            GameAssetManager.getGameAssetManager().getSkin(), currentUser);
        mainMenuController.setView(mainMenuView);

        this.mainMenuScreen = mainMenuView;
    }

    public void goToMainMenu() {
        setScreen(mainMenuScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
        if (userDao != null) {
            userDao.dispose();
        }
    }

    public static Main getMain() { return main; }
    public static SpriteBatch getBatch() { return batch; }
    public Music getBackgroundMusic() { return backgroundMusic; }
    public void setBackgroundMusic(Music music) {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
        this.backgroundMusic = music;
    }
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

}
