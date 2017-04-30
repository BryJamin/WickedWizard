package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 18/03/2017.
 */

public class ArenaShellFactory extends AbstractFactory {

    protected DecorFactory decorFactory;
    protected ArenaSkin arenaSkin;

    public ArenaShellFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.arenaSkin = arenaSkin;
    }

    //TODO move this constant somewhere else
    public static final float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public static final float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

    public final float WIDTH = MainGame.GAME_WIDTH;
    public final float HEIGHT = MainGame.GAME_HEIGHT;

    public final float WALLWIDTH = Measure.units(5);

    public Arena createOmniArena(){
        return createOmniArena(new MapCoords(0,0));
    }

    public Arena createOmniArena(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena = new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR))
                .buildArena(arena);

        return arena;
    }

    public Arena createSmallArena(MapCoords defaultCoords, boolean leftDoor, boolean rightDoor, boolean ceilingDoor, boolean topDoor) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena = new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR))
                .buildArena(arena);

        return arena;
    }


    public Arena createWidth2Arena(){
        return createWidth2Arena(new MapCoords(0,0));
    }


    public Arena createWidth2Arena(MapCoords defaultCoords) {
        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH * 2);
        arena.setHeight(SECTION_HEIGHT);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena(arena);

        return arena;
    }


    public Arena createHeight2Arena(MapCoords defaultCoords) {
        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT * 2);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE)).buildArena(arena);

        return arena;
    }




    public Arena createWidth2DeadEndArena(MapCoords defaultCoords, boolean deadEndOnLeft) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH * 2);
        arena.setHeight(SECTION_HEIGHT);

        ArenaBuilder.Section left;
        ArenaBuilder.Section right;

        if(deadEndOnLeft) {
            left = new ArenaBuilder.Section(defaultCoords,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.NONE,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.FULL);

            right = new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                    ArenaBuilder.wall.NONE,
                    ArenaBuilder.wall.DOOR,
                    ArenaBuilder.wall.DOOR,
                    ArenaBuilder.wall.DOOR);
        } else {
            left = new ArenaBuilder.Section(defaultCoords,
                    ArenaBuilder.wall.DOOR,
                    ArenaBuilder.wall.NONE,
                    ArenaBuilder.wall.DOOR,
                    ArenaBuilder.wall.DOOR);

            right = new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                    ArenaBuilder.wall.NONE,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.FULL,
                    ArenaBuilder.wall.FULL);
        }

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(left)
                .addSection(right).buildArena(arena);

        return arena;
    }


    public Arena createDeadEndArena(MapCoords defaultCoords, boolean deadEndOnLeft) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds);
        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);


        ArenaBuilder.wall left;
        ArenaBuilder.wall right;


        if(deadEndOnLeft) {
            left = ArenaBuilder.wall.FULL;
            right = ArenaBuilder.wall.DOOR;
        } else {
            left = ArenaBuilder.wall.DOOR;
            right = ArenaBuilder.wall.FULL;
        }

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        left,
                        right,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena(arena);

        return arena;
    }



    public Arena createLetterIArena(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1));
        containingCorrds.add(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 2));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 2));

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH * 3);
        arena.setHeight(SECTION_HEIGHT * 3);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL ))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 2),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 2),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena(arena);

        arena.addEntity(decorFactory.wallBag(0, SECTION_HEIGHT, SECTION_WIDTH, SECTION_HEIGHT, arenaSkin));
        arena.addEntity(decorFactory.wallBag(SECTION_WIDTH * 2, SECTION_HEIGHT, SECTION_WIDTH, SECTION_HEIGHT, arenaSkin));

        return arena;
    }



    public void cleanArenas(Array<Arena> arenas){
        for(int i = 0; i < arenas.size; i++) {
            Arena a = arenas.get(i);
            for(int j = a.getDoors().size - 1; j >=0; j--) {//for (DoorComponent dc : a.getDoors()) {
                DoorComponent dc = a.getDoors().get(j);
                    if (!findDoorWithinFoundRoom(dc, arenas)) {
                        Bag<Component> bag = a.findBag(dc);
                        if (BagSearch.contains(ActiveOnTouchComponent.class, bag)) {
                            a.getBagOfEntities().remove(bag);
                        } else {
                            CollisionBoundComponent cbc = BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag);
                            if(cbc != null) {
                                a.getBagOfEntities().remove(bag);
                                a.addEntity(decorFactory.wallBag(cbc.bound.x, cbc.bound.y, cbc.bound.getWidth(), cbc.bound.getHeight(), arenaSkin));
                            }
                        }
                        a.adjacentCoords.removeValue(dc.leaveCoords, false);
                        a.doors.removeValue(dc, true);
                    }
                }
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public boolean findDoorWithinFoundRoom(DoorComponent dc, Array<Arena> arenas){
        Arena a = findRoom(dc.leaveCoords, arenas);
        if(a != null) {
            //System.out.println("Find Door " + a.adjacentCoords.contains(dc.currentCoords, false));
            return a.adjacentCoords.contains(dc.currentCoords, false);
        }
        return false;
    }

    public static Arena findRoom(MapCoords mc, Array<Arena> arenas){
        for(Arena a : arenas){
            //System.out.println("Find room " + a.cotainingCoords.contains(mc, false));
            if(a.cotainingCoords.contains(mc, false)){
                return a;
            }
        }
        return null;
    }









}
