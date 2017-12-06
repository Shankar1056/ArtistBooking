package bigappcompany.com.artistbooking.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.artistbooking.network.JsonParser;


public class PhotosModel {
	private final String id, title, description,sl_image,date;
	//For DBACCess
	private String local_path;
	ArrayList<ImageObj> images;

	public PhotosModel(JSONObject json) throws JSONException
	{
		id=json.getString(JsonParser.TYPE_ID);
		title=json.getString(JsonParser.TYPE_TITLE);
		description=json.getString(JsonParser.TYPE_DESC);
		sl_image=json.getString(JsonParser.TYPE_LINK);
		date=json.getString(JsonParser.TYPE_DATE);
		images=new ArrayList<>();
		JSONArray array=json.getJSONArray(JsonParser.IMAGES);
		for(int i=0;i<array.length();i++)
		{
			images.add(new ImageObj(array.getJSONObject(i)));
		}
	}

	public ArrayList<ImageObj> getImages()
	{
		return images;
	}
	//For DBAccess
	public PhotosModel(String id, String musicUrl, String title, String description, String date)
	{
		this.id=id;
		this.sl_image=musicUrl;
		this.title=title;
		this.description=description;
		this.date=date;
	}

	//For DBACCESS
	public void setLocal_path(String local_path)
	{
		this.local_path=local_path;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	public String getDate() {
		return date;
	}


	public String getPhoto() {
		return sl_image;
	}

	//DBACCESS
	public String getLocal_path() {
		if(local_path==null)
		{return "";}
		else
		return local_path;
	}
}
