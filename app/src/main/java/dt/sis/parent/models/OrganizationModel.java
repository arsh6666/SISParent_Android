package dt.sis.parent.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class OrganizationModel {

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
        private boolean checked;

        @SerializedName("organizationUnitName")
        private String organizationunitname;
        @SerializedName("id")
        private int id;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getOrganizationunitname() {
            return organizationunitname;
        }

        public void setOrganizationunitname(String organizationunitname) {
            this.organizationunitname = organizationunitname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
