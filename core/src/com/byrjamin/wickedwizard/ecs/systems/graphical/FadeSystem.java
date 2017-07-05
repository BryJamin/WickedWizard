package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

/**
 * Created by Home on 01/04/2017.
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

        fc.alphaTimer = fc.fadeIn ? fc.alphaTimer + world.delta : fc.alphaTimer - world.delta;

        fc.alpha = fc.alphaTimer / fc.alphaTimeLimit;
        if(fc.alpha <= fc.minAlpha){
            if(fc.isEndless) {
                fc.fadeIn = true;
                fc.alpha = fc.minAlpha;
            } else {
                fc.alpha = fc.minAlpha;
            }
        } else if (fc.alpha >= fc.maxAlpha) {
            if(fc.isEndless) {
                fc.fadeIn = false;
                fc.alpha = fc.maxAlpha;
            } else {
                fc.alpha = fc.maxAlpha;
            }
        }

        if(trm.has(e)) trm.get(e).color.a = fc.alpha;
        if(trbm.has(e)) trbm.get(e).color.a = fc.alpha;
        if(tfm.has(e)) tfm.get(e).color.a = fc.alpha;





    }

}
