package dt.sis.parent.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AppUtils {
    public final static  String KIDS_PALACE = "kidspalace";
    public final static  String THE_PALACE = "thepalace";
    public final static  String SIS_MAIN = "sismain";

    public void Util() {

    }


    public String authorize(String userName, String userType) {
        String authorization = "";
        try {
            String passbuffer = userName + ":" + userType;
            byte[] data = passbuffer.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
            authorization = "Basic " + base64;
        } catch (UnsupportedEncodingException ex) {
        }
        return authorization;
    }

    public static String prepareOAuthHeader(String realm, String kvpair) {
        String[] kvPairs = kvpair.split("&");
        StringBuilder sb = new StringBuilder("Basic " + realm);
        for (int i = 0; i < kvPairs.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            String key = kvPairs[i].substring(0, kvPairs[i].indexOf("="));
            String value = kvPairs[i].substring(kvPairs[i].indexOf("=") + 1);
            sb.append(key + "=\"" + value + "\"");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public void showDailog(Context context, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("ErrorModel").setMessage(message);
        dialog.setPositiveButton("Ok", null).show();
    }

    public static String gotoEncrypted(String str) {
        byte[] encrypted = null;
        try {
            String key = "FSRFSR@1FSRFSR@1"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            encrypted = cipher.doFinal(str.getBytes());
            //System.err.println(new String(encrypted));
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(encrypted));
            //System.err.println(decrypted);
        } catch (Exception e) {

        }
        return Arrays.toString(encrypted);
    }
    public static String getDate(String inputDateStr) {
        String substr=inputDateStr.substring(0,inputDateStr.indexOf("T"));
//        Log.e("substringDate",substr);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
//        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
//        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = inputFormat.parse(substr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        return outputDateStr;
    }

    public static String gotoDecrypted(String str) {
        byte[] array = null;
        if (str != null) {
            String[] split = str.substring(1, str.length() - 1).split(", ");
            array = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                array[i] = Byte.parseByte(split[i]);
            }
        }

        String decrypted = null;
        try {
            String key = "FSRFSR@1FSRFSR@1"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");

            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            decrypted = new String(cipher.doFinal(array));
            //System.err.println(decrypted);
        } catch (Exception e) {

        }
        return decrypted;
    }
    public static String getAge(String day, String month, String year) {
        int dobYear = Integer.parseInt(year);
        int dobMonth = Integer.parseInt(month);
        int dobDay = Integer.parseInt(day);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(dobYear, dobMonth, dobDay);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static String getDateTimeFromUTC(String inputDateStr){

        if (inputDateStr == null || inputDateStr.isEmpty()){
            return "";
        }


        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.getDefault());
//        oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal ="";
        try {
            value = oldFormatter.parse(inputDateStr);
            SimpleDateFormat newFormatter = new SimpleDateFormat("hh:mm a",Locale.getDefault());

//            newFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dueDateAsNormal;
    }
    public static String getTimeFromUTC(String inputDateStr){
        if(inputDateStr==null || inputDateStr.isEmpty()){
            return "";
        }
//        Log.e("inputDateStr",inputDateStr+" ");
        if(inputDateStr.contains(".")){
            inputDateStr  = inputDateStr.substring(0,inputDateStr.indexOf("."));
        }
        inputDateStr = inputDateStr.replace("Z","");
        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault());
        oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal ="";
        try {
            value = oldFormatter.parse(inputDateStr);
            SimpleDateFormat newFormatter = new SimpleDateFormat("hh:mm a",Locale.getDefault());

            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dueDateAsNormal;
    }
    public static String geTimeFromUTC(String inputDateStr){
        if (inputDateStr == null){
            return "";
        }
        int index =  inputDateStr.indexOf("\\.");
        if(index!=-1) {
            inputDateStr = inputDateStr.substring(0, index);
        }
        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault());
//        oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal ="";
        try {
            value = oldFormatter.parse(inputDateStr);
            SimpleDateFormat newFormatter = new SimpleDateFormat("hh:mm a",Locale.getDefault());

//            newFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dueDateAsNormal;
    }

    public static String getDateFromUTC(String inputDateStr) {
        if(inputDateStr==null || inputDateStr.isEmpty()){
            return "";
        }
//        Log.e("inputDateStr",inputDateStr+" ");
        if(inputDateStr.contains(".")){
            inputDateStr  = inputDateStr.substring(0,inputDateStr.indexOf("."));
        }
        inputDateStr = inputDateStr.replace("Z","");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        outputFormat.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        return outputDateStr;
    }
    public static String getDayFromUTC(String inputDateStr) {
        if(inputDateStr==null || inputDateStr.isEmpty()){
            return "";
        }
//        Log.e("inputDateStr",inputDateStr+" ");
        if(inputDateStr.contains(".")){
            inputDateStr  = inputDateStr.substring(0,inputDateStr.indexOf("."));
        }
        inputDateStr = inputDateStr.replace("Z","");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE",Locale.getDefault());
        SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        outputFormat.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String currentDate = df.format(c);
        String day = "";
        if(date!=null) {
            if (currentDate.equals(compareFormat.format(date))) {
                day = "Today";
            } else {
                day = outputFormat.format(date);
            }
        }
        return day;
    }

    public static String getCurrentDateAndTimeInUTC() {
        String currentDate = "";
        Date myDate = new Date();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDate = outputFmt.format(myDate);
        return currentDate;
    }

    public static String convertLocalDateAndTimeInUTC(String input) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.getDefault());
        inputFormatter.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = inputFormatter.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDate = outputFmt.format(date);
        Log.e("getCurrentDate=: ", currentDate);
        return currentDate;
    }

    public static String getCurrentDate() {
        String currentDate = "";
        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        outputFmt.setTimeZone(TimeZone.getDefault());
        currentDate = outputFmt.format(time);
        Log.e("getCurrentDate=: ", currentDate);
        return currentDate;
    }


    public static String gotoDateBirth(String str) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }
}
