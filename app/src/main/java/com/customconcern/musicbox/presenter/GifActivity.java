package com.customconcern.musicbox.presenter;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.customconcern.musicbox.R;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;

//To make the .Ion work, you have to go File menu, Project Structure, Dependencies, plus sign, and search for the Ion Koush thing
//You also have to put the following in the manifest:
//    <uses-permission android:name="android.permission.INTERNET"/>

/**
 * Created by davidglazerman on 7/19/16.
 */
public class GifActivity extends Activity {

// We want to do some fun little method things later in this activity, so we have to declare some things now
// to be able to use them elsewhere later
    private ArrayList<String> ourGifList;  //To hold all the videos
    private HashMap<String, Integer> vidWithTimeMap;  //To hold the video times with the video
    private ImageView imageViewGifPlayer;  //A place to play & display the video
    private Handler handler; // Might not really need it since I think it is well covered later
    private String randomVideoLink;  //Going to hold the chosen video that gets passed around
    private int videoLength;



    //Here is a mini method that will look at the big ol' list o' gifs and choose just 1
    public String getRandomVideoLink() {
        return ourGifList.get((int) (Math.random()* ourGifList.size()));
    }

//When the screen is first created, it finds the wiring, makes a new GifList and fills it with the possible videos
// then a random video is chosen.  The cool Ion library uses the link to get the gif from the internet and play it
// Finally, I set up a method call to try to loop this choice to something after a few seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        imageViewGifPlayer = (ImageView) findViewById(R.id.imageViewGifPlayer);
        ourGifList = new ArrayList<String>();
        vidWithTimeMap = new HashMap<String, Integer>();

        handler = new Handler();  //Makes the handler that will pass stuff... I think


        populateList();  //Calls the method that puts all the stuff in the list and map
        randomVideoLink = getRandomVideoLink();  //Choose a random video for the first run
        Ion.with(imageViewGifPlayer).load(randomVideoLink);  //Load a random video the first time
        videoLength = setVideoLength(randomVideoLink);

        mHandler.sendEmptyMessageDelayed(DISPLAY_DATA, videoLength);  //Makes a thread that will eventually call a method in 5000 milliseconds

    }

    //The next few lines are modified from the internet, and it is a wee bit complicated.

    private static final int DISPLAY_DATA = 1;
    // this handler will receive a delayed message
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            videoLoop(randomVideoLink);

        }
    };

    private String videoLoop(String oldVideoLink) {

        randomVideoLink = getRandomVideoLink();
        while (oldVideoLink == randomVideoLink) {
            randomVideoLink = getRandomVideoLink();
        }

        Ion.with(imageViewGifPlayer).load(randomVideoLink);
        videoLength = setVideoLength(randomVideoLink);
        mHandler.sendEmptyMessageDelayed(DISPLAY_DATA, videoLength);

        return randomVideoLink;
    }

    private int setVideoLength(String chosenVideo) {
        return vidWithTimeMap.get(chosenVideo);
    }


    //If you find more nice Gifs, you can add to this list the same way for a bigger selection
    private void populateList() {

       ourGifList.add("http://forgifs.com/gallery/d/264463-2/Beard-slap.gif");
       ourGifList.add("http://forgifs.com/gallery/d/263300-2/Coke-mentos-bottle-sword-fail.gif");
       ourGifList.add("http://forgifs.com/gallery/d/261776-2/Woman-judo-throws-attacker-faceplant.gif");
       ourGifList.add("http://forgifs.com/gallery/d/257509-2/Interactive-shark-display.gif");
       ourGifList.add("http://forgifs.com/gallery/d/252328-2/Rock-star-singer-beer-catch.gif");
       ourGifList.add("http://forgifs.com/gallery/d/250355-2/Gymnast-tossing-kids.gif");
       ourGifList.add("http://forgifs.com/gallery/d/249306-2/McDonalds-noodles-smooth-feint.gif");
       ourGifList.add("http://forgifs.com/gallery/d/249143-2/Mini-Bruce-Lee-nunchucks.gif");
       ourGifList.add("http://forgifs.com/gallery/d/242465-2/Soccer-juggling-high-heels.gif");
       ourGifList.add("http://forgifs.com/gallery/d/239126-2/Toy-store-real-lightsaber.gif");
       ourGifList.add("http://forgifs.com/gallery/d/237070-2/Young-gymnast-handsprings.gif");
       ourGifList.add("http://forgifs.com/gallery/d/235296-2/Wrestling-acrobatic-flip-slam.gif");
       ourGifList.add("http://forgifs.com/gallery/d/233584-2/Microwaved-glow-stick-explosion.gif");
       ourGifList.add("http://forgifs.com/gallery/d/232928-4/Water-bottle-kick.gif");
       ourGifList.add("http://forgifs.com/gallery/d/232367-2/Jellyfish-smoke-trick.gif");
       ourGifList.add("http://forgifs.com/gallery/d/232166-2/Thief-steals-wine-bottles.gif");
       ourGifList.add("http://forgifs.com/gallery/d/231063-2/Smart-kid-escapes-baby-gate-pillow.gif");
       ourGifList.add("http://forgifs.com/gallery/d/230337-2/Balance-beam-smooth-save.gif" );
       ourGifList.add("http://forgifs.com/gallery/d/229561-2/Chocolate-bomb-dessert.gif");
       ourGifList.add("http://forgifs.com/gallery/d/229369-2/Making-chains.gif" );
       ourGifList.add("http://forgifs.com/gallery/d/229344-2/Straight-bar-through-curved-hole.gif" );
       ourGifList.add("http://forgifs.com/gallery/d/229166-2/Train-tracks-walking-texting.gif" );
       ourGifList.add("http://forgifs.com/gallery/d/228830-2/Wheelbarrow-race-somersault.gif" );

        vidWithTimeMap.put("http://forgifs.com/gallery/d/264463-2/Beard-slap.gif", 4100);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/263300-2/Coke-mentos-bottle-sword-fail.gif", 10650);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/261776-2/Woman-judo-throws-attacker-faceplant.gif", 7200);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/257509-2/Interactive-shark-display.gif", 6825);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/252328-2/Rock-star-singer-beer-catch.gif", 6000);   //Not for K12
        vidWithTimeMap.put("http://forgifs.com/gallery/d/250355-2/Gymnast-tossing-kids.gif", 8200);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/249306-2/McDonalds-noodles-smooth-feint.gif", 9650);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/249143-2/Mini-Bruce-Lee-nunchucks.gif", 6450);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/242465-2/Soccer-juggling-high-heels.gif", 12150);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/239126-2/Toy-store-real-lightsaber.gif", 8000);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/237070-2/Young-gymnast-handsprings.gif", 8550);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/235296-2/Wrestling-acrobatic-flip-slam.gif", 3650);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/233584-2/Microwaved-glow-stick-explosion.gif", 5350);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/232928-4/Water-bottle-kick.gif", 7900);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/232367-2/Jellyfish-smoke-trick.gif", 9600);   //Not for K12
        vidWithTimeMap.put("http://forgifs.com/gallery/d/232166-2/Thief-steals-wine-bottles.gif", 11800);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/231063-2/Smart-kid-escapes-baby-gate-pillow.gif", 9500);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/230337-2/Balance-beam-smooth-save.gif" , 8200);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/229561-2/Chocolate-bomb-dessert.gif", 11200);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/229369-2/Making-chains.gif", 4800);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/229344-2/Straight-bar-through-curved-hole.gif", 3600);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/229166-2/Train-tracks-walking-texting.gif", 13000);
        vidWithTimeMap.put("http://forgifs.com/gallery/d/228830-2/Wheelbarrow-race-somersault.gif", 6725);
    }



    /*
    This block is set up for if we have a menu that needs to expand to better show the video.
    I included it as an option for us to work with if we want to figure it out.
    Though, it doesn't look like we'll be needing it, so whatevers...

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
}
