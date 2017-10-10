package bigappcompany.com.artistbooking.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;

import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.models.ImageObj;


public class CustomImagePager extends PagerAdapter {

	Context mContext;
	Activity activity;
	LayoutInflater mLayoutInflater;
	ArrayList<ImageObj> models;
	Timer timer;
	int page = 1;
	private boolean isDynamic = false;
	private ViewPager viewPager;

	@Override
	public int getCount() {
		return models.size();
	}

	public void setData(ArrayList<ImageObj> res, Context context, Activity activity) {
		this.models = res;
		this.mContext = context;
		this.activity = activity;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void isDynamic(boolean isAnimatable) {
		this.isDynamic = isAnimatable;
	}

	public void setVP(ViewPager viewPager) {
		this.viewPager = viewPager;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View itemView = mLayoutInflater.inflate(R.layout.pager_item_zoom, container, false);

		final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager);
		try {

					Picasso.with(activity).load(models.get(position).getPhoto()).resize(imageView.getResources().getDisplayMetrics().widthPixels,imageView.getResources().getDisplayMetrics().heightPixels).centerInside().into(imageView);


		} catch (Exception e) {
			Log.e("image Exception", e.getMessage());
		}

		container.addView(itemView);


		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((LinearLayout) object);
	}




}
