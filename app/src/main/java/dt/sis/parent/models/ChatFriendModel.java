package dt.sis.parent.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class ChatFriendModel {

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
        @SerializedName("friends")
        private List<Friends> friends;
        @SerializedName("serverTime")
        private String servertime;

        public List<Friends> getFriends() {
            return friends;
        }

        public void setFriends(List<Friends> friends) {
            this.friends = friends;
        }

        public String getServertime() {
            return servertime;
        }

        public void setServertime(String servertime) {
            this.servertime = servertime;
        }
    }

    public static class Friends {
        @SerializedName("state")
        private int state;
        @SerializedName("isOnline")
        private boolean isonline;
        @SerializedName("unreadMessageCount")
        private int unreadmessagecount;
        @SerializedName("friendProfilePictureId")
        private String friendprofilepictureid;
        @SerializedName("friendTenancyName")
        private String friendtenancyname;
        @SerializedName("friendName")
        private String friendname;
        @SerializedName("friendUserName")
        private String friendusername;
        @SerializedName("friendTenantId")
        private int friendtenantid;
        @SerializedName("friendUserId")
        private int frienduserid;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean getIsonline() {
            return isonline;
        }

        public void setIsonline(boolean isonline) {
            this.isonline = isonline;
        }

        public int getUnreadmessagecount() {
            return unreadmessagecount;
        }

        public void setUnreadmessagecount(int unreadmessagecount) {
            this.unreadmessagecount = unreadmessagecount;
        }

        public String getFriendprofilepictureid() {
            return friendprofilepictureid;
        }

        public void setFriendprofilepictureid(String friendprofilepictureid) {
            this.friendprofilepictureid = friendprofilepictureid;
        }

        public String getFriendtenancyname() {
            return friendtenancyname;
        }

        public void setFriendtenancyname(String friendtenancyname) {
            this.friendtenancyname = friendtenancyname;
        }

        public String getFriendname() {
            return friendname;
        }

        public void setFriendname(String friendname) {
            this.friendname = friendname;
        }

        public String getFriendusername() {
            return friendusername;
        }

        public void setFriendusername(String friendusername) {
            this.friendusername = friendusername;
        }

        public int getFriendtenantid() {
            return friendtenantid;
        }

        public void setFriendtenantid(int friendtenantid) {
            this.friendtenantid = friendtenantid;
        }

        public int getFrienduserid() {
            return frienduserid;
        }

        public void setFrienduserid(int frienduserid) {
            this.frienduserid = frienduserid;
        }
    }
}
