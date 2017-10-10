package bigappcompany.com.artistbooking.models;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artistbooking.network.JsonParser;


public class ImageTypeModel {
	private final String id, title, description,sl_image,date;
	//For DBACCess
	private String local_path;

	public ImageTypeModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.TYPE_ID);
		sl_image = object.getString(JsonParser.IMAGE_LINK);
		title = object.getString(JsonParser.TYPE_TITLE);
		description = object.getString(JsonParser.DESCRIPTION);
		date = object.getString(JsonParser.DATE_ADD);

	}
	//For DBAccess
	public ImageTypeModel(String id, String musicUrl, String title, String description, String date)
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
