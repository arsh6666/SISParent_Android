package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotifcationModel {

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
        @Expose
        @SerializedName("items")
        private List<Items> items;
        @Expose
        @SerializedName("totalCount")
        private int totalcount;
        @Expose
        @SerializedName("unreadCount")
        private int unreadcount;

        public List<Items> getItems() {
            return items;
        }

        public void setItems(List<Items> items) {
            this.items = items;
        }

        public int getTotalcount() {
            return totalcount;
        }

        public void setTotalcount(int totalcount) {
            this.totalcount = totalcount;
        }

        public int getUnreadcount() {
            return unreadcount;
        }

        public void setUnreadcount(int unreadcount) {
            this.unreadcount = unreadcount;
        }
    }

    public static class Items {
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("notification")
        private Notification notification;
        @Expose
        @SerializedName("state")
        private int state;
        @Expose
        @SerializedName("userId")
        private int userid;
        @Expose
        @SerializedName("tenantId")
        private int tenantid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Notification getNotification() {
            return notification;
        }

        public void setNotification(Notification notification) {
            this.notification = notification;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getTenantid() {
            return tenantid;
        }

        public void setTenantid(int tenantid) {
            this.tenantid = tenantid;
        }
    }

    public static class Notification {
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("creationTime")
        private String creationtime;
        @Expose
        @SerializedName("severity")
        private int severity;
        @Expose
        @SerializedName("entityId")
        private String entityid;
        @Expose
        @SerializedName("entityTypeName")
        private String entitytypename;
        @Expose
        @SerializedName("entityType")
        private String entitytype;
        @Expose
        @SerializedName("data")
        private Data data;
        @Expose
        @SerializedName("notificationName")
        private String notificationname;
        @Expose
        @SerializedName("tenantId")
        private int tenantid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreationtime() {
            return creationtime;
        }

        public void setCreationtime(String creationtime) {
            this.creationtime = creationtime;
        }

        public int getSeverity() {
            return severity;
        }

        public void setSeverity(int severity) {
            this.severity = severity;
        }

        public String getEntityid() {
            return entityid;
        }

        public void setEntityid(String entityid) {
            this.entityid = entityid;
        }

        public String getEntitytypename() {
            return entitytypename;
        }

        public void setEntitytypename(String entitytypename) {
            this.entitytypename = entitytypename;
        }

        public String getEntitytype() {
            return entitytype;
        }

        public void setEntitytype(String entitytype) {
            this.entitytype = entitytype;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getNotificationname() {
            return notificationname;
        }

        public void setNotificationname(String notificationname) {
            this.notificationname = notificationname;
        }

        public int getTenantid() {
            return tenantid;
        }

        public void setTenantid(int tenantid) {
            this.tenantid = tenantid;
        }
    }

    public static class Data {
        @Expose
        @SerializedName("properties")
        private Properties properties;
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("message")
        private String message;

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Properties {
        @Expose
        @SerializedName("Message")
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
