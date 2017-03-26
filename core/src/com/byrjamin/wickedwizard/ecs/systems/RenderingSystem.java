package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
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

    private ArrayList<Entity> orderedEntities;

    public SpriteBatch batch;
    public OrthographicCamera gamecam;

    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch, OrthographicCamera gamecam) {
        super(Aspect.all(PositionComponent.class).one(TextureRegionComponent.class, TextureRegionBatchComponent.class));
        this.batch = batch;
        this.gamecam = gamecam;
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
                    trc.width, trc.height,
                    trc.scaleX, trc.scaleY,
                    trc.rotation);
            batch.setColor(Color.WHITE);
        }

        if(trbm.has(e)){
            TextureRegionBatchComponent trbc = trbm.get(e);
            int count = 0;
            for(int i = 0; i < trbc.columns; i++){
                for(int j = 0; j < trbc.rows; j ++){
                    batch.draw(trbc.regions.get(count), pc.getX() + (trbc.width * i), pc.getY() + (trbc.height * j), trbc.width, trbc.height);
                    count++;
                }
            }

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
                }

                if(trm.has(e2)) {
                    layer2 = trm.get(e2).layer;
                } else if(trbm.has(e2)){
                    layer2 = trbm.get(e2).layer;
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
