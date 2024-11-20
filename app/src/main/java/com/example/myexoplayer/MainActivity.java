package com.example.myexoplayer;

import android.content.ComponentName;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.example.myexoplayer.databinding.ActivityMainBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create a MediaItem with the video URL
        MediaItem videoItem = MediaItem.fromUri("https://github.com/dicodingacademy/assets/releases/download/release-video/VideoDicoding.mp4");
        MediaItem audioItem = MediaItem.fromUri("https://github.com/dicodingacademy/assets/raw/main/android_intermediate_academy/bensound_ukulele.mp3");

        // Initialize the ExoPlayer and set up the player
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        player.setMediaItem(videoItem);
        player.addMediaItem(audioItem);
        player.prepare();

        // Bind the ExoPlayer to the player view
        binding.playerView.setPlayer(player);

        // Call the method to hide system UI
        hideSystemUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Create a session token for the PlaybackService
        SessionToken sessionToken = new SessionToken(this, new ComponentName(this, PlaybackService.class));

        // Build the MediaController asynchronously and set the player
        ListenableFuture<MediaController> controllerFuture = new MediaController.Builder(this, sessionToken).buildAsync();
        controllerFuture.addListener(() -> {
            try {
                // Set the player on the player view
                binding.playerView.setPlayer(controllerFuture.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
    }

    private void hideSystemUI() {
        // Set the window to not fit system windows
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Get the WindowInsetsControllerCompat to manage the system bars visibility
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), binding.playerView);

        // Hide the system bars (status bar, navigation bar)
        controller.hide(WindowInsetsCompat.Type.systemBars());

        // Set the behavior for showing transient bars by swipe
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the player when the activity is destroyed
        if (binding.playerView.getPlayer() != null) {
            binding.playerView.getPlayer().release();
        }
    }
}