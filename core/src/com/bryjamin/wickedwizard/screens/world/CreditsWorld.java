package com.bryjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Condition;
import com.bryjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.ai.ActionAfterTimeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.screens.MenuButton;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 26/07/2017.
 */

public class CreditsWorld {


    private static final float skipWidth = Measure.units(30f);
    private static final float skipHeight = Measure.units(7.5f);
    private static final float skipOffsetY = Measure.units(5);


    private static final float creditStartY = Measure.units(0);

    private static final float creditsSpeed = Measure.units(10f);


    private static final float creditsSmallGap = Measure.units(5f);
    private static final float creditsLargeGap = Measure.units(10f);


    private float positionTrack;


    private Viewport gameport;
    private TextureAtlas atlas;

    private MainGame game;

    private World world;

    private Entity mainMenuTrigger;

    private static final String skipText = "Tap Here To Skip";


    public CreditsWorld(MainGame game, Viewport gameport){
        this.game = game;
        this.atlas  = game.assetManager.get(FileLocationStrings.spriteAtlas);
        this.gameport = gameport;
        createCreditsWorld();
    }




    public void createCreditsWorld(){

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        //new FindPlayerSystem(player),
                        new ActionOnTouchSystem(),
                        new com.bryjamin.wickedwizard.ecs.systems.ai.ConditionalActionSystem(),
                        new ActionAfterTimeSystem(),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, game.assetManager, gameport)
                        //new BoundsDrawingSystem(true)
                )
                .build();


        world = new World(config);





        Entity mainMenuTrigger = new MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.small, atlas.findRegion(TextureStrings.BLOCK))
                .width(skipWidth)
                .height(skipHeight)
                .foregroundColor(Color.WHITE)
                .backgroundColor(new Color(0,0,0,0))
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));
                    }
                }).build()
                .createButton(world, skipText,
                Measure.units(0f),
                Measure.units(52.5f));


        createCreditsPart("Game Created By", creditsSmallGap);
        createCreditsPart("Benjamin Bryant", creditsSmallGap);

        createCreditsPart("Built Using", creditsLargeGap);
        createCreditsPart("LibGDX", creditsSmallGap);
        createCreditsPart("Artemis ODB", creditsSmallGap);

        createCreditsPart("Music And SFX created using", creditsLargeGap);
        createCreditsPart("LMMS", creditsSmallGap);


        createCreditsPart("Special Thanks", creditsLargeGap);
        createCreditsPart("Mark-Adam Kellerman", creditsSmallGap);
        createCreditsPart("Phoebe Clarke", creditsSmallGap);
        createCreditsPart("Johan (\"Flowy\") Almeflo", creditsSmallGap);
        createCreditsPart("Louis Hampton", creditsSmallGap);
        createCreditsPart("James O'Toole", creditsSmallGap);
        createCreditsPart("Mohammedali (\"The First\") Dhanji", creditsSmallGap);
        createCreditsPart("Michelle Bryant", creditsSmallGap);

        createCreditsPart("Pete Colley", creditsSmallGap);
        createCreditsPart("Dave (\"Sharma\") Shama", creditsSmallGap);
        createCreditsPart("Anthony Gibson", creditsSmallGap);



        Entity e = createCreditsPart("Thanks for playing", creditsLargeGap * 3);

        e.edit().add(new ConditionalActionComponent(new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {

                return entity.getComponent(PositionComponent.class).position.y >= gameport.getCamera().position.y;
            }
        }, new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.getComponent(VelocityComponent.class).velocity.y = 0;

                e.edit().add(new ActionAfterTimeComponent(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {
                                e.edit().add(new FadeComponent(false, 1f, false));
                            }
                        }, 0.5f));
                e.edit().remove(ConditionalActionComponent.class);

                e.edit().add(new ConditionalActionComponent(new Condition() {
                    @Override
                    public boolean condition(World world, Entity entity) {
                        return entity.getComponent(TextureFontComponent.class).color.a <= 0f;
                    }
                }, new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));
                    }
                }));


            }
        }));

/*
        world.createEntity();

        mainMenuTrigger.edit().add(new PositionComponent(gameport.getCamera().position.x,
                gameport.getCamera().position.y - Measure.units(20f)));

        mainMenuTrigger.edit().add(new TextureFontComponent(FontAssets.small, skipText,0, skipOffsetY, skipWidth, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, new Color(Color.WHITE)));

        mainMenuTrigger.edit().add(new CollisionBoundComponent(new Rectangle(mainMenuTrigger.getComponent(PositionComponent.class).getX(),
                mainMenuTrigger.getComponent(PositionComponent.class).getY(), skipWidth, skipHeight)));

        mainMenuTrigger.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new MenuScreen(game));
            }
        }));
*/








    };


    private ComponentBag createCreditsBag(String text){


        ComponentBag bag = new ComponentBag();

        PositionComponent pc = new PositionComponent(gameport.getCamera().position.x - MainGame.GAME_WIDTH / 2,
                -Measure.units(5f));

        bag.add(pc);
        //bag.add(new VelocityComponent(0, creditsSpeed));
        bag.add(new TextureFontComponent(com.bryjamin.wickedwizard.assets.FontAssets.small, text,
                MainGame.GAME_WIDTH,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                new Color(Color.WHITE)));

        bag.add(new ExpiryRangeComponent(pc.position, Measure.units(150f)));

        return bag;

    }

    private Pair<ComponentBag, Float> createCreditsPart (ComponentBag bag, float distanceTravelledTillNextInLine){
        return new Pair<ComponentBag, Float>(bag, distanceTravelledTillNextInLine);
    }


    private Entity createCreditsPart(String text, float distanceAdded){

        Entity e = world.createEntity();

        positionTrack -= distanceAdded;

        PositionComponent pc = new PositionComponent(gameport.getCamera().position.x - MainGame.GAME_WIDTH / 2,
                -Measure.units(5f) + positionTrack);


        e.edit().add(pc);
        //bag.add(new VelocityComponent(0, creditsSpeed));
        e.edit().add(new TextureFontComponent(com.bryjamin.wickedwizard.assets.FontAssets.small, text,
                MainGame.GAME_WIDTH,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                new Color(Color.WHITE)));

        e.edit().add(new ExpiryRangeComponent(pc.position, Measure.units(150f) + positionTrack));

        e.edit().add(new VelocityComponent(0, creditsSpeed));

        return e;
    }

/*


    private class CreditRoll extends ConditionalActionComponent {

        priv






    }
*/


    public void process(float delta){
        world.setDelta(delta < 0.030f ? delta : 0.030f);
        world.process();
    }

    public World getWorld() {
        return world;
    }
}
