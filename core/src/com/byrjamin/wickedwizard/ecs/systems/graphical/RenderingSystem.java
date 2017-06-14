package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.HighlightComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Home on 04/03/2017.
 */
public class RenderingSystem extends EntitySystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<BlinkComponent> bm;
    private ComponentMapper<BulletComponent> bulletm;
    private ComponentMapper<HighlightComponent> hm;
    private ComponentMapper<TextureRegionComponent> trm;
    private ComponentMapper<TextureRegionBatchComponent> trbm;
    private ComponentMapper<TextureFontComponent> trfm;
    private ComponentMapper<ShapeComponent> sm;
    private ComponentMapper<CollisionBoundComponent> cbm;

    private ArrayList<Entity> orderedEntities;

    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public OrthographicCamera gamecam;
    public AssetManager assetManager;
    public TextureAtlas atlas;

    public Color batchColor = new Color(Color.WHITE);

    public ShaderProgram whiteShaderProgram;

    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch, AssetManager assetManager, OrthographicCamera gamecam) {
        super(Aspect.all(PositionComponent.class).one(
                TextureRegionComponent.class,
                TextureRegionBatchComponent.class,
                TextureFontComponent.class,
                ShapeComponent.class
        ));
        this.batch = batch;
        this.gamecam = gamecam;
        this.assetManager = assetManager;
        this.atlas = assetManager.get("sprite.atlas", TextureAtlas.class);

        shapeRenderer = new ShapeRenderer();
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
        batch.setProjectionMatrix(gamecam.combined);
        if(!batch.isDrawing()) {
            batch.begin();
        }
    }

    @Override
    protected void processSystem() {
        for (int i = 0; orderedEntities.size() > i; i++) {
            process(orderedEntities.get(i));
        }
    }

    protected void process(Entity e) {

        //System.out.println("INSIDE");

        PositionComponent pc = pm.get(e);


        if(trm.has(e)) {
            TextureRegionComponent trc = trm.get(e);

/*            if(world.getSystem(CameraSystem.class) != null) {
                if (!world.getSystem(CameraSystem.class).isOnCamera(new Rectangle(pc.getX(), pc.getY(), trc.width, trc.height)))
                    return;
            }*/

            boolean shaderOn = false;

            if(bm.has(e)){
                shaderOn = bm.get(e).isHit && bm.get(e).blinktype == BlinkComponent.BLINKTYPE.CONSTANT;
            }

            if(shaderOn){
                batch.end();
                batch.setShader(whiteShaderProgram);
                batch.begin();
            }

            float originX = trc.width * 0.5f;
            float originY = trc.height * 0.5f;

            batch.setColor(trc.color);

            batch.draw(trc.region,
                    pc.getX() + trc.offsetX, pc.getY() + trc.offsetY,
                    originX, originY,
                    trc.width, trc.height,
                    trc.scaleX * rendDirection(e), trc.scaleY,
                    trc.rotation);
            batch.setColor(batchColor);

            if(shaderOn){
                    batch.end();
                    batch.setShader(null);
                    batch.begin();
            }

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
            bmf.draw(batch, trfc.text,
                    pc.getX() + trfc.offsetX, pc.getY() + trfc.offsetY
            ,trfc.width, Align.center, true);
            bmf.setColor(Color.WHITE);
        }

        if(sm.has(e)) {

            batch.end();
            ShapeComponent sc = sm.get(e);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(gamecam.combined);
            shapeRenderer.begin(sc.shapeType);
            shapeRenderer.setColor(sc.color);
            shapeRenderer.rect(pc.getX(),pc.getY(), sc.width, sc.height);
            shapeRenderer.setColor(batchColor);
            shapeRenderer.end();
            batch.begin();
        }

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
                } else if(sm.has(e1)){
                    layer1 = sm.get(e1).layer;
                }

                if(trm.has(e2)) {
                    layer2 = trm.get(e2).layer;
                } else if(trbm.has(e2)){
                    layer2 = trbm.get(e2).layer;
                } else if(trfm.has(e2)){
                    layer2 = trfm.get(e2).layer;
                } else if(sm.has(e2)){
                    layer2 = sm.get(e2).layer;
                }

                return ((Integer)layer1).compareTo(layer2);
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
