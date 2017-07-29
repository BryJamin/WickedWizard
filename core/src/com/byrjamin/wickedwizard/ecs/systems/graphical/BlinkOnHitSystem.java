package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by BB on 09/03/2017.
 *
 * Update 29/07/2017
 *
 * This is system is incredibly legacy
 *
 * The main part of this system is tracking a players flash as unlike enemies they blink in and out when hit,
 * whereas enemies just flash white
 *
 *
 */
public class BlinkOnHitSystem extends EntityProcessingSystem {

    ComponentMapper<BlinkOnHitComponent> bm;
    ComponentMapper<TextureRegionComponent> trm;


    @SuppressWarnings("unchecked")
    public BlinkOnHitSystem() {
        super(Aspect.all(BlinkOnHitComponent.class, TextureRegionComponent.class));
    }

    @Override
    protected void process(Entity e) {

        BlinkOnHitComponent bc = bm.get(e);
        TextureRegionComponent trc = trm.get(e);

        if(bc.isHit){

            bc.timeUntilNoLongerBlinking -= world.delta;
            if(bc.timeUntilNoLongerBlinking > 0){

                if(bc.blinktype == BlinkOnHitComponent.BLINKTYPE.FLASHING){
                    bc.blinkTimer -= world.delta;;
                    if (bc.blinkTimer < 0) {
                        bc.blinkTimer = bc.resetTimer;
                        bc.isBlinking = !bc.isBlinking;
                    }

                    trc.color.a = bc.isBlinking ? 0.1f : trc.DEFAULT.a;

                }


            } else {
                bc.isHit = false;
                bc.timeUntilNoLongerBlinking = bc.resetTimeUntilNoLongerBlinking;
                bc.blinkTimer = bc.resetTimer;
                trc.color.set(trc.DEFAULT);
            }

        }


    }

}
