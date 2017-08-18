package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 17/08/2017.
 */

public class HealthBarSystem extends EntitySystem {

    private SpriteBatch batch;

    public boolean isActive = false;
    private boolean processingFlag = false;



    private static float initialHealthBarWidth = Measure.units(50f);
    private static float initialHealthBarHeight = Measure.units(2.5f);

    private static float initialHealthBarOffsetY = Measure.units(61.5f);

    private static final float resetTime = 1.5f;
    private float countDownTime = resetTime;

    private float whiteHealthBarWidth;
    private float redHealthBarWidth;

    private static final float whiteHealthBarSpeed = Measure.units(10f);
    private static final float redHealthBarSpeed = Measure.units(80f);

    private float initialHealth = 10;

    private Viewport gameport;
    private Camera gamecam;

    private AssetManager assetManager;
    private TextureAtlas atlas;


    public HealthBarSystem(MainGame game, Viewport gameport) {
        super(Aspect.all(BossComponent.class, HealthComponent.class, BlinkOnHitComponent.class));
        this.batch = game.batch;
        this.gameport = gameport;
        this.gamecam = gameport.getCamera();
        this.assetManager = game.assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);


    }

    @Override
    protected void begin() {
        if (!batch.isDrawing()) {
            batch.begin();
        }

    }

    @Override
    protected boolean checkProcessing() {

        if(!processingFlag) {
            processingFlag = this.getEntities().size() > 0;
        }


        return processingFlag;
    }

    @Override
    protected void processSystem() {


        float maxHealth = 0;
        float health = 0;
        boolean isFlashing = false;

        for(Entity e : this.getEntities()) {
            maxHealth += e.getComponent(HealthComponent.class).maxHealth;
            health += e.getComponent(HealthComponent.class).health;
        }


        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        //Back black bar

        batch.setColor(Color.BLACK);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK),
                camX + CenterMath.offsetX(gamecam.viewportWidth, initialHealthBarWidth),
                camY + initialHealthBarOffsetY,
                initialHealthBarWidth,
                initialHealthBarHeight);



        float currentHealthBarWidth = (health / maxHealth) * initialHealthBarWidth;


        if(redHealthBarWidth >= currentHealthBarWidth){
            redHealthBarWidth = currentHealthBarWidth;
        } else if(redHealthBarWidth < currentHealthBarWidth){
            redHealthBarWidth += redHealthBarSpeed * world.delta;

            if(redHealthBarWidth > currentHealthBarWidth) redHealthBarWidth = currentHealthBarWidth;

        }



        if(whiteHealthBarWidth < redHealthBarWidth){
            whiteHealthBarWidth = redHealthBarWidth;
            countDownTime = resetTime;
        } else if(whiteHealthBarWidth >= redHealthBarWidth){

            countDownTime -= world.delta;

            if(countDownTime <= 0){
                whiteHealthBarWidth -= whiteHealthBarSpeed * world.delta;
            }

        }


        batch.setColor(Color.WHITE);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK),
                camX + CenterMath.offsetX(gamecam.viewportWidth, initialHealthBarWidth),
                camY + initialHealthBarOffsetY ,
                whiteHealthBarWidth,
                initialHealthBarHeight);



        batch.setColor(Color.RED);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK),
                camX + CenterMath.offsetX(gamecam.viewportWidth, initialHealthBarWidth),
                camY + initialHealthBarOffsetY ,
                redHealthBarWidth,
                initialHealthBarHeight);



        processingFlag = false;


    }


    @Override
    protected void end() {
        batch.end();
    }


}
