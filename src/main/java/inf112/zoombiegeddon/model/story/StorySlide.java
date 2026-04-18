package inf112.zoombiegeddon.model.story;

import inf112.zoombiegeddon.audio.MusicTrack;
import inf112.zoombiegeddon.audio.Sfx;

public record StorySlide(
        String imagePath,
        MusicTrack musicTrack,
        Sfx sfxTrack,
        boolean stopPreviousMusic
) {}
