package bigappcompany.com.artistbooking;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.adapters.CustomImagePager;
import bigappcompany.com.artistbooking.models.ImageObj;

public class ImageSlide extends AppCompatActivity {
ViewPager pager_images;
    ArrayList<ImageObj> model;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slide);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" ");

        pager_images=(ViewPager)findViewById(R.id.pager_photos);
        try {
            model=(ArrayList<ImageObj>) getIntent().getSerializableExtra("model");
            position=getIntent().getIntExtra("pos",-1);
            CustomImagePager adapter=new CustomImagePager();
            adapter.setData(model,this,this);
            pager_images.setAdapter(adapter);
            if(position!=-1)
            {
                pager_images.setCurrentItem(position);
            }
            setStatusBarColor();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void setStatusBarColor() {
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#212121"));
        }
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
