package com.bryjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.bryjamin.wickedwizard.assets.Mix;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Locale;

/**
 * Created by BB on 19/08/2017.
 */

public class OnLoadFactory {


    public com.bryjamin.wickedwizard.utils.ComponentBag largeMessageBag(final String string){

        com.bryjamin.wickedwizard.utils.ComponentBag messageAction = new com.bryjamin.wickedwizard.utils.ComponentBag();
        messageAction.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem.class).createLevelBanner(string);
                e.deleteFromWorld();
            }
        }, 0f, false));

        return messageAction;

    }


    public com.bryjamin.wickedwizard.utils.ComponentBag startMusicEntity(final Mix mix){

        com.bryjamin.wickedwizard.utils.ComponentBag messageAction = new com.bryjamin.wickedwizard.utils.ComponentBag();
        messageAction.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.MusicSystem.class).changeMix(mix);
                e.deleteFromWorld();
            }
        }, 0f, false));

        return messageAction;

    }







    public com.bryjamin.wickedwizard.utils.ComponentBag challengeTimer(final float timeLimit){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();


        bag.add(new com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {


                Entity player = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerEntity();

                Entity timer = world.createEntity();
                timer.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(player.getComponent(CollisionBoundComponent.class).getCenterX(), player.getComponent(CollisionBoundComponent.class).getCenterY()));
                timer.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class).position,
                        player.getComponent(CollisionBoundComponent.class).bound.width / 2, Measure.units(10)));

                timer.edit().add(new FadeComponent(false, 0.5f, true));
                timer.edit().add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class)));
                timer.edit().add(new com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent(com.bryjamin.wickedwizard.assets.FontAssets.small,
                        String.format(Locale.getDefault(), "%.0f", timeLimit), TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(Color.WHITE)));
                timer.edit().add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent(timeLimit));
                timer.edit().add(new ActionAfterTimeComponent(new Action() {

                    @Override
                    public void performAction(World world, Entity e) {
                        com.bryjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent challengeTimerComponent = e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent.class);

                        com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent textureFontComponent = e.getComponent(com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent.class);

                        textureFontComponent.text = String.format(Locale.getDefault(), "%.0f", challengeTimerComponent.time);

                        if(challengeTimerComponent.time <= 0){
                            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).health = -1;
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
