package inf112.zoombiegeddon.audio;

/**
 * Interface defining the contract for audio playback and management.
 * Provides methods for playing sound effects, managing volume, and state control.
 */
public interface IAudioService {
    /**
     * Plays a specific sound effect.
     * @param sfx The sound effect to play.
     */
    void playSfx(Sfx sfx);

    /**
     * Plays a music track.
     *
     * @param music The music track to play.
     * @param loop True if the track should loop continuously, false otherwise.
     */
    void playMusic(MusicTrack music, boolean loop);

    /**
     * Stops the currently playing music track.
     */
    void stopMusic();

    /**
     * Sets the music volume.
     *
     * @param volume01 The volume level normalized between 0.0 and 1.0.
     */
    void setMusicVolume(float volume01);

    /**
     * Returns the current music volume.
     *
     * @return The current music volume.
     */
    float getMusicVolume();


    /**
     * Randomly selects and plays one of the provided sound effects.
     * @param options A variadic list of sound effects to choose from.
     */
    void playRandom(Sfx... options);

    /**
     * Updates the sound effects volume.
     * @param volume01 The volume level normalized between 0.0 and 1.0.
     */
    void setSfxVolume(float volume01);

    /**
     * Starts looping a specific sound effect.
     *
     * @param sfx The sound effect to loop.
     * @return The id of the looping sound instance.
     */
    long loopSfx(Sfx sfx);

    /**
     * Stops a specific sound effect.
     *
     * @param sfx The sound effect to stop.
     */
    void stopSfx(Sfx sfx);

    /**
     * Sets the mute state of the audio system.
     * @param muted True to mute all sounds, false otherwise.
     */
    void setMuted(boolean muted);

    /**
     * Returns the current sound effects volume.
     *
     * @return The current sound effects volume.
     */
    float getSfxVolume();

    /**
     * Returns whether the audio system is muted.
     *
     * @return True if audio is muted, false otherwise.
     */
    boolean isMuted();

    /** Releases resources associated with the audio service. */
    void dispose();
}

