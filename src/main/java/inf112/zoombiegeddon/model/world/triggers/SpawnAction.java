package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.IPlayer;

public class SpawnAction implements ITriggerAction{
    private float x;
    private float y;

    public SpawnAction(float spawnX, float spawnY) {
        this.x = spawnX;
        this.y = spawnY;

    }

    @Override
    public String onTrigger(IPlayer player) {
        player.setPosition(this.x, this.y);
        return null;
    }
}
