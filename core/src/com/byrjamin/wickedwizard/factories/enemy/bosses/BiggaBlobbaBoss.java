package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.Pair;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 30/06/2017.
 */

public class BiggaBlobbaBoss extends EnemyFactory {

    private static final float width = Measure.units(25);
    private static final float height = Measure.units(25);

    private static final float textureSize = Measure.units(32.5f);

    private static final float bottomWidth = Measure.units(25);
    private static final float bottomHeight = Measure.units(10);

    private static final float bottomMidWidth = Measure.units(20);
    private static final float bottomMidHeight = Measure.units(7.5f);

    private static final float bottomTopWidth = Measure.units(12f);
    private static final float bottomTopHeight = Measure.units(4.5f);

    private static final float crownWidth = Measure.units(5f);
    private static final float crownHeight = Measure.units(4.5f);

    private static final float gunOffsetY = Measure.units(12.5f);

    private static final float health = 65;

    private static final float speed = Measure.units(60f);

    private static final float jumpTransitionVly = Measure.units(40f);
    private static final float jumpVlx = Measure.units(45f);
    private static final float jumpVly = Measure.units(75f);

    private static final float stompShakeTime = 0.25f;
    private static final float stompIntensity = 0.5f;


    //Phases
    private static final float weaponPhaseTime = 2.0f;
    private static final float angryTransitionTime = 1.0f;
    private static final float jumpingPhaseTime = 5.0f;
    private static final float chargingPhaseTime = 5.0f;

    private static final int CHARGINGANIMATION = 5;


    public BiggaBlobbaBoss(AssetManager assetManager) {
        super(assetManager);
    }



    public ComponentBag centeredBlobbaBag(float x, float y){

        x = x - width / 2;
        y = y - height / 2;

        return biggaBlobbaBag(x, y);
    }

    public ComponentBag biggaBlobbaBag(float x, float y){

        ComponentBag bag = this.defaultBossBag(new ComponentBag(), x, y, health);
        bag.add(new VelocityComponent(0, 0));

        CollisionBoundComponent cbc = new CollisionBoundComponent(new Rectangle(x, y, width, height));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, bottomWidth, bottomHeight)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, bottomMidWidth, bottomMidHeight),
                CenterMath.offsetX(width, bottomMidWidth), bottomHeight));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, bottomTopWidth, bottomTopHeight),
                CenterMath.offsetX(width, bottomTopWidth), bottomHeight + bottomMidHeight));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, crownWidth, crownHeight),
                CenterMath.offsetX(width, crownWidth), bottomHeight + bottomMidHeight + bottomTopHeight));


        bag.add(cbc);
        bag.add(new GravityComponent());
        // bag.add(new AccelerantComponent(Measure.units(10), Measure.units(20f)));
        // bag.add(new MoveToPlayerComponent());
        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(1f / 20f, atlas.findRegions(TextureStrings.BIGGABLOBBA_STANDING), Animation.PlayMode.LOOP));
        animMap.put(CHARGINGANIMATION, new Animation<TextureRegion>(1f / 40f, atlas.findRegions(TextureStrings.BIGGABLOBBA_STANDING), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BIGGABLOBBA_STANDING),
                CenterMath.offsetX(width, textureSize),
                0,
                textureSize,
                textureSize,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        WeaponComponent wc = new WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .angles(20,40,60,80, 100, 120, 140, 160)
                .shotSpeed(Measure.units(75f))
                .gravity(true)
                .build());
        bag.add(wc);

        Task weaponPhase = new Task(){
            @Override
            public void performAction(World world, Entity e) {

                FiringAIComponent f = new FiringAIComponent(0);
                f.offsetY = gunOffsetY;

                e.edit().add(f);
            }
            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(FiringAIComponent.class);
            }
        };

        Pair<Task, Condition> angryTransitionPhase = new Pair<Task, Condition>(phaseChangeJump(), landingCondition(angryTransitionTime));

        PhaseComponent pc = new PhaseComponent();
        pc.addPhase(angryTransitionPhase);
        pc.addPhase(jumpingPhase(jumpingPhaseTime));
        pc.addPhase(angryTransitionPhase);
        pc.addPhase(chargingPhaseTime, movementPhase());
        pc.addPhase(new BlobbaMoveToPhase(), new OnTargetXCondition());
        //pc.addPhaseSequence(1,0, 2);

        bag.add(pc);


        return bag;

    }






    private Task movementPhase(){

        return new Task() {
            @Override
            public void performAction(World world, Entity e) {
                CollisionBoundComponent playerCbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
                CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                VelocityComponent vc = e.getComponent(VelocityComponent.class);
                vc.velocity.x = cbc.getCenterX() > playerCbc.getCenterX() ? vc.velocity.x = -speed : speed;
                e.getComponent(AnimationStateComponent.class).setDefaultState(CHARGINGANIMATION);
                e.edit().add(blobOCAC(speed));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(OnCollisionActionComponent.class);
                e.getComponent(AnimationStateComponent.class).setDefaultState(0);
            }
        };

    }


    private class BlobbaMoveToPhase implements Task {

        Vector3 position = new Vector3();

        @Override
        public void performAction(World world, Entity e) {
            position.x = world.getSystem(RoomTransitionSystem.class).getCurrentArena().getWidth() / 2;
            e.edit().add(new MoveToPositionComponent(position));
            e.edit().add(new AccelerantComponent(speed, speed));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(MoveToPositionComponent.class);
            e.edit().remove(AccelerantComponent.class);
        }
    }


    private class OnTargetXCondition implements Condition{
        @Override
        public boolean condition(World world, Entity entity) {
            return entity.getComponent(MoveToPositionComponent.class).isOnTargetX;
        }
    }

    private Task phaseChangeJump(){
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new BounceComponent());
                e.getComponent(VelocityComponent.class).velocity.y = jumpTransitionVly;
                e.getComponent(VelocityComponent.class).velocity.x = 0;
                e.edit().add(blobbaOnCollisionActionGroundShake());
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.getComponent(VelocityComponent.class).velocity.y = 0;
                e.edit().remove(BounceComponent.class);
            }
        };
    }

    private Condition landingCondition(final float time){
        return new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(PhaseComponent.class).currentPhaseTime > time &&
                        entity.getComponent(CollisionBoundComponent.class).getRecentCollisions().contains(Collider.Collision.BOTTOM, true);
            }
        };
    }


    private Pair<Task, Condition> jumpingPhase(final float time){

        Task task =  new Task() {
            @Override
            public void performAction(World world, Entity e) {

                e.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        CollisionBoundComponent pcbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
                        CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                        boolean isLeftOfPlayer = (cbc.getCenterX() < pcbc.getCenterX());

                        e.getComponent(VelocityComponent.class).velocity.x = isLeftOfPlayer ? jumpVlx : -jumpVlx;
                        e.getComponent(VelocityComponent.class).velocity.y = jumpVly;

                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);

                        e.edit().add(blobbaOnCollisionActionGroundShake());

                    }

                }, 0, 1.5f, true));


                FrictionComponent fc = new FrictionComponent(true, false);
                fc.airFriction = false;
                e.edit().add(fc);

            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(ActionAfterTimeComponent.class);
                e.edit().remove(FrictionComponent.class);
            }
        };

        return new Pair<Task, Condition>(task, landingCondition(time));

    }


    private OnCollisionActionComponent blobbaOnCollisionActionGroundShake(){
        OnCollisionActionComponent ocac = new OnCollisionActionComponent();
        ocac.bottom = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().remove(OnCollisionActionComponent.class);
                Entity shaker = world.createEntity();
                shaker.edit().add(new ExpireComponent(stompShakeTime));
                shaker.edit().add(new CameraShakeComponent(stompIntensity));
            }
        };
        return ocac;
    }






    private OnCollisionActionComponent blobOCAC(final float speed){
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



}
