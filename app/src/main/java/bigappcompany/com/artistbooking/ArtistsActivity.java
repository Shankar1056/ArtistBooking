package bigappcompany.com.artistbooking;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.adapters.ArtistAdapter;
import bigappcompany.com.artistbooking.models.ImageObj;
import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;

public class ArtistsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    String[] singers=new String[]{"Geetha Madhuri","Suneetha","Shreya Ghoshal","Devi Sri Prasad"};
    String[] images_=new String[]{"https://s-media-cache-ak0.pinimg.com/564x/4e/03/15/4e0315db0aa580b8f8e794d568564d57.jpg","http://www.tollywoodshow.com/wp-content/gallery/sunitha-all-time-hot-pics/sinitha-12.jpg","http://www.hotstarz.info/wp-content/uploads/2015/10/dzxois.png","http://cdn.ytalkies.com/illusionzmedia/uploads/2015/08/Devi-Sri-Prasad-Birthday-Special-01.jpg"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView=(RecyclerView)findViewById(R.id.photos_RV);



        GridLayoutManager manager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager);

        //setData();
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                closeDialog();
                setUpData(response);
            }
        });
        web.setReqType(false);
        try {
            web.setData(
                    new JSONObject()
                            .put(JsonParser.SUB_CAT_ID,getIntent().getStringExtra(JsonParser.SUB_CAT_ID))
                            .put(JsonParser.CITY,getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.CITY,""))
                            .toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        web.execute(new ApiUrl().ARTISTS);
        showDailog();

    }
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    private void setData() {
        ArrayList<ImageObj> images=new ArrayList<>();
        for(int i=0;i<9;i++) {
            images.add(new ImageObj(""+i, images_[i%images_.length], singers[i%singers.length]));
        }
        ArtistAdapter adapter=new ArtistAdapter(images);
        adapter.setReference(this);
        recyclerView.setAdapter(adapter);
    }private void setUpData(String json) {
        try {
            if (new JSONObject(json).getBoolean(JsonParser.RESPONSE_STATUS)) {

                ArrayList<ImageObj> images = new ArrayList<>();
                for (int i = 0; i < (new JSONObject(json).getJSONArray(JsonParser.DATA)).length(); i++)
                {
                    JSONObject object=new JSONObject(json).getJSONArray(JsonParser.DATA).getJSONObject(i);
                    ImageObj obj=new ImageObj(object.getString(JsonParser.ID),(ApiUrl.BASE_URL)+( object.getString(JsonParser.IMG_PROFILE)), object.getString(JsonParser.ARTIST_NAME));
                    obj.setDes(getIntent().getStringExtra(JsonParser.SUB_NAME));
                    images.add(obj);
                }
                ArtistAdapter adapter = new ArtistAdapter(images);
                adapter.setReference(this);
                recyclerView.setAdapter(adapter);
            }
        }
        catch (JSONException je)
        {
            je.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    ProgressDialog dialog;
    void showDailog()
    {
        dialog=new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void closeDialog()
    {
        if(dialog!=null)
            dialog.cancel();
    }
}
