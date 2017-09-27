package com.bryjamin.wickedwizard.factories.arenas.presetrooms;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.DuringRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by BB on 24/09/2017.
 */

public class PortalRooms extends ArenaShellFactory {

    private PortalFactory portalFactory;

    public PortalRooms(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
        this.portalFactory = new PortalFactory(assetManager);
        this.arenaSkin = arenaSkin; //new BrightWhiteSkin(atlas);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, this.arenaSkin);
    }


    private ComponentBag portalPosition(float x, float y, BossTeleporterComponent btc){
        return portalFactory.mapPortal(x, y, btc);
    }


    /**
     * This is the arena placed inside of the main level map, that is used to teleport to a boss map.
     * @param mapCoords - Mapcoords of the arena
     * @param btc - The boss teleporter component that holds the link to another map.
     * @return - Returns the arena
     */
    public final Arena allDoorsPortalArena(MapCoords mapCoords, BossTeleporterComponent btc){

        Arena arena = new ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(mapCoords, Arena.ArenaType.BOSS);
        arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(25f), Measure.units(5f)));
        arena.addEntity(portalFactory.mapPortal(Measure.units(17.5f), Measure.units(45f), btc));

        arena.addEntity(onLoadSetBossTeleComponentOffsets(btc, Measure.units(25f), 0));

        return arena;
    }



    public Arena portalRoomToBossLeftDoor(MapCoords defaultCoords, BossTeleporterComponent btc){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.BOSS)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), arena.getWidth(), arena.getHeight()));

        arena.addEntity(portalPosition(arena.getWidth() - Measure.units(15f), Measure.units(20f), btc));

        arena.addEntity(onLoadSetBossTeleComponentOffsets(btc, -Measure.units(25f), 0));



        return arena;
    }


    public Arena portalRoomToBossRightDoor(MapCoords defaultCoords, BossTeleporterComponent btc){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.BOSS)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(0, Measure.units(30f), arena.getWidth(), arena.getHeight()));



        arena.addEntity(onLoadSetBossTeleComponentOffsets(btc, Measure.units(25f), 0));

        arena.addEntity(portalPosition(Measure.units(15f), Measure.units(20f), btc));

        return arena;
    }



    public Arena portalRoomToBossPortalInCenter(MapCoords defaultCoords, BossTeleporterComponent btc){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.BOSS)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(32.5f), Measure.units(60f), Measure.units(5f)));
        arena.addEntity(decorFactory.decorativeBlock(Measure.units(25f), Measure.units(32.5f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
        arena.addEntity(decorFactory.decorativeBlock(Measure.units(75f) - com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(32.5f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));


        arena.addEntity(onLoadSetBossTeleComponentOffsets(btc, 0, -Measure.units(25f)));

        arena.addEntity(portalPosition(arena.getWidth() / 2, Measure.units(45f), btc));

        return arena;
    }


    public ComponentBag onLoadSetBossTeleComponentOffsets(final BossTeleporterComponent btc, final float offsetX, final float offsetY){

        ComponentBag bag = new ComponentBag();
        bag.add(new DuringRoomLoadActionComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                btc.offsetX = offsetX;
                btc.offsetY = offsetY;
            }
        }));

        return bag;
    }



    public Arena portalRoomToBossCeilingDoor(MapCoords defaultCoords, BossTeleporterComponent btc){


        boolean isLeft = random.nextBoolean();

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.BOSS)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.FULL)).buildArena();


        float wallPos = isLeft ? arena.getWidth() - Measure.units(40f) : Measure.units(5f);
        float skywallPos = isLeft ? Measure.units(5f) : arena.getWidth() - Measure.units(40);

        arena.addEntity(decorFactory.wallBag(wallPos, Measure.units(5f), Measure.units(35), arena.getHeight()));
        arena.addEntity(decorFactory.wallBag(skywallPos, Measure.units(30f), Measure.units(35), Measure.units(30f)));

        arena.addEntity(onLoadSetBossTeleComponentOffsets(btc, isLeft ? Measure.units(25f) : -Measure.units(25f), 0));

        arena.addEntity(portalPosition(isLeft ? Measure.units(15f) : arena.getWidth() - Measure.units(15f), Measure.units(20f), btc));

        return arena;
    }


    public Arena portalRoomToBossBottomDoor(MapCoords defaultCoords, BossTeleporterComponent btc){


        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.BOSS)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena();



        float wallWidth = Measure.units(10f);

        arena.addEntity(decorFactory.wallBag(Measure.units(15f), Measure.units(10f), wallWidth, arena.getHeight()));
        arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(15f) - wallWidth, Measure.units(10f), wallWidth, arena.getHeight()));

        arena.addEntity(onLoadSetBossTeleComponentOffsets(btc, 0, -Measure.units(15f)));

        arena.addEntity(portalPosition(arena.getWidth() / 2, arena.getHeight() / 2 + Measure.units(5f), btc));

        return arena;
    }








}
