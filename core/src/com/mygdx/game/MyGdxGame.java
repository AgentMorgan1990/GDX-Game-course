package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private boolean movementRight = true;
	private boolean turned = false;
	private int positionX = 0;
	private Anim anim;

	@Override
	public void create() {
		batch = new SpriteBatch();
		anim = new Anim("1.png", 6, 5, Animation.PlayMode.LOOP);
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		anim.setTime(Gdx.graphics.getDeltaTime());

		if (!anim.getFrame().isFlipX() && turned) {
			anim.getFrame().flip(true, false);
		}

		if (anim.getFrame().isFlipX() && !turned) {
			anim.getFrame().flip(true, false);
		}

		if (positionX + anim.getFrame().getRegionWidth() == Gdx.graphics.getWidth()) {
			movementRight = false;
			turned = true;
		}

		if (positionX == 0) {
			movementRight = true;
			turned = false;
		}

		if (movementRight) {
			positionX = positionX + 5;
		} else {
			positionX = positionX - 5;
		}

		batch.begin();
		batch.draw(anim.getFrame(), positionX, 0);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		anim.dispose();
	}
}
