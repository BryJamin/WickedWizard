package com.bryjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 16/04/2017.
 */

public class ArenaBuilder {

    public static final float SECTION_WIDTH = com.bryjamin.wickedwizard.MainGame.GAME_WIDTH;
    public static final float SECTION_HEIGHT = com.bryjamin.wickedwizard.MainGame.GAME_HEIGHT;

/*    public static final float WIDTH = MainGame.GAME_WIDTH;
    public static final float HEIGHT = MainGame.GAME_HEIGHT;*/

    public static final float WALLWIDTH = Measure.units(5.0f);

    public enum wall {
        FULL, DOOR, MANDATORYDOOR, NONE, GRAPPLE, MANDATORYGRAPPLE; //TODO waaaaay too hackkkkkyy
    }

    private MapCoords defaultCoords;
    private Array<Section> sections = new Array<Section>();


    private Arena.ArenaType arenaType = Arena.ArenaType.TRAP;

    private AssetManager assetManager;
    private TextureAtlas atlas;
    private ArenaSkin arenaSkin;
    private com.bryjamin.wickedwizard.factories.BackgroundFactory bf = new com.bryjamin.wickedwizard.factories.BackgroundFactory();
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;

    public ArenaBuilder(AssetManager assetManager, ArenaSkin arenaSkin, Arena.ArenaType arenaType, Section... sections){
        this.assetManager = assetManager;
        this.arenaSkin = arenaSkin;
        this.arenaType = arenaType;
        atlas = this.assetManager.get(com.bryjamin.wickedwizard.assets.FileLocationStrings.spriteAtlas, TextureAtlas.class);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);

        this.sections.addAll(sections);

    }

    public ArenaBuilder addSection(Section s){
        sections.add(s);
        return this;
    }




    public void setWidthAndHeight(Arena a, Array<Section> sections){

        Section s = sections.first();

        int minX = s.coords.getX();
        int maxX = s.coords.getX();
        int minY = s.coords.getY();
        int maxY = s.coords.getY();

        for(Section section : sections) {
            int x = section.coords.getX();
            int y = section.coords.getY();

            minX = x < minX ? x : minX;
            maxX = x > maxX ? x : maxX;

            minY = y < minY ? y : minY;
            maxY = y > maxY ? y : maxY;
        }

        int diffX = Math.abs(minX - maxX);
        int diffY = Math.abs(minY - maxY);


        a.setWidth(SECTION_WIDTH * (diffX + 1));
        a.setHeight(SECTION_HEIGHT * (diffY + 1));

    }

    public void setArenaCoords(Arena a, Array<Section> sections){
        a.setStartingCoords(sections.get(0).coords);
        for(Section s : sections){
            a.addCoords(s.coords);
        }
    }



    public Arena buildArena(){

        if(sections.size <= 0) return new Arena(new com.bryjamin.wickedwizard.factories.arenas.skins.LightGraySkin(), new MapCoords());

        Array<MapCoords> mapCoordses = new Array<MapCoords>();

        for(Section s : sections){
            mapCoordses.add(s.coords);
        }

        Arena arena = new Arena(arenaType, arenaSkin, mapCoordses);
        defaultCoords = sections.first().coords;
        setWidthAndHeight(arena, sections);



        for(Section s : sections) {

            int multX = s.coords.getX() - defaultCoords.getX();
            int multY = s.coords.getY() - defaultCoords.getY();

            int coordX = defaultCoords.getX() + multX;
            int coordY = defaultCoords.getY() + multY;

            float posX = multX * SECTION_WIDTH;
            float posY = multY * SECTION_HEIGHT;

            boolean hasSkin = s.arenaSkin != null;

            ArenaSkin arenaSkin;

            if(hasSkin){
                arenaSkin = s.arenaSkin;
                decorFactory.setArenaSkin(arenaSkin);
            } else {
                decorFactory.setArenaSkin(this.arenaSkin);
                arenaSkin = this.arenaSkin;
            }


            if(isLeftMostWall(arena, posX)){
                arena.addEntity(decorFactory.wallBag(0 + posX - WALLWIDTH * 4, 0 + posY, WALLWIDTH * 4, SECTION_HEIGHT, arenaSkin));
            }

            if(isRightMostWall(arena, posX)){
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH + posX, 0 + posY, WALLWIDTH * 4, SECTION_HEIGHT, arenaSkin));
            }


            if(isCeiling(arena, posY)){
                arena.addEntity(decorFactory.wallBag(0 + posX,  SECTION_HEIGHT + posY, SECTION_WIDTH, WALLWIDTH * 4, arenaSkin));
            }


            //float


            //Left
            if(s.left == wall.FULL) {
                arena.addEntity(decorFactory.wallBag(0 + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT, arenaSkin));
            } else if(s.left == wall.DOOR || s.left == wall.MANDATORYDOOR){
                arena.addEntity(decorFactory.wallBag(0 + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, arenaSkin));
                arena.addEntity(decorFactory.wallBag(0 + posX, 0 + posY, WALLWIDTH, Measure.units(10f), arenaSkin));
                arena.addDoor(decorFactory.defaultDoorBag(0 + posX, Measure.units(10) + posY,
                        true,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX - 1, coordY),
                        com.bryjamin.wickedwizard.utils.enums.Direction.LEFT), s.left == wall.MANDATORYDOOR);
            }
            //Right
            if(s.right == wall.FULL) {
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT, arenaSkin));
            } else if(s.right == wall.DOOR || s.right == wall.MANDATORYDOOR){
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT -  WALLWIDTH * 4, arenaSkin));
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, 0 + posY, WALLWIDTH, Measure.units(10f)));
                arena.addDoor(decorFactory.defaultDoorBag(SECTION_WIDTH - WALLWIDTH + posX, Measure.units(10) + posY,
                        true,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX + 1, coordY),
                        com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT), s.right == wall.MANDATORYDOOR);
            }

            //Ceiling
            if(s.ceiling == wall.FULL){
                arena.addEntity(decorFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH, arenaSkin));

            } else if(s.ceiling == wall.DOOR || s.ceiling == wall.MANDATORYDOOR ||s.ceiling == wall.GRAPPLE || s.ceiling == wall.MANDATORYGRAPPLE) {

                arena.addEntity(decorFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, Measure.units(40f), WALLWIDTH, arenaSkin));

                arena.addDoor(decorFactory.defaultDoorBag(Measure.units(40f) + posX, SECTION_HEIGHT - WALLWIDTH + posY,
                        false,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX, coordY + 1),
                        com.bryjamin.wickedwizard.utils.enums.Direction.UP), (s.ceiling == wall.MANDATORYDOOR || s.ceiling == wall.MANDATORYGRAPPLE));

                arena.addEntity(decorFactory.wallBag(Measure.units(60) + posX,  SECTION_HEIGHT - WALLWIDTH + posY, Measure.units(40f), WALLWIDTH, arenaSkin));

                if(s.ceiling == wall.GRAPPLE || s.ceiling == wall.MANDATORYGRAPPLE){

                    ComponentBag bag = decorFactory.hiddenGrapplePointBag(posX + SECTION_WIDTH / 2, posY + ((SECTION_HEIGHT / 4) * 3));
                    com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc = new com.bryjamin.wickedwizard.ecs.components.object.DoorComponent(
                            new MapCoords(coordX, coordY),
                            new MapCoords(coordX, coordY + 1),
                            com.bryjamin.wickedwizard.utils.enums.Direction.UP);
                    dc.ignore = true;
                    bag.add(dc);
                    arena.addDoor(bag);


                }

            }

            //Floor
            buildFloor(arena, arenaSkin, s, posX, posY, coordX, coordY);

            //Background
            arena.addEntity(bf.backgroundBags(0 + posX,0 + posY,
                    SECTION_WIDTH,
                    SECTION_HEIGHT,
                    Measure.units(20f),
                    atlas.findRegions(arenaSkin.getBackgroundTextures()),
                    arenaSkin));



        }



        return arena;




    }


    /**
     * Note to future. All arena currently start a (0,0) however, it could be useful to have an arena here if
     * arenas ever do not start a (0,0)
     * @param arena
     * @param posX
     * @return
     */
    private boolean isLeftMostWall(Arena arena, float posX){
        return posX == 0;
    }


    private boolean isRightMostWall(Arena arena, float posX){
        return posX + SECTION_WIDTH == arena.getWidth();
    }


    private boolean isCeiling(Arena arena, float posY){
        return posY + SECTION_HEIGHT == arena.getHeight();
    }

    private boolean isFloor(Arena arena, float posY){
        return posY == 0;
    }




    public void buildFloor(Arena arena, ArenaSkin arenaSkin, Section s, float posX, float posY, int coordX, int coordY){
        if(s.floor == wall.FULL){

            if(isFloor(arena, posY)) {


                arena.addEntity(decorFactory.wallBag(0 + posX, 0 + posY - WALLWIDTH * 2, SECTION_WIDTH, WALLWIDTH * 4, arenaSkin));

            } else {

                arena.addEntity(decorFactory.wallBag(0 + posX, 0 + posY, SECTION_WIDTH, WALLWIDTH * 2, arenaSkin));

            }

        } else if(s.floor == wall.DOOR || s.floor == wall.MANDATORYDOOR) {

            arena.addEntity(decorFactory.wallBag(0 + posX,  0 + posY, Measure.units(40f), WALLWIDTH * 2, arenaSkin));


            arena.addDoor(decorFactory.unTexturedDoorBag(Measure.units(40f) + posX, 0 + posY,
                    Measure.units(20f),
                    Measure.units(5f),
                    new MapCoords(coordX, coordY),
                    new MapCoords(coordX, coordY - 1),
                    com.bryjamin.wickedwizard.utils.enums.Direction.DOWN));

            Bag<Component> bag = decorFactory.doorBag(Measure.units(40f) + posX, 0 + posY, Measure.units(20f), Measure.units(10f),
                    false,
                    new MapCoords(coordX, coordY),
                    new MapCoords(coordX, coordY - 1),
                    com.bryjamin.wickedwizard.utils.enums.Direction.DOWN);

            com.bryjamin.wickedwizard.utils.BagSearch.getObjectOfTypeClass(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent.class, bag).ignore = true;

            arena.addEntity(decorFactory.platform(Measure.units(40f) + posX, Measure.units(5f), Measure.units(20f)));

            arena.addDoor(bag);

            arena.addEntity(decorFactory.wallBag(Measure.units(60) + posX,  0 + posY, Measure.units(40f), WALLWIDTH * 2, arenaSkin));

        }
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

        public ArenaSkin arenaSkin;


        public Section(){
            this(new MapCoords(), wall.DOOR, wall.DOOR, wall.DOOR, wall.DOOR);
        }


        public Section(MapCoords coords, wall left, wall right, wall ceiling, wall floor){
            this.coords = coords;
            this.left = left;
            this.right = right;
            this.ceiling = ceiling;
            this.floor = floor;
        }

        public Section(MapCoords coords, wall left, wall right, wall ceiling, wall floor, ArenaSkin arenaSkin){
            this.coords = coords;
            this.left = left;
            this.right = right;
            this.ceiling = ceiling;
            this.floor = floor;
            this.arenaSkin = arenaSkin;
        }

    }

}
