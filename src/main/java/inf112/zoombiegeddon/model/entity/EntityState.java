package inf112.zoombiegeddon.model.entity;

/**
 * Represents the various behavioral and physical states an entity can be in.
 * Used primarily by the AnimationHandler to determine which animation
 * sequence to play and by the Player/Entity classes to handle logic.
 */
public enum EntityState {
    IDLE,
    WALKING,
    RUNNING,
    JUMPING,
    FALLING,
    PUNCHING
}