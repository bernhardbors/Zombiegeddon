package inf112.zoombiegeddon.audio;

/**
 * Interface defining the contract for audio playback and management.
 * Provides methods for playing sound effects, managing volume, and state control.
 */
public interface AudioService {
    /**
     * Plays a specific sound effect.
     * @param sfx The sound effect to play.
     */
    void playSfx(Sfx sfx);

    /**
     * Randomly selects and plays one of the provided sound effects.
     * @param options A variadic list of sound effects to choose from.
     */
    void playRandom(Sfx... options);

    long loopSfx(Sfx sfx);

    void stopLoop(Sfx sfx);
    /**
     * Updates the sound effects volume.
     * @param volume01 The volume level normalized between 0.0 and 1.0.
     */
    void setSfxVolume(float volume01);

    /**
     * Sets the mute state of the audio system.
     * @param muted True to mute all sounds, false otherwise.
     */
    void setMuted(boolean muted);

    /** @return The current sound effects volume. */
    float getSfxVolume();

    /** @return True if audio is muted, false otherwise. */
    boolean isMuted();

    /** Releases resources associated with the audio service. */
    void dispose();
}

