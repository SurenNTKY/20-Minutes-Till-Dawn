package io.github.TillDawn.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    private static GameAssetManager gameAssetManager;
    private final Skin skin = new Skin(Gdx.files.internal("Skin/pixthulhu-ui.json"));

    private final String character1_idle0 = "Heros/Dasher/idle/Idle_0 #8325.png";
    private final String character1_idle1 = "Heros/Dasher/idle/Idle_1 #8355.png";
    private final String character1_idle2 = "Heros/Dasher/idle/Idle_2 #8814.png";
    private final String character1_idle3 = "Heros/Dasher/idle/Idle_3 #8452.png";
    private final String character1_idle4 = "Heros/Dasher/idle/Idle_4 #8313.png";
    private final String character1_idle5 = "Heros/Dasher/idle/Idle_5 #8302.png";
    private final Texture character1_idle0_tex = new Texture(character1_idle0);
    private final Texture character1_idle1_tex = new Texture(character1_idle1);
    private final Texture character1_idle2_tex = new Texture(character1_idle2);
    private final Texture character1_idle3_tex = new Texture(character1_idle3);
    private final Texture character1_idle4_tex = new Texture(character1_idle4);
    private final Texture character1_idle5_tex = new Texture(character1_idle5);
    private final Animation<Texture> character1_idle_frames = new Animation<>(
        0.1f,
        character1_idle0_tex,
        character1_idle1_tex,
        character1_idle2_tex,
        character1_idle3_tex,
        character1_idle4_tex,
        character1_idle5_tex
    );

    private final String smg = "Weapons/SMGStill.png";
    private final Texture smgTexture = new Texture(smg);

    private final String[] enemyFramePaths = new String[] {
        "Brain/BrainMonster_0.png",
        "Brain/BrainMonster_1.png",
        "Brain/BrainMonster_2.png",
        "Brain/BrainMonster_3.png"
    };
    private final TextureRegion[] enemyFrames;

    private final String bullet = "bullet.png";

    private final String[] treeFramePaths = new String[] {
        "Tree/T_TreeMonster_0.png",
        "Tree/T_TreeMonster_1.png",
        "Tree/T_TreeMonster_2.png"
    };
    private final TextureRegion[] treeFrames;
    private final Animation<TextureRegion> treeAnimation;

    private GameAssetManager() {
        enemyFrames = new TextureRegion[enemyFramePaths.length];
        for (int i = 0; i < enemyFramePaths.length; i++) {
            Texture tex = new Texture(enemyFramePaths[i]);
            enemyFrames[i] = new TextureRegion(tex);
        }
        treeFrames = new TextureRegion[treeFramePaths.length];
        for (int i = 0; i < treeFramePaths.length; i++) {
            Texture tex = new Texture(treeFramePaths[i]);
            treeFrames[i] = new TextureRegion(tex);
        }
        treeAnimation = new Animation<>(0.3f, treeFrames);
    }

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }

    public Skin getSkin() {
        return skin;
    }

    public Animation<Texture> getCharacter1_idle_animation() {
        return character1_idle_frames;
    }

    public String getCharacter1_idle0() {
        return character1_idle0;
    }

    public Texture getSmgTexture() {
        return smgTexture;
    }

    public String getSmg() {
        return smg;
    }

    public String getBullet() {
        return bullet;
    }

    public TextureRegion[] getEnemyFrames() {
        return enemyFrames;
    }

    public Animation<TextureRegion> getTreeAnimation() {
        return treeAnimation;
    }
}
