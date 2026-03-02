package inf112.zoombiegeddon.audio;

public interface AudioService {
    void playSfx(Sfx sfx);
    void playRandom(Sfx... options);

    void setSfxVolume(float volume01);
    void setMuted(boolean muted);

    float getSfxVolume();
    boolean isMuted();
    void dispose();
}

