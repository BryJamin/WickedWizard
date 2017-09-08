package com.bryjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.Weapon;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.ai.PhaseSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.bryjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 03/07/2017.
 */

public class BossAdoj extends BossFactory {


    private static final float height = Measure.units(24f);
    private static final float width = Measure.units(24f);

    private static final float mainBodyHitBoxWidth = Measure.units(18f);
    private static final float mainBodyHitBoxHeight = Measure.units(14f);
    private static final float mainBodyHitBoxOffsetY = Measure.units(4f);

    private static final float hornHitBoxWidth = Measure.units(2f);
    private static final float hornHitBoxHeight = Measure.units(5f);
    private static final float hornHitBoxOffsetX = Measure.units(1f);
    private static final float hornHitBoxOffsetY = Measure.units(15.5f);

    private static final float rightHornHitBoxOffsetX = Measure.units(21f);

    private static final float health = 70;

    private static final float firingAiOffsetY = Measure.units(5f);
    private static final float fastPistolFireRate = 0.1f;
    private static final float speed = Measure.units(40f);

    private static final float tommyReload = 1f;
    private static final float tommyFiringTime = 0.3f;


    public BossAdoj(AssetManager assetManager) {
        super(assetManager);
    }



    public ComponentBag bossAdoj(float x, float y){

        ComponentBag bag = this.defaultBossBag(new ComponentBag(), x, y,health);


        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height),
                new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, mainBodyHitBoxWidth, mainBodyHitBoxHeight),
                        CenterMath.offsetX(width, mainBodyHitBoxWidth),
                        mainBodyHitBoxOffsetY),
                new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, hornHitBoxWidth, hornHitBoxHeight),
                        hornHitBoxOffsetX,
                        hornHitBoxOffsetY),
                new com.bryjamin.wickedwizard.utils.collider.HitBox(new Rectangle(x, y, hornHitBoxWidth, hornHitBoxHeight),
                        rightHornHitBoxOffsetX,
                        hornHitBoxOffsetY)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ADOJ), width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        bag.add(new AnimationStateComponent());
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions(TextureStrings.ADOJ), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.025f / 1f, atlas.findRegions(TextureStrings.ADOJ_FIRING)));
        bag.add(new AnimationComponent(animMap));

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent());

        PhaseComponent pc = new PhaseComponent();
        OnTargetXCondition onTargetXCondition = new OnTargetXCondition();

        pc.addPhase(20f, new TommyGunPhase(tommyReload));
        pc.addPhase(new MoveToPhase(Direction.UP), onTargetXCondition);
        pc.addPhase(20f, new TommySpreadGunPhase(false));
        pc.addPhase(new MoveToPhase(Direction.LEFT), onTargetXCondition);
        pc.addPhase(20f, new TommyGunPhase(tommyReload));
        pc.addPhase(new MoveToPhase(Direction.UP), onTargetXCondition);
        pc.addPhase(20f, new TommySpreadGunPhase(true));
        pc.addPhase(new MoveToPhase(Direction.RIGHT), onTargetXCondition);



        bag.add(pc);


        return bag;

    }


    private class MoveToPhase implements Task {

        private Direction direction;

        public MoveToPhase(Direction direction){
            this.direction = direction;
        }

        Vector3 position = new Vector3();

        @Override
        public void performAction(World world, Entity e) {

            switch (direction){
                case LEFT:
                default: position.x = world.getSystem(RoomTransitionSystem.class).getCurrentArena().getWidth() / 6;
                    break;
                case RIGHT: position.x = world.getSystem(RoomTransitionSystem.class).getCurrentArena().getWidth() / 6 * 5;
                    break;
                case UP: position.x = world.getSystem(RoomTransitionSystem.class).getCurrentArena().getWidth() / 2;

            }
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent(position));
            e.edit().add(new AccelerantComponent(speed, speed));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent.class);
            e.edit().remove(AccelerantComponent.class);
        }
    }




    private class TommyGunPhase implements Task {

        private Weapon tommyGun;
        private float reloadTime;

        TommyGunPhase(float reloadTime){

            this.reloadTime = reloadTime;
            this.tommyGun = new MultiPistol.PistolBuilder(assetManager)
                    .shotScale(3)
                    .shotSpeed(Measure.units(75f))
                    .fireRate(0.05f)
                    .build();

        }

        @Override
        public void performAction(World world, Entity e) {

            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(tommyGun));
            e.edit().add(new ActionAfterTimeComponent(new Action() {

                private boolean hasBullets = true;

                int count = 0;

                @Override
                public void performAction(World world, Entity e) {
                   // e.getComponent(WeaponComponent.class).addChargeTime(reloadTime);
                    if(hasBullets) {
                        CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                        CollisionBoundComponent playerCbc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);

                        e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
                        e.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.AI.UNTARGETED,
                                BulletMath.angleOfTravel(cbc.getCenterX(), cbc.getCenterY() + firingAiOffsetY, playerCbc.getCenterX(), playerCbc.getCenterY())
                                , 0, firingAiOffsetY));
                        e.getComponent(ActionAfterTimeComponent.class).resetTime = tommyFiringTime;


                    } else {
                        e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
                        e.getComponent(ActionAfterTimeComponent.class).resetTime = tommyReload;


                        count++;

                        if(count >= 3){
                            world.getSystem(PhaseSystem.class).startNextPhase(e);
                        }
                    }

                    hasBullets = !hasBullets;

                }
            }, tommyReload / 2, true));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.WeaponComponent.class);
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
            e.edit().remove(ActionAfterTimeComponent.class);
        }
    }

    private class OnTargetXCondition implements Condition{
        @Override
        public boolean condition(World world, Entity entity) {
            return entity.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent.class).isOnTargetX;
        }
    }


    private class TommySpreadGunPhase implements Task {

        private Weapon fastPistol;
        private boolean isLeft;

        public TommySpreadGunPhase(boolean isLeft){
            fastPistol =  new MultiPistol.PistolBuilder(assetManager)
                    .shotScale(3)
                    .fireRate(fastPistolFireRate)
                    .build();
            this.isLeft = isLeft;
        }

        //TODO BASED ON PLAYER DIRECTION THIS IS HOW THE GUN STARTS SHOOTING
        //TODO ALSO SHOULD PUT THE ANGLES CHANGES IN HERE

        @Override
        public void performAction(World world, Entity e) {
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(fastPistol, 0.5f));
            com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent firingAIComponent = new com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent(isLeft ? -80 : 260);
            firingAIComponent.offsetY = firingAiOffsetY;
            e.edit().add(firingAIComponent);

            if(isLeft) {
                e.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent faic = e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
                        faic.firingAngleInRadians += Math.toRadians(5);
                        if(faic.firingAngleInRadians > Math.toRadians(180)){
                            world.getSystem(PhaseSystem.class).startNextPhase(e);
                        }
                    }
                }, fastPistolFireRate, true));
            } else {


                e.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent faic = e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
                        faic.firingAngleInRadians -= Math.toRadians(5);
                        if(faic.firingAngleInRadians < Math.toRadians(0)){
                            world.getSystem(PhaseSystem.class).startNextPhase(e);
                        }
                    }
                }, fastPistolFireRate, true));




            }


        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.WeaponComponent.class);
            e.edit().remove(com.bryjamin.wickedwizard.ecs.components.ai.FiringAIComponent.class);
            e.edit().remove(ActionAfterTimeComponent.class);
        }
    }



}
