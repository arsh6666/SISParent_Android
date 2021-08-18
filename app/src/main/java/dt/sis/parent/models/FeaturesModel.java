package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeaturesModel {
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("targetUrl")
    @Expose
    private String targetUrl;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("unAuthorizedRequest")
    @Expose
    private Boolean unAuthorizedRequest;
    @SerializedName("__abp")
    @Expose
    private Boolean abp;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean getUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(Boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public Boolean getAbp() {
        return abp;
    }

    public void setAbp(Boolean abp) {
        this.abp = abp;
    }

    public class Result {
        @SerializedName("setting")
        @Expose
        private Setting setting;

        @SerializedName("features")
        @Expose
        private Features features;

        public Setting getSetting() {
            return setting;
        }

        public void setSetting(Setting setting) {
            this.setting = setting;
        }

        public Features getFeatures() {
            return features;
        }

        public void setFeatures(Features features) {
            this.features = features;
        }
    }

    public class Setting {
        @SerializedName("values")
        @Expose
        private Values values;

        public Values getValues() {
            return values;
        }

        public void setValues(Values values) {
            this.values = values;
        }
    }
    public class Values {
        @SerializedName("Mobile.Android.Teacher.AppVersion")
        @Expose
        private String mobileAndroidTeacherAppVersion;

        @SerializedName("Mobile.Android.Parent.AppVersion")
        @Expose
        private String mobileAndroidParentAppVersion;

        @SerializedName("Mobile.ParentApp.Validation")
        @Expose
        private String mobileParentAppValidation;

        @SerializedName("Mobile.TeacherApp.Validation")
        @Expose
        private String mobileTeacherAppValidation;

        public String getMobileAndroidTeacherAppVersion() {
            return mobileAndroidTeacherAppVersion;
        }

        public void setMobileAndroidTeacherAppVersion(String mobileAndroidTeacherAppVersion) {
            this.mobileAndroidTeacherAppVersion = mobileAndroidTeacherAppVersion;
        }

        public String getMobileAndroidParentAppVersion() {
            return mobileAndroidParentAppVersion;
        }

        public void setMobileAndroidParentAppVersion(String mobileAndroidParentAppVersion) {
            this.mobileAndroidParentAppVersion = mobileAndroidParentAppVersion;
        }

        public String getMobileParentAppValidation() {
            return mobileParentAppValidation;
        }

        public void setMobileParentAppValidation(String mobileParentAppValidation) {
            this.mobileParentAppValidation = mobileParentAppValidation;
        }

        public String getMobileTeacherAppValidation() {
            return mobileTeacherAppValidation;
        }

        public void setMobileTeacherAppValidation(String mobileTeacherAppValidation) {
            this.mobileTeacherAppValidation = mobileTeacherAppValidation;
        }
    }

    public class Features {

        @SerializedName("allFeatures")
        @Expose
        private AllFeatures allFeatures;

        public AllFeatures getAllFeatures() {
            return allFeatures;
        }

        public void setAllFeatures(AllFeatures allFeatures) {
            this.allFeatures = allFeatures;
        }

    }

    public class AllFeatures{
        @SerializedName("App.MaxUserCount")
        @Expose
        private AppMaxUserCount appMaxUserCount;
        @SerializedName("App.ChatFeature")
        @Expose
        private AppChatFeature appChatFeature;
        @SerializedName("App.HealthFeature")
        @Expose
        private AppHealthFeature appHealthFeature;
        @SerializedName("App.AttendanceFeature")
        @Expose
        private AppAttendanceFeature appAttendanceFeature;
        @SerializedName("App.DayManagmentFeature")
        @Expose
        private AppDayManagmentFeature appDayManagmentFeature;
        @SerializedName("App.GalleryManagmentFeature")
        @Expose
        private AppGalleryManagmentFeature appGalleryManagmentFeature;
        @SerializedName("App.PlanningManagmentFeature")
        @Expose
        private AppPlanningManagmentFeature appPlanningManagmentFeature;
        @SerializedName("App.FeeManagmentFeature")
        @Expose
        private AppFeeManagmentFeature appFeeManagmentFeature;
        @SerializedName("App.MobileFeatures")
        @Expose
        private AppMobileFeatures appMobileFeatures;
        @SerializedName("App.ChatFeature.TenantToTenant")
        @Expose
        private AppChatFeatureTenantToTenant appChatFeatureTenantToTenant;
        @SerializedName("App.ChatFeature.TenantToHost")
        @Expose
        private AppChatFeatureTenantToHost appChatFeatureTenantToHost;
        @SerializedName("App.Mobile.ParentFeatures")
        @Expose
        private AppMobileParentFeatures appMobileParentFeatures;
        @SerializedName("Mobile.Parent.ChatFeature")
        @Expose
        private MobileParentChatFeature mobileParentChatFeature;
        @SerializedName("Mobile.Parent.AttendanceFeature")
        @Expose
        private MobileParentAttendanceFeature mobileParentAttendanceFeature;
        @SerializedName("Mobile.Parent.FeeManagmentFeature")
        @Expose
        private MobileParentFeeManagmentFeature mobileParentFeeManagmentFeature;
        @SerializedName("Mobile.Parent.GalleryFeature")
        @Expose
        private MobileParentGalleryFeature mobileParentGalleryFeature;
        @SerializedName("Mobile.Parent.DailyRoutineFeature")
        @Expose
        private MobileParentDailyRoutineFeature mobileParentDailyRoutineFeature;
        @SerializedName("Mobile.Parent.CommentFeature")
        @Expose
        private MobileParentCommentFeature mobileParentCommentFeature;
        @SerializedName("App.Mobile.TeacherFeatures")
        @Expose
        private AppMobileTeacherFeatures appMobileTeacherFeatures;
        @SerializedName("Mobile.Teacher.ChatFeature")
        @Expose
        private MobileTeacherChatFeature mobileTeacherChatFeature;
        @SerializedName("Mobile.Teacher.AttendanceFeature")
        @Expose
        private MobileTeacherAttendanceFeature mobileTeacherAttendanceFeature;
        @SerializedName("Mobile.Teacher.GalleryFeature")
        @Expose
        private MobileTeacherGalleryFeature mobileTeacherGalleryFeature;
        @SerializedName("Mobile.Teacher.DailyRoutineFeature")
        @Expose
        private MobileTeacherDailyRoutineFeature mobileTeacherDailyRoutineFeature;
        @SerializedName("Mobile.Teacher.CommentFeature")
        @Expose
        private MobileTeacherCommentFeature mobileTeacherCommentFeature;

        public AppMaxUserCount getAppMaxUserCount() {
            return appMaxUserCount;
        }

        public void setAppMaxUserCount(AppMaxUserCount appMaxUserCount) {
            this.appMaxUserCount = appMaxUserCount;
        }

        public AppChatFeature getAppChatFeature() {
            return appChatFeature;
        }

        public void setAppChatFeature(AppChatFeature appChatFeature) {
            this.appChatFeature = appChatFeature;
        }

        public AppHealthFeature getAppHealthFeature() {
            return appHealthFeature;
        }

        public void setAppHealthFeature(AppHealthFeature appHealthFeature) {
            this.appHealthFeature = appHealthFeature;
        }

        public AppAttendanceFeature getAppAttendanceFeature() {
            return appAttendanceFeature;
        }

        public void setAppAttendanceFeature(AppAttendanceFeature appAttendanceFeature) {
            this.appAttendanceFeature = appAttendanceFeature;
        }

        public AppDayManagmentFeature getAppDayManagmentFeature() {
            return appDayManagmentFeature;
        }

        public void setAppDayManagmentFeature(AppDayManagmentFeature appDayManagmentFeature) {
            this.appDayManagmentFeature = appDayManagmentFeature;
        }

        public AppGalleryManagmentFeature getAppGalleryManagmentFeature() {
            return appGalleryManagmentFeature;
        }

        public void setAppGalleryManagmentFeature(AppGalleryManagmentFeature appGalleryManagmentFeature) {
            this.appGalleryManagmentFeature = appGalleryManagmentFeature;
        }

        public AppPlanningManagmentFeature getAppPlanningManagmentFeature() {
            return appPlanningManagmentFeature;
        }

        public void setAppPlanningManagmentFeature(AppPlanningManagmentFeature appPlanningManagmentFeature) {
            this.appPlanningManagmentFeature = appPlanningManagmentFeature;
        }

        public AppFeeManagmentFeature getAppFeeManagmentFeature() {
            return appFeeManagmentFeature;
        }

        public void setAppFeeManagmentFeature(AppFeeManagmentFeature appFeeManagmentFeature) {
            this.appFeeManagmentFeature = appFeeManagmentFeature;
        }

        public AppMobileFeatures getAppMobileFeatures() {
            return appMobileFeatures;
        }

        public void setAppMobileFeatures(AppMobileFeatures appMobileFeatures) {
            this.appMobileFeatures = appMobileFeatures;
        }

        public AppChatFeatureTenantToTenant getAppChatFeatureTenantToTenant() {
            return appChatFeatureTenantToTenant;
        }

        public void setAppChatFeatureTenantToTenant(AppChatFeatureTenantToTenant appChatFeatureTenantToTenant) {
            this.appChatFeatureTenantToTenant = appChatFeatureTenantToTenant;
        }

        public AppChatFeatureTenantToHost getAppChatFeatureTenantToHost() {
            return appChatFeatureTenantToHost;
        }

        public void setAppChatFeatureTenantToHost(AppChatFeatureTenantToHost appChatFeatureTenantToHost) {
            this.appChatFeatureTenantToHost = appChatFeatureTenantToHost;
        }

        public AppMobileParentFeatures getAppMobileParentFeatures() {
            return appMobileParentFeatures;
        }

        public void setAppMobileParentFeatures(AppMobileParentFeatures appMobileParentFeatures) {
            this.appMobileParentFeatures = appMobileParentFeatures;
        }

        public MobileParentChatFeature getMobileParentChatFeature() {
            return mobileParentChatFeature;
        }

        public void setMobileParentChatFeature(MobileParentChatFeature mobileParentChatFeature) {
            this.mobileParentChatFeature = mobileParentChatFeature;
        }

        public MobileParentAttendanceFeature getMobileParentAttendanceFeature() {
            return mobileParentAttendanceFeature;
        }

        public void setMobileParentAttendanceFeature(MobileParentAttendanceFeature mobileParentAttendanceFeature) {
            this.mobileParentAttendanceFeature = mobileParentAttendanceFeature;
        }

        public MobileParentFeeManagmentFeature getMobileParentFeeManagmentFeature() {
            return mobileParentFeeManagmentFeature;
        }

        public void setMobileParentFeeManagmentFeature(MobileParentFeeManagmentFeature mobileParentFeeManagmentFeature) {
            this.mobileParentFeeManagmentFeature = mobileParentFeeManagmentFeature;
        }

        public MobileParentGalleryFeature getMobileParentGalleryFeature() {
            return mobileParentGalleryFeature;
        }

        public void setMobileParentGalleryFeature(MobileParentGalleryFeature mobileParentGalleryFeature) {
            this.mobileParentGalleryFeature = mobileParentGalleryFeature;
        }

        public MobileParentDailyRoutineFeature getMobileParentDailyRoutineFeature() {
            return mobileParentDailyRoutineFeature;
        }

        public void setMobileParentDailyRoutineFeature(MobileParentDailyRoutineFeature mobileParentDailyRoutineFeature) {
            this.mobileParentDailyRoutineFeature = mobileParentDailyRoutineFeature;
        }

        public MobileParentCommentFeature getMobileParentCommentFeature() {
            return mobileParentCommentFeature;
        }

        public void setMobileParentCommentFeature(MobileParentCommentFeature mobileParentCommentFeature) {
            this.mobileParentCommentFeature = mobileParentCommentFeature;
        }

        public AppMobileTeacherFeatures getAppMobileTeacherFeatures() {
            return appMobileTeacherFeatures;
        }

        public void setAppMobileTeacherFeatures(AppMobileTeacherFeatures appMobileTeacherFeatures) {
            this.appMobileTeacherFeatures = appMobileTeacherFeatures;
        }

        public MobileTeacherChatFeature getMobileTeacherChatFeature() {
            return mobileTeacherChatFeature;
        }

        public void setMobileTeacherChatFeature(MobileTeacherChatFeature mobileTeacherChatFeature) {
            this.mobileTeacherChatFeature = mobileTeacherChatFeature;
        }

        public MobileTeacherAttendanceFeature getMobileTeacherAttendanceFeature() {
            return mobileTeacherAttendanceFeature;
        }

        public void setMobileTeacherAttendanceFeature(MobileTeacherAttendanceFeature mobileTeacherAttendanceFeature) {
            this.mobileTeacherAttendanceFeature = mobileTeacherAttendanceFeature;
        }

        public MobileTeacherGalleryFeature getMobileTeacherGalleryFeature() {
            return mobileTeacherGalleryFeature;
        }

        public void setMobileTeacherGalleryFeature(MobileTeacherGalleryFeature mobileTeacherGalleryFeature) {
            this.mobileTeacherGalleryFeature = mobileTeacherGalleryFeature;
        }

        public MobileTeacherDailyRoutineFeature getMobileTeacherDailyRoutineFeature() {
            return mobileTeacherDailyRoutineFeature;
        }

        public void setMobileTeacherDailyRoutineFeature(MobileTeacherDailyRoutineFeature mobileTeacherDailyRoutineFeature) {
            this.mobileTeacherDailyRoutineFeature = mobileTeacherDailyRoutineFeature;
        }

        public MobileTeacherCommentFeature getMobileTeacherCommentFeature() {
            return mobileTeacherCommentFeature;
        }

        public void setMobileTeacherCommentFeature(MobileTeacherCommentFeature mobileTeacherCommentFeature) {
            this.mobileTeacherCommentFeature = mobileTeacherCommentFeature;
        }

    }

    public class AppAttendanceFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppChatFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppChatFeatureTenantToHost {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppChatFeatureTenantToTenant {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppDayManagmentFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppFeeManagmentFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppGalleryManagmentFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppHealthFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppMaxUserCount {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppMobileFeatures {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppMobileParentFeatures {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppMobileTeacherFeatures {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class AppPlanningManagmentFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileParentAttendanceFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileParentChatFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileParentCommentFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileParentDailyRoutineFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileParentFeeManagmentFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileParentGalleryFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileTeacherAttendanceFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileTeacherChatFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileTeacherCommentFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileTeacherDailyRoutineFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class MobileTeacherGalleryFeature {

        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
