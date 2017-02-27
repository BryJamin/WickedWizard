package com.byrjamin.wickedwizard.entity.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.entity.ActiveBullets;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.timer.StateTimer;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.spelltypes.Projectile;

/**
 * Created by Home on 26/02/2017.
 */
public class GroundTurret extends GroundedEnemy {


    private StateTimer reloadTimer = new StateTimer(2.0f, 2.0f);

    public static class GroundTurretBuilder {

        //Required Parameters
        private final float posX;
        private final float posY;

        //Optional Parameters
        private float health = 7;
        private float scale = 1;
        private float speed = 1;

        public GroundTurretBuilder(float posX, float posY) {
            this.posX = posX;
            this.posY = posY;
        }

        public GroundTurretBuilder health(float val)
        { health = val; return this; }

        public GroundTurretBuilder scale(float val)
        { scale = val; return this; }

        public GroundTurretBuilder speed(float val)
        { speed = val; return this; }

        public GroundTurret build() {
            return new GroundTurret(this);
        }

    }


    public GroundTurret(GroundTurretBuilder b) {
        super();
        health = b.health;


        HEIGHT = Measure.units(10);
        WIDTH = Measure.units(10);

        HEIGHT *= b.scale;
        WIDTH *= b.scale;

        position = new Vector2(b.posX, b.posY);
        collisionBound = new Rectangle(b.posX + (Measure.units(1) * b.scale), b.posY,
                WIDTH - (Measure.units(2.5f) * b.scale),
                HEIGHT - (Measure.units(2.5f) * b.scale));
        bounds.add(collisionBound);
        currentFrame = PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING);
        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f / 1f, TextureStrings.BLOB_DYING));

    }


    @Override
    public void update(float dt, Room r) {
        super.update(dt, r);

        if(this.getState() == STATE.DYING || this.getState() == STATE.DEAD) {
            dyingUpdate(dt);
        } else if(state == STATE.ALIVE) {
            reloadTimer.update(dt);
            if (reloadTimer.isFinished()) {

                double angleInDegrees = (r.getWizard().getCenterX() > position.x + WIDTH / 2) ? 0 : 180;

                bullets.addProjectile(new Projectile.ProjectileBuilder(position.x + WIDTH / 2, position.y + HEIGHT / 2, angleInDegrees)
                        .damage(1)
                        .speed(Measure.units(60f))
                        .drawingColor(Color.RED)
                        .build());
                reloadTimer.reset();
            }
        }

    }

    @Override
    public void draw(SpriteBatch batch) {
       // bulletDraw(batch);
        super.draw(batch);
    }
}
