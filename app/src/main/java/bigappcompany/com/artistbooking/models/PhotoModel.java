package bigappcompany.com.artistbooking.models;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.JsonParser;


public class PhotoModel {
	private final String id, title, description,sl_image;
	//For DBACCess
	private String local_path;

	public PhotoModel(JSONObject object) throws JSONException {
		id = "";
		sl_image = ApiUrl.BASE_URL+object.getString(JsonParser.SL_IMAGE);
		title = object.getString(JsonParser.TITLE);
		description = "";

	}
	//For DBAccess
	public PhotoModel(String id, String musicUrl, String title, String description, String date)
	{
		this.id=id;
		this.sl_image=musicUrl;
		this.title=title;
		this.description=description;

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
