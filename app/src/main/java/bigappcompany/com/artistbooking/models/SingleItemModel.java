package bigappcompany.com.artistbooking.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.JsonParser;

/**
 * Created by pratap.kesaboyina on 01-12-2015.
 */
public class SingleItemModel implements Serializable{


    private String name;
    private String url;
    private String description;
    private String id;


    public SingleItemModel() {
    }
    public SingleItemModel(JSONObject object) throws JSONException {
        name=object.getString(JsonParser.SUB_NAME);
        id=object.getString(JsonParser.SUB_ID);
        url=(new ApiUrl().BASE_URL)+(object.getString(JsonParser.SUB_URL));
    }

    public SingleItemModel(String name, String url,String id) {
        this.name = name;
        this.url = url;
        this.id=id;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id){this.id=id;}

    public String getId(){return id;}

}
