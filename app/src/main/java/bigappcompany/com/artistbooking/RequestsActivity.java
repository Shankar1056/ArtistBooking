package bigappcompany.com.artistbooking;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.adapters.RequestsAdapter;
import bigappcompany.com.artistbooking.models.RequestsModel;
import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;


public class RequestsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private String TAG_BOTTOM="Bottom";
    public static boolean active=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Bookings History");

        recyclerView=(RecyclerView)findViewById(R.id.list_recycle);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RequestsAdapter(new ArrayList<RequestsModel>());
        recyclerView.setAdapter(adapter);

        active=true;

        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    closeDialog();
                    if(new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS))
                    {
                        JSONArray array=new JSONObject(response).getJSONArray(JsonParser.DATA);
                        for(int i=0;i<array.length();i++)
                        {
                            adapter.add_event(new RequestsModel(array.getJSONObject(i)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        web.execute(ApiUrl.BOOKINGS
                +(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.CUSTOMER_ID,"")));
        showDailog();

        /*LocalBroadcastManager.getInstance(RequestsActivity.this).registerReceiver(
                mMessageReceiver, new IntentFilter("Accepted"));*/
    }
    @Override
    public void onPause()
    {
        active=false;
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    RequestsAdapter adapter;

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
    @Override
    public void onResume()
    {
        active=true;
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Log.e("inside","reciever");
            String data = intent.getStringExtra(JsonParser.DATA);
            try {

                notifyNewBooking(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void notifyNewBooking(JSONObject object) {
        try {

            Log.e("inside","add");
            //((RecyclerViewDataAdapter)my_recycler_view.getAdapter()).addItem(new SingleItemModel(object),0,0);
            ((RequestsAdapter)(recyclerView.getAdapter())).addItem(new RequestsModel(object),0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
