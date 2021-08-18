package dt.sis.parent.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatMessageModel {

    @SerializedName("__abp")
    private boolean Abp;
    @SerializedName("unAuthorizedRequest")
    private boolean unauthorizedrequest;
    @SerializedName("error")
    private String error;
    @SerializedName("success")
    private boolean success;
    @SerializedName("targetUrl")
    private String targeturl;
    @SerializedName("result")
    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        @SerializedName("items")
        private List<Items> items;

        public List<Items> getItems() {
            return items;
        }

        public void setItems(List<Items> items) {
            this.items = items;
        }
    }

    public static class Items {
        @SerializedName("id")
        private int id;
        @SerializedName("sharedMessageId")
        private String sharedmessageid;
        @SerializedName("creationTime")
        private String creationtime;
        @SerializedName("message")
        private String message;
        @SerializedName("receiverReadState")
        private int receiverreadstate;
        @SerializedName("readState")
        private int readstate;
        @SerializedName("side")
        private int side;
        @SerializedName("targetTenantId")
        private int targettenantid;
        @SerializedName("targetUserId")
        private int targetuserid;
        @SerializedName("tenantId")
        private int tenantid;
        @SerializedName("userId")
        private int userid;

        boolean showDate = false;

        public boolean isShowDate() {
            return showDate;
        }

        public void setShowDate(boolean showDate) {
            this.showDate = showDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSharedmessageid() {
            return sharedmessageid;
        }

        public void setSharedmessageid(String sharedmessageid) {
            this.sharedmessageid = sharedmessageid;
        }

        public String getCreationtime() {
            return creationtime;
        }

        public void setCreationtime(String creationtime) {
            this.creationtime = creationtime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getReceiverreadstate() {
            return receiverreadstate;
        }

        public void setReceiverreadstate(int receiverreadstate) {
            this.receiverreadstate = receiverreadstate;
        }

        public int getReadstate() {
            return readstate;
        }

        public void setReadstate(int readstate) {
            this.readstate = readstate;
        }

        public int getSide() {
            return side;
        }

        public void setSide(int side) {
            this.side = side;
        }

        public int getTargettenantid() {
            return targettenantid;
        }

        public void setTargettenantid(int targettenantid) {
            this.targettenantid = targettenantid;
        }

        public int getTargetuserid() {
            return targetuserid;
        }

        public void setTargetuserid(int targetuserid) {
            this.targetuserid = targetuserid;
        }

        public int getTenantid() {
            return tenantid;
        }

        public void setTenantid(int tenantid) {
            this.tenantid = tenantid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }
    }
}
