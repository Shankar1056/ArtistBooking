package bigappcompany.com.artistbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.adapters.PhAdapter;
import bigappcompany.com.artistbooking.models.Constants;
import bigappcompany.com.artistbooking.models.ImageObj;
import bigappcompany.com.artistbooking.models.SectionDataModel;
import bigappcompany.com.artistbooking.network.PicassoTrustAll;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ImageView img;
    private SectionDataModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.CATG));

        img=(ImageView)findViewById(R.id.img);
        recyclerView=(RecyclerView)findViewById(R.id.photos_RV);



        GridLayoutManager manager=new GridLayoutManager(img.getContext(),3);
        recyclerView.setLayoutManager(manager);

        //setData();
        model=(SectionDataModel) getIntent().getSerializableExtra(Constants.CATEGORY);
        setUpData(model);

        ViewTreeObserver vto = img.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int finalHeight, finalWidth;

                img.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = img.getMeasuredHeight();
                finalWidth = img.getMeasuredWidth();


                Log.e("height,width",finalHeight+","+finalWidth);
                //Picasso.with(img.getContext()).load(model.getPhoto()).resize(Math.max(finalHeight,finalWidth),Math.max(finalHeight,finalWidth)).centerCrop().into(img);
                PicassoTrustAll.getInstance(img.getContext()).load(model.getUrl()).resize(finalWidth,finalHeight).centerCrop().into(img);
                return true;
            }
        });
    }



    @Override
    public void onStart()
    {

        super.onStart();
    }
    String[] images_=new String[]{"https://static.pexels.com/photos/325521/pexels-photo-325521.jpeg","https://static.pexels.com/photos/141635/pexels-photo-141635.jpeg","https://static.pexels.com/photos/62663/ischnura-elegans-dragonfly-palisades-spurge-bad-luck-dragonfly-62663.jpeg","https://static.pexels.com/photos/163798/motor-scooter-vespa-roller-cult-163798.jpeg"};

    private void setData() {
        ArrayList<ImageObj> images=new ArrayList<>();
        for(int i=0;i<9;i++) {
            images.add(new ImageObj(""+i, images_[i%images_.length], "Item"+i));
        }
        recyclerView.setAdapter(new PhAdapter(images));
        final ScrollView scrollView=(ScrollView)findViewById(R.id.activity_category);
        //scrollView.scrollTo(0,0);

        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });

        //scrollView.fullScroll(View.FOCUS_UP);//if you move at the end of the scroll

        //scrollView.pageScroll(View.FOCUS_UP);//if you move at the middle of the scroll
    }
    private void setUpData(SectionDataModel model) {
        ArrayList<ImageObj> images=new ArrayList<>();
        for(int i=0;i<model.getAllItemsInSection().size();i++) {
            images.add(new ImageObj(model.getAllItemsInSection().get(i).getId(), model.getAllItemsInSection().get(i).getUrl(), model.getAllItemsInSection().get(i).getName()));
        }
        recyclerView.setAdapter(new PhAdapter(images));
        final ScrollView scrollView=(ScrollView)findViewById(R.id.activity_category);
        //scrollView.scrollTo(0,0);

        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });

        //scrollView.fullScroll(View.FOCUS_UP);//if you move at the end of the scroll

        //scrollView.pageScroll(View.FOCUS_UP);//if you move at the middle of the scroll
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
