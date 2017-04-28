package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.utils.Measure;

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

        DecorFactory decorFactory = new DecorFactory(assetManager, arenaSkin);

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
                        DoorComponent.DIRECTION.left));
            }
            //Right
            if(s.right == wall.FULL) {
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT, arenaSkin));
            } else if(s.right == wall.DOOR){
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT -  WALLWIDTH * 4, arenaSkin));
                arena.addDoor(decorFactory.doorBag(SECTION_WIDTH - WALLWIDTH + posX, Measure.units(10) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX + 1, coordY),
                        DoorComponent.DIRECTION.right));
            }

            //Ceiling
            if(s.ceiling != wall.NONE){
                arena.addEntity(decorFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH, arenaSkin));
                if(s.ceiling == wall.DOOR) {
                    arena.addDoor(decorFactory.grateBag(SECTION_WIDTH / 2 + posX, 900 + posY,
                            new MapCoords(coordX, coordY),
                            new MapCoords(coordX, coordY + 1),
                            DoorComponent.DIRECTION.up));
                }
            }

            //Floor
            if(s.floor != wall.NONE){
                arena.addEntity(decorFactory.wallBag(0 + posX,  -WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH * 3, arenaSkin));

                if(s.floor == wall.DOOR) {
                    arena.addDoor(decorFactory.grateBag(SECTION_WIDTH / 2 + posX, 400 + posY,
                            new MapCoords(coordX, coordY),
                            new MapCoords(coordX, coordY -1),
                            DoorComponent.DIRECTION.down));
                }
            }

            //Background
            arena.addEntity(BackgroundFactory.backgroundBags(0 + posX,0 + posY,
                    SECTION_WIDTH,
                    SECTION_HEIGHT,
                    Measure.units(15),
                    assetManager.get("sprite.atlas", TextureAtlas.class).findRegions("backgrounds/wall")));



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
