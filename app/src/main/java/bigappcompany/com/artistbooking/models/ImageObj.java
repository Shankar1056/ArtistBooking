package bigappcompany.com.artistbooking.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


import bigappcompany.com.artistbooking.network.JsonParser;


/**
 * Created by sam on 5/2/17.
 */

public class ImageObj implements Serializable{
	private final String id,sl_image,title;
	//For DBACCess
	private String des;

	public ImageObj(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.IMG_ID);
		sl_image = object.getString(JsonParser.IMG_LINK);
		title=object.getString(JsonParser.TITLE);
	}
	//For DBAccess
	public ImageObj(String id, String musicUrl, String title)
	{
		this.id=id;
		this.sl_image=musicUrl;
		this.title=title;
	}

	//For DBACCESS
	public void setDes(String des)
	{
		this.des = des;
	}

	public String getId() {
		return id;
	}


	public String getPhoto() {
		return sl_image;
	}

	public String getTitle(){return title;}

	//DBACCESS
	public String getDes() {
		if(des ==null)
		{return "";}
		else
		return des;
	}
}
