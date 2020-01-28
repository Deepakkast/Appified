package app.appified.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.zxing.common.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static app.appified.Utils.StringUtils.isEmpty;

//import UserApp.appified.UserApp.appified.Config.App;

/**
 * Created by Radhey on 17/05/18.
 *
 * @author Radhey
 */
public class CommonUtils {

    public static final boolean isDebug = true;
    public static final String DATA_DIR = "Voicera";
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //public static final String EMAIL_PATTERN = "[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,4}";


    public static boolean isExtStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static void createDataDirIfNotExists() {
        if (isExtStorageAvailable()) {
            File root = Environment.getExternalStorageDirectory();
            File dataDir = new File(root, DATA_DIR);
            if (!(dataDir.exists() && dataDir.isDirectory())) {
                dataDir.mkdirs();
            }
        }
    }

    public static void LogMsg(String tag, String message) {
        if (isDebug) {
            Log.d(tag, message);
        }
    }

    public static boolean validateURL(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    public static String getDataDir() {
        createDataDirIfNotExists();
        File dataDir = new File(Environment.getExternalStorageDirectory(), DATA_DIR);
        return dataDir.getAbsolutePath();
    }




    public static void hideKeyboard(Activity activity, EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static String formatDate(String date) {

        SimpleDateFormat spf = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss 'GMT+0530 (IST)'", Locale.US);

        Date newDate = null;

        try {

            newDate = spf.parse(date);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        spf = new SimpleDateFormat("d MMM, yyyy", Locale.US);

        return spf.format(newDate);

    }




    public static boolean validateEmail(String email) {
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }




    public static SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    public static SimpleDateFormat dateFormatForEvent = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public static SimpleDateFormat dateFormatForDay = new SimpleDateFormat("dd MMM yyyy hh:mm", Locale.getDefault());

	/*public static Dialog getProgressDialog(Activity activity) {
		Dialog dialog = new Dialog(activity);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog_progress);
		return dialog;
	}*/


    public static String getFontName(int index) {
        String font = "MavenPro-Regular.ttf";
        switch (index) {
            case 2:
                font = "MavenPro-Bold.ttf";
                break;
            case 3:
                font = "MavenPro-Medium.ttf";
                break;
        }
        return font;
    }


    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static void copyFile(File src, File dest) throws IOException, NullPointerException {
        if (null == src || null == dest) {
            throw new NullPointerException("Source or Destination is null");
        }
        File parentFile = dest.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination directory cannot be created");
            }
        }
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        sourceChannel = new FileInputStream(src).getChannel();
        destChannel = new FileOutputStream(dest).getChannel();
        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        sourceChannel.close();
        destChannel.close();
    }

    public static String getFormattedDateForDeadLine(Date date) {
        String dateFormatted = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            dateFormatted = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormatted;
    }


	/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static long getDate(String time) {
        //2017-10-15T07:31:32.189753
        if (!app.appified.Utils.StringUtils.isEmpty(time)) {
            try {
                SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.ENGLISH);
                return (sdfServer.parse(time).getTime() / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return (System.currentTimeMillis() / 1000);
    }

    public static String getMonthString(String date) {
        return getMonthId(Integer.parseInt(getMonth(date)));
    }

    private static String getMonth(String date) {
        if (!app.appified.Utils.StringUtils.isEmpty(date)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.ENGLISH);
                //2017-10-25T11:00:30
                SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                return sdf.format(sdfServer.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getDateString(String date) {
        if (!app.appified.Utils.StringUtils.isEmpty(date)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.ENGLISH);
                //2017-10-25T11:00:30
                SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                return sdf.format(sdfServer.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getYearString(String date) {
        if (!app.appified.Utils.StringUtils.isEmpty(date)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                //2017-10-25T11:00:30
                SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                return sdf.format(sdfServer.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    private static String getMonthId(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }

    public static boolean getValidFileType(String extension) {
        String[] fileTypes = {".ppt", ".PPT", ".PPTX", ".pptx", ".jpg", ".JPG", ".jpeg", ".JPEG", ".png", ".PNG", ".bmp", ".BMP", ".gif", ".GIF", ".pdf", ".PDF", ".doc", ".DOC", ".docx", ".txt", ".xls", ".xlsx"};
        if (Arrays.asList(fileTypes).contains(extension)) {
            return true;
        }
        return false;
    }

    public static boolean getImage(String extension) {
        String[] fileTypes = {"jpg", "JPG", "PPT", "ppt", "jpeg", "JPEG", "png", "PNG", "bmp", "BMP", "gif", "GIF"};
        if (Arrays.asList(fileTypes).contains(extension)) {
            return true;
        }
        return false;
    }


    public static String getFileExtension(String fileNameWithExtension) {
        String extension = null;
        extension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf(".") + 1);
//        extension =".jpg";
//        extension +="."+extension;
        if (extension.equals("pdf") || extension.equals("PDF")) {
            return "PDF";
        }
        if (extension.equals("txt") || extension.equals("TXT")) {
            return "TEXT";
        }
        if (extension.equals("xls") || extension.equals("xlsx") || extension.equals("XLS") || extension.equals("XLSX")) {
            return "EXCEL";
        }
        if (extension.equals("doc") || extension.equals("docx") || extension.equals("DOC") || extension.equals("DOCX")) {
            return "WORD";
        }
        if (extension.equals("ppt") || extension.equals("PPT") || extension.equals("PPTX") || extension.equals("pptx")) {
            return "POWERPOINT";
        }
        if (getImage(extension)) {
            return "IMAGE";
        }
        return extension;
    }


    public static String getDateStr(String date) {
        if (!app.appified.Utils.StringUtils.isEmpty(date)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                //2017-10-25T11:00:30
                SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                return sdf.format(sdfServer.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String timeConvert(String time) {
//			String[] parts = string.split("-", 2);
//			String part1 = parts[0]; // 004
//			String part2 = parts[1]; // 034556-42

        String timeConverted = null;
        if (time.contains(":")) {
            String[] timeSet = time.split(":");
            timeConverted = timeSet[0];
            if (Integer.parseInt(timeConverted) <= 12) {
                timeConverted += ":" + timeSet[1] + " AM";
            } else {
                timeConverted = String.valueOf(Integer.parseInt(timeConverted) - 12);
                timeConverted += ":" + timeSet[1] + " PM";
            }
        }
        return timeConverted;
    }

    public static String getTimeConvert(String time) {
        String timeConverted = null;
        if (time.contains(":")) {
            String[] timeSet = time.split(":");
            timeConverted = timeSet[0];
            if (Integer.parseInt(timeConverted) <= 12) {
                timeConverted += ":" + timeSet[1];
            } else {
                timeConverted = String.valueOf(Integer.parseInt(timeConverted) - 12);
                timeConverted += ":" + timeSet[1];
            }
        }
        return timeConverted;
    }

    public static String getTimeToAmPm(String time) {
        String timeConverted = null;
        try {
            if (time.contains(":")) {
                String[] timeSet = time.split(":");
                timeConverted = timeSet[0];
                if (Integer.parseInt(timeConverted) <= 12) {
                    timeConverted = "AM";
                } else {
                    timeConverted = "PM";
                }
            }
        } catch (Exception e) {
            timeConverted = "";
        }
        return timeConverted;
    }

    public static boolean isAllowAttendance(String time) {
        String now = new SimpleDateFormat("HH:mm").format(new Date().getTime());

        if (time.compareTo(now) > 0) {
            System.out.println("start is after end" + time.compareTo(now));
            return false;
        } else if (time.compareTo(now) < 0 || time.compareTo(now) == 0) {
            System.out.println("start is before end" + time.compareTo(now));
            return true;
        } else {
            System.out.println("Something weird happened...");
            return false;
        }
    }

    public static String timeConvertInAmPm(String time) {
        if (time.contains(":")) {

        }
        return null;
    }

    public static String getFileName(String url) {
        String filenameWithExtension = null;
        filenameWithExtension = url.substring(url.lastIndexOf("/") + 1);
        if (filenameWithExtension == null) {
            return null;
        }
        return filenameWithExtension;
    }

    public static String getFileNameWithExtension(String url) {
        String filenameWithExtension = null;
        filenameWithExtension = url.substring(url.lastIndexOf("&") + 1);
        if (filenameWithExtension == null) {
            return null;
        }
        return filenameWithExtension;
    }

    public static String getTimeStr(String date) {
        if (!app.appified.Utils.StringUtils.isEmpty(date)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                //2017-10-25T11:00:30
                SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
//				Log.d("Chat", sdf.format((sdfServer.parse(date).getTime())).toString());
                return sdf.format(sdfServer.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "ERROR";
    }

    public static String getTimeStrWithDays(String date) {
        if (!app.appified.Utils.StringUtils.isEmpty(date)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                //2017-10-25T11:00:30
                SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//				Log.d("Chat", sdf.format((sdfServer.parse(date).getTime())).toString());
                return sdf.format(sdfServer.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "ERROR";
    }

    public static String getTimeStrWithStr(String date) {
        if (!app.appified.Utils.StringUtils.isEmpty(date)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //2017-10-25T11:00:30
                SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//				Log.d("Chat", sdf.format((sdfServer.parse(date).getTime())).toString());
                return sdf.format(sdfServer.parse(date).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }



    public static  String formatDates(String date)
    {
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        Date newDate=null;
        try {
            newDate =spf.parse(date);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        spf=new SimpleDateFormat("d MMM,yyyy",Locale.US);
        return spf.format(newDate);
    }

    public static String epochToDate(String epoch) {

        long timestamp = Long.parseLong(epoch);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy",Locale.US);
        return sdf.format(new Date(timestamp));
         }

    public static String getLocalDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat formatDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        return  formatDate.format(cal.getTime());
    }

    public static String  getServerDate(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return  formatDate.format(cal.getTime());
    }

    public static Date getDateToString(String date) {
//        t='2018-05-04T14:00:31',
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        try {
            date1 = formatDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date1;
    }

    public static Date getOnlyDateToString(String date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        try {
            date1 = formatDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date1;
    }


    public static String getOnlyDateToString(Long date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        return  formatDate.format(new Date(date));
    }

//    long val = 1346524199000l;
    public static String getLongDataToStringData(long longDate){
        Date date=new Date(longDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(date);
        return dateText;
    }

    public static String Format(Integer number){
        String[] suffix = new String[]{"K","M","B","T"};
        int size = (number.intValue() != 0) ? (int) Math.log10(number) : 0;
        if (size >= 3){
            while (size % 3 != 0) {
                size = size - 1;
            }
        }
        double notation = Math.pow(10, size);
        String result = ((size >= 3) ? + (Math.round((number / notation) * 100) / 100.0d)+suffix[(size/3) - 1] : + number + "")+"+";
        System.out.println(result);
        return result;
    }

}
