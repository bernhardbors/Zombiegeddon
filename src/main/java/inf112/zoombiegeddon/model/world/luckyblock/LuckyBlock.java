package inf112.zoombiegeddon.model.world.luckyblock;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Concrete Lucky Block implementation backed by a Box2D static body.
 */
public class LuckyBlock implements ILuckyBlock {

    private final Body body;
    private final String rewardType;
    private boolean broken;

    public LuckyBlock(World world, Vector2 centerPosition, float width, float height, String rewardType) {
        this.rewardType = rewardType;
        this.body = createBody(world, centerPosition, width, height);
        this.body.setUserData(this);
        this.broken = false;
    }

    private Body createBody(World world, Vector2 centerPosition, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(centerPosition);

        Body createdBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;
        fixtureDef.friction = 0.4f;

        createdBody.createFixture(fixtureDef).setUserData("Luckyblock");
        shape.dispose();
        return createdBody;
    }

    @Override
    public void breakBlock() {
        if (broken) {
            return;
        }
        broken = true;
        System.out.println("Block broken.");
        SpawnReward();
    }

    @Override
    public void SpawnReward() {
        // Placeholder for future reward spawning logic.
        // rewardType can be mapped to upgrades such as speed/coin/health.
    }

    @Override
    public boolean isBroken() {
        return broken;
    }

    @Override
    public Body getBody() {
        return body;
    }

    public String getRewardType() {
        return rewardType;
    }
}