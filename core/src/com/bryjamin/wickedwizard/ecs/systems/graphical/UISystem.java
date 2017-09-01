package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.bryjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.bryjamin.wickedwizard.factories.items.PickUp;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 31/07/2017.
 */

public class UISystem extends EntitySystem {

    private SpriteBatch batch;

    public boolean isPaused = false;

    private Viewport gameport;

    private AssetManager assetManager;
    private TextureAtlas atlas;

    private ArenaGUI arenaGUI;

    private Array<TextureRegion> healthRegions = new Array<TextureRegion>();

    private com.bryjamin.wickedwizard.ecs.components.StatComponent playerStats;
    private CurrencyComponent playerCurrency;

    private BitmapFont currencyFont;

    public UISystem(MainGame game, Viewport gameport, ArenaGUI arenaGUI, com.bryjamin.wickedwizard.ecs.components.StatComponent playerStats, CurrencyComponent playerCurrency) {
        super(Aspect.all(UIComponent.class, TextureRegionComponent.class, PositionComponent.class));
        this.batch = game.batch;
        this.gameport = gameport;

        this.assetManager = game.assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        this.currencyFont = assetManager.get(com.bryjamin.wickedwizard.assets.FontAssets.small, BitmapFont.class);// font size 12 pixels

        this.playerStats = playerStats;
        this.playerCurrency = playerCurrency;

        this.arenaGUI = arenaGUI;


    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(gameport.getCamera().combined);
        if(!batch.isDrawing()) {
            batch.begin();
        }

    }

    @Override
    protected void processSystem() {

        drawScreenBorder(batch, atlas, gameport.getCamera());
        drawMapAndHud(isPaused);

        for(Entity e : this.getEntities()) {

            TextureRegionComponent trc = e.getComponent(TextureRegionComponent.class);
            PositionComponent pc = e.getComponent(PositionComponent.class);

            batch.draw(trc.region,
                    pc.getX() + trc.offsetX, pc.getY() + trc.offsetY,
                    trc.width, trc.height);

        }

    }


    private void drawScreenBorder(SpriteBatch batch, TextureAtlas atlas, Camera gamecam){
        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        batch.setColor(new Color(Color.BLACK));
        batch.draw(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), camX, camY + gamecam.viewportHeight - Measure.units(5f), gamecam.viewportWidth, Measure.units(5f));
        batch.draw(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), camX, camY, gamecam.viewportWidth, Measure.units(5f));
        batch.draw(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), camX, camY, Measure.units(5f), gamecam.viewportHeight);
        batch.draw(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.BLOCK), camX + gamecam.viewportWidth - Measure.units(5f), camY, Measure.units(5f), gamecam.viewportHeight);
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
                healthRegions.add(atlas.findRegion(com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.healthUp.region.getLeft(), 0));
            } else if(playerStats.health % 2 != 0 && i == playerStats.health){
                healthRegions.add(atlas.findRegion(com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.healthUp.region.getLeft(), 1));
            }
        }

        int emptyHealth = playerStats.maxHealth - playerStats.health;
        emptyHealth = (emptyHealth % 2 == 0) ? emptyHealth : emptyHealth - 1;

        for(int i = 1; i <= emptyHealth; i++) {
            if(i <= emptyHealth && i % 2 == 0) {
                healthRegions.add(atlas.findRegion(com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.healthUp.region.getLeft(), 2));
            }
        }

        for(int i = 0; i < playerStats.armor; i++) {
            healthRegions.add(atlas.findRegion(com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.armorUp.region.getLeft()));
        }

        float screenoffset = Measure.units(10f);

        for(int i = 0; i < ((healthRegions.size < 14) ? healthRegions.size : 14); i++) {
            batch.draw(healthRegions.get(i),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (105 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(5f) + CenterMath.offsetY(Measure.units(5), Measure.units(4f)),
                    MainGame.GAME_UNITS * 4, MainGame.GAME_UNITS * 4);
        }


        PickUp p = new com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1();

        float offsetX = Measure.units(1.5f);

        batch.draw(atlas.findRegion(p.getValues().region.getLeft(), p.getValues().region.getRight()),
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
