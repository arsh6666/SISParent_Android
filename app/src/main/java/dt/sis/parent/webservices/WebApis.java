package dt.sis.parent.webservices;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Created by Er. Shashank on 21 Sep 2019.
 */

public interface WebApis {

    @POST("/api/services/app/Parent/SendCommentForStudent")
    Call<JsonObject> sendCommentForStudent(@Body JsonObject jsonParams);

    @POST("api/TokenAuth/Authenticate")
    Call<JsonObject> loginAuthenticate(@Body JsonObject jsonParams);


    @POST("api/services/app/Profile/ChangePassword")
    Call<JsonObject> changePasswordApi(@Body JsonObject jsonParams);

    @POST("api/services/app/Account/SendPasswordResetCode")
    Call<JsonObject> sendResetCode(@Body JsonObject jsonParams);

    @POST("api/services/app/Account/ResetPassword")
    Call<JsonObject> resetPassword(@Body JsonObject jsonParams);


    @POST("api/TokenAuth/Refresh")
    Call<JsonObject> refreshToken(@Body JsonObject jsonParams);

    @GET("api/services/app/Profile/GetProfilePicture")
    Call<JsonObject> getProfileImageApi();

    @GET("api/services/app/Profile/GetCurrentUserProfileForEdit")
    Call<JsonObject> getProfileForEdit(
            @Query("tenantId") String tenantId,
            @Query("organizationId") String organizationId
    );

    @GET("api/services/app/Parent/GetTeacherComments")
    Call<JsonObject> getParentsComments();


    @POST("api/services/app/Parent/ReplyToTeacherComment")
    Call<JsonObject> ReplyToTeacherComment(@Body JsonObject jsonParams);

    @POST("api/services/app/Parent/SetParentReadComment")
    Call<JsonObject> SetParentReadComment(@Body JsonObject jsonParams);

    @GET("api/services/app/Parent/GetParent")
    Call<JsonObject> getChildren(
            @Query("tenantId") String tenantId,
            @Query("organizationId") String organizationId
    );

    @GET("api/services/app/NoticeBoard/GetNoticeBoardForParent")
    Call<JsonObject> getNoticeboard(
            @Query("tenantId") String tenantId,
            @Query("organizationId") String organizationId
    );

    @GET("api/services/app/Gallery/GetMediaForStudent")
    Call<JsonObject> getStudentGallery(
            @Query("studentId") int studentId
    );

    @GET("api/services/app/Gallery/GetMediaForStudentWithGrouping")
    Call<JsonObject> getStudentGalleryGrouping(
            @Query("studentId") int studentId
    );

    @Multipart
    @POST("Profile/UploadProfilePicture")
    Call<JsonObject> uploadProfilePicture(@Part List<MultipartBody.Part> surveyImage);

    @PUT("api/services/app/Profile/UpdateProfilePicture")
    Call<JsonObject> updateProfilePicture(@Body JsonObject jsonParams);

    @PUT("api/services/app/Profile/UpdateCurrentUserProfile")
    Call<JsonObject> updateProfileApi(@Body JsonObject jsonParams);

    @POST("api/services/app/CommonLookup/SendContactFormRequest")
    Call<JsonObject> contactusApi(@Body JsonObject jsonParams);

    @GET("api/services/app/Tenant/GetTenantAboutUsInformation")
    Call<JsonObject> getAboutUs(
            @Query("tenantId") String tenantId,
            @Query("organizationId") String organizationId
    );


    @GET("api/services/app/Notification/GetUserNotifications")
    Call<JsonObject> getNotification(@Query("State") String State);

    @Streaming
    @GET("File/DownloadFile")
    Call<ResponseBody> downloadFile(
            @Query("fileId") String fileId,
            @Query("tenantId") String tenantId
    );

    @GET("AbpUserConfiguration/GetAll")
    Call<JsonObject> geEnableAppFeatures(
    );

    @GET("api/services/app/Habit/GetAllHabbits")
    Call<JsonObject> getAllHabbits();

    @GET("api/services/app/Habit/GetStudentDailyHabit")
    Call<JsonObject> getDailyHabit(
            @Query("studentId") int studentId,
            @Query("habitDate") String habitDate
    );

    @GET("api/services/app/Student/GetStudentAttendance")
    Call<JsonObject> getAttendance(
            @Query("code") int studentId,
            @Query("tenantId") String habitDate
    );

    @GET("api/services/app/Parent/GetGuardians")
    Call<JsonObject> getGuardians(
            @Query("tenantId") String tenantId,
            @Query("organizationId") String organizationId
    );

    @GET("api/services/app/Parent/GetGuardianTypes")
    Call<JsonObject> getGuardianTypes(
    );

    @POST("api/services/app/Parent/CreateOrUpdateGuardian")
    Call<JsonObject> createOrUpdateGuardian(@Body JsonObject jsonParams);

    @PUT("api/services/app/Parent/UpdateGuardianPicture")
    Call<JsonObject> updateGuardianPicture(@Body JsonObject jsonParams);

    @DELETE("api/services/app/Parent/DeleteGuardian")
    Call<JsonObject> deleteGuardian(@Query("id") int id);

    @GET("api/services/app/Account/GetTenants")
    Call<JsonObject> getSchoolTenants(
    );

    @GET("api/services/app/Account/GetOrganizationUnitsForTenant")
    Call<JsonObject> getOrganizationForTenants(
            @Query("tenantId") String tenantId,
            @Query("organizationId") String organizationId
    );

    @GET("api/services/app/chat/GetUserChatFriendsWithSettings")
    Call<JsonObject> getUserChatFriends(
            @Query("teacherCode") String teacherCode,
            @Query("tenantId") String tenantId,
            @Query("organizationId") String organizationId
    );

    @GET("api/services/app/chat/GetUserChatMessages")
    Call<JsonObject> getUserChatMessage(
            @Query("teacherCode") String teacherCode,
            @Query("tenantId") String tenantId,
            @Query("organizationId") String organizationId,
            @Query("userId") int userId
    );

    @Streaming
    @GET()
    Call<ResponseBody> downloadFileSample(@Url String fileUrl
    );

}
