package com.byrjamin.wickedwizard.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.components.PositionComponent;
import com.byrjamin.wickedwizard.components.TextureRegionComponent;
import com.byrjamin.wickedwizard.components.VelocityComponent;

/**
 * Created by Home on 04/03/2017.
 */
public class RenderingSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<TextureRegionComponent> trm;

    public SpriteBatch batch;
    OrthographicCamera gamecam;

    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch, OrthographicCamera gamecam) {
        super(Aspect.all(PositionComponent.class, TextureRegionComponent.class));
        this.batch = batch;
        this.gamecam = gamecam;
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(gamecam.combined);
        if(!batch.isDrawing()) {
            batch.begin();
        }
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        TextureRegionComponent trc = trm.get(e);

        float originX = trc.width * 0.5f;
        float originY = trc.height * 0.5f;


        batch.setColor(trc.color);
        batch.draw(trc.region,
                pc.getX() + trc.offsetX , pc.getY() + trc.offsetY,
                originX, originY,
                trc.width, trc.height,
                trc.scaleX, trc.scaleY,
                trc.rotation);
        batch.setColor(Color.WHITE);

    }

    @Override
    protected void end() {
        batch.end();
    }
}
