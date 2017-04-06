package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Home on 04/03/2017.
 */
public class RenderingSystem extends EntitySystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<TextureRegionComponent> trm;
    private ComponentMapper<TextureRegionBatchComponent> trbm;
    private ComponentMapper<TextureFontComponent> trfm;
    private ComponentMapper<ShapeComponent> sm;

    private ArrayList<Entity> orderedEntities;

    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public OrthographicCamera gamecam;

    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch, OrthographicCamera gamecam) {
        super(Aspect.all(PositionComponent.class).one(
                TextureRegionComponent.class,
                TextureRegionBatchComponent.class,
                TextureFontComponent.class,
                ShapeComponent.class
        ));
        this.batch = batch;
        this.gamecam = gamecam;
        shapeRenderer = new ShapeRenderer();
        orderedEntities = new ArrayList<Entity>();
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

        PositionComponent pc = pm.get(e);

        if(trm.has(e)) {
            TextureRegionComponent trc = trm.get(e);

            float originX = trc.width * 0.5f;
            float originY = trc.height * 0.5f;
            batch.setColor(trc.color);
            batch.draw(trc.region,
                    pc.getX() + trc.offsetX, pc.getY() + trc.offsetY,
                    originX, originY,
                    trc.width, trc.height, //This is to avoid pixel errors between repeated textures
                    trc.scaleX, trc.scaleY,
                    trc.rotation);
            batch.setColor(Color.WHITE);
        }

        if(trbm.has(e)){
            TextureRegionBatchComponent trbc = trbm.get(e);
            int count = 0;
            for(int i = 0; i < trbc.columns; i++){
                for(int j = 0; j < trbc.rows; j ++){
                    batch.draw(trbc.regions.get(count),
                            pc.getX() + (trbc.width * i),
                            pc.getY() + (trbc.height * j),
                            trbc.width + 1, trbc.height + 1); //This is to avoid pixel errors between repeated textures
                    count++;
                }
            }

        }

        if(trfm.has(e)) {
            TextureFontComponent trfc = trfm.get(e);
            trfc.font.setColor(trfc.color);
            trfc.font.draw(batch, trfc.text,
                    pc.getX() + trfc.offsetX, pc.getY() + trfc.offsetY
            ,gamecam.viewportWidth, Align.center, true);
            trfc.font.setColor(Color.WHITE);
        }

        if(sm.has(e)) {

            batch.end();

            ShapeComponent sc = sm.get(e);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(gamecam.combined);
            shapeRenderer.begin(sc.shapeType);
            shapeRenderer.setColor(sc.color);
            shapeRenderer.rect(pc.getX(),pc.getY(), sc.width, sc.height);
            shapeRenderer.end();
            batch.begin();
        }



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
    
}
