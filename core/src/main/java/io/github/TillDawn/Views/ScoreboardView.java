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
import io.github.TillDawn.Controllers.ScoreboardController;
import io.github.TillDawn.Models.User;

import java.util.List;

public class ScoreboardView implements Screen {
    private Stage stage;
    private final Skin skin;
    private final ScoreboardController controller;

    private Table table;
    private TextButton sortByScoreBtn;
    private TextButton sortByUsernameBtn;
    private TextButton sortByKillsBtn;
    private TextButton sortBySurvivalTimeBtn;
    private Label feedbackLabel;
    private final TextButton backButton;

    public ScoreboardView(ScoreboardController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        controller.setView(this);

        backButton = new TextButton("Back", skin);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTexture = new Texture(Gdx.files.internal("MyImage/Scoreboard.png"));
        Image bgImage = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        table = new Table();
        table.setFillParent(true);
        table.center();

        sortByScoreBtn = new TextButton("Sort by Score", skin);
        sortByUsernameBtn = new TextButton("Sort by Username", skin);
        sortByKillsBtn = new TextButton("Sort by Kills", skin);
        sortBySurvivalTimeBtn = new TextButton("Sort by Survival Time", skin);
        feedbackLabel = new Label("", skin);

        Table headerTable = new Table(skin);
        headerTable.add(sortByScoreBtn).pad(10);
        headerTable.add(sortByUsernameBtn).pad(10);
        headerTable.add(sortByKillsBtn).pad(10);
        headerTable.add(sortBySurvivalTimeBtn).pad(10);

        table.add(new Label("Scoreboard - Top 10 Users", skin)).colspan(5).padBottom(20);
        table.row();
        table.add(headerTable).colspan(5);
        table.row();

        table.row().padTop(20);
        table.add(backButton).colspan(2);

        stage.addActor(table);

        controller.updateView();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
        controller.update();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }

    public boolean isSortByScorePressed() { return sortByScoreBtn.isPressed(); }
    public boolean isSortByUsernamePressed() { return sortByUsernameBtn.isPressed(); }
    public boolean isSortByKillsPressed() { return sortByKillsBtn.isPressed(); }
    public boolean isSortBySurvivalTimePressed() { return sortBySurvivalTimeBtn.isPressed(); }
    public TextButton getBackButton() { return backButton; }

    public void updateScoreboard(List<User> users, String currentUsername) {
        table.clearChildren();

        table.add(new Label("Scoreboard - Top 10 Users", skin)).colspan(5).padBottom(20);
        table.row();

        Table headerTable = new Table(skin);
        headerTable.add(sortByScoreBtn).pad(10);
        headerTable.add(sortByUsernameBtn).pad(10);
        headerTable.add(sortByKillsBtn).pad(10);
        headerTable.add(sortBySurvivalTimeBtn).pad(10);

        table.add(headerTable).colspan(5);
        table.row();

        table.add(new Label("Rank", skin));
        table.add(new Label("Username", skin));
        table.add(new Label("Score", skin));
        table.add(new Label("Kills", skin));
        table.add(new Label("Survival Time", skin));
        table.row();

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            Label rankLabel = new Label(String.valueOf(i+1), skin);
            Label usernameLabel = new Label(u.getUsername(), skin);
            Label scoreLabel = new Label(String.valueOf(u.getScore()), skin);
            Label killsLabel = new Label(String.valueOf(u.getKills()), skin);
            Label survivalLabel = new Label(String.format("%02d:%02d", u.getSurvivalTime()/60, u.getSurvivalTime()%60), skin);

            if (i == 0) {
                rankLabel.setColor(1f, 0.84f, 0f, 1f);
                usernameLabel.setColor(1f, 0.84f, 0f, 1f);
            } else if (i == 1) {
                rankLabel.setColor(0.75f, 0.75f, 0.75f, 1f);
                usernameLabel.setColor(0.75f, 0.75f, 0.75f, 1f);
            } else if (i == 2) {
                rankLabel.setColor(0.8f, 0.5f, 0.2f, 1f);
                usernameLabel.setColor(0.8f, 0.5f, 0.2f, 1f);
            }


            if (u.getUsername().equals(currentUsername)) {
                usernameLabel.setColor(0f, 1f, 0f, 1f);
            }

            table.add(rankLabel);
            table.add(usernameLabel);
            table.add(scoreLabel);
            table.add(killsLabel);
            table.add(survivalLabel);
            table.row();
        }
    }
}
