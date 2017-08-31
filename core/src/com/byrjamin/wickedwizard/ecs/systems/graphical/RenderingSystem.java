package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Home on 04/03/2017.
 */
public class RenderingSystem extends EntitySystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<BlinkOnHitComponent> bm;
    private ComponentMapper<BulletComponent> bulletm;
    private ComponentMapper<TextureRegionComponent> trm;
    private ComponentMapper<TextureRegionBatchComponent> trbm;
    private ComponentMapper<TextureFontComponent> trfm;
    private ComponentMapper<CollisionBoundComponent> cbm;

    private ArrayList<Entity> orderedEntities;

    public SpriteBatch batch;
    public Viewport gameport;
    public AssetManager assetManager;
    public TextureAtlas atlas;

    public Color batchColor = new Color(Color.WHITE);

    public ShaderProgram whiteShaderProgram;

    private GlyphLayout glyphLayout = new GlyphLayout();


    PerformanceCounter performanceCounter = new PerformanceCounter("Rendering System Counter");


    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch, AssetManager assetManager, Viewport gameport) {
        super(Aspect.all(PositionComponent.class).exclude(UIComponent.class).one(
                TextureRegionComponent.class,
                TextureRegionBatchComponent.class,
                TextureFontComponent.class
        ));
        this.batch = batch;
        this.gameport = gameport;
        this.assetManager = assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);

        orderedEntities = new ArrayList<Entity>();

        loadShader();

    }

    public void loadShader() {
        whiteShaderProgram = new ShaderProgram( Gdx.files.internal("shader/VertexShader.glsl"),
                Gdx.files.internal("shader/WhiteFragmentShader.glsl"));
        if (!whiteShaderProgram.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + whiteShaderProgram.getLog());
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(gameport.getCamera().combined);
        if(!batch.isDrawing()) {
            batch.begin();
        }


        performanceCounter.start();

    }

    @Override
    protected void processSystem() {
        for (int i = 0; orderedEntities.size() > i; i++) {
            process(orderedEntities.get(i));
        }
    }

    protected boolean applyShaderForBlinkOnHitComponent(Entity e){

        boolean shaderOn = false;

        if(bm.has(e)){
            shaderOn = bm.get(e).isHit && bm.get(e).blinktype == BlinkOnHitComponent.BLINKTYPE.CONSTANT;
        }

        if(shaderOn){
            batch.end();
            batch.setShader(whiteShaderProgram);
            batch.begin();
        }

        return shaderOn;

    }

    private void removeShader(){
        batch.end();
        batch.setShader(null);
        batch.begin();
    }

    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);

        boolean shaderOn = applyShaderForBlinkOnHitComponent(e);

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
                    trc.scaleX * rendDirection(e), trc.scaleY,
                    trc.rotation);
            batch.setColor(batchColor);

        }

        if(trbm.has(e)){
            TextureRegionBatchComponent trbc = trbm.get(e);
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

        if(shaderOn) removeShader();

        batch.setColor(batchColor);



    }


    public float rendDirection(Entity e){

        if(world.getMapper(DirectionalComponent.class).has(e)) {
            if(world.getMapper(DirectionalComponent.class).get(e).getDirection() == Direction.LEFT){
                return -1;
            };
        }

        return 1;

    }

    @Override
    protected void end() {
        batch.end();




        performanceCounter.stop();
        performanceCounter.tick();

/*        System.out.println("\n BLOCK");
        System.out.println(performanceCounter.name + " BLOCK");
        System.out.println("MAX " + performanceCounter.time.max);
        System.out.println("AVERAGE " + performanceCounter.time.average);
        System.out.println("LATEST " + performanceCounter.time.latest);*/

/*        System.out.println("Render call:" + batch.renderCalls);


        System.out.println("Render call:" + batch.maxSpritesInBatch);*/

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

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
}
