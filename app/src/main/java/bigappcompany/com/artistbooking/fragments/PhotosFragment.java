package bigappcompany.com.artistbooking.fragments;

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

import bigappcompany.com.artistbooking.MainActivity;
import bigappcompany.com.artistbooking.R;
import bigappcompany.com.artistbooking.adapters.PhotoAdapter;
import bigappcompany.com.artistbooking.models.ImageObj;
import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;


public class PhotosFragment extends Fragment implements  View.OnClickListener {
	ArrayList<ImageObj> models;

	RecyclerView recyclerView;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_photo, container, false);

		recyclerView=(RecyclerView)rootView.findViewById(R.id.photos_RV);

		LinearLayoutManager manager=new LinearLayoutManager(getContext());
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(manager);

		recyclerView.setAdapter(new PhotoAdapter(models));
		Download_web web=new Download_web(getContext(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try
				{
					JSONObject object=new JSONObject(response);
					if(object.getBoolean(JsonParser.RESPONSE_STATUS))
					{
						JSONArray array=object.getJSONArray(JsonParser.DATA);
						for(int i=0;i<array.length();i++)
						{
							ImageObj obj=new ImageObj(array.getJSONObject(i).getString(JsonParser.ID),(ApiUrl.BASE_URL)+(array.getJSONObject(i).getString(JsonParser.IMG_LINK)),array.getJSONObject(i).getString(JsonParser.TITLE));
							obj.setDes(array.getJSONObject(i).getString(JsonParser.DATE));
							models.add(obj);
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
		web.execute(ApiUrl.IMAGES+((MainActivity)getActivity()).getId());
		return rootView;
	}

	public void setModels(ArrayList<ImageObj> models)
	{
		this.models=models;
		recyclerView.getAdapter().notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {

	}


	public static Fragment newInstance(ArrayList<ImageObj> models) {
		PhotosFragment fragment=new PhotosFragment();
		fragment.models=models;
		return fragment;
	}
}


