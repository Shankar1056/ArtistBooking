package bigappcompany.com.artistbooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import noman.weekcalendar.listener.OnDateClickListener;
import noman.weekcalendar.listener.OnWeekChangeListener;
import bigappcompany.com.artistbooking.fragments.BioFragment;
import bigappcompany.com.artistbooking.fragments.PhotosFragment;
import bigappcompany.com.artistbooking.fragments.VideosFragment;
import bigappcompany.com.artistbooking.models.Constants;
import bigappcompany.com.artistbooking.models.ImageObj;
import bigappcompany.com.artistbooking.models.VideoObj;
import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;
import bigappcompany.com.artistbooking.network.PicassoTrustAll;

public class MainActivity extends AppCompatActivity implements OnWeekChangeListener,OnDateClickListener{

    ImageView img;
    TextView tv_art,tv_des;
    Button bt_book;
    RippleView rp_book;
    private TabLayout tabs;
    ViewPager pager;
    ImageObj obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Artist Information");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        pager=(ViewPager)findViewById(R.id.pager_content);

        img=(ImageView)findViewById(R.id.img_artist);
        tv_art=(TextView)findViewById(R.id.tvArtist);
        tv_des=(TextView)findViewById(R.id.tvDesig);

        bt_book=(Button)findViewById(R.id.bt_book);

        rp_book=(RippleView)findViewById(R.id.rp_book);



        try {
            obj = (ImageObj) getIntent().getSerializableExtra(Constants.ARTIST);

            if(obj!=null)
            {
                ViewTreeObserver vto = img.getViewTreeObserver();
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        int finalHeight, finalWidth;

                        img.getViewTreeObserver().removeOnPreDrawListener(this);

                        finalHeight = img.getMeasuredHeight();
                        finalWidth = img.getMeasuredWidth();


                        Log.e("height,width",finalHeight+","+finalWidth);
                        //Picasso.with(img.getContext()).load(model.getPhoto()).resize(Math.max(finalHeight,finalWidth),Math.max(finalHeight,finalWidth)).centerCrop().into(img);
                        PicassoTrustAll.getInstance(img.getContext()).load(obj.getPhoto()).resize(finalWidth,finalHeight).centerCrop().into(img);
                        //PicassoTrustAll.getInstance(img.getContext()).load(obj.getPhoto()).resize(finalWidth,finalHeight).centerCrop().into((ImageView)findViewById(R.id.img_blur));

                        return true;
                    }
                });
                tv_art.setText(obj.getTitle());
                getSupportActionBar().setTitle(obj.getTitle());


                bt_book.setText("BOOK "+obj.getTitle());
                new Download_web(MainActivity.this, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String response) {
                        closeDialog();
                        setUpData(response);
                    }
                }).execute(ApiUrl.ARTIST_PROFILE+obj.getId());
                showDailog();
            }
            rp_book.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    startActivity(new Intent(MainActivity.this,BookingActivityNew.class).putExtra(JsonParser.ID,obj.getId()));
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        tabs=(TabLayout)findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(getApplicationContext(),tab.getText(),Toast.LENGTH_LONG).show();
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabs.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        phmodels=new ArrayList<>();
        /*phmodels.add(new ImageObj("1","https://static.pexels.com/photos/325521/pexels-photo-325521.jpeg","Title1"));
        phmodels.add(new ImageObj("2","https://static.pexels.com/photos/141635/pexels-photo-141635.jpeg","Title2"));
        phmodels.add(new ImageObj("3","https://static.pexels.com/photos/62663/ischnura-elegans-dragonfly-palisades-spurge-bad-luck-dragonfly-62663.jpeg","Title3"));*/

        vidModels=new ArrayList<>();
        /*vidModels.add(new VideoObj("3","https://static.pexels.com/photos/62663/ischnura-elegans-dragonfly-palisades-spurge-bad-luck-dragonfly-62663.jpeg","Title3"));
        vidModels.add(new VideoObj("2","https://static.pexels.com/photos/141635/pexels-photo-141635.jpeg","Title2"));
        vidModels.add(new VideoObj("1","https://static.pexels.com/photos/325521/pexels-photo-325521.jpeg","Title1"));*/



        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        //tabs.setupWithViewPager(pager);
    }

    private void setUpData(String response) {
        try {
            if(new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS)) {
                JSONObject detials=new JSONObject(response).getJSONObject(JsonParser.DATA);
//                JSONObject catg=new JSONObject(response).getJSONObject(JsonParser.DATA).getJSONArray(JsonParser.CATG).getJSONObject(0);
//                tv_des.setText(catg.getString(JsonParser.CAT_NAME));
                if(((MyPagerAdapter)pager.getAdapter()).getItem(0)!=null)
                {
                    bio=detials.getString(JsonParser.BIO);
                    Fragment fragment = ((MyPagerAdapter)pager.getAdapter()).getFragment(0);
                    if (fragment != null) {
                        ((BioFragment)fragment).setText(bio);
                    }
                }
                ((TextView)findViewById(R.id.tv_ph)).setText(detials.getString(JsonParser.ARTIST_MOB));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onWeekChange(DateTime firstDayOfTheWeek, boolean forward) {

    }

    @Override
    public void onDateClick(DateTime dateTime) {

    }

    public String getId() {
        return obj.getId();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public Fragment getItem(int pos)
        {
            if(pos==0)
                return BioFragment.newInstance(bio);
            if (pos==1)
                return PhotosFragment.newInstance(phmodels);
            if (pos==2)
                return  VideosFragment.newInstance();
            else return null;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
                mFragmentTags.put(position, tag);
            }
            return object;
        }

        public Fragment getFragment(int position) {
            Fragment fragment = null;
            String tag = mFragmentTags.get(position);
            if (tag != null) {
                fragment = mFragmentManager.findFragmentByTag(tag);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    ArrayList<ImageObj> phmodels;
    ArrayList<VideoObj> vidModels;
    String bio;

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
