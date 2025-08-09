package io.github.TillDawn.Models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Enemy {
    private TextureRegion currentFrame;
    private Animation<TextureRegion> walkAnimation;
    private Vector2 position;
    private Vector2 velocity;
    private float speed;
    private int health;
    private boolean isAlive;

    public Enemy(TextureRegion[] frames, Vector2 spawnPosition, float speed, int health) {
        this.walkAnimation = new Animation<>(0.1f, frames);
        this.position = new Vector2(spawnPosition);
        this.velocity = new Vector2();
        this.speed = speed;
        this.health = health;
        this.isAlive = true;
    }

    public void update(float deltaTime, Vector2 playerPosition) {
        if (!isAlive) return;

        Vector2 direction = playerPosition.cpy().sub(position).nor();
        velocity.set(direction.scl(speed));
        position.add(velocity.cpy().scl(deltaTime));

        currentFrame = walkAnimation.getKeyFrame(TimeUtils.nanoTime() / 1000000000.0f, true);
    }

    public void draw(SpriteBatch batch) {
        if (isAlive)
            batch.draw(currentFrame, position.x, position.y);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBoundingRectangle() {
        if (currentFrame == null) {
            return new Rectangle(position.x, position.y, 32, 32);
        }
        return new Rectangle(position.x, position.y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }
}
