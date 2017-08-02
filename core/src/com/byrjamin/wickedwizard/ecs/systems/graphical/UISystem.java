package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
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
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.UIComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.utils.Measure;

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

    private StatComponent playerStats;
    private CurrencyComponent playerCurrency;

    private BitmapFont currencyFont;

    public UISystem(MainGame game, Viewport gameport, ArenaGUI arenaGUI, StatComponent playerStats, CurrencyComponent playerCurrency) {
        super(Aspect.all(UIComponent.class));
        this.batch = game.batch;
        this.gameport = gameport;

        this.assetManager = game.manager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        this.currencyFont = assetManager.get(Assets.small, BitmapFont.class);// font size 12 pixels

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

        Array<TextureRegion> healthRegions = new Array<TextureRegion>();

        for(int i = 1; i <= playerStats.health; i++){
            if(i <= playerStats.health && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("item/heart", 0));
            } else if(playerStats.health % 2 != 0 && i == playerStats.health){
                healthRegions.add(atlas.findRegion("item/heart", 1));
            }
        }

        int emptyHealth = playerStats.maxHealth - playerStats.health;
        emptyHealth = (emptyHealth % 2 == 0) ? emptyHealth : emptyHealth - 1;

        for(int i = 1; i <= emptyHealth; i++) {
            if(i <= emptyHealth && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("item/heart", 2));
            }
        }

        float screenoffset = Measure.units(0f);

        int count = 0;

        float otherUIPosition = 0;

        for(int i = 0; i < healthRegions.size; i++) {
            batch.draw(healthRegions.get(i),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (110 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(5f),
                    MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);

            count++;
        }

        int otherCount = count;

        for(int i = count; i < playerStats.armor + count; i++) {
            batch.draw(atlas.findRegion("item/armor"),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (110 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(5f),
                    MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
            otherCount++;
        }

        otherUIPosition = otherCount * 110;

        PickUp p = new MoneyPlus1();

        batch.draw(atlas.findRegion(p.getRegionName().getLeft(), p.getRegionName().getRight()),
                camX + otherUIPosition + 55,
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(3.5f),
                Measure.units(2.5f), Measure.units(2.5f));

        currencyFont.draw(batch, "" + playerCurrency.money,
                camX + otherUIPosition + Measure.units(6f),
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(1.5f),
                Measure.units(7f), Align.left, true);

    }

    @Override
    protected void end() {
        batch.end();
    }


}
