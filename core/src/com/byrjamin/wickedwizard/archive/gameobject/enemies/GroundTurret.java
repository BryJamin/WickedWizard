package com.byrjamin.wickedwizard.archive.gameobject.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.timer.StateTimer;
import com.byrjamin.wickedwizard.archive.maps.rooms.Room;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.archive.spelltypes.Projectile;

/**
 * Created by Home on 26/02/2017.
 */
public class GroundTurret extends GroundedEnemy {

    private StateTimer reloadTimer = new StateTimer(2.0f, 2.0f);

    public static class GroundTurretBuilder extends GBuilder {
        public GroundTurretBuilder(float posX, float posY) {
            super(posX, posY);
            health(7);
        }

        @Override
        public GroundTurret build() {
            return new GroundTurret(this);
        }
    }



    public GroundTurret(GroundTurretBuilder b) {
        super(b);
        HEIGHT = Measure.units(10);
        WIDTH = Measure.units(10);

        HEIGHT *= scale;
        WIDTH *= scale;

        position = new Vector2(position.x, position.y);
        collisionBound = new Rectangle(position.x + (Measure.units(1) * scale), position.y,
                WIDTH - (Measure.units(2.5f) * scale),
                HEIGHT - (Measure.units(2.5f) * scale));
        bounds.add(collisionBound);
        currentFrame = PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING);
        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f / 1f, TextureStrings.BLOB_DYING));

    }


    @Override
    public void update(float dt, Room r) {
        super.update(dt, r);

        System.out.println(health);

        if(this.getState() == Enemy.STATE.DYING || this.getState() == Enemy.STATE.DEAD) {
            dyingUpdate(dt);
        } else if(state == Enemy.STATE.ALIVE) {
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
