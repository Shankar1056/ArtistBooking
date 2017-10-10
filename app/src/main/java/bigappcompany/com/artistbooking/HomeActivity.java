package bigappcompany.com.artistbooking;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.adapters.CustomAdapter;
import bigappcompany.com.artistbooking.adapters.CustomPagerAdapter;
import bigappcompany.com.artistbooking.adapters.DotsAdapter;
import bigappcompany.com.artistbooking.adapters.RecyclerViewDataAdapter;
import bigappcompany.com.artistbooking.models.CitiesModel;
import bigappcompany.com.artistbooking.models.PhotoModel;
import bigappcompany.com.artistbooking.models.SectionDataModel;
import bigappcompany.com.artistbooking.models.SingleItemModel;
import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QOlRzNIvZGvCPoUlTePnwumXP";
    private static final String TWITTER_SECRET = "b26ZiCaJ1RrJrSHGUiqcRk5uqRWr26kVpjbDSsyoMLOmEIhU3t";
    public static boolean active=false;
    ArrayList<SectionDataModel> allSampleData;
    private int previousPage;
    private int previousState;
    private ImageView img_main,navIV;
    private boolean geasturesEnable;
    private float previous;
    private float now;
    private int imgPtr = 0;

    private ViewPager mViewPager;
    TextView maskTV;
    private CustomPagerAdapter mCustomPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_home);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(" ");
        setStatusBarColor();
        setSupportActionBar(toolbar);
        active=true;

        Typeface face = Typeface.createFromAsset(getAssets(),
                getResources().getString(R.string.medium));

        Log.e("FCM", FirebaseInstanceId.getInstance().getToken()+" ");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_name)).setText("Hi, "+(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString((JsonParser.CUST_NAME),"Guest")));

        //navView Width
        int width = getResources().getDisplayMetrics().widthPixels*4/5;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);
        /*LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)navigationView.getHeaderView(0).getLayoutParams();
        params1.width=width;
        params1.height=width*4/6;
        navigationView.getHeaderView(0).setLayoutParams(params1);*/

        allSampleData = new ArrayList<SectionDataModel>();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            SpannableString title=new SpannableString("HOME");
            title.setSpan(face,0,title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            toolbar.setTitleTextAppearance(this,R.style.Toolbar_TitleText);
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle(title);

        }


        //createDummyData();
        new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                closeDialog();
                createData(response);
            }
        }).execute(new ApiUrl().CATEGORIES);
        showDailog();

        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);




        mViewPager = (ViewPager) findViewById(R.id.pager_zoom);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e("onPageScrool","position "+position+" , positionoffset "+positionOffset+" , positionoffsetPixels"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //Log.e("onPageSelected", "position " + position);
                if (previousPage != position) {
                    Log.e("inside","page");
                    //changed

                    dotsAdapter.setSelected(position);
                    dotsAdapter.notifyDataSetChanged();
                }
                previousPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.e("onScrollStateChanged", "state " + state);

                previousState = state;

            }
        });

        img_main = (ImageView) findViewById(R.id.image_zoom);

        img_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (geasturesEnable == true) {
                    img_main.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        previous = event.getX();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        now = event.getX();
                        if ((previous + 100) < (now)) {
                            //Log.e("action", "right");
                            previousImage();

                        } else if ((previous - 100) > now) {

                            nextImage();
                            //Log.e("action", "left");

                        } else
                            img_main.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    return true;
                }
                return false;
            }
        });
        setAdapterData();

        load_cities();

        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(
                mMessageReceiver, new IntentFilter("Notification"));
    }
    NavigationView navigationView;
    RecyclerView my_recycler_view;
    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        getMenuInflater().inflate(R.menu.home_try, menu);

        // Get the notifications MenuItem and LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_notify);


        if(count>0) {
            LayerDrawable icon = (LayerDrawable) item.getIcon();

            // Update LayerDrawable's BadgeDrawable
            Utils2.setBadgeCount(this, icon, count);
        }

        MenuItem item_c = menu.findItem(R.id.action_search);
        item_c.setTitle(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.CITY,JsonParser.CITY));

        return true;
    }
    int count=0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            if(cities!=null)
            select_city();

            return true;
        }
        if(id==R.id.action_notify)
        {
            startActivity(new Intent(this,NotificationsActivity.class));
            return true;
        }
        if(id== android.R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if(!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            }
            else
            {
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action
            startActivity(new Intent(HomeActivity.this,AboutUsActivity.class));
        } else
        if(FirebaseAuth.getInstance()!=null&&getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).getBoolean(JsonParser.GO, false)) {
            if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, MyProfile.class));
            } else if (id == R.id.nav_bookings) {
                startActivity(new Intent(HomeActivity.this, RequestsActivity.class));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setStatusBarColor() {
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#3e4449"));
        }
    }
    String[] images_=new String[]{"https://s-media-cache-ak0.pinimg.com/564x/4e/03/15/4e0315db0aa580b8f8e794d568564d57.jpg","http://www.tollywoodshow.com/wp-content/gallery/sunitha-all-time-hot-pics/sinitha-12.jpg","http://www.hotstarz.info/wp-content/uploads/2015/10/dzxois.png","http://cdn.ytalkies.com/illusionzmedia/uploads/2015/08/Devi-Sri-Prasad-Birthday-Special-01.jpg"};
    public void createDummyData() {
        for (int i = 1; i <= 3; i++) {

            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle("Category " + i);

            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
            for (int j = 0; j <= 5; j++) {
                singleItem.add(new SingleItemModel("Sub catg " + j, images_[j%images_.length] + j,((i*100)+j)+""));
            }

            dm.setAllItemsInSection(singleItem);
            dm.setId(i+"");

            allSampleData.add(dm);

        }
    }



    public void createData(String response)
    {
        try {
            JSONObject root=new JSONObject(response);
            if(root.getBoolean(JsonParser.RESPONSE_STATUS))
            {
                JSONArray array=root.getJSONArray(JsonParser.DATA);
                for (int i=0;i<array.length();i++)
                {
                    SectionDataModel model=new SectionDataModel();
                    model.setHeaderTitle(array.getJSONObject(i).getString(JsonParser.CAT_NAME));
                    model.setUrl((new ApiUrl().BASE_URL)+(array.getJSONObject(i).getString(JsonParser.CATG_URL)));

                    model.setId(array.getJSONObject(i).getString(JsonParser.CAT_ID));
                    ArrayList<SingleItemModel> models=new ArrayList<>();
                    for(int j=0;j<array.getJSONObject(i).getJSONArray(JsonParser.SUBCATG).length();j++)
                    {
                        models.add(new SingleItemModel(array.getJSONObject(i).getJSONArray(JsonParser.SUBCATG).getJSONObject(j)));
                    }
                    model.setAllItemsInSection(models);
                    allSampleData.add(model);

                }
                my_recycler_view.getAdapter().notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void nextImage() {

        if (imgPtr < 4) {
            imgPtr++;
            /*buttons[imgPtr].setVisibility(View.GONE);

            buttons[imgPtr].setVisibility(View.VISIBLE);*/
            //img_main.setImageBitmap(rowItems.get(imgPtr));


        } else {
            imgPtr = 0;
            /*buttons[imgPtr].setVisibility(View.GONE);

            buttons[imgPtr].setVisibility(View.VISIBLE);*/

            //img_main.setImageBitmap(rowItems.get(imgPtr));
        }
        dotsAdapter.setSelected(imgPtr);
        dotsAdapter.notifyDataSetChanged();
    }

    void previousImage() {
        if (imgPtr > 0) {
            imgPtr--;
            /*buttons[imgPtr].setVisibility(View.GONE);

            buttons[imgPtr].setVisibility(View.VISIBLE);*/

            //img_main.setImageBitmap(images.get(imgPtr));
        } else {
            imgPtr = 4;
            /*buttons[imgPtr].setVisibility(View.GONE);

            buttons[imgPtr].setVisibility(View.VISIBLE);*/
            //img_main.setImageBitmap(rowItems.get(imgPtr));
        }

        dotsAdapter.setSelected(imgPtr);
        dotsAdapter.notifyDataSetChanged();
    }
    DotsAdapter dotsAdapter;
    void setAdapterData()
    {
        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                try {
                    if (new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS))
                    {
                        JSONArray array=new JSONObject(response).getJSONArray(JsonParser.DATA);
                        ArrayList<PhotoModel> models=new ArrayList<>();
                        for(int i=0;i<array.length();i++)
                        {
                            models.add(new PhotoModel(array.getJSONObject(i)));
                        }
                        CustomPagerAdapter adapter=new CustomPagerAdapter();

                        adapter.isDynamic(true);
                        adapter.setData(models,getApplicationContext(),HomeActivity.this);
                        adapter.setVP(mViewPager);
                        mViewPager.setAdapter(adapter);
                        if(models.size()>1) {
                            //Dots
                            dots = (RecyclerView) findViewById(R.id.dots);
                            dots.setHasFixedSize(true);
                            dots.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                            dotsAdapter = new DotsAdapter(models.size());
                            dots.setAdapter(dotsAdapter);
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        web.execute(ApiUrl.SLIDERS);
        /*ArrayList<PhotoModel> models=new ArrayList<>();
        PhotoModel model=new PhotoModel("1","https://static.pexels.com/photos/325521/pexels-photo-325521.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model1=new PhotoModel("2","https://static.pexels.com/photos/141635/pexels-photo-141635.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model2=new PhotoModel("3","https://static.pexels.com/photos/62663/ischnura-elegans-dragonfly-palisades-spurge-bad-luck-dragonfly-62663.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model3=new PhotoModel("4","https://static.pexels.com/photos/163798/motor-scooter-vespa-roller-cult-163798.jpeg","Title1","Description1","26-2-2017");
        models.add(model);
        models.add(model1);
        models.add(model2);
        models.add(model3);*/

    }
    RecyclerView dots;

    @Override
    public void onPause()
    {
        if(mViewPager.getAdapter()!=null&&mViewPager.getChildCount()>1)
        ((CustomPagerAdapter)mViewPager.getAdapter()).stopTimer();
        active=false;
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    @Override
    public void onResume()
    {
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_name)).setText("Hi, "+(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString((JsonParser.CUST_NAME),"Guest")));
        TextView tv_log=(TextView)findViewById(R.id.tv_signout);
        if(FirebaseAuth.getInstance()==null||getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).getBoolean(JsonParser.GO, false))
        {
            tv_log.setText("Sign Out");
        }
        else
        {
            tv_log.setText("SIGN IN");
        }
        tv_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance()==null||getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE)
                    .getBoolean
                    (JsonParser.GO, false)) {
                    getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).edit().clear().commit();

                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(), "Signout Successful", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    //startActivity(new Intent(HomeActivity.this,));
                }
            }
        });
        if(mViewPager.getAdapter()!=null&&mViewPager.getChildCount()>1)
        ((CustomPagerAdapter)mViewPager.getAdapter()).startTimer();
        active=true;
        super.onResume();

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
    ArrayList<CitiesModel> cities;
    private void select_city() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        try {
            View view = inflater.inflate(R.layout.dialog_title, null);
            builder.setCustomTitle(view);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("setTitleError",e.getMessage().toString());
        }
        final CustomAdapter adapter1=new CustomAdapter(getApplicationContext(),R.layout.simple_list_item,cities);
        builder.setSingleChoiceItems(adapter1, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE)
                        .edit()
                        .putString(JsonParser.CITY,adapter1.getItem(which).getCity())
                        .commit();

                invalidateOptionsMenu();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    void load_cities()
    {
        new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Log.e("response",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if(object.getBoolean(JsonParser.RESPONSE_STATUS))
                    {

                        cities=new ArrayList<CitiesModel>();
                        JSONArray array=object.getJSONArray(JsonParser.DATA);
                        for(int i=0;i<array.length();i++)
                        {
                            CitiesModel model=new CitiesModel(array.getJSONObject(i));
                            cities.add(model);
                        }
                        /*final CustomAdapter adapter1=new CustomAdapter(getApplicationContext(),R.layout.simple_list_item,models);
                        //chipCity.setAdapter(adapter1);
                        at_city.setAdapter(adapter1);
                        at_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                city_id=adapter1.getItem(position).getId();
                            }
                        });*/

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute(ApiUrl.CITIES);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Log.e("inside","reciever");
            String data = intent.getStringExtra(JsonParser.DATA);
            try {

                notifyNewNotification(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void notifyNewNotification(JSONObject object) {

            Log.e("inside","add");
            count++;
        invalidateOptionsMenu();

    }
}
