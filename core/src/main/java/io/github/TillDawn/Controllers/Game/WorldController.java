package io.github.TillDawn.Controllers.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.GameAssetManager;
import io.github.TillDawn.Models.Tree;

import java.util.ArrayList;
import java.util.List;

public class WorldController {
    private PlayerController playerController;
    private TextureRegion background;
    private float backgroundX = 0;
    private float backgroundY = 0;

    private List<Tree> trees = new ArrayList<>();
    private Animation<TextureRegion> treeAnimation;
    private float treeAnimTime = 0;

    private final int treeCount = 40;
    private final float mapWidth = 1920;
    private final float mapHeight = 1080;

    public WorldController(PlayerController playerController) {
        this.playerController = playerController;
        this.background = new TextureRegion(new Texture("background.png"));

        this.treeAnimation = GameAssetManager.getGameAssetManager().getTreeAnimation();
        spawnTrees();
    }

    private void spawnTrees() {
        for (int i = 0; i < treeCount; i++) {
            float x = MathUtils.random(0, mapWidth - 32);
            float y = MathUtils.random(0, mapHeight - 32);
            TextureRegion initialFrame = treeAnimation.getKeyFrame(0);
            trees.add(new Tree(initialFrame, new Vector2(x, y)));
        }
    }

    public void update(float delta) {
        backgroundX = playerController.getPlayer().getPosX();
        backgroundY = playerController.getPlayer().getPosY();
        treeAnimTime += delta;

        SpriteBatch batch = Main.getBatch();
        batch.draw(background, backgroundX, backgroundY);

        TextureRegion currentTreeFrame = treeAnimation.getKeyFrame(treeAnimTime, true);
        for (Tree t : trees) {
            t.setCurrentFrame(currentTreeFrame);
            t.draw(batch);
        }
    }
}
