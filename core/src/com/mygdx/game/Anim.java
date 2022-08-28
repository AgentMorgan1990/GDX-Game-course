package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Anim {

    private Texture image;
    private Animation<TextureRegion> animation;
    private float time;
    private TextureAtlas atlas;

    public Anim(String imageName, int columns, int rows, Animation.PlayMode playMode) {

        image = new Texture(imageName);
        TextureRegion region0 = new TextureRegion(image);
        int xCnt = region0.getRegionWidth() / columns;
        int yCnt = region0.getRegionHeight() / rows;
        TextureRegion[][] regions0 = region0.split(xCnt, yCnt);
        TextureRegion[] regions = new TextureRegion[regions0.length * regions0[0].length];

        int cnt = 0;
        for (int i = 0; i < regions0.length; i++) {
            for (int j = 0; j < regions0[0].length; j++) {
                regions[cnt++] = regions0[i][j];
            }
        }

        this.animation = new Animation<TextureRegion>(1 / 5f, regions);
        this.animation.setPlayMode(playMode);
        time += Gdx.graphics.getDeltaTime();
    }
    public Anim(String atlasName, Animation.PlayMode playMode,String action) {

        atlas = new TextureAtlas(atlasName);
        animation = new Animation<TextureRegion>(1/5f, atlas.findRegions(action));
        animation.setPlayMode(playMode);

        time += Gdx.graphics.getDeltaTime();
    }

    public void dispose() {
        if (image!=null) {
            image.dispose();
        }
        atlas.dispose();
    }

    public TextureRegion getFrame() {
        return animation.getKeyFrame(time);
    }

    public void setTime(float time) {
        this.time += time;
    }

    public void zeroTime() {
        this.time = 0;
    }

    public boolean thisAnimationOver() {
        return animation.isAnimationFinished(time);
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        animation.setPlayMode(playMode);
    }
}
