package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 05/03/2017.
 */
public class HealthSystem extends EntityProcessingSystem {

    ComponentMapper<HealthComponent> hm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<StatComponent> sm;
    ComponentMapper<PlayerComponent> pm;

    ComponentMapper<BlinkComponent> bm;

    @SuppressWarnings("unchecked")
    public HealthSystem() {
        super(Aspect.all(HealthComponent.class));
    }

    @Override
    protected void process(Entity e) {
        HealthComponent hc = hm.get(e);


        if(hc.getAccumulatedDamage() > 0) {
            if (sm.has(e) && pm.has(e)) {

                if(!bm.get(e).isHit) {

                    if (Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GODMODE, false))
                        return;

                    StatComponent sc = sm.get(e);
                    if (sc.armor > 0) {
                        sc.armor -= 1;
                    } else {
                        hc.health -= 1;
                    }

                    bm.get(e).isHit = true;

                }





            } else {
                hc.health = hc.health - hc.getAccumulatedDamage();
            }
        }

        hc.clearDamage();


        if(hc.health <= 0){
            world.getSystem(com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(e);
        }
    }





}
