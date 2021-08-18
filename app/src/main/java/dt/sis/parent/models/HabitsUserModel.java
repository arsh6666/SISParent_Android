package dt.sis.parent.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HabitsUserModel {

    @Expose
    @SerializedName("__abp")
    private boolean __abp;
    @Expose
    @SerializedName("unAuthorizedRequest")
    private boolean unAuthorizedRequest;
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("result")
    private Result result;

    public boolean get__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public boolean getUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        @Expose
        @SerializedName("habits")
        private List<Habits> habits;
        @Expose
        @SerializedName("sleepingDescription")
        private String sleepingDescription;
        @Expose
        @SerializedName("drinkingDescription")
        private String drinkingDescription;
        @Expose
        @SerializedName("foodDescription")
        private String foodDescription;

        public List<Habits> getHabits() {
            return habits;
        }

        public void setHabits(List<Habits> habits) {
            this.habits = habits;
        }

        public String getSleepingDescription() {
            return sleepingDescription;
        }

        public void setSleepingDescription(String sleepingDescription) {
            this.sleepingDescription = sleepingDescription;
        }

        public String getDrinkingDescription() {
            return drinkingDescription;
        }

        public void setDrinkingDescription(String drinkingDescription) {
            this.drinkingDescription = drinkingDescription;
        }

        public String getFoodDescription() {
            return foodDescription;
        }

        public void setFoodDescription(String foodDescription) {
            this.foodDescription = foodDescription;
        }
    }

    public static class Habits {
        @Expose
        @SerializedName("habitOptionName")
        private String habitOptionName;
        @Expose
        @SerializedName("habitOptionId")
        private int habitOptionId;
        @Expose
        @SerializedName("habitCode")
        private String habitCode;
        @Expose
        @SerializedName("habitName")
        private String habitName;
        @Expose
        @SerializedName("habitTypeName")
        private String habitTypeName;
        @Expose
        @SerializedName("habitTypeId")
        private int habitTypeId;
        @Expose
        @SerializedName("habitId")
        private int habitId;
        @Expose
        @SerializedName("recordTime")
        private String recordTime;
        @Expose
        @SerializedName("dailyHabitId")
        private int dailyHabitId;
        @Expose
        @SerializedName("iconUrl")
        private String iconurl;

        public String getIconurl() {
            return iconurl;
        }

        public void setIconurl(String iconurl) {
            this.iconurl = iconurl;
        }

        public String getHabitOptionName() {
            return habitOptionName;
        }

        public void setHabitOptionName(String habitOptionName) {
            this.habitOptionName = habitOptionName;
        }

        public int getHabitOptionId() {
            return habitOptionId;
        }

        public void setHabitOptionId(int habitOptionId) {
            this.habitOptionId = habitOptionId;
        }

        public String getHabitCode() {
            return habitCode;
        }

        public void setHabitCode(String habitCode) {
            this.habitCode = habitCode;
        }

        public String getHabitName() {
            return habitName;
        }

        public void setHabitName(String habitName) {
            this.habitName = habitName;
        }

        public String getHabitTypeName() {
            return habitTypeName;
        }

        public void setHabitTypeName(String habitTypeName) {
            this.habitTypeName = habitTypeName;
        }

        public int getHabitTypeId() {
            return habitTypeId;
        }

        public void setHabitTypeId(int habitTypeId) {
            this.habitTypeId = habitTypeId;
        }

        public int getHabitId() {
            return habitId;
        }

        public void setHabitId(int habitId) {
            this.habitId = habitId;
        }

        public String getRecordTime() {
            return recordTime;
        }

        public void setRecordTime(String recordTime) {
            this.recordTime = recordTime;
        }

        public int getDailyHabitId() {
            return dailyHabitId;
        }

        public void setDailyHabitId(int dailyHabitId) {
            this.dailyHabitId = dailyHabitId;
        }
    }
}
