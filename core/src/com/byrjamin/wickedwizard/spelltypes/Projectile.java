package com.byrjamin.wickedwizard.spelltypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.staticstrings.TextureStrings;

/**
 * Created by Home on 07/11/2016.
 */
public class Projectile {

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public enum STATE {
        DEAD, EXPLODING, ALIVE
    }

    STATE state;

    private double projectAngle;
    private float xDistance;
    private float yDistance;

    private Color drawingColor = Color.WHITE;

    Vector2 position;

    private Animation explosion_animation;
    float time = 0;

    TextureRegion explosionTextureRegion;

    private float HORIZONTAL_VELOCITY = Measure.units(200f);

    //Required Parameters
    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;

    private float WIDTH = Measure.units(3);
    private float HEIGHT = Measure.units(3);

    //Optional Parameters
    private float damage;
    private Rectangle hitBox;

    public static class ProjectileBuilder {

        //Required Parameters
        private final float x1;
        private final float y1;
        private final float x2;
        private final float y2;

        //Optional Parameters
        private float damage = 0;
        private float HORIZONTAL_VELOCITY = Measure.units(50f);
        private Color drawingColor;


        public ProjectileBuilder(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public ProjectileBuilder damage(float val) {
            damage = val;
            return this;
        }

        public ProjectileBuilder drawingColor(Color val) {
            drawingColor = val;
            return this;
        }

        public ProjectileBuilder HORIZONTAL_VELOCITY(float val) {
            HORIZONTAL_VELOCITY = val;
            return this;
        }

        public Projectile build() {
            return new Projectile(this);
        }


    }

    public Projectile(ProjectileBuilder builder) {
        x1 = builder.x1 - (WIDTH / 2);
        x2 = builder.x2 - (WIDTH / 2);
        y1 = builder.y1 - (HEIGHT / 2);
        y2 = builder.y2 - (HEIGHT / 2);
        damage = builder.damage;
        HORIZONTAL_VELOCITY = builder.HORIZONTAL_VELOCITY;
        drawingColor = builder.drawingColor;
        //TODO fix this crap
        position = new Vector2(x1, y1);
        hitBox = new Rectangle(x1, y1, WIDTH, HEIGHT);
        calculateAngle(x1, y1, x2, y2);
        //sprite.rotate((float) Math.toDegrees(projectAngle));
        state = STATE.ALIVE;
        explosion_animation = AnimationPacker.genAnimation(0.02f / 1f, TextureStrings.EXPLOSION);
    }

    public void calculateAngle(float x1, float y1, float x2, float y2) {
        projectAngle = (Math.atan2(y2 - y1, x2 - x1));
        xDistance = (float) (HORIZONTAL_VELOCITY * Math.cos(projectAngle));
        yDistance = (float) (HORIZONTAL_VELOCITY * Math.sin(projectAngle));
    }

    public void update(float dt) {
        if (getState() == STATE.ALIVE) {
            travelUpdate(dt);
        } else if (getState() == STATE.EXPLODING) {
            time += dt;
            if (explosion_animation.isAnimationFinished(time)) {
                this.setState(STATE.DEAD);
            }
            explosionTextureRegion = explosion_animation.getKeyFrame(time);
        }
    }

    public void travelUpdate(float dt) {
        position.x += xDistance * dt;
        position.y += yDistance * dt;

        hitBox.y = position.y;
        hitBox.x = position.x;
    }

    public void draw(SpriteBatch batch) {
        BoundsDrawer.drawBounds(batch, hitBox);
        if (getState() == STATE.ALIVE) {
            batch.setColor(drawingColor);
            batch.draw(PlayScreen.atlas.findRegion("bullet"), position.x, position.y, WIDTH, HEIGHT);
            batch.setColor(Color.WHITE);
        } else if (getState() == STATE.EXPLODING) {
            batch.draw(explosion_animation.getKeyFrame(time), position.x, position.y, WIDTH, HEIGHT);
        }
    }

    public Rectangle getBounds() {
        return hitBox;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Projectile.STATE getState() {
        return state;
    }

    public void setState(Projectile.STATE state) {
        this.state = state;
    }
}
