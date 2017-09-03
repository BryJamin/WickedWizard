package com.bryjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.BackPackComponent;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.WingComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 06/04/2017.
 */

public class PlayerFactory extends AbstractFactory {


    private static final int startingMoney = 0;
    private static final float pauseBeforeShooting = 0.15f;


    public PlayerFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public ComponentBag playerBag(float x , float y){

        float width = Measure.units(5f);
        float height = Measure.units(5f);

        x = x - width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();


       // bag.add(new CameraShakeComponent(1f));
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new PlayerComponent());
        bag.add(new BackPackComponent());
        bag.add(new FrictionComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(x,y,width, height)));
        bag.add(new GravityComponent());
        bag.add(new MoveToComponent());
        bag.add(new CurrencyComponent(startingMoney));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.JumpComponent());
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.GlideComponent());
        bag.add(new AccelerantComponent(Measure.units(30f), Measure.units(30f), Measure.units(80f), Measure.units(80f)));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(AnimationStateComponent.DEFAULT);
        bag.add(sc);

        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(1/ 9f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK_WALK), Animation.PlayMode.LOOP));
        k.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(1 / 15f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK_BLINK)));
        bag.add(new AnimationComponent(k));


        StatComponent statComponent = new com.bryjamin.wickedwizard.ecs.components.StatComponent();

        bag.add(statComponent);
        com.bryjamin.wickedwizard.ecs.components.WeaponComponent wc = new com.bryjamin.wickedwizard.ecs.components.WeaponComponent(new com.bryjamin.wickedwizard.factories.weapons.PlayerPistol(assetManager, statComponent), pauseBeforeShooting);
        bag.add(wc);
        bag.add(new com.bryjamin.wickedwizard.ecs.components.HealthComponent(6));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent(1, com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent.BLINKTYPE.FLASHING));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent());

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK_WALK),
               width, height, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        trc.color = new Color(Color.WHITE);
        trc.DEFAULT = new Color(Color.WHITE);
        bag.add(trc);

        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent());




        bag.add(new ConditionalActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(StatComponent.class).health <= 0;
            }
        }, new Action() {
            @Override
            public void performAction(World world, Entity e) {

                new com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder(assetManager)
                        .numberOfGibletPairs(20)
                        .expiryTime(1.0f)
                        .maxSpeed(Measure.units(80f))
                        .minSpeed(Measure.units(10f))
                        .mixes(com.bryjamin.wickedwizard.assets.SoundFileStrings.explosionMegaMix)
                        .size(Measure.units(0.75f))
                        .intangible(true)
                        .colors(new Color(Color.BLACK), new Color(Color.DARK_GRAY), new Color(Color.WHITE))
                        .build().performAction(world, e);

                e.getComponent(TextureRegionComponent.class).color.a = 0;
                e.getComponent(TextureRegionComponent.class).DEFAULT.a = 0;

                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).killChildComponents(e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class));
                e.edit().remove(ConditionalActionComponent.class);
                e.edit().remove(BlinkOnHitComponent.class);


            }
        }));



        return bag;
    }


    /**
     * @param parc - Parent Component the wings will be attached to
     * @param pc - Position Components the wings will be followings
     * @param isLeft - Whether or not the wing is a left or right wing
     * @return - Returns a bag of components used to create an Entity
     */
    public ComponentBag wings(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent parc, com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent pc, boolean isLeft){

        ComponentBag bag = new ComponentBag();
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(pc.getX(), pc.getY()));
        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(pc.position, isLeft ? Measure.units(4) : -Measure.units(4), -Measure.units(1)));
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(0, new Animation<TextureRegion>(0.7f / 10, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.WINGS), Animation.PlayMode.LOOP));
        AnimationComponent ac = new AnimationComponent(aniMap);
        bag.add(ac);

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.WINGS),
                -Measure.units(0.5f), 0, Measure.units(6), Measure.units(6), TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.scaleX = isLeft ? 1 : -1;
        bag.add(trc);

        bag.add(new WingComponent());

        com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent cc = new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent();
        parc.children.add(cc);


        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                Entity parent = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindChildSystem.class).findParentEntity(e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent.class));

                if(parent != null){
                    TextureRegionComponent parentTexture = parent.getComponent(TextureRegionComponent.class);

                    if(parentTexture != null){
                        e.getComponent(TextureRegionComponent.class).color.a = parentTexture.color.a;
                    }
                }

            }
        }, 0, true));


        bag.add(cc);

        return bag;
    }

    public ComponentBag grappleShot(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent parc, float x, float y, final float targetX, final float targetY, double angle){


        float width = Measure.units(2);
        float height = Measure.units(2);

        x = x - width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x, y));




        //TODO I should switch this to the moveto component which should also use the way the moveToPlayerAI works
        bag.add(new com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent(BulletMath.velocityX(Measure.units(200f), angle), BulletMath.velocityY(Measure.units(200f), angle)));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        //bag.add(new IntangibleComponent());
        //bag.add(new BulletComponent());

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK),
                width, height, TextureRegionComponent.PLAYER_LAYER_NEAR, new Color(Color.BLACK));

        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent(new com.bryjamin.wickedwizard.factories.weapons.Giblets.GibletBuilder(assetManager)
                .numberOfGibletPairs(3)
                .size(Measure.units(0.5f))
                .minSpeed(Measure.units(10f))
                .maxSpeed(Measure.units(20f))
                .colors(new Color(Color.BLACK))
                .intangible(false)
                .expiryTime(0.2f)
                .build()));

        bag.add(trc);


        com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent cc = new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent();
        parc.children.add(cc);
        bag.add(cc);

        bag.add(new com.bryjamin.wickedwizard.ecs.components.identifiers.GrappleComponent());


        ConditionalActionComponent cac = new ConditionalActionComponent(new com.bryjamin.wickedwizard.ecs.components.ai.Condition() {
            @Override
            public boolean condition(World world, Entity entity) {

                Rectangle r = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.input.GrapplePointSystem.class).returnTouchedGrapple(entity.getComponent(CollisionBoundComponent.class).getCenterX(),
                        entity.getComponent(CollisionBoundComponent.class).getCenterY());

                if(r != null && r.contains(targetX, targetY)) {

                    CollisionBoundComponent cbc = entity.getComponent(CollisionBoundComponent.class);
                    com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent pc = entity.getComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class);
                    cbc.bound.setCenter(r.getX() + r.getWidth() / 2, r.getY() + r.getHeight() / 2);
                    pc.position.set(cbc.bound.x, cbc.bound.y, pc.position.z);


                    return true;

                }

                return false;
            }
        }, new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                com.bryjamin.wickedwizard.ecs.components.movement.MoveToComponent mtc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.MoveToComponent.class);

                CollisionBoundComponent cbc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);


                float x = e.getComponent(CollisionBoundComponent.class).getCenterX();
                float y = e.getComponent(CollisionBoundComponent.class).getCenterY();

                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.input.GrappleSystem.class).flyToNoPathCheck(
                        BulletMath.angleOfTravel(cbc.getCenterX(), cbc.getCenterY(), x, y),
                        x, y,
                        Measure.units(150f),
                        mtc, cbc);

                mtc.endSpeedX = 0;
                mtc.maxEndSpeedY = Measure.units(80f);//150f = MAXGRAPPLEMOVEMENT


                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem.class).playRandomSound(com.bryjamin.wickedwizard.assets.SoundFileStrings.grappleFireMix);

                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent.class).ignoreGravity = true;

                e.edit().remove(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class);

                e.edit().remove(ConditionalActionComponent.class);

                e.edit().add(new ExpireComponent(1f));
                e.edit().add(new FadeComponent(false, 1f, false));

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });

        bag.add(cac);



        return bag;
    }




}
