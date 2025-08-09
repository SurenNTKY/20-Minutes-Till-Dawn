package io.github.TillDawn.Controllers.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.GameAssetManager;
import io.github.TillDawn.Models.Player;
import io.github.TillDawn.Models.Weapon;
import io.github.TillDawn.Views.GameView;

public class GameController {
    private GameView view;
    private PlayerController playerController;
    private WorldController worldController;
    private WeaponController weaponController;
    private EnemyController enemyController;

    public void setView(GameView view) {
        this.view = view;
        playerController = new PlayerController(new Player());
        worldController = new WorldController(playerController);
        weaponController = new WeaponController(new Weapon());

        float initialSpawnInterval = 3.0f;
        enemyController = new EnemyController(
            GameAssetManager.getGameAssetManager().getEnemyFrames(),
            initialSpawnInterval
        );
    }

    public void updateGame() {
        if (view == null) return;

        float delta = Gdx.graphics.getDeltaTime();
        Vector2 playerPos = playerController.getPlayer().getPosition();

        worldController.update(delta);
        playerController.update();
        weaponController.update();

        enemyController.update(delta, playerPos);
        enemyController.draw(Main.getBatch());
        enemyController.checkBulletCollisions(weaponController.getBullets());
        enemyController.accelerateSpawnRate(delta);
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public WeaponController getWeaponController() {
        return weaponController;
    }
}
