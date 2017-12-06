package bigappcompany.com.artistbooking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.BasicPlayerActivity;
import bigappcompany.com.artistbooking.MainActivity;
import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.adapters.VideoAdapter;
import bigappcompany.com.artistbooking.models.Constants;
import bigappcompany.com.artistbooking.models.VideoObj;
import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;


public class VideosFragment extends Fragment implements  VideoAdapter.OnVideoItemListener,View.OnClickListener {

	RecyclerView recyclerView;
	String js_res="";

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_photo, container, false);

		recyclerView=(RecyclerView)rootView.findViewById(R.id.photos_RV);

		LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
		recyclerView.setLayoutManager(manager);

		recyclerView.setAdapter(new VideoAdapter(new ArrayList<VideoObj>(),this));

		Download_web web=new Download_web(getContext(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try
				{
					js_res=response;
					JSONObject object=new JSONObject(response);
					if(object.getBoolean(JsonParser.RESPONSE_STATUS))
					{
						JSONArray array=object.getJSONArray(JsonParser.DATA);
						for(int i=0;i<array.length();i++)
						{
							VideoObj obj=new VideoObj(array.getJSONObject(i));
							((VideoAdapter)recyclerView.getAdapter()).addItem(obj);

						}
					}
					else
					{
						//Toast.makeText(getContext(),"No data",Toast.LENGTH_LONG).show();
					}
					recyclerView.getAdapter().notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		web.execute(ApiUrl.VIDEOS+((MainActivity)getActivity()).getId());
		return rootView;
	}



	@Override
	public void onClick(View v) {

	}




	public static Fragment newInstance() {
		VideosFragment fragment=new VideosFragment();

		return fragment;
	}

	@Override
	public void onVideo(int pos) {
		startActivity(new Intent(getContext(), BasicPlayerActivity.class).putExtra(Constants.POS,pos).putExtra(JsonParser.DATA,js_res));
	}
}


