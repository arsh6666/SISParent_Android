package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GalleryDateGroupModel {

    private String date;

    private List<Result> result;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public static class Result {
        @Expose
        @SerializedName("count")
        private int count;
        @Expose
        @SerializedName("isApproved")
        private boolean isApproved;
        @Expose
        @SerializedName("galleryGroupId")
        private String galleryGroupId;
        @Expose
        @SerializedName("fileExtension")
        private String fileExtension;
        @Expose
        @SerializedName("mediaDate")
        private String mediaDate;
        @Expose
        @SerializedName("creatorUserId")
        private int creatorUserId;
        @Expose
        @SerializedName("contentType")
        private String contentType;
        @Expose
        @SerializedName("fileName")
        private String fileName;
        @Expose
        @SerializedName("mediaId")
        private String mediaId;
        @Expose
        @SerializedName("groupName")
        private String groupName;

        public String getMediaDate() {
            return mediaDate;
        }

        public void setMediaDate(String mediaDate) {
            this.mediaDate = mediaDate;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean getIsApproved() {
            return isApproved;
        }

        public void setIsApproved(boolean isApproved) {
            this.isApproved = isApproved;
        }

        public String getGalleryGroupId() {
            return galleryGroupId;
        }

        public void setGalleryGroupId(String galleryGroupId) {
            this.galleryGroupId = galleryGroupId;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        public void setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        public int getCreatorUserId() {
            return creatorUserId;
        }

        public void setCreatorUserId(int creatorUserId) {
            this.creatorUserId = creatorUserId;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
    }
}
