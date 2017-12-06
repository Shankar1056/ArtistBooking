package bigappcompany.com.artistbooking.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pratap.kesaboyina on 30-11-2015.
 */
public class SectionDataModel implements Serializable{



    private String headerTitle;
    private String id;
    private String url;
    private ArrayList<SingleItemModel> allItemsInSection;


    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle, ArrayList<SingleItemModel> allItemsInSection,String id) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
        this.id=id;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }
    public void setUrl(String url){this.url=url;}
    public String getUrl()
    {
        return url;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }
    public void setId(String id)
    {this.id=id;}

    public String getId(){return id;}

    public ArrayList<SingleItemModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SingleItemModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
