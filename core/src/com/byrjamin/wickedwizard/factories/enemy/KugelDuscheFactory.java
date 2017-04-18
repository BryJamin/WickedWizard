package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.OnDeathComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 13/04/2017.
 */

public class KugelDuscheFactory {

    private static float width = Measure.units(9);
    private static float height = Measure.units(9f);

    private static float health = 11f;

    private static final float textureWidth = Measure.units(12);
    private static final float textureHeight = Measure.units(12);

    private static final float textureOffsetX = -Measure.units(1.5f);
    private static final float textureOffsetY = 0;

    private static final float phaseTime = 0.15f;




    public static ComponentBag kugelDusche(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new EnemyComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height), true));
        bag.add(new BlinkComponent());

        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion("sprite_health2"),
                width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE
        ));

        bag.add(new HealthComponent(25));
        bag.add(new FiringAIComponent(Math.toRadians(45)));
        bag.add(new WeaponComponent(kugelWeapon(), 0.1f));

        Action p = new Action() {

            private float angle = (new Random().nextBoolean()) ? 5 : -5;

            @Override
            public void performAction(World w, Entity e) {
                e.getComponent(FiringAIComponent.class).firingAngleInRadians += Math.toRadians(angle);
            }

            @Override
            public void cleanUpAction(World w, Entity e) {

            }
        };

        PhaseComponent pc = new PhaseComponent();
        pc.addPhase(phaseTime, p);
        pc.addPhaseSequence(0,0);

        bag.add(pc);
        bag.add(DeathFactory.basicOnDeathExplosion(new OnDeathComponent(), width, height, 0,0));



        return bag;


    }


    public static Weapon kugelWeapon(){

        return new Weapon() {

            int[] angles = new int[] {0,180};

            @Override
            public void fire(World world,Entity e, float x, float y, double angle) {
                //Math.toRadians()
                for(int i : angles){
                    double angleOfTravel = angle + Math.toRadians(i);
                    Bag<Component> bag = BulletFactory.basicEnemyBulletBag(x, y, 1.7f);
                    bag.add(new VelocityComponent((float) (Measure.units(37) * Math.cos(angleOfTravel)), (float) (Measure.units(34) * Math.sin(angleOfTravel))));
                    BagSearch.getObjectOfTypeClass(TextureRegionComponent.class, bag).layer = TextureRegionComponent.ENEMY_LAYER_FAR;

                    Entity bullet = world.createEntity();
                    for(Component c : bag){
                        bullet.edit().add(c);
                    }
                }
            }

            @Override
            public float getBaseFireRate() {
                return 0.2f;
            }

            @Override
            public float getBaseDamage() {
                return 0;
            }
        };

    }


}
