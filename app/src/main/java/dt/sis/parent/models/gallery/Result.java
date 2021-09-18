
package dt.sis.parent.models.gallery;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public String getKey() {
        if (key == null)
            return "";
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
