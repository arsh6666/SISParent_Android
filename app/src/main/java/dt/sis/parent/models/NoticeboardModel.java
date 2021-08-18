package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NoticeboardModel {

    @Expose
    @SerializedName("__abp")
    private boolean Abp;
    @Expose
    @SerializedName("unAuthorizedRequest")
    private boolean unauthorizedrequest;
    @Expose
    @SerializedName("error")
    private String error;
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("targetUrl")
    private String targeturl;
    @Expose
    @SerializedName("result")
    private List<Result> result;
    @Expose
    @SerializedName("data")
    private String data;

    public boolean getAbp() {
        return Abp;
    }

    public void setAbp(boolean Abp) {
        this.Abp = Abp;
    }

    public boolean getUnauthorizedrequest() {
        return unauthorizedrequest;
    }

    public void setUnauthorizedrequest(boolean unauthorizedrequest) {
        this.unauthorizedrequest = unauthorizedrequest;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTargeturl() {
        return targeturl;
    }

    public void setTargeturl(String targeturl) {
        this.targeturl = targeturl;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class Result {
        @Expose
        @SerializedName("isFileAvailable")
        private boolean isFileAvailable;
       @Expose
        @SerializedName("tempFileName")
        private String tempFileName;
      @Expose
        @SerializedName("contentType")
        private String contentType;
       @Expose
        @SerializedName("extension")
        private String extension;
       @Expose
        @SerializedName("fileId")
        private String fileId;
        @Expose
        @SerializedName("eventDate")
        private String eventdate;
        @Expose
        @SerializedName("locationName")
        private String locationname;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;

        public boolean isFileAvailable() {
            return isFileAvailable;
        }

        public String getTempFileName() {
            return tempFileName;
        }

        public void setTempFileName(String tempFileName) {
            this.tempFileName = tempFileName;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public void setFileAvailable(boolean fileAvailable) {
            isFileAvailable = fileAvailable;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getEventdate() {
            return eventdate;
        }

        public void setEventdate(String eventdate) {
            this.eventdate = eventdate;
        }

        public String getLocationname() {
            return locationname;
        }

        public void setLocationname(String locationname) {
            this.locationname = locationname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
