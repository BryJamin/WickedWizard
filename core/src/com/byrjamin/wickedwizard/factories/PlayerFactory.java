package com.byrjamin.wickedwizard.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.weapons.Pistol;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 06/04/2017.
 */

public class PlayerFactory extends AbstractFactory {

    public PlayerFactory(AssetManager assetManager) {
        super(assetManager);
    }

    public ComponentBag playerBag(){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(600,900));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new PlayerComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(600,900,100, 100)));
        bag.add(new GravityComponent());
        bag.add(new MoveToComponent());
        bag.add(new CurrencyComponent(50));
        bag.add(new JumpComponent());
        bag.add(new GlideComponent());
        bag.add(new AccelerantComponent(Measure.units(30f), Measure.units(30f), Measure.units(80f), Measure.units(80f)));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);

        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, new Animation<TextureRegion>(1/10f, atlas.findRegions("squ_walk"), Animation.PlayMode.LOOP));
        k.put(1, new Animation<TextureRegion>(0.15f / 10, atlas.findRegions("squ_firing")));
        bag.add(new AnimationComponent(k));


        StatComponent statComponent = new StatComponent();
        statComponent.fireRate = 10f;
        statComponent.damage = 10f;
        statComponent.speed = 1.5f;

        bag.add(statComponent);
        WeaponComponent wc = new WeaponComponent(new Pistol(assetManager), 0.3f);
        bag.add(wc);
        bag.add(new HealthComponent(6));
        bag.add(new BlinkComponent(1, BlinkComponent.BLINKTYPE.FLASHING));
        bag.add(new ParentComponent());

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("squ_walk"),-Measure.units(0.5f), 0,
                Measure.units(6), Measure.units(6), TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        bag.add(trc);

        bag.add(new DirectionalComponent());

        return bag;
    }


    public ComponentBag rightWings(ParentComponent parc, PositionComponent pc){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(pc.getX(), pc.getY()));
        bag.add(new FollowPositionComponent(pc.position, Measure.units(4), -Measure.units(1)));
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(0, new Animation<TextureRegion>(0.7f / 10, atlas.findRegions("wings"), Animation.PlayMode.LOOP));
        AnimationComponent ac = new AnimationComponent(aniMap);
        bag.add(ac);

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("wings"),
                -Measure.units(0.5f), 0, Measure.units(6), Measure.units(6), TextureRegionComponent.PLAYER_LAYER_FAR);
        bag.add(trc);


        ChildComponent cc = new ChildComponent();
        parc.children.add(cc);
        bag.add(cc);

        return bag;
    }

    public ComponentBag leftWings(ParentComponent parc, PositionComponent pc){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(pc.getX(), pc.getY()));
        bag.add(new FollowPositionComponent(pc.position, -Measure.units(4), -Measure.units(1)));
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(0, new Animation<TextureRegion>(0.7f / 10, atlas.findRegions("wings"), Animation.PlayMode.LOOP));
        AnimationComponent ac = new AnimationComponent(aniMap);
        bag.add(ac);

        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("wings"),
                -Measure.units(0.5f), 0, Measure.units(6), Measure.units(6),
                TextureRegionComponent.PLAYER_LAYER_FAR);
        trc.scaleX = -1;
        bag.add(trc);

        ChildComponent cc = new ChildComponent();
        parc.children.add(cc);
        bag.add(cc);

        return bag;
    }





}
