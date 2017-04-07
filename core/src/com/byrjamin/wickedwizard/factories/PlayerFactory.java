package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
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
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;

/**
 * Created by Home on 06/04/2017.
 */

public class PlayerFactory {


    public static Bag<Component> playerBag(){

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(600,900));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new PlayerComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(0,0,100, 100)));
        bag.add(new GravityComponent());
        bag.add(new MoveToComponent());
        bag.add(new JumpComponent());
        bag.add(new GlideComponent());
        bag.add(new AccelerantComponent(Measure.units(15f), Measure.units(15f), Measure.units(80f), Measure.units(80f)));

        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);

        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, TextureStrings.SQU_WALK);
        k.put(1, TextureStrings.SQU_FIRING);

        bag.add(new AnimationComponent(k));


        WeaponComponent wc = new WeaponComponent(0.3f, 0.3f);
        wc.additionalComponenets.add(new FriendlyComponent());
        bag.add(wc);
        bag.add(new HealthComponent(6));
        bag.add(new BlinkComponent(1, BlinkComponent.BLINKTYPE.FLASHING));
        bag.add(new ParentComponent());

        TextureRegionComponent trc = new TextureRegionComponent(PlayScreen.atlas.findRegion("squ_walk"),-Measure.units(0.5f), 0, Measure.units(6), Measure.units(6));
        trc.layer = 2;
        bag.add(trc);

        bag.add(new DirectionalComponent());

        return bag;
    }


    public static ComponentBag rightWings(ParentComponent parc, PositionComponent pc){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(pc.getX(), pc.getY()));
        bag.add(new FollowPositionComponent(pc.position, Measure.units(4), -Measure.units(1)));
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(0, TextureStrings.WINGS);
        AnimationComponent ac = new AnimationComponent(aniMap);
        bag.add(ac);

        TextureRegionComponent trc = new TextureRegionComponent(PlayScreen.atlas.findRegion("wings"),
                -Measure.units(0.5f), 0, Measure.units(6), Measure.units(6));
        trc.layer = 1;
        bag.add(trc);


        ChildComponent cc = new ChildComponent();
        parc.children.add(cc);
        bag.add(cc);

        return bag;
    }

    public static ComponentBag leftWings(ParentComponent parc, PositionComponent pc){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(pc.getX(), pc.getY()));
        bag.add(new FollowPositionComponent(pc.position, -Measure.units(4), -Measure.units(1)));
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> aniMap = new IntMap<Animation<TextureRegion>>();
        aniMap.put(0, TextureStrings.WINGS);
        AnimationComponent ac = new AnimationComponent(aniMap);
        bag.add(ac);

        TextureRegionComponent trc = new TextureRegionComponent(PlayScreen.atlas.findRegion("wings"),
                -Measure.units(0.5f), 0, Measure.units(6), Measure.units(6));
        trc.layer = 1;
        trc.scaleX = -1;
        bag.add(trc);

        ChildComponent cc = new ChildComponent();
        parc.children.add(cc);
        bag.add(cc);

        return bag;
    }





}
