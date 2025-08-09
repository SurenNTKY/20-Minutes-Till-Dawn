package io.github.TillDawn.Controllers.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.TillDawn.Main;
import io.github.TillDawn.Models.Bullet;
import io.github.TillDawn.Models.Weapon;


import java.util.ArrayList;

public class WeaponController {
    private Weapon weapon;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    private final float reloadDuration = 2.0f;
    private boolean isReloading = false;
    private float reloadTimer = 0f;
    private int reloadKey;

    public WeaponController(Weapon weapon){
        this.weapon = weapon;
        this.reloadKey = Input.Keys.R;
    }

    public void update(){
        weapon.getSmgSprite().draw(Main.getBatch());

        if (Gdx.input.isKeyJustPressed(reloadKey) && !isReloading) {
            isReloading = true;
            reloadTimer = 0f;
        }
        if (isReloading) {
            reloadTimer += Gdx.graphics.getDeltaTime();
            if (reloadTimer >= reloadDuration) {
                weapon.setAmmo(weapon.getMaxAmmo());
                isReloading = false;
            }
        }

        updateBullets();
    }

    public void handleWeaponRotation(int x, int y) {
        Sprite weaponSprite = weapon.getSmgSprite();

        float weaponCenterX = (float) Gdx.graphics.getWidth() / 2;
        float weaponCenterY = (float) Gdx.graphics.getHeight() / 2;

        float angle = (float) Math.atan2(y - weaponCenterY, x - weaponCenterX);

        weaponSprite.setRotation((float) (3.14 - angle * MathUtils.radiansToDegrees));
    }

    public void handleWeaponShoot(int x, int y){
        if (isReloading || weapon.getAmmo() <= 0) return;
        bullets.add(new Bullet(x, y));
        weapon.setAmmo(weapon.getAmmo() - 1);
    }

    public void updateBullets() {
        for(Bullet b : bullets) {
            b.getSprite().draw(Main.getBatch());
            Vector2 direction = new Vector2(
                Gdx.graphics.getWidth()/2f - b.getX(),
                Gdx.graphics.getHeight()/2f - b.getY()
            ).nor();

            b.getSprite().setX(b.getSprite().getX() - direction.x * 5);
            b.getSprite().setY(b.getSprite().getY() + direction.y * 5);
        }
    }
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
}
