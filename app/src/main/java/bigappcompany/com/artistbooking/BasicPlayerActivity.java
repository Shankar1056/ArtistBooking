package bigappcompany.com.artistbooking;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.adapters.VideoYAdapter;
import bigappcompany.com.artistbooking.models.Constants;
import bigappcompany.com.artistbooking.models.VideoObj;
import bigappcompany.com.artistbooking.network.JsonParser;

import static com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import static com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import static com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import static com.google.android.youtube.player.YouTubePlayer.Provider;

public class BasicPlayerActivity extends YouTubeBaseActivity implements  OnInitializedListener {
    public static final String API_KEY = "AIzaSyAtuodicPxtqAhceTuFeIxdZ5UYj88_lS4";

    //https://www.youtube.com/watch?v=<VIDEO_ID>

    private RecyclerView recyclerView;
    String response="";
    private String currentVideo="";
    ArrayList<VideoObj> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // attaching layout xml
        setContentView(R.layout.activity_basic_player);

        recyclerView=(RecyclerView)findViewById(R.id.photos_RV);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        models=new ArrayList<>();

        recyclerView.setAdapter(new VideoYAdapter(models,listener));

        try
        {
            response=getIntent().getStringExtra(JsonParser.DATA);
            int pos=getIntent().getIntExtra(Constants.POS,-1);



            JSONObject object=new JSONObject(response);
            if(object.getBoolean(JsonParser.RESPONSE_STATUS))
            {
                JSONArray array=object.getJSONArray(JsonParser.DATA);
                for(int i=0;i<array.length();i++)
                {
                    VideoObj obj=new VideoObj(array.getJSONObject(i));
                    models.add(obj);
                    recyclerView.getAdapter().notifyItemInserted(i);
                }
            }
            else
            {
                Toast.makeText(this,"No data",Toast.LENGTH_LONG).show();
            }
            recyclerView.getAdapter().notifyDataSetChanged();
            if(pos>=0)
            listener.onVideo(models.get(pos),pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Initializing YouTube player view
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(API_KEY, this);

    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if(null== player) return;

        // Start buffering
        if (!wasRestored) {
            player.cueVideo(currentVideo);
        }

        // Add listeners to YouTubePlayer instance
        player.setPlayerStateChangeListener(new PlayerStateChangeListener() {
            @Override public void onAdStarted() { }
            @Override public void onError(ErrorReason arg0) { }
            @Override public void onLoaded(String arg0) { }
            @Override public void onLoading() { }
            @Override public void onVideoEnded() { }
            @Override public void onVideoStarted() { }
        });


        player.setPlaybackEventListener(new PlaybackEventListener() {
            @Override public void onBuffering(boolean arg0) { }
            @Override public void onPaused() { }
            @Override public void onPlaying() { }
            @Override public void onSeekTo(int arg0) { }
            @Override public void onStopped() { }
        });
    }


    VideoYAdapter.OnVideoItemListener listener=new VideoYAdapter.OnVideoItemListener() {
        @Override
        public void onVideo(VideoObj model, int pos) {
            Toast.makeText(getApplicationContext(),model.getVideoId()+" "+pos,Toast.LENGTH_LONG).show();
            currentVideo=model.getVideoId();
        }


    };
}