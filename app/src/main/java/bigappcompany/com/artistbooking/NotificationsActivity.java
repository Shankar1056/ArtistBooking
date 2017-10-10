package bigappcompany.com.artistbooking;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.adapters.CustomPagerAdapter;
import bigappcompany.com.artistbooking.adapters.NotificationsAdapter;
import bigappcompany.com.artistbooking.adapters.VideoAdapter;
import bigappcompany.com.artistbooking.models.VideoObj;
import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;

public class NotificationsActivity extends AppCompatActivity {
    RecyclerView notifRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        notifRecyclerView=(RecyclerView)findViewById(R.id.recycle_notify);
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    closeDialog();
                    if(new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS))
                    {
                        JSONArray array=new JSONObject(response).getJSONArray(JsonParser.DATA);
                        ArrayList<VideoObj> models=new ArrayList<>();
                        for(int i=0;i<array.length();i++)
                        {
                            String notification=getNotification(array.getJSONObject(i).getString("event_status"));
                            if(!notification.equals("")) {
                                VideoObj obj = new VideoObj(array.getJSONObject(i).getString(JsonParser.CAT_NAME),
                                        ApiUrl.BASE_URL + (array.getJSONObject(i).getString(JsonParser.ARTIST_PRO)),
                                        array.getJSONObject(i).getString(JsonParser.ARTIST_NAME),
                                        notification, "", "", "");
                                models.add(obj);
                            }

                        }
                        LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        notifRecyclerView.setLayoutManager(manager);

                        notifRecyclerView.setAdapter(new NotificationsAdapter(models));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        web.setReqType(false);
        try {
            web.setData(new JSONObject().put(JsonParser.CUSTOMER_ID,getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.CUSTOMER_ID,"")).toString());
            web.execute(ApiUrl.NOTIFICATIONS);
            showDailog();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getNotification(String event_status) {
        switch (event_status) {
            case "0":
                return "";
            case "1":
                return "Your booking has been confirmed";
            case "2":
                return "Your booking has been completed";
            case "3":
                return "Your booking has been declined";
        }
        return "";
    }

    ProgressDialog dialog;
    void showDailog()
    {
        dialog=new ProgressDialog(this);
        dialog.setTitle("Please Wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void closeDialog()
    {
        if(dialog!=null)
            dialog.cancel();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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
}
