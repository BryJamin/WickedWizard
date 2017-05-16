package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 16/04/2017.
 */

public class ArenaBuilder {

    public static final float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public static final float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

/*    public static final float WIDTH = MainGame.GAME_WIDTH;
    public static final float HEIGHT = MainGame.GAME_HEIGHT;*/

    public static final float WALLWIDTH = Measure.units(5);

    public enum wall {
        FULL, DOOR, NONE
    }

    private MapCoords defaultCoords;
    private Array<Section> sections = new Array<Section>();

    private AssetManager assetManager;
    private ArenaSkin arenaSkin;
    private BackgroundFactory bf = new BackgroundFactory();

    public ArenaBuilder(AssetManager assetManager, ArenaSkin arenaSkin){
        this.assetManager = assetManager;
        this.arenaSkin = arenaSkin;
    }

    public ArenaBuilder addSection(Section s){
        sections.add(s);
        return this;
    }


    public Arena buildArena(Arena arena){

        defaultCoords = arena.getStartingCoords();

        com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);

        for(Section s : sections) {


            //arena.cotainingCoords.add(s.coords);

            //System.out.println(sections.size);

            int multX = s.coords.getX() - defaultCoords.getX();
            int multY = s.coords.getY() - defaultCoords.getY();

            int coordX = defaultCoords.getX() + multX;
            int coordY = defaultCoords.getY() + multY;

            float posX = multX * SECTION_WIDTH;
            float posY = multY * SECTION_HEIGHT;



            //float


            //Left
            if(s.left == wall.FULL) {
                arena.addEntity(decorFactory.wallBag(0 + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT, arenaSkin));
            } else if(s.left == wall.DOOR){
                arena.addEntity(decorFactory.wallBag(0 + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, arenaSkin));
                arena.addDoor(decorFactory.doorBag(0 + posX, Measure.units(10) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX - 1, coordY),
                        Direction.LEFT));
            }
            //Right
            if(s.right == wall.FULL) {
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT, arenaSkin));
            } else if(s.right == wall.DOOR){
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT -  WALLWIDTH * 4, arenaSkin));
                arena.addDoor(decorFactory.doorBag(SECTION_WIDTH - WALLWIDTH + posX, Measure.units(10) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX + 1, coordY),
                        Direction.RIGHT));
            }

            //Ceiling
            if(s.ceiling == wall.FULL){
                arena.addEntity(decorFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH, arenaSkin));
/*                if(s.ceiling == wall.DOOR) {
                    arena.addDoor(decorFactory.grateBag(SECTION_WIDTH / 2 + posX, 900 + posY,
                            new MapCoords(coordX, coordY),
                            new MapCoords(coordX, coordY + 1),
                            Direction.UP));
                }*/
            } else if(s.ceiling == wall.DOOR) {

                arena.addEntity(decorFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, Measure.units(40f), WALLWIDTH, arenaSkin));

                arena.addDoor(decorFactory.horizontalDoorBag(Measure.units(40f) + posX, SECTION_HEIGHT - WALLWIDTH + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX, coordY + 1),
                        Direction.UP));

                arena.addEntity(decorFactory.wallBag(Measure.units(60) + posX,  SECTION_HEIGHT - WALLWIDTH + posY, Measure.units(40f), WALLWIDTH, arenaSkin));

            }

            //Floor
            if(s.floor == wall.FULL){
                arena.addEntity(decorFactory.wallBag(0 + posX,  0 + posY, SECTION_WIDTH, WALLWIDTH * 2, arenaSkin));
/*                if(s.ceiling == wall.DOOR) {
                    arena.addDoor(decorFactory.grateBag(SECTION_WIDTH / 2 + posX, 900 + posY,
                            new MapCoords(coordX, coordY),
                            new MapCoords(coordX, coordY + 1),
                            Direction.UP));
                }*/
            } else if(s.floor == wall.DOOR) {

                arena.addEntity(decorFactory.wallBag(0 + posX,  0 + posY, Measure.units(40f), WALLWIDTH * 2, arenaSkin));

                arena.addDoor(decorFactory.horizontalDoorBag(Measure.units(40f) + posX, 0 + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX, coordY - 1),
                        Direction.DOWN));

                Bag<Component> bag = decorFactory.horizontalDoorBag(Measure.units(40f) + posX, Measure.units(5f) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX, coordY - 1),
                        Direction.DOWN);

                BagSearch.getObjectOfTypeClass(DoorComponent.class, bag).ignore = true;


                arena.addEntity(decorFactory.platform(Measure.units(40f), Measure.units(5f), Measure.units(20f)));

                arena.addDoor(bag);

                arena.addEntity(decorFactory.wallBag(Measure.units(60) + posX,  0 + posY, Measure.units(40f), WALLWIDTH * 2, arenaSkin));

            }

            //Background
            arena.addEntity(bf.backgroundBags(0 + posX,0 + posY,
                    SECTION_WIDTH,
                    SECTION_HEIGHT,
                    Measure.units(20),
                    arenaSkin.getBackgroundTextures(),
                    arenaSkin));



        }



        return arena;




    }




    public Section findSection(MapCoords mapCoords) {
        for(Section s : sections) {
            if(s.coords.equals(mapCoords)) {
                return s;
            }
        }
        return null;
    }







    public static class Section {

        public MapCoords coords;
        public wall left;
        public wall right;
        public wall ceiling;
        public wall floor;


        public Section(MapCoords coords, wall left, wall right, wall ceiling, wall floor){
            this.coords = coords;
            this.left = left;
            this.right = right;
            this.ceiling = ceiling;
            this.floor = floor;
        }

    }

}
