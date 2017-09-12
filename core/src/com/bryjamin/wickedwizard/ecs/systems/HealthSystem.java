package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.ecs.components.HealthComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem;

/**
 * Created by Home on 05/03/2017.
 */
public class HealthSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.HealthComponent> hm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent> vm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.StatComponent> sm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.audio.HitSoundComponent> hitSoundComponent;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent> pm;

    ComponentMapper<BlinkOnHitComponent> bm;

    @SuppressWarnings("unchecked")
    public HealthSystem() {
        super(Aspect.all(HealthComponent.class));
    }

    @Override
    protected void process(Entity e) {
        com.bryjamin.wickedwizard.ecs.components.HealthComponent hc = hm.get(e);


        if(hc.getAccumulatedDamage() > 0) {
            if (sm.has(e) && pm.has(e)) {
                if(bm.has(e)) {
                    if (!bm.get(e).isHit) {

                        if (Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getBoolean(PreferenceStrings.DEV_GODMODE, false))
                            return;

                        if(sm.has(e)) {
                            StatComponent sc = sm.get(e);
                            if (sc.armor > 0) {
                                sc.armor -= 1;
                            } else {
                                sc.health -= 1;
                            }

                        }

                        bm.get(e).isHit = true;

                    }
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
            world.getSystem(OnDeathSystem.class).kill(e);
        }
    }





}
