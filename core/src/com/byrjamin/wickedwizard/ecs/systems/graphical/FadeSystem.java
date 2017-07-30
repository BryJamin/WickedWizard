package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by BB on 01/04/2017.
 *
 * Controls the Fade effect on all entities with the FadeComponent
 *
 */

public class FadeSystem extends EntityProcessingSystem {

    ComponentMapper<FadeComponent> fm;

    ComponentMapper<TextureFontComponent> tfm;
    ComponentMapper<TextureRegionComponent> trm;
    ComponentMapper<TextureRegionBatchComponent> trbm;

    @SuppressWarnings("unchecked")
    public FadeSystem() {
        super(Aspect.all(FadeComponent.class));
    }

    @Override
    protected void process(Entity e) {

        FadeComponent fc = fm.get(e);

        if(fc.flicker){
            applyFade(e, fc.maxAlpha);
            fc.flicker = false;
            return;
        }

        fc.alphaTimer = fc.fadeIn ? fc.alphaTimer + world.delta : fc.alphaTimer - world.delta;

        //TODO Currently converted this to use the maxAlpha and time so this may have affected a bunch
        //TODO of fades in the game. Need to review them.
        //TODO note: code previously was: fc.alpha = (fc.alphaTimer / fc.alphaTimeLimit)
        //TODO note: if it was also using the min value it'd be (fc.maxAlpha - fc.minAlpha) + fc.minAlpha

        //Interpolation.fade.apply((fadeElapsed-SUBTITLE_FADE_DELAY) / FADE_IN_TIME);

       // fc.alpha = ((fc.alphaTimer / fc.alphaTimeLimit) * (fc.maxAlpha - fc.minAlpha)) + fc.minAlpha;
        //Can't really tell the difference but apparently interpolation is better?
        fc.alpha = Interpolation.fade.apply(((fc.alphaTimer / fc.alphaTimeLimit) * (fc.maxAlpha - fc.minAlpha)) + fc.minAlpha);
        if(fc.alpha <= fc.minAlpha){
            if(fc.isEndless || fc.count > 0) {
                fc.fadeIn = true;
                fc.alpha = fc.minAlpha;
                fc.count--;

            } else {
                fc.alpha = fc.minAlpha;
            }
        } else if (fc.alpha >= fc.maxAlpha) {
            if(fc.isEndless || fc.count > 0) {
                fc.fadeIn = false;
                fc.alpha = fc.maxAlpha;
                fc.count--;

            } else {
                fc.alpha = fc.maxAlpha;
            }
        }

        applyFade(e, fc.alpha);

    }

    private void applyFade(Entity e, float alpha){
        if(trm.has(e)) trm.get(e).color.a = alpha;
        if(trbm.has(e)) trbm.get(e).color.a = alpha;
        if(tfm.has(e)) tfm.get(e).color.a = alpha;
    }

}
