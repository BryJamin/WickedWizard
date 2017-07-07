package com.byrjamin.wickedwizard.factories.enemy.bosses;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.OnCollisionActionComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.factories.enemy.EnemyFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.MultiPistol;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.Pair;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 30/06/2017.
 */

public class BiggaBlobbaBoss extends EnemyFactory {

    private static final float width = Measure.units(27);
    private static final float height = Measure.units(30);


    private static final float bottomWidth = Measure.units(27);
    private static final float bottomHeight = Measure.units(10);

    private static final float bottomMidWidth = Measure.units(23);
    private static final float bottomMidHeight = Measure.units(8);

    private static final float bottomTopWidth = Measure.units(12f);
    private static final float bottomTopHeight = Measure.units(5);

    private static final float crownWidth = Measure.units(5f);
    private static final float crownHeight = Measure.units(5);

    private static final float gunOffsetY = Measure.units(12.5f);


    private static final float speed = Measure.units(60f);

    private static final float jumpTransitionVly = Measure.units(40f);
    private static final float jumpVlx = Measure.units(45f);
    private static final float jumpVly = Measure.units(75f);

    private static final float textureSize = Measure.units(35f);

    private static final int CHARGINGANIMATION = 5;


    public BiggaBlobbaBoss(AssetManager assetManager) {
        super(assetManager);
    }




    public Bag<Component> biggaBlobbaBag(float x, float y){


        x = x - width / 2;
        y = y - height / 2;


        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));

        Rectangle collision = new Rectangle(x, y, Measure.units(33), Measure.units(38));

        CollisionBoundComponent cbc = new CollisionBoundComponent(new Rectangle(x, y, width, height));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, bottomWidth, bottomHeight)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, bottomMidWidth, bottomMidHeight),
                CenterMath.offsetX(width, bottomMidWidth), bottomHeight));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, bottomTopWidth, bottomTopHeight),
                CenterMath.offsetX(width, bottomTopWidth), bottomHeight + bottomMidHeight));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, crownWidth, crownHeight),
                CenterMath.offsetX(width, crownWidth), bottomHeight + bottomMidHeight + bottomTopHeight));


/*        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(29), Measure.units(5)),
                Measure.units(2), Measure.units(17)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(23), Measure.units(5)),
                Measure.units(5), Measure.units(22)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(15), Measure.units(3)),
                Measure.units(9), Measure.units(27)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(9), Measure.units(7)),
                Measure.units(12), Measure.units(30)));*/

        bag.add(cbc);
        bag.add(new GravityComponent());
        bag.add(new LootComponent(5));
        // bag.add(new AccelerantComponent(Measure.units(10), Measure.units(20f)));
        // bag.add(new MoveToPlayerComponent());
        bag.add(new EnemyComponent());
        bag.add(new HealthComponent(65));
        bag.add(new BlinkComponent());
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

        //TODO fix biggablobba
/*        OnDeathComponent odc = new OnDeathComponent();
        odc.getComponentBags().addAll(itemf.createIntangibleFollowingPickUpBag(0,0, new MoneyPlus1()));
        odc.getComponentBags().addAll(itemf.createIntangibleFollowingPickUpBag(0,0, new MoneyPlus1()));
        odc.getComponentBags().addAll(itemf.createIntangibleFollowingPickUpBag(0,0, new MoneyPlus1()));
        odc.getComponentBags().addAll(itemf.createIntangibleFollowingPickUpBag(0,0, new MoneyPlus1()));
        df.giblets(odc, 10, Color.GREEN);
        bag.add(odc);*/


        WeaponComponent wc = new WeaponComponent(new MultiPistol.PistolBuilder(assetManager)
                .angles(20,40,60,80, 100, 120, 140, 160)
                .shotSpeed(Measure.units(75f))
                .gravity(true)
                .build(),  1.5f);
        bag.add(wc);

        Task task1 = new Task(){
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


        PhaseComponent pc = new PhaseComponent();
        pc.addPhase(5.0f, task1);
        pc.addPhase(new Pair<Task, Condition>(phaseChangeJump(), landingCondition(1f)));
        pc.addPhase(jumpingPhase(5.0f));
        pc.addPhase(new Pair<Task, Condition>(phaseChangeJump(), landingCondition(1f)));
        pc.addPhase(6.0f, movementPhase());
        pc.addPhase(new Pair<Task, Condition>(phaseChangeJump(), landingCondition(1f)));
        //pc.addPhaseSequence(1,0, 2);

        bag.add(pc);


        return bag;

    }




    private Task movementPhase(){

        return new Task() {
            @Override
            public void performAction(World world, Entity e) {
                CollisionBoundComponent playerCbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
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

    private Task phaseChangeJump(){
        return new Task() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new BounceComponent());
                e.getComponent(VelocityComponent.class).velocity.y = jumpTransitionVly;
                e.getComponent(VelocityComponent.class).velocity.x = 0;
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
                        CollisionBoundComponent pcbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                        CollisionBoundComponent cbc = e.getComponent(CollisionBoundComponent.class);

                        boolean isLeftOfPlayer = (cbc.getCenterX() < pcbc.getCenterX());

                        e.getComponent(VelocityComponent.class).velocity.x = isLeftOfPlayer ? jumpVlx : -jumpVlx;
                        e.getComponent(VelocityComponent.class).velocity.y = jumpVly;

                        e.getComponent(AnimationStateComponent.class).queueAnimationState(AnimationStateComponent.FIRING);


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

        return new Pair<Task, Condition>(task, landingCondition(time    ));

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
