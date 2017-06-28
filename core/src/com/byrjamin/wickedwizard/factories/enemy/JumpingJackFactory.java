package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.OnCollisionActionComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 28/06/2017.
 */

public class JumpingJackFactory extends EnemyFactory{


    private static final float width = Measure.units(10f);
    private static final float height = Measure.units(10f);

    private static final float speed = Measure.units(40f);
    private static final float tiredSpeed = Measure.units(20f);
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
        y = y - width / 2;

        ComponentBag bag = this.defaultEnemyBag(new ComponentBag(), x, y, health);

        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width,height), true));

        bag.add(new AnimationStateComponent(JUMPINGJACKANIMATION));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(JUMPINGJACKANIMATION,  new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions(TextureStrings.JUMPING_JACK), Animation.PlayMode.LOOP));
        animMap.put(JUMPINGJACKTIREDANIMATION,  new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.JUMPING_JACK_TIRED), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.JUMPING_JACK),
                width, height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new VelocityComponent(startsRight ? speed : -speed, 0));


    PhaseComponent phaseComponent = new PhaseComponent();
        phaseComponent.addPhase(awakePhaseTime, new JumpJackPhase());
        phaseComponent.addPhase(tiredPhaseTime, new JumpJackTiredPhase());

        phaseComponent.addPhaseSequence(0,1);


        bag.add(phaseComponent);


        return bag;

    }


    private OnCollisionActionComponent jackOCAC(final float speed){
        OnCollisionActionComponent onCollisionActionComponent = new OnCollisionActionComponent();
        onCollisionActionComponent.left = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(VelocityComponent.class).velocity.x = speed;
            }
        };

        onCollisionActionComponent.right = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(VelocityComponent.class).velocity.x = -speed;
            }
        };

        return onCollisionActionComponent;
    }


    private class JumpJackPhase implements Task {

        @Override
        public void performAction(World world, Entity e) {
            VelocityComponent vc = e.getComponent(VelocityComponent.class);
            vc.velocity.y = jumpSpeed;
            vc.velocity.x = vc.velocity.x < 0 ? vc.velocity.x = -speed : speed;
            e.edit().add(new GravityComponent());
            e.edit().add(new FiringAIComponent(0));
            e.edit().add(new WeaponComponent(new JumpingJackWeapon(assetManager), 0.5f));

            e.getComponent(AnimationStateComponent.class).setDefaultState(JUMPINGJACKANIMATION);

            e.edit().add(jackOCAC(speed));

            e.edit().add(new ActionAfterTimeComponent(new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    e.edit().remove(new GravityComponent());
                    e.getComponent(VelocityComponent.class).velocity.y = 0;
                }

                @Override
                public void cleanUpAction(World world, Entity e) {

                }
            }, airTime));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(FiringAIComponent.class);
            e.edit().remove(WeaponComponent.class);
            e.edit().remove(OnCollisionActionComponent.class);
        }
    }


    private class JumpJackTiredPhase implements Task {

        @Override
        public void performAction(World world, Entity e) {

            VelocityComponent vc = e.getComponent(VelocityComponent.class);
            vc.velocity.x = vc.velocity.x < 0 ? vc.velocity.x = -tiredSpeed : tiredSpeed;
            e.edit().add(new GravityComponent());

            e.getComponent(AnimationStateComponent.class).setDefaultState(JUMPINGJACKTIREDANIMATION);
            //e.edit().add(new FiringAIComponent());
            //e.edit().add(new WeaponComponent(new JumpingJackWeapon(assetManager), 0.5f));
            e.edit().add(jackOCAC(tiredSpeed));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(GravityComponent.class);
        }
    }



    private class JumpingJackWeapon implements Weapon {

        private MultiPistol weapon1;
        private MultiPistol weapon2;

        private boolean isWeapon1;

        public JumpingJackWeapon(AssetManager assetManager) {

            int i = 15;
            int j = 30;
            int k = 45;
            int l = 60;
            int m = 75;

            MultiPistol.PistolBuilder mp = new MultiPistol.PistolBuilder(assetManager)
                    .shotScale(3)
                    .angles(i, j, k, l, m, i + 180, j + 180, k + 180, l + 180, m + 180);

            weapon1 = mp.build();

            weapon2 = mp.angles(i + 90, j + 90, k + 90, l + 90, m + 90,  i + 270, j + 270, k + 270, l + 270, m + 270)
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

        @Override
        public float getBaseDamage() {
            return 0;
        }
    }






}
