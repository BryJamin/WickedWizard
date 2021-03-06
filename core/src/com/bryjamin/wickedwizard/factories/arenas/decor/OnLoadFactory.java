package com.bryjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.bryjamin.wickedwizard.assets.Mix;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Locale;

/**
 * Created by BB on 19/08/2017.
 */

public class OnLoadFactory {



    public ComponentBag startMusicEntity(final Mix mix){

        ComponentBag messageAction = new ComponentBag();
        messageAction.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.MusicSystem.class).changeMix(mix);
                e.deleteFromWorld();
            }
        }, 0f, false));

        return messageAction;

    }







    public ComponentBag challengeTimer(final float timeLimit){

        ComponentBag bag = new ComponentBag();


        bag.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {


                Entity player = world.getSystem(FindPlayerSystem.class).getPlayerEntity();

                Entity timer = world.createEntity();
                timer.edit().add(new PositionComponent(player.getComponent(CollisionBoundComponent.class).getCenterX(), player.getComponent(CollisionBoundComponent.class).getCenterY()));
                timer.edit().add(new FollowPositionComponent(
                        world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class).position,
                        player.getComponent(CollisionBoundComponent.class).bound.width / 2, Measure.units(10)));

                timer.edit().add(new FadeComponent(false, 0.5f, true));
                timer.edit().add(new ChildComponent(world.getSystem(FindPlayerSystem.class).getPlayerComponent(ParentComponent.class)));
                timer.edit().add(new TextureFontComponent(com.bryjamin.wickedwizard.assets.FontAssets.small,
                        String.format(Locale.getDefault(), "%.0f", timeLimit), TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(Color.WHITE)));
                timer.edit().add(new ChallengeTimerComponent(timeLimit));
                timer.edit().add(new ActionAfterTimeComponent(new Action() {

                    @Override
                    public void performAction(World world, Entity e) {
                        ChallengeTimerComponent challengeTimerComponent = e.getComponent(ChallengeTimerComponent.class);

                        TextureFontComponent textureFontComponent = e.getComponent(TextureFontComponent.class);

                        textureFontComponent.text = String.format(Locale.getDefault(), "%.0f", challengeTimerComponent.time);

                        if(challengeTimerComponent.time <= 0){
                            world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).setHealth(-1);
                            e.deleteFromWorld();
                        }

                    }
                }, 0, true));

                e.deleteFromWorld();
            }
        }));

        return bag;

    }









}
