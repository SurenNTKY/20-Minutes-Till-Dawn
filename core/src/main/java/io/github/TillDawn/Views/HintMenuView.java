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
import io.github.TillDawn.Controllers.HintMenuController;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.User;

public class HintMenuView implements Screen {
    private Stage stage;
    private final Skin skin;
    private final HintMenuController controller;
    private User loggedInUser;

    private Table table;

    public HintMenuView(HintMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        controller.setView(this);
        this.loggedInUser = Main.getMain().getCurrentUser();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture bgTexture = new Texture(Gdx.files.internal("MyImage/Hint.jpg"));
        Image bgImage = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        buildUI();
    }

    private void buildUI() {
        table.clear();

        Label title = new Label("Game Hints & Controls", skin);
        title.setFontScale(1.5f);
        table.add(title).colspan(2).padBottom(20);
        table.row();

        table.add(new Label("Heroes Info:", skin)).left().padBottom(10).padRight(20);
        VerticalGroup heroGroup = new VerticalGroup();
        for (String hint : controller.getHeroHints()) {
            heroGroup.addActor(new Label("- " + hint, skin));
        }
        table.add(heroGroup).left();
        table.row().padBottom(15);

        table.add(new Label("Active Game Keys:", skin)).left().padBottom(10).padRight(20);
        VerticalGroup keysGroup = new VerticalGroup();
        for (String key : controller.getActiveKeys()) {
            keysGroup.addActor(new Label("- " + key, skin));
        }
        table.add(keysGroup).left();
        table.row().padBottom(15);

        table.add(new Label("Cheat Codes:", skin)).left().padBottom(10).padRight(20);
        VerticalGroup cheatGroup = new VerticalGroup();
        for (String cheat : controller.getCheatCodes()) {
            cheatGroup.addActor(new Label("- " + cheat, skin));
        }
        table.add(cheatGroup).left();
        table.row().padBottom(15);

        table.add(new Label("Abilities Info:", skin)).left().padBottom(10).padRight(20);
        VerticalGroup abilityGroup = new VerticalGroup();
        for (String ability : controller.getAbilityDescriptions()) {
            abilityGroup.addActor(new Label("- " + ability, skin));
        }
        table.add(abilityGroup).left();
        table.row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(event -> {
            if (event.toString().contains("touchDown")) {
                Gdx.app.log("HintMenuView", "Back button pressed");
                Main.getMain().initMainMenu(loggedInUser);
                Main.getMain().goToMainMenu();
                return true;
            }
            return false;
        });

        table.add(backButton).colspan(2).padTop(20);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        controller.update();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
    }
}
