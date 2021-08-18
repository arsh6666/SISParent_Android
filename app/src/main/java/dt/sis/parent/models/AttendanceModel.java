package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceModel {

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

   public static class Result {
        @Expose
        @SerializedName("present")
        private String present;
        @Expose
        @SerializedName("attendanceDay")
        private String attendanceday;
        @Expose
        @SerializedName("attendanceMonth")
        private String attendancemonth;
        @Expose
        @SerializedName("checkOutBlockName")
        private String checkoutblockname;
        @Expose
        @SerializedName("checkOutTime")
        private String checkouttime;
        @Expose
        @SerializedName("checkInBlockName")
        private String checkinblockname;
        @Expose
        @SerializedName("checkinTime")
        private String checkintime;
        @Expose
        @SerializedName("attendanceDate")
        private String attendancedate;

        public String getPresent() {
            return present;
        }

        public void setPresent(String present) {
            this.present = present;
        }

        public String getAttendanceday() {
            return attendanceday;
        }

        public void setAttendanceday(String attendanceday) {
            this.attendanceday = attendanceday;
        }

        public String getAttendancemonth() {
            return attendancemonth;
        }

        public void setAttendancemonth(String attendancemonth) {
            this.attendancemonth = attendancemonth;
        }

        public String getCheckoutblockname() {
            return checkoutblockname;
        }

        public void setCheckoutblockname(String checkoutblockname) {
            this.checkoutblockname = checkoutblockname;
        }

        public String getCheckouttime() {
            return checkouttime;
        }

        public void setCheckouttime(String checkouttime) {
            this.checkouttime = checkouttime;
        }

        public String getCheckinblockname() {
            return checkinblockname;
        }

        public void setCheckinblockname(String checkinblockname) {
            this.checkinblockname = checkinblockname;
        }

        public String getCheckintime() {
            return checkintime;
        }

        public void setCheckintime(String checkintime) {
            this.checkintime = checkintime;
        }

        public String getAttendancedate() {
            return attendancedate;
        }

        public void setAttendancedate(String attendancedate) {
            this.attendancedate = attendancedate;
        }
    }
}
