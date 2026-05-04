package inf112.zoombiegeddon.model.world.luckyblock;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import inf112.zoombiegeddon.model.world.physics.IWorldPhysics;

/**
 * Concrete Lucky Block implementation backed by a Box2D static body.
 */
public class LuckyBlock implements ILuckyBlock {

    private final Body body;
    private final String rewardType;
    private boolean broken;

    /**
     * Constructs a new lucky block in the physics world.
     *
     * @param world The Box2D world where the block is created.
     * @param centerPosition The center position of the block.
     * @param width The width of the block.
     * @param height The height of the block.
     * @param rewardType The reward type associated with the block.
     */
    public LuckyBlock(World world, Vector2 centerPosition, float width, float height, String rewardType) {
        this.rewardType = rewardType;
        this.body = createBody(world, centerPosition, width, height);
        this.body.setUserData(this);
        this.broken = false;
    }

    /**
     * Creates the Box2D body used by the lucky block.
     *
     * @param world The Box2D world where the body is created.
     * @param centerPosition The center position of the body.
     * @param width The width of the body.
     * @param height The height of the body.
     * @return The created static body.
     */
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
    public void breakBlock(IWorldPhysics worldPhysics) {
        if (broken) {
            return;
        }
        broken = true;
        spawnReward(worldPhysics);
    }

    @Override
    public void spawnReward(IWorldPhysics worldPhysics) {
        if (worldPhysics == null || rewardType == null || rewardType.isEmpty()) {
            return;
        }

        Vector2 spawnPosition = new Vector2(body.getPosition().x, body.getPosition().y + 0.9f);
        worldPhysics.spawnPickup(rewardType, spawnPosition);
    }

    @Override
    public boolean isBroken() {
        return broken;
    }

    @Override
    public Body getBody() {
        return body;
    }


    /**
     * Returns the reward type associated with this lucky block.
     *
     * @return The reward type.
     */
    public String getRewardType() {
        return rewardType;
    }
}