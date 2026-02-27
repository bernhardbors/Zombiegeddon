package inf112.zoombiegeddon.audio;

/**
 * Registry for all in-game sound effects.
 * This enum works as a central source of truth for all audio resources.
 * By using this enum, we avoid magic strings and assures both {@link AudioManager}
 * and game logic consistently refers to the same files.
 */
public enum GameSound {
    JUMP1("JUMP1", "jumppp11.wav"),
    JUMP2("JUMP2", "jumppp22.wav");

    private final String key;
    private final String fileName;

    /**
     * @param key      The unique identifier used to retrieve the sound from the {@link AudioManager}.
     * @param fileName The name of the file (including extension) located in "assets/audio/sfx/".
     */
    GameSound(String key, String fileName) {
        this.key = key;
        this.fileName = fileName;
    }

    /**
     * @return The unique key for this sound.
     */
    public String getKey() { return key; }

    /**
     * @return The file name of the sound resource.
     */
    public String getFileName() { return fileName; }
}
