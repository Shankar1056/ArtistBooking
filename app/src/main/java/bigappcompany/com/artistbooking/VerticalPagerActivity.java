package bigappcompany.com.artistbooking;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.adapters.VerticalPagerAdapter;
import bigappcompany.com.artistbooking.models.PhotoModel;

public class VerticalPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_pager);
        mViewPager = (ViewPager) findViewById(R.id.pager_vertical);
        setupViewPager();
    }

    void setAdapterData()
    {
        ArrayList<PhotoModel> models=new ArrayList<>();
        PhotoModel model=new PhotoModel("1","https://static.pexels.com/photos/325521/pexels-photo-325521.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model1=new PhotoModel("2","https://static.pexels.com/photos/141635/pexels-photo-141635.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model2=new PhotoModel("3","https://static.pexels.com/photos/62663/ischnura-elegans-dragonfly-palisades-spurge-bad-luck-dragonfly-62663.jpeg","Title1","Description1","26-2-2017");
        PhotoModel model3=new PhotoModel("4","https://static.pexels.com/photos/163798/motor-scooter-vespa-roller-cult-163798.jpeg","Title1","Description1","26-2-2017");
        models.add(model);
        models.add(model1);
        models.add(model2);
        models.add(model3);
        VerticalPagerAdapter adapter=new VerticalPagerAdapter();
        adapter.setData(models,this,this);
        adapter.setVP(mViewPager);
        mViewPager.setAdapter(adapter);
    }
    private void setupViewPager() {
        ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        layoutParams.height = 200 / 7 * 6;
        //layoutParams.height = (int) ((layoutParams.width / 0.75));

        mViewPager.setOffscreenPageLimit(3);

        if (mViewPager.getParent() instanceof ViewGroup) {
            ViewGroup viewParent = ((ViewGroup) mViewPager.getParent());
            viewParent.setClipChildren(false);
            mViewPager.setClipChildren(false);
        }
        mViewPager.setOffscreenPageLimit(2);
        setAdapterData();
    }
}
