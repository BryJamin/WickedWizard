package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.audio.HitSoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.SoundSystem;

/**
 * Created by Home on 05/03/2017.
 */
public class HealthSystem extends EntityProcessingSystem {

    ComponentMapper<HealthComponent> hm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<StatComponent> sm;
    ComponentMapper<HitSoundComponent> hitSoundComponent;
    ComponentMapper<PlayerComponent> pm;

    ComponentMapper<BlinkOnHitComponent> bm;

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

                    if (Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getBoolean(PreferenceStrings.DEV_GODMODE, false))
                        return;

                    StatComponent sc = sm.get(e);
                    if (sc.armor > 0) {
                        sc.armor -= 1;
                    } else {
                        sc.health -= 1;
                    }

                    bm.get(e).isHit = true;

                }

            } else {
                hc.health = hc.health - hc.getAccumulatedDamage();
                if(hitSoundComponent.has(e)){
                    world.getSystem(SoundSystem.class).playRandomSound(hitSoundComponent.get(e).hitSounds);
                }

            }
        }

        hc.clearDamage();


        if(hc.health <= 0){
            world.getSystem(com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(e);
        }
    }





}
