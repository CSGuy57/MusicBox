package com.customconcern.musicbox.presenter;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.customconcern.musicbox.R;
import com.customconcern.musicbox.model.Song;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Daniel Lande on 7/19/2016.
 */
public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //region Fields

    private MediaPlayer player;
    private MusicController controller;
    private ArrayList<Song> songs;
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle = "";
    private static final int NOTIFY_ID=1;
    private boolean shuffle = false;
    private Random rand;
    private Stack<Integer> songHistory;

    //endregion


    //region Methods

    //region Getters and Setters

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public int getSongPosn() {
        return songPosn;
    }

    public void setList(ArrayList<Song> theSongs){
        songs = theSongs;
    }

    public void setSong(int songIndex){
        songPosn = songIndex;
    }

    //endregion


    //region Overrides

    @Override
    public void onCreate(){
        // create the service
        super.onCreate();

        // initialize position
        songPosn = 0;

        // create player
        player = new MediaPlayer();

        rand = new Random();

        songHistory = new Stack<>();

        initMusicPlayer();
    }

    public void setController(MusicController controller){
        this.controller = controller;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();

        controller.show(0);

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
        .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition() < 0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    // endregion


    //region Player methods

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void go(){
        player.start();
    }

    public void playSong(){
        player.reset();

        songHistory.push(songPosn);

        //get song
        Song playSong = songs.get(songPosn);
        songTitle = playSong.getTitle();

        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();

    }

    public void playPrev(){
        // If the songs are being shuffled and their is a song history
        /*if(shuffle && songHistory.size() > 0)
        {
            songPosn = songHistory.pop();
        } else {
            songPosn--;
            if (songPosn < 0) {
                songPosn = songs.size() - 1;
            }
        }*/
        if(shuffle && songHistory.size() > 1) {
            int currentSong = songHistory.pop();
            songPosn = songHistory.pop();
        } else {
            songPosn--;
            if (songPosn < 0) {
                songPosn = songs.size() - 1;
            }
        }

        playSong();
    }

    public void playNext(){
        if(shuffle){
            int newSong = songPosn;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.size());
            }
            songPosn=newSong;
        }
        else{
            songPosn++;
            if(songPosn >= songs.size()) songPosn = 0;
        }
        playSong();
    }

    public void pausePlayer(){
        player.pause();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void setShuffle(){
        songHistory.clear();

        if(shuffle) {
            shuffle=false;
        } else {
            shuffle=true;
        }
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    //endregion

    //endregion
}
