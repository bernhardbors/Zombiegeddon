package inf112.zoombiegeddon.model.world.triggers;

import inf112.zoombiegeddon.model.entity.Player;

public class SpawnAction implements ITriggerAction{
    private float x;
    private float y;

    public SpawnAction(float spawnX, float spawnY) {
        this.x = spawnX;
        this.y = spawnY;

    }

    @Override
    public String onTrigger(Player player) {
        player.setPosition(x,y);
        return null;
    }
}
