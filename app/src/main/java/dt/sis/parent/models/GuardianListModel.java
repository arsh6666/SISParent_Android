package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GuardianListModel implements Serializable {

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

    public static class Result implements Serializable {
        @Expose
        @SerializedName("address")
        private String address;
        @Expose
        @SerializedName("guardianType")
        private String guardiantype;
        @Expose
        @SerializedName("mobileNumber")
        private String mobilenumber;
        @Expose
        @SerializedName("guardianTypeId")
        private int guardiantypeid;
        @Expose
        @SerializedName("emiratesIdNumber")
        private int emiratesidnumber;
        @Expose
        @SerializedName("lastName")
        private String lastname;
        @Expose
        @SerializedName("firstName")
        private String firstname;
        @Expose
        @SerializedName("id")
        private int id;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGuardiantype() {
            return guardiantype;
        }

        public void setGuardiantype(String guardiantype) {
            this.guardiantype = guardiantype;
        }

        public String getMobilenumber() {
            return mobilenumber;
        }

        public void setMobilenumber(String mobilenumber) {
            this.mobilenumber = mobilenumber;
        }

        public int getGuardiantypeid() {
            return guardiantypeid;
        }

        public void setGuardiantypeid(int guardiantypeid) {
            this.guardiantypeid = guardiantypeid;
        }

        public int getEmiratesidnumber() {
            return emiratesidnumber;
        }

        public void setEmiratesidnumber(int emiratesidnumber) {
            this.emiratesidnumber = emiratesidnumber;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
