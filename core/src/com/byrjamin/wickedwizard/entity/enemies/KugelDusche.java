package com.byrjamin.wickedwizard.entity.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.timer.StateTimer;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.spelltypes.Projectile;

import java.util.Random;

/**
 * Created by Home on 27/02/2017.
 */
public class KugelDusche extends Enemy{

    private StateTimer reloadTimer = new StateTimer(0.05f);
    private float startingAngleInDegress = -90;
    private float currentAngleInDegrees = startingAngleInDegress;
    private float degreeChange = 1.5f;
    private boolean isClockwise;

    public enum TYPE {
        FOUR_BULLETS, TWO_BULLETS
    }

    private TYPE type;


    public static class KugelDuscheBuilder extends GroundedEnemy.GBuilder {

        private TYPE type = TYPE.FOUR_BULLETS;

        public KugelDuscheBuilder(float posX, float posY) {
            super(posX, posY);
            health(20);
        }

        public KugelDuscheBuilder type(TYPE val)
        { type = val; return this; }

        @Override
        public KugelDusche build() {
            return new KugelDusche(this);
        }
    }



    public KugelDusche(KugelDuscheBuilder b){
        super(b);
        type = b.type;
        HEIGHT = Measure.units(10);
        WIDTH = Measure.units(10);

        HEIGHT *= scale;
        WIDTH *= scale;

        position = new Vector2(position.x, position.y);
/*        collisionBound = new Rectangle(position.x + (Measure.units(1) * scale), position.y,
                WIDTH - (Measure.units(2.5f) * scale),
                HEIGHT - (Measure.units(2.5f) * scale));*/


        collisionBound = new Rectangle(position.x, position.y,
                WIDTH,
                HEIGHT);

        bounds.add(collisionBound);
        currentFrame = PlayScreen.atlas.findRegion("bullet_blue");
        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f / 1f, TextureStrings.EXPLOSION));

        Random random = new Random();
        isClockwise = random.nextBoolean();

    }


    @Override
    public void update(float dt, Room r) {
        super.update(dt, r);

        if(this.getState() == STATE.DYING || this.getState() == STATE.DEAD) {
            dyingUpdate(dt);
        } else if(state == STATE.ALIVE) {
            reloadTimer.update(dt);
            if (reloadTimer.isFinishedAndReset()) {

                switch(type){
                    case FOUR_BULLETS: bulletsAndRotation();
                        break;
                    case TWO_BULLETS: twoBullets();
                        break;
                }
                //bulletsAndRotation();
            }
        }

    }

    public void bulletsAndRotation(){
        bullets.addProjectile(getProjectile(currentAngleInDegrees));
        bullets.addProjectile(getProjectile(currentAngleInDegrees - 90));
        bullets.addProjectile(getProjectile(currentAngleInDegrees + 90));
        bullets.addProjectile(getProjectile(currentAngleInDegrees - 180));
        currentAngleInDegrees = isClockwise ? currentAngleInDegrees - degreeChange : currentAngleInDegrees + degreeChange;
        if(isClockwise && currentAngleInDegrees < startingAngleInDegress - 45){
            isClockwise = false;
        } else if(!isClockwise && currentAngleInDegrees > startingAngleInDegress + 45) {
            isClockwise = true;
        }
    }

    public void twoBullets(){
        bullets.addProjectile(getProjectile(currentAngleInDegrees));
        bullets.addProjectile(getProjectile(currentAngleInDegrees - 180));
        currentAngleInDegrees = isClockwise ? currentAngleInDegrees - degreeChange : currentAngleInDegrees + degreeChange;
    }

    @Override
    public void reduceHealth(float i) {
        super.reduceHealth(i);
        if(health <= 0){
            bullets.kill();
        }
    }

    public Projectile getProjectile(double angle){
        return new Projectile.ProjectileBuilder(position.x + WIDTH / 2, position.y + HEIGHT / 2, angle)
                .damage(1)
                .drawingColor(Color.RED)
                .speed(Measure.units(45f))
                .scale(1f)
                .build();
    }


}
