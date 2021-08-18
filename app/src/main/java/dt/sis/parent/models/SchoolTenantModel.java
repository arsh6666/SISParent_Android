package dt.sis.parent.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class SchoolTenantModel {

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

        @SerializedName("tenancyName")
        private String tenancyname;
        @SerializedName("name")
        private String name;
        @SerializedName("tenantId")
        private int tenantid;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getTenancyname() {
            return tenancyname;
        }

        public void setTenancyname(String tenancyname) {
            this.tenancyname = tenancyname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTenantid() {
            return tenantid;
        }

        public void setTenantid(int tenantid) {
            this.tenantid = tenantid;
        }
    }
}
