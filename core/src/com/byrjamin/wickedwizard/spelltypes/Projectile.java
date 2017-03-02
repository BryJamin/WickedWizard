package com.byrjamin.wickedwizard.spelltypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.assets.TextureStrings;

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

    private double angleOfTravel;
    private float xDistance;
    private float yDistance;

    private float scale;

    private Color drawingColor = Color.WHITE;

    Vector2 position;
    Vector2 velocity;

    private Animation<TextureRegion> explosion_animation;
    float time = 0;

    TextureRegion explosionTextureRegion;

    private float speed;

    //Required Parameters
    private final float x1;
    private final float y1;

    private float WIDTH = Measure.units(2);
    private float HEIGHT = Measure.units(2);

    //Optional Parameters
    private float damage;
    private Rectangle hitBox;
    private boolean gravity;

    public static class ProjectileBuilder {

        //Required Parameters
        private final float x1;
        private final float y1;
        private final double angleOfTravel;

        //Optional Parameters
        private float damage = 0;
        private float speed = Measure.units(50f);
        private Color drawingColor;
        private float scale = 1;
        private boolean gravity;


        public ProjectileBuilder(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            angleOfTravel = (Math.atan2(y2 - y1, x2 - x1));
        }

        public ProjectileBuilder(float x1, float y1, double angleOfTravelInDegress) {
            this.x1 = x1;
            this.y1 = y1;
            angleOfTravel = Math.toRadians(angleOfTravelInDegress);
        }

        public ProjectileBuilder damage(float val) {
            damage = val;
            return this;
        }

        public ProjectileBuilder drawingColor(Color val) {
            drawingColor = val;
            return this;
        }

        public ProjectileBuilder speed(float val) {
            speed = val;
            return this;
        }

        public ProjectileBuilder scale(float val) {
            scale = val;
            return this;
        }

        public ProjectileBuilder gravity(boolean val) {
            gravity = val;
            return this;
        }

        public Projectile build() {
            return new Projectile(this);
        }


    }

    public Projectile(ProjectileBuilder builder) {
        scale = builder.scale;

        WIDTH = WIDTH * scale;
        HEIGHT = HEIGHT * scale;

        x1 = builder.x1 - (WIDTH / 2);
        y1 = builder.y1 - (HEIGHT / 2);
        damage = builder.damage;
        speed = builder.speed;
        drawingColor = builder.drawingColor;
        angleOfTravel = builder.angleOfTravel;
        //TODO fix this crap
        position = new Vector2(x1, y1);
        velocity = new Vector2((float) (speed * Math.cos(angleOfTravel)), (float) (speed * Math.sin(angleOfTravel)));
        hitBox = new Rectangle(x1, y1, WIDTH, HEIGHT);
        gravity = builder.gravity;

        //sprite.rotate((float) Math.toDegrees(projectAngle));
        state = STATE.ALIVE;
        explosion_animation = AnimationPacker.genAnimation(0.02f / 1f, TextureStrings.EXPLOSION);
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

        if(gravity) {
            velocity.add(0, -Measure.units(1.5f));
        }

        position.add(velocity.x * dt, velocity.y * dt);
        hitBox.y = position.y;
        hitBox.x = position.x;
    }

    public void draw(SpriteBatch batch) {
        if (getState() == STATE.ALIVE) {
            batch.setColor(drawingColor);
            batch.draw(PlayScreen.atlas.findRegion("bullet"), position.x - WIDTH / 2, position.y - HEIGHT / 2, WIDTH + WIDTH, HEIGHT + HEIGHT);
            batch.setColor(Color.WHITE);
        } else if (getState() == STATE.EXPLODING) {
            batch.draw(explosion_animation.getKeyFrame(time), position.x - WIDTH / 2, position.y - HEIGHT / 2, WIDTH + WIDTH, HEIGHT + HEIGHT);
        }

        BoundsDrawer.drawBounds(batch, hitBox);
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
