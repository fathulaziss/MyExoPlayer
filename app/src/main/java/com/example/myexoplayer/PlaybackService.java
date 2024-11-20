package com.example.myexoplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

public class PlaybackService extends MediaSessionService {
    private MediaSession mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeSessionAndPlayer();
    }

    private void initializeSessionAndPlayer() {
        // Create the video MediaItem
        MediaItem videoItem = new MediaItem.Builder()
                .setUri("https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4")
                .setMediaMetadata(new MediaMetadata.Builder()
                        .setTitle("Bersama Dicoding, Kembangkan Dirimu Menjadi Talenta Digital Berstandar Global")
                        .setArtist("Dicoding")
                        .build())
                .build();

        // Create the audio MediaItem
        MediaItem audioItem = new MediaItem.Builder()
                .setUri("https://github.com/dicodingacademy/assets/raw/main/android_intermediate_academy/bensound_ukulele.mp3")
                .setMediaMetadata(new MediaMetadata.Builder()
                        .setTitle("Ukulele")
                        .setArtist("Bensound")
                        .build())
                .build();

        // Initialize ExoPlayer
        ExoPlayer player = new ExoPlayer.Builder(this).build();

        // Set the media items to the player
        player.setMediaItem(videoItem);
        player.addMediaItem(audioItem);
        player.prepare(); // Prepare the player for playback

        // Initialize the MediaSession with the player
        mediaSession = new MediaSession.Builder(this, player).build();
    }

    @Nullable
    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mediaSession != null) {
            ExoPlayer player = (ExoPlayer) mediaSession.getPlayer();
            if (player != null) {
                player.release(); // Release the player resources
            }
            mediaSession.release(); // Release the media session
            mediaSession = null;
        }
        super.onDestroy();
    }
}