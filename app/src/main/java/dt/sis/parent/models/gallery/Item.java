
package dt.sis.parent.models.gallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("mediaId")
    @Expose
    private String mediaId;
    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("mediaDate")
    @Expose
    private String mediaDate;
    @SerializedName("fileExtension")
    @Expose
    private String fileExtension;
    @SerializedName("galleryGroupId")
    @Expose
    private String galleryGroupId;
    @SerializedName("count")
    @Expose
    private Integer count;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMediaId() {
        if (mediaId == null)
            return "";
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMediaDate() {
        return mediaDate;
    }

    public void setMediaDate(String mediaDate) {
        this.mediaDate = mediaDate;
    }

    public String getFileExtension() {
        if (fileExtension == null)
            return "";
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getGalleryGroupId() {
        return galleryGroupId;
    }

    public void setGalleryGroupId(String galleryGroupId) {
        this.galleryGroupId = galleryGroupId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
