package com.byrjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.GrappleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.WingComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.FrictionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrapplePointSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrappleSystem;
import com.byrjamin.wickedwizard.factories.weapons.Pistol;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.BulletMath;

/**
 * Created by Home on 06/04/2017.
 */

public class PlayerFactory extends AbstractFactory {

    public PlayerFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public ComponentBag playerBag(float x , float y){

        float width = Measure.units(5f);
        float height = Measure.units(5f);

        x = x - width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new PlayerComponent());
        bag.add(new FrictionComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(600,900,width, height)));
        bag.add(new GravityComponent());
        bag.add(new MoveToComponent());
        bag.add(new CurrencyComponent(50, 50));
        bag.add(new JumpComponent());
        bag.add(new GlideComponent());
        bag.add(new AccelerantComponent(Measure.units(30f), Measure.units(30f), Measure.units(80f), Measure.units(80f)));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(0);
        bag.add(sc);

        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, new Animation<TextureRegion>(1/10f, atlas.findRegions("block_walk"), Animation.PlayMode.LOOP));
        k.put(AnimationStateComponent.FIRING, new Animation<TextureRegion>(0.15f / 10, atlas.findRegions("block_walk")));
        bag.add(new AnimationComponent(k));


        StatComponent statComponent = new StatComponent();
        statComponent.fireRate = 1f;
        statComponent.damage = 1f;
        statComponent.speed = 1f;

        bag.add(statComponent);
        WeaponComponent wc = new WeaponComponent(new Pistol(assetManager), 0.3f);
        bag.add(wc);
        bag.add(new HealthComponent(6));
        bag.add(new BlinkComponent(1, BlinkComponent.BLINKTYPE.FLASHING));
        bag.add(new ParentComponent());

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block_walk"),
               width, height, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        trc.color = new Color(Color.WHITE);
        trc.DEFAULT = new Color(Color.WHITE);
        bag.add(trc);

        bag.add(new DirectionalComponent());

        return bag;
    }


    /**
     * @param parc - Parent Component the wings will be attached to
     * @param pc - Position Components the wings will be followings
     * @param isLeft - Whether or not the wing is a left or right wing
     * @return - Returns a bag of components used to create an Entity
     */
    public ComponentBag wings(ParentComponent parc, PositionComponent pc, boolean isLeft){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(pc.getX(), pc.getY()));
        bag.add(new FollowPositionComponent(pc.position, isLeft ? Measure.units(4) : -Measure.units(4), -Measure.units(1)));
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(0, new Animation<TextureRegion>(0.7f / 10, atlas.findRegions("wings"), Animation.PlayMode.LOOP));
        AnimationComponent ac = new AnimationComponent(aniMap);
        bag.add(ac);

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("wings"),
                -Measure.units(0.5f), 0, Measure.units(6), Measure.units(6), TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.scaleX = isLeft ? 1 : -1;
        bag.add(trc);

        bag.add(new WingComponent());

        ChildComponent cc = new ChildComponent();
        parc.children.add(cc);

        bag.add(cc);

        return bag;
    }

    public ComponentBag grappleShot(ParentComponent parc,float x, float y, double angle){


        float width = Measure.units(2);
        float height = Measure.units(2);

        x = x - width / 2;
        y = y - height / 2;


        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));

        bag.add(new VelocityComponent(BulletMath.velocityX(Measure.units(150f), angle), BulletMath.velocityY(Measure.units(150f), angle)));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        //bag.add(new IntangibleComponent());
        //bag.add(new BulletComponent());

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block"),
                width, height, TextureRegionComponent.PLAYER_LAYER_NEAR, new Color(Color.BLACK));

        bag.add(new OnDeathActionComponent(new GibletFactory(assetManager).defaultGiblets(new Color(Color.BLACK))));

        bag.add(trc);


        ChildComponent cc = new ChildComponent();
        parc.children.add(cc);
        bag.add(cc);

        bag.add(new GrappleComponent());


        ConditionalActionComponent cac = new ConditionalActionComponent();
        cac.condition = new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {

                Rectangle r = world.getSystem(GrapplePointSystem.class).returnTouchedGrapple(entity.getComponent(CollisionBoundComponent.class).getCenterX(),
                entity.getComponent(CollisionBoundComponent.class).getCenterY());

                if(r != null) {

                    CollisionBoundComponent cbc = entity.getComponent(CollisionBoundComponent.class);
                    PositionComponent pc = entity.getComponent(PositionComponent.class);
                    cbc.bound.setCenter(r.getX() + r.getWidth() / 2, r.getY() + r.getHeight() / 2);
                    pc.position.set(cbc.bound.x, cbc.bound.y, pc.position.z);


                    return true;

                }

                return false;
            }
        };


        cac.action = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPC(MoveToComponent.class);

                CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);


                float x = e.getComponent(CollisionBoundComponent.class).getCenterX();
                float y = e.getComponent(CollisionBoundComponent.class).getCenterY();

                world.getSystem(GrappleSystem.class).flyToNoPathCheck(
                        BulletMath.angleOfTravel(cbc.getCenterX(), cbc.getCenterY(), x, y),
                        x, y,
                        Measure.units(150f),
                        mtc, cbc);

                mtc.endSpeedX = 0;
                mtc.maxEndSpeedY = Measure.units(80f);//150f = MAXGRAPPLEMOVEMENT


                world.getSystem(FindPlayerSystem.class).getPC(GravityComponent.class).ignoreGravity = true;

                e.edit().remove(VelocityComponent.class);

                e.edit().remove(ConditionalActionComponent.class);

                e.edit().add(new ExpireComponent(1f));
                e.edit().add(new FadeComponent(false, 1f, false));

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };


        bag.add(cac);



        return bag;
    }




}
