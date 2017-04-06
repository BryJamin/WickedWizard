package com.byrjamin.wickedwizard.archive.gameobject.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.archive.maps.rooms.Room;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.assets.TextureStrings;

/**
 * Basic starter enemy, used for testing purposes.
 */
public class Blob extends GroundedEnemy {

    private Animation<TextureRegion> walk
    = AnimationPacker.genAnimation(0.25f / 1f, TextureStrings.BLOB_STANDING, Animation.PlayMode.LOOP);
    private Animation<TextureRegion> currentAnimation;

    private TextureRegion currentFrame;

    private Vector2 velocity = new Vector2();

    //Optional Parameters
    private float MOVEMENT;
    private float HEIGHT = Measure.units(10);
    private float WIDTH = Measure.units(10);

    public static class BlobBuilder extends GBuilder{
        public BlobBuilder(float posX, float posY) {
            super(posX, posY);
        }

        @Override
        public Blob build() {
            return new Blob(this);
        }
    }

    public Blob(BlobBuilder b) {
        super(b);
        MOVEMENT = Measure.units(15) * speed;
        HEIGHT *= scale;
        WIDTH *= scale;
        collisionBound = new Rectangle(position.x + (Measure.units(1) * scale), position.y,
                WIDTH - (Measure.units(2.5f) * scale),
                HEIGHT - (Measure.units(2.5f) * scale));
        bounds.add(collisionBound);
        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f / 1f, TextureStrings.BLOB_DYING));
        currentAnimation = walk;
        currentFrame = currentAnimation.getKeyFrame(time);
    }

    @Override
    public void update(float dt, Room r) {
        super.update(dt, r);
        if(this.getState() == Enemy.STATE.ALIVE){
            aliveUpdate(dt, r);
        } else if(this.getState() == Enemy.STATE.DYING){
            dyingUpdate(dt);
        }
        currentFrame = currentAnimation.getKeyFrame(time);
    }

    public void aliveUpdate(float dt,  Room room){
        time += dt;
        if(!collisionBound.overlaps(room.getWizard().getBounds())) {
            velocity.x = room.getWizard().getCenterX() > position.x ? MOVEMENT * dt : -MOVEMENT * dt;
        } else {
            velocity.x = 0;
        }
        position.add(velocity.x, 0);
        collisionBound.x = collisionBound.x + velocity.x;
    }

    public void dyingUpdate(float dt){
        time+=dt;
        currentAnimation = this.getDyingAnimation();
        if(this.getDyingAnimation().isAnimationFinished(time)){
            this.setState(Enemy.STATE.DEAD);
        }
    }


    public void draw(SpriteBatch batch){
        if(isFlashing) {
            Color color = batch.getColor();
            batch.setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
            batch.setColor(color);
        } else {
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
        }
        BoundsDrawer.drawBounds(batch, collisionBound);
    }

}
