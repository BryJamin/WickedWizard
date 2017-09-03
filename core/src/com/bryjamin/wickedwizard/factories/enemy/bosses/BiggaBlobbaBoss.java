package com.bryjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 30/06/2017.
 */

public class BiggaBlobbaBoss extends BossFactory {

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
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(0, 0));

        com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent cbc = new com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent(new Rectangle(x, y, width, height));
        cbc.hitBoxes.add(new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, bottomWidth, bottomHeight)));
        cbc.hitBoxes.add(new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, bottomMidWidth, bottomMidHeight),
                com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, bottomMidWidth), bottomHeight));
        cbc.hitBoxes.add(new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, bottomTopWidth, bottomTopHeight),
                com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, bottomTopWidth), bottomHeight + bottomMidHeight));
        cbc.hitBoxes.add(new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, crownWidth, crownHeight),
                com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, crownWidth), bottomHeight + bottomMidHeight + bottomTopHeight));


        bag.add(cbc);
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());
        // bag.add(new AccelerantComponent(Measure.units(10), Measure.units(20f)));
        // bag.add(new MoveToPlayerComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(1f / 20f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.BIGGABLOBBA_STANDING), Animation.PlayMode.LOOP));
        animMap.put(CHARGINGANIMATION, new Animation<TextureRegion>(1f / 40f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.BIGGABLOBBA_STANDING), Animation.PlayMode.LOOP));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent(animMap));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BIGGABLOBBA_STANDING),
                com.bryjamin.wickedwizard.utils.CenterMath.offsetX(width, textureSize),
                0,
                textureSize,
                textureSize,
                com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        com.bryjamin.wickedwizard.ecs.components.WeaponComponent wc = new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(new com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol.PistolBuilder(assetManager)
                .angles(20,40,60,80, 100, 120, 140, 160)
                .shotSpeed(Measure.units(75f))
                .gravity(true)
                .build());
        bag.add(wc);

        com.bryjamin.wickedwizard.ecs.components.ai.Task weaponPhase = new com.bryjamin.wickedwizard.ecs.components.ai.Task(){
            @Override
            public void performAction(World world, Entity e) {

                com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent f = new com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent(0);
                f.offsetY = gunOffsetY;

                e.edit().add(f);
            }
            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
            }
        };

        com.bryjamin.wickedwizard.utils.Pair<Task, Condition> angryTransitionPhase = new com.bryjamin.wickedwizard.utils.Pair<Task, Condition>(phaseChangeJump(), landingCondition(angryTransitionTime));

        com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent pc = new com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent();
        pc.addPhase(angryTransitionPhase);
        pc.addPhase(jumpingPhase(jumpingPhaseTime));
        pc.addPhase(angryTransitionPhase);
        pc.addPhase(chargingPhaseTime, movementPhase());
        pc.addPhase(new BlobbaMoveToPhase(), new OnTargetXCondition());
        //pc.addPhaseSequence(1,0, 2);

        bag.add(pc);


        return bag;

    }






    private com.bryjamin.wickedwizard.ecs.components.ai.Task movementPhase(){

        return new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent playerCbc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class);
                com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent cbc = e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class);
                com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class);
                vc.velocity.x = cbc.getCenterX() > playerCbc.getCenterX() ? vc.velocity.x = -speed : speed;
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent.class).setDefaultState(CHARGINGANIMATION);
                e.edit().add(blobOCAC(speed));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent.class);
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent.class).setDefaultState(0);
            }
        };

    }


    private class BlobbaMoveToPhase implements com.bryjamin.wickedwizard.ecs.components.ai.Task {

        Vector3 position = new Vector3();

        @Override
        public void performAction(World world, Entity e) {
            position.x = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem.class).getCurrentArena().getWidth() / 2;
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent(position));
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent(speed, speed));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent.class);
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent.class);
        }
    }


    private class OnTargetXCondition implements com.bryjamin.wickedwizard.ecs.components.ai.Condition {
        @Override
        public boolean condition(World world, Entity entity) {
            return entity.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent.class).isOnTargetX;
        }
    }

    private com.bryjamin.wickedwizard.ecs.components.ai.Task phaseChangeJump(){
        return new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent());
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.y = jumpTransitionVly;
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = 0;
                e.edit().add(blobbaOnCollisionActionGroundShake());
            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.y = 0;
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent.class);
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent.class);
            }
        };
    }

    private com.bryjamin.wickedwizard.ecs.components.ai.Condition landingCondition(final float time){
        return new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent.class).currentPhaseTime > time &&
                        entity.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class).getRecentCollisions().contains(com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM, true);
            }
        };
    }


    private com.bryjamin.wickedwizard.utils.Pair<Task, Condition> jumpingPhase(final float time){

        com.bryjamin.wickedwizard.ecs.components.ai.Task task =  new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {

                e.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent pcbc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class);
                        com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent cbc = e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class);

                        boolean isLeftOfPlayer = (cbc.getCenterX() < pcbc.getCenterX());

                        e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = isLeftOfPlayer ? jumpVlx : -jumpVlx;
                        e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.y = jumpVly;

                        e.getComponent(com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent.class).queueAnimationState(com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent.FIRING);

                        e.edit().add(blobbaOnCollisionActionGroundShake());

                    }

                }, 0, 1.5f, true));


                com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent fc = new com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent(true, false);
                fc.airFriction = false;
                e.edit().add(fc);

            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent.class);
                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent.class);
            }
        };

        return new com.bryjamin.wickedwizard.utils.Pair<Task, Condition>(task, landingCondition(time));

    }


    private com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent blobbaOnCollisionActionGroundShake(){
        com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent ocac = new com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent();
        ocac.bottom = new com.bryjamin.wickedwizard.ecs.components.ai.Action() {
            @Override
            public void performAction(World world, Entity e) {

                if(e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent.class) == null) {
                    e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent.class);
                }
                Entity shaker = world.createEntity();
                shaker.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent(stompShakeTime));
                shaker.edit().add(new com.bryjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent(stompIntensity));
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem.class).playRandomSound(com.bryjamin.wickedwizard.assets.SoundFileStrings.enemyJumpLandingMegaMix);
            }
        };
        return ocac;
    }






    private com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent blobOCAC(final float speed){
        com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent onCollisionActionComponent = new com.bryjamin.wickedwizard.ecs.components.ai.OnCollisionActionComponent();
        onCollisionActionComponent.left = new com.bryjamin.wickedwizard.ecs.components.ai.Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = speed;
            }
        };

        onCollisionActionComponent.right = new com.bryjamin.wickedwizard.ecs.components.ai.Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity.x = -speed;
            }
        };

        return onCollisionActionComponent;
    }



}
