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
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.Weapon;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.PhaseSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 03/07/2017.
 */

public class BossAdoj extends EnemyFactory {


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

    private static final float tommyReload = 0.6f;
    private static final float tommyFiringTime = 0.3f;


    public BossAdoj(AssetManager assetManager) {
        super(assetManager);
    }



    public ComponentBag bossAdoj(float x, float y){

        ComponentBag bag = this.defaultBossBag(new ComponentBag(), x, y,health);


        bag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height),
                new HitBox(new Rectangle(x, y, mainBodyHitBoxWidth, mainBodyHitBoxHeight),
                        CenterMath.offsetX(width, mainBodyHitBoxWidth),
                        mainBodyHitBoxOffsetY),
                new HitBox(new Rectangle(x, y, hornHitBoxWidth, hornHitBoxHeight),
                        hornHitBoxOffsetX,
                        hornHitBoxOffsetY),
                new HitBox(new Rectangle(x, y, hornHitBoxWidth, hornHitBoxHeight),
                        rightHornHitBoxOffsetX,
                        hornHitBoxOffsetY)));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.ADOJ), width, height, TextureRegionComponent.ENEMY_LAYER_MIDDLE));


        bag.add(new AnimationStateComponent());
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.05f / 1f, atlas.findRegions(TextureStrings.ADOJ), Animation.PlayMode.LOOP));
        animMap.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.025f / 1f, atlas.findRegions(TextureStrings.ADOJ_FIRING)));
        bag.add(new AnimationComponent(animMap));

        bag.add(new VelocityComponent());
        bag.add(new GravityComponent());

        PhaseComponent pc = new PhaseComponent();
        OnTargetXCondition onTargetXCondition = new OnTargetXCondition();

        pc.addPhase(6f, new TommyGunPhase(tommyReload));
        pc.addPhase(new MoveToPhase(Direction.UP), onTargetXCondition);
        pc.addPhase(9f, new TommySpreadGunPhase(false));
        pc.addPhase(new MoveToPhase(Direction.LEFT), onTargetXCondition);
        pc.addPhase(6f, new TommyGunPhase(tommyReload));
        pc.addPhase(new MoveToPhase(Direction.UP), onTargetXCondition);
        pc.addPhase(9f, new TommySpreadGunPhase(true));
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
            e.edit().add(new MoveToPositionComponent(position));
            e.edit().add(new AccelerantComponent(speed, speed));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(MoveToPositionComponent.class);
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

            e.edit().add(new WeaponComponent(tommyGun));
            e.edit().add(new ActionAfterTimeComponent(new Action() {

                private boolean flip;

                @Override
                public void performAction(World world, Entity e) {
                   // e.getComponent(WeaponComponent.class).addChargeTime(reloadTime);
                    if(flip) {
                        CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);
                        CollisionBoundComponent playerCbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);

                        e.edit().remove(FiringAIComponent.class);
                        e.edit().add(new FiringAIComponent(FiringAIComponent.AI.UNTARGETED,
                                BulletMath.angleOfTravel(cbc.getCenterX(), cbc.getCenterY() + firingAiOffsetY, playerCbc.getCenterX(), playerCbc.getCenterY())
                                , 0, firingAiOffsetY));
                        e.getComponent(ActionAfterTimeComponent.class).resetTime = tommyFiringTime;


                    } else {
                        e.edit().remove(FiringAIComponent.class);
                        e.getComponent(ActionAfterTimeComponent.class).resetTime = tommyReload;
                    }

                    flip = !flip;

                }
            }, tommyReload, true));
        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(WeaponComponent.class);
            e.edit().remove(FiringAIComponent.class);
            e.edit().remove(ActionAfterTimeComponent.class);
        }
    }

    private class OnTargetXCondition implements Condition{
        @Override
        public boolean condition(World world, Entity entity) {
            return entity.getComponent(MoveToPositionComponent.class).isOnTargetX;
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
            e.edit().add(new WeaponComponent(fastPistol, 0.5f));
            FiringAIComponent firingAIComponent = new FiringAIComponent(isLeft ? -80 : 260);
            firingAIComponent.offsetY = firingAiOffsetY;
            e.edit().add(firingAIComponent);

            if(isLeft) {
                e.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        FiringAIComponent faic = e.getComponent(FiringAIComponent.class);
                        faic.firingAngleInRadians += Math.toRadians(6);
                        if(faic.firingAngleInRadians > Math.toRadians(200)){
                            world.getSystem(PhaseSystem.class).startNextPhase(e);
                        }
                    }
                }, fastPistolFireRate, true));
            } else {


                e.edit().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        FiringAIComponent faic = e.getComponent(FiringAIComponent.class);
                        faic.firingAngleInRadians -= Math.toRadians(6);
                        if(faic.firingAngleInRadians < Math.toRadians(-20)){
                            world.getSystem(PhaseSystem.class).startNextPhase(e);
                        }
                    }
                }, fastPistolFireRate, true));




            }


        }

        @Override
        public void cleanUpAction(World world, Entity e) {
            e.edit().remove(WeaponComponent.class);
            e.edit().remove(FiringAIComponent.class);
            e.edit().remove(ActionAfterTimeComponent.class);
        }
    }



}
