package bigappcompany.com.artistbooking.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bigappcompany.com.artistbooking.R;


public class BioFragment extends Fragment implements  View.OnClickListener {
String bio="";

	TextView tv_bio;


	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bio, container, false);
		tv_bio=(TextView)rootView.findViewById(R.id.txt_bio);
		tv_bio.setText(bio);
		return rootView;
	}
	public void setText(String bio)
	{
		this.bio=bio;
		tv_bio.setText(bio);
	}


	@Override
	public void onClick(View v) {

	}


	public static Fragment newInstance(String bio) {
		BioFragment fragment=new BioFragment();
		fragment.bio=bio;
		return fragment;
	}
}


