package inf112.zoombiegeddon.model.entity.player;

/**
 * Manages a temporary speed boost for an entity.
 * Tracks the active multiplier and remaining duration,
 * resetting to 1.0 when the boost expires.
 */
public class SpeedBoostComponent {

    private float multiplier = 1.0f;
    private float remaining = 0f;

    /**
     * Applies a speed boost for the given duration.
     *
     * @param multiplier      The speed multiplier to apply. Values below 1.0 are clamped to 1.0.
     * @param durationSeconds How long the boost lasts in seconds.
     */
    public void apply(float multiplier, float durationSeconds) {
        this.multiplier = Math.max(1.0f, multiplier);
        this.remaining = Math.max(0f, durationSeconds);
    }

    /**
     * Updates the boost timer.
     *
     * @param dt Time in seconds since the last update.
     */
    public void update(float dt) {
        if (remaining > 0f) {
            remaining -= dt;
            if (remaining <= 0f) {
                remaining = 0f;
                multiplier = 1.0f;
            }
        }
    }

    /**
     * @return The current speed multiplier, 1.0 if no boost is active.
     */
    public float getMultiplier() { return multiplier; }
}
