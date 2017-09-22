package com.bryjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 28/06/2017.
 */

public class JumpingJackFactory extends EnemyFactory{


    private static final float width = Measure.units(10f);
    private static final float height = Measure.units(10f);

    private static final float speed = Measure.units(40f);
    private static final float tiredSpeed = Measure.units(20f);
    private static final float tiredjumpSpeed = Measure.units(20);
    private static final float health = 25;

    private static final float jumpSpeed = Measure.units(100);
    private static final float airTime = 0.4f;

    private static final float tiredPhaseTime = 3.0f;
    private static final float awakePhaseTime = 6.0f;


    private static final int JUMPINGJACKANIMATION = 0;
    private static final int JUMPINGJACKTIREDANIMATION = 1;

    public JumpingJackFactory(AssetManager assetManager) {
        super(assetManager);
    }


    public ComponentBag jumpingJack(float x, float y, boolean startsRight) {


        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));

        bag.add(new AnimationStateComponent(JUMPINGJACKANIMATION));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(JUMPINGJACKANIMATION,  new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions(TextureStrings.JUMPING_JACK), Animation.PlayMode.LOOP));
        animMap.put(JUMPINGJACKTIREDANIMATION,  new Animation<TextureRegion>(0.2f / 1f, atlas.findRegions(TextureStrings.JUMPING_JACK_TIRED), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.JUMPING_JACK_TIRED),
                width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(startsRight ? speed : -speed, 0));


        PhaseComponent phaseComponent = new PhaseComponent();
        phaseComponent.addPhase(tiredPhaseTime, new JumpJackTiredPhase());
        phaseComponent.addPhase(awakePhaseTime, new JumpJackPhase(startsRight));
        phaseComponent.addPhase(tiredPhaseTime, new JumpJackTiredPhase());
        phaseComponent.addPhase(awakePhaseTime, new JumpJackPhase(!startsRight));
        bag.add(phaseComponent);


        return bag;

    }


    private com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent jackOCAC(final float speed){
        com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent onCollisionActionComponent = new com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent();
        onCollisionActionComponent.left = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = speed;
            }
        };

        onCollisionActionComponent.right = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = -speed;
            }
        };

        return onCollisionActionComponent;
    }


    private class JumpJackPhase implements com.bryjamin.wickedwizard.ecs.components.ai.Task {


        private boolean startsRight;

        public JumpJackPhase(boolean startsRight){
            this.startsRight = startsRight;
        }

        @Override
        public void performAction(World world, Entity e) {
            com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class);
            vc.velocity.y = jumpSpeed;
            vc.velocity.x = 0;
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent(0));
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(new JumpingJackWeapon(assetManager), 0.5f));

            e.getComponent(AnimationStateComponent.class).setDefaultState(JUMPINGJACKANIMATION);

            e.edit().add(jackOCAC(speed));

            e.edit().add(new ActionAfterTimeComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
                @Override
                public void performAction(World world, Entity e) {
                    e.edit().remove(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
                    com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class);
                    vc.velocity.y = 0;
                    vc.velocity.x = startsRight ? vc.velocity.x = -speed : speed;
                }

                @Override
                public void cleanUpAction(World world, Entity e) {

                }
            }, airTime));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.WeaponComponent.class);
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent.class);
        }
    }


    private class JumpJackTiredPhase implements com.bryjamin.wickedwizard.ecs.components.ai.Task {

        @Override
        public void performAction(World world, Entity e) {

            com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class);
            vc.velocity.x = 0;
            vc.velocity.y = tiredjumpSpeed;
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
            e.getComponent(AnimationStateComponent.class).setDefaultState(JUMPINGJACKTIREDANIMATION);

        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent.class);
        }
    }



    private class JumpingJackWeapon implements Weapon {

        private com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol weapon1;
        private com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol weapon2;

        private boolean isWeapon1;

        public JumpingJackWeapon(AssetManager assetManager) {

            int i = 10;
            int j = 35;
            int k = 45;
            int l = 80;
            com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder mp = new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                    .shotScale(3)
                    .shotSpeed(Measure.units(60f))
                    .angles(i, j, k, l, i + 180, j + 180, k + 180, l + 180);

            weapon1 = mp.build();

            weapon2 = mp.angles(i + 90, j + 90, k + 90, l + 90, i + 270, j + 270, k + 270, l + 270)
                    .build();

        }



        @Override
        public void fire(World world, Entity e, float x, float y, double angleInRadians) {

            if(isWeapon1) {
                weapon1.fire(world, e,x,y,angleInRadians);
                isWeapon1 = false;
            } else {
                weapon2.fire(world,e,x,y,angleInRadians);
                isWeapon1 = true;
            }


        }

        @Override
        public float getBaseFireRate() {
            return 1;
        }

    }






}
