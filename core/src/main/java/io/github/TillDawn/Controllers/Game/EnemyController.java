package io.github.TillDawn.Controllers.Game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.TillDawn.Models.Bullet;
import io.github.TillDawn.Models.Enemy;

import java.util.ArrayList;

public class EnemyController {
    private Array<Enemy> enemies = new Array<>();
    private TextureRegion[] frames;
    private float spawnTimer = 0f;
    private float spawnInterval;
    private int totalSpawned = 0;

    private final float accelerationRate = 0.01f;

    public EnemyController(TextureRegion[] frames, float initialInterval) {
        this.frames = frames;
        this.spawnInterval = initialInterval;
    }

    public void update(float deltaTime, Vector2 playerPos) {
        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval) {
            spawnEnemy();
            spawnTimer = 0f;
        }

        for (Enemy e : enemies) {
            if (e.isAlive())
                e.update(deltaTime, playerPos);
            else {
                spawnSeed(e.getPosition());
            }
        }
    }

    private void spawnEnemy() {
        float x, y;
        int side = MathUtils.random(0, 3);
        switch (side) {
            case 0: x = MathUtils.random(0, 800); y = 600; break;
            case 1: x = MathUtils.random(0, 800); y = 0;   break;
            case 2: x = 0; y = MathUtils.random(0, 600);   break;
            default: x = 800; y = MathUtils.random(0, 600);break;
        }
        enemies.add(new Enemy(frames, new Vector2(x, y), 100f, 3));
    }

    public void draw(SpriteBatch batch) {
        for (Enemy e : enemies)
            e.draw(batch);
    }

    public void checkBulletCollisions(ArrayList<Bullet> bullets) {
        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;
            for (Bullet b : bullets) {
                if (b.getBoundingRectangle().overlaps(e.getBoundingRectangle())) {
                    e.takeDamage(b.getDamage());
                    b.setDestroyed(true);
                }
            }
        }
    }

    public void accelerateSpawnRate(float deltaTime) {
        spawnInterval = Math.max(0.5f, spawnInterval - accelerationRate * deltaTime);
    }

    private void spawnSeed(Vector2 pos) {
    }
}
