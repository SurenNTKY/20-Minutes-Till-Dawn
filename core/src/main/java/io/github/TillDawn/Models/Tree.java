package io.github.TillDawn.Models;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Tree {
    private Vector2 position;
    private TextureRegion currentFrame;

    public Tree(TextureRegion frame, Vector2 position) {
        this.position = position;
        this.currentFrame = frame;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(currentFrame, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setCurrentFrame(TextureRegion frame) {
        this.currentFrame = frame;
    }
}
