package bigappcompany.com.artistbooking.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import bigappcompany.com.artistbooking.network.JsonParser;


/**
 * Created by sam on 5/2/17.
 */

public class VideoObj implements Serializable{
	private final String id,thumb,title,video_id,video_url,date,artist_id;



	public VideoObj(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.ID);
		thumb = new JSONObject(object.getString(JsonParser.THUMB)).getJSONObject(JsonParser.HIGH).getString(JsonParser.URL);
		title=object.getString(JsonParser.TITLE);
		video_id=object.getString(JsonParser.VIDEO_ID);
		video_url=object.getString(JsonParser.VIDEO_URL);
		date=object.getString(JsonParser.DATE);
		artist_id=object.getString(JsonParser.ARTIST_ID);
	}
	//For DBAccess
	public VideoObj(String id, String Url, String title,String video_id,String video_url,String artist_id,String date)
	{
		this.id=id;
		this.thumb=Url;
		this.title=title;
		this.video_id=video_id;
		this.video_url=video_url;
		this.artist_id=artist_id;
		this.date=date;
	}



	public String getId() {
		return id;
	}


	public String getPhoto() {
		return thumb;
	}

	public String getTitle(){return title;}


	public String getVideoId() {
		return video_id;
	}
}
