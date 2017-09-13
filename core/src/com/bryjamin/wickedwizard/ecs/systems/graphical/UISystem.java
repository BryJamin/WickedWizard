package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.bryjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.bryjamin.wickedwizard.factories.items.PickUp;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Home on 31/07/2017.
 */

public class UISystem extends EntitySystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent> bulletm;
    private ComponentMapper<TextureRegionComponent> trm;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent> trbm;
    private ComponentMapper<TextureFontComponent> trfm;
    private ComponentMapper<CollisionBoundComponent> cbm;

    private ArrayList<Entity> orderedEntities = new ArrayList<Entity>();

    public SpriteBatch batch;
    public Viewport gameport;
    public AssetManager assetManager;
    public TextureAtlas atlas;

    public Color batchColor = new Color(Color.WHITE);

    public ShaderProgram whiteShaderProgram;

    private GlyphLayout glyphLayout = new GlyphLayout();


    public boolean isPaused = false;


    public boolean disable = false;



    private ArenaGUI arenaGUI;

    private Array<TextureRegion> healthRegions = new Array<TextureRegion>();
    private StatComponent playerStats;
    private CurrencyComponent playerCurrency;

    private RenderingSystem renderingSystem;

    private BitmapFont currencyFont;


    private Color white = new Color(Color.WHITE);

    private boolean drawGuideLine;

    public void updateGuideLineUsingPreferences(){
        this.drawGuideLine = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true);
    }

    public UISystem(MainGame game, Viewport gameport, ArenaGUI arenaGUI, com.bryjamin.wickedwizard.ecs.components.StatComponent playerStats, CurrencyComponent playerCurrency) {
        super(Aspect.all(PositionComponent.class, UIComponent.class).one(
                TextureRegionComponent.class,
                TextureRegionBatchComponent.class,
                TextureFontComponent.class));

        this.batch = game.batch;
        this.gameport = gameport;

        this.renderingSystem = new RenderingSystem(batch, game.assetManager, gameport);

        this.assetManager = game.assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        this.currencyFont = assetManager.get(com.bryjamin.wickedwizard.assets.FontAssets.small, BitmapFont.class);// font size 12 pixels

        this.playerStats = playerStats;
        this.playerCurrency = playerCurrency;

        this.arenaGUI = arenaGUI;

        this.drawGuideLine = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true);




    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(gameport.getCamera().combined);
        batch.enableBlending();
        if(!batch.isDrawing()) {
            batch.begin();
        }



    }



    @Override
    public void inserted(Entity e) {
        orderedEntities.add(e);
        Collections.sort(orderedEntities, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {

                Integer layer1 = 0;
                Integer layer2 = 0;

                if(trm.has(e1)) {
                    layer1 = trm.get(e1).layer;
                } else if(trbm.has(e1)){
                    layer1 = trbm.get(e1).layer;
                } else if(trfm.has(e1)){
                    layer1 = trfm.get(e1).layer;
                }

                if(trm.has(e2)) {
                    layer2 = trm.get(e2).layer;
                } else if(trbm.has(e2)){
                    layer2 = trbm.get(e2).layer;
                } else if(trfm.has(e2)){
                    layer2 = trfm.get(e2).layer;
                }

                return layer1.compareTo(layer2);
            }
        });
    }

    @Override
    public void removed(Entity e) {
        orderedEntities.remove(e);
    }



    @Override
    protected void processSystem() {

        drawScreenBorder(batch, atlas, gameport.getCamera());

        if(!disable) {
            if (drawGuideLine) {
                drawGuideLine(batch, atlas, gameport.getCamera());
            }
            drawMapAndHud(isPaused);



        for (int i = 0; orderedEntities.size() > i; i++) {
            if(process(orderedEntities.get(i))){
                // count++;
            };
        }

        }


    }

    private void drawGuideLine(SpriteBatch batch, TextureAtlas atlas, Camera gamecam) {
        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;
        batch.setColor(1,1,1,1f);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY  + MainGame.GAME_BORDER + Measure.units(9.5f), gamecam.viewportWidth, Measure.units(0.5f));
        batch.setColor(white);
    }


    protected boolean process(Entity e) {

        PositionComponent pc = pm.get(e);

        if(cbm.has(e)) {
            if (!CameraSystem.isOnCamera(cbm.get(e).bound, gameport.getCamera())) return false;
        }


        if(trm.has(e)) {
            TextureRegionComponent trc = trm.get(e);


            float originX = trc.width * 0.5f;
            float originY = trc.height * 0.5f;

            batch.setColor(trc.color);


            TextureRegion tr = trc.region != null ? trc.region : atlas.findRegion(TextureStrings.BLOCK);

            batch.draw(tr,
                    pc.getX() + trc.offsetX, pc.getY() + trc.offsetY,
                    originX, originY,
                    trc.width, trc.height,
                    trc.scaleX, trc.scaleY,
                    trc.rotation);
            batch.setColor(batchColor);

        }

        if(trbm.has(e)){
            com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent trbc = trbm.get(e);
            int count = 0;
            batch.setColor(trbc.color);

            float originX = trbc.width * 0.5f;
            float originY = trbc.height * 0.5f;

            for(int i = 0; i < trbc.columns; i++){
                for(int j = 0; j < trbc.rows; j ++){
                    batch.draw(trbc.regions.get(count),
                            pc.getX() + (trbc.width * i) + trbc.offsetX,
                            pc.getY() + (trbc.height * j) + trbc.offsetY,
                            originX,
                            originY,
                            trbc.width + 1,
                            //The top does not have the extra pixel
                            (j == trbc.rows - 1) ? trbc.height : trbc.height + 1,
                            trbc.scaleX,
                            trbc.scaleY,
                            trbc.rotation); //This is to avoid pixel errors between repeated textures
                    count++;
                }
            }
            batch.setColor(batchColor);

        }

        if(trfm.has(e)) {

            //TODO should really use the font width not this random ass camera shizz

            TextureFontComponent trfc = trfm.get(e);
            BitmapFont bmf = assetManager.get(trfc.font, BitmapFont.class);
            bmf.setColor(trfc.color);

            if(cbm.has(e)){
                CollisionBoundComponent cbc = cbm.get(e);
                glyphLayout.setText(bmf, trfc.text);

/*
                System.out.println("layout height" + glyphLayout.height);
                System.out.println("Bound height" + cbc.bound.height);*/

                bmf.draw(batch, glyphLayout,
                        pc.getX() + CenterMath.offsetX(cbc.bound.getWidth(), glyphLayout.width) + trfc.offsetX,
                        pc.getY() + glyphLayout.height + CenterMath.offsetY(cbc.bound.getHeight(), glyphLayout.height) + trfc.offsetY);
            } else {
                bmf.draw(batch, trfc.text,
                        pc.getX() + trfc.offsetX, pc.getY() + trfc.offsetY
                        ,trfc.width, trfc.align, true);
            }

            bmf.setColor(Color.WHITE);
        }

        batch.setColor(batchColor);

        return true;

    }



    private void drawScreenBorder(SpriteBatch batch, TextureAtlas atlas, Camera gamecam){
        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        batch.setColor(new Color(Color.BLACK));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY + gamecam.viewportHeight - Measure.units(5f), gamecam.viewportWidth, Measure.units(5f));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY, gamecam.viewportWidth, Measure.units(5f));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY, Measure.units(5f), gamecam.viewportHeight);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX + gamecam.viewportWidth - Measure.units(5f), camY, Measure.units(5f), gamecam.viewportHeight);
        batch.setColor(new Color(Color.WHITE));
    }






    public void drawMapAndHud(boolean isPaused){

        if(!isPaused) {
            RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
            arenaGUI.update(world.delta,
                    gameport.getCamera().position.x + Measure.units(45),
                    gameport.getCamera().position.y + Measure.units(25),
                    rts.getCurrentMap(),
                    rts.getCurrentPlayerLocation());
        }

        drawHUD(world, gameport.getCamera());
        arenaGUI.draw(batch);

        //HUD




    }



    public void drawHUD(World world, Camera gamecam){

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;
        //BORDER

        healthRegions.clear();

        for(int i = 1; i <= playerStats.health; i++){
            if(i <= playerStats.health && i % 2 == 0) {
                healthRegions.add(atlas.findRegion(com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.healthUp.getRegion().getLeft(), 0));
            } else if(playerStats.health % 2 != 0 && i == playerStats.health){
                healthRegions.add(atlas.findRegion(com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.healthUp.getRegion().getLeft(), 1));
            }
        }

        int emptyHealth = playerStats.maxHealth - playerStats.health;
        emptyHealth = (emptyHealth % 2 == 0) ? emptyHealth : emptyHealth - 1;

        for(int i = 1; i <= emptyHealth; i++) {
            if(i <= emptyHealth && i % 2 == 0) {
                healthRegions.add(atlas.findRegion(com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.healthUp.getRegion().getLeft(), 2));
            }
        }

        for(int i = 0; i < playerStats.armor; i++) {
            healthRegions.add(atlas.findRegion(com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.armorUp.getRegion().getLeft()));
        }

        float screenoffset = Measure.units(10f);

        for(int i = 0; i < ((healthRegions.size < 14) ? healthRegions.size : 14); i++) {
            batch.draw(healthRegions.get(i),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (Measure.units(5.25f) * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(5f) + CenterMath.offsetY(Measure.units(5), Measure.units(4f)),
                    MainGame.GAME_UNITS * 4, MainGame.GAME_UNITS * 4);
        }


        PickUp p = new com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1();

        float offsetX = Measure.units(1.5f);

        batch.draw(atlas.findRegion(p.getValues().getRegion().getLeft(), p.getValues().getRegion().getRight()),
                camX + offsetX,
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(3.5f),
                Measure.units(2.5f), Measure.units(2.5f));

        currencyFont.draw(batch, "" + playerCurrency.money,
                camX + offsetX + Measure.units(3f),
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(1.25f),
                0, Align.left, true);

  //      currencyFont.dr

    }

    @Override
    protected void end() {
        batch.end();
    }


}
