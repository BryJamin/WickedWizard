package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.components.BlinkComponent;
import com.byrjamin.wickedwizard.components.PlayerComponent;
import com.byrjamin.wickedwizard.components.StateComponent;
import com.byrjamin.wickedwizard.components.TextureRegionComponent;

/**
 * Created by Home on 09/03/2017.
 */
public class BlinkSystem extends EntityProcessingSystem {

    ComponentMapper<BlinkComponent> bm;
    ComponentMapper<TextureRegionComponent> trm;
    ComponentMapper<PlayerComponent> pm;


    @SuppressWarnings("unchecked")
    public BlinkSystem() {
        super(Aspect.all(BlinkComponent.class, TextureRegionComponent.class));
    }

    @Override
    protected void process(Entity e) {

        BlinkComponent bc = bm.get(e);
        TextureRegionComponent trc = trm.get(e);

        if(bc.isHit){

            bc.blinkFrames -= world.delta;
            if(bc.blinkFrames > 0){

                if(bc.blinktype == BlinkComponent.BLINKTYPE.CONSTANT){
                    trc.setColor(0.0f,0.0f,0.0f,0.95f);
                } else {
                    bc.blinkTimer -= world.delta;;
                    if (bc.blinkTimer < 0) {
                        bc.blinkTimer = bc.resetTimer;
                        bc.isBlinking = !bc.isBlinking;
                    }
                    if(bc.isBlinking) {
                        trc.color.a = 0.1f;
                    } else {
                        trc.color.a = trc.DEFAULT.a;
                    }
                }


            } else {
                bc.isHit = false;
                bc.blinkFrames = bc.resetFrames;
                bc.blinkTimer = bc.resetTimer;
                trc.color.set(trc.DEFAULT);
            }

        }


    }

}
