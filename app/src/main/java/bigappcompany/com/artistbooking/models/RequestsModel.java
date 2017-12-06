package bigappcompany.com.artistbooking.models;

import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artistbooking.network.JsonParser;


public class RequestsModel {
	private final String id, event_name, name,date,address,phone;


	public RequestsModel(JSONObject object) throws JSONException {
		id = object.getString(JsonParser.ID);
		name = object.getString(JsonParser.ARTIST_NAME);
		event_name = object.getString(JsonParser.EVENT_NAME);
		date=object.getString(JsonParser.EVENT_DATE);
		address=object.getString(JsonParser.EVENT_VENUE);
		phone=object.getString(JsonParser.AR_MOB_NUM);
	}
	//For DBAccess
	public RequestsModel(String id, String name, String event_name, String date, String address, String phone)
	{
		this.id=id;
		this.name =name;
		this.event_name = event_name;
		this.date=date;
		this.address=address;
		this.phone=phone;
	}



	public String getId() {
		return id;
	}

	public String getEvent_name() {
		return event_name;
	}

	public String getName(){return name;}

	public String getDate(){return date;}

	public String getAddress(){return address;}

	public String getPhone(){return phone;}


}
