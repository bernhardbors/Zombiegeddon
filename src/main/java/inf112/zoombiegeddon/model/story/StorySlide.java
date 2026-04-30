package inf112.zoombiegeddon.model.story;

import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.audio.Sfx;

/**
 * Represents a single slide in a story sequence, combining a background image
 * with optional audio cues.
 *
 * @param imagePath         path to the slide's background image
 * @param musicTrack        background music to start, or {@code null} for none
 * @param sfxTrack          sound effect to play, or {@code null} for none
 * @param stopPreviousMusic whether to stop the currently playing music before
 *                          starting {@code musicTrack}
 */
public record StorySlide(
        String imagePath,
        MusicTrack musicTrack,
        Sfx sfxTrack,
        boolean stopPreviousMusic
) {}
