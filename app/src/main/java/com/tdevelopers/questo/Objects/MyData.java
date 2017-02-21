package com.tdevelopers.questo.Objects;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import com.facebook.Profile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tdevelopers.questo.MyApplication;
import com.tdevelopers.questo.Pushes.GcmServerSideSender;
import com.tdevelopers.questo.Pushes.LoggingService;
import com.tdevelopers.questo.Pushes.Message;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyData {

    private static final String DEEP_LINK_URL = "https://questoapp.com";
    private static final String app_code = "vpz9b";
    final public static String apiname = "Your API Key";
    public static int type = 0;
    public static Context context = MyApplication.getInstance().getApplicationContext();
    public static HashSet<String> tags = new HashSet<>();

    public static int exsist = 0;
    public static TabLayout liketab;
    public static ImageView notfound;
    public static int pageflag = 0;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static Uri buildDeepLink(String category, String id) {
        // Get the unique appcode for this app.


        Uri deepLink = Uri.parse(DEEP_LINK_URL + "/" + category + "/" + id);
        String appCode = app_code;

        // Get this app's package name.
        String packageName = getApplicationContext().getPackageName();

        // Build the link with all required parameters
        Uri.Builder builder = new Uri.Builder()
                .scheme("https")
                .authority(appCode + ".app.goo.gl")
                .path("/")
                .appendQueryParameter("link", deepLink.toString())
                .appendQueryParameter("apn", packageName);


        // Return the completed deep link.
        return builder.build();
    }

    public static TabLayout getLiketab() {
        return liketab;
    }

    public static void setLiketab(TabLayout liketab) {
        MyData.liketab = liketab;
    }

    public static String toCamelCase(final String init) {
        if (init == null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(init);


        while (matcher.find()) {
            String word = matcher.group();
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == init.length()))
                ret.append(" ");
        }

        return ret.toString().trim();
    }

    public static ImageView getNotfound() {
        return notfound;
    }

    public static void setNotfound(ImageView notfound) {
        MyData.notfound = notfound;
    }

    public static void pushNotification(String title, String body, final String to, final String link, String userid) {
        context = MyApplication.getInstance().getApplicationContext();

        if (context != null && !userid.equals(Profile.getCurrentProfile().getId())) {
            final Message.Builder messageBuilder = new Message.Builder();
            messageBuilder.notificationTitle(title + "");
            messageBuilder.notificationBody(Profile.getCurrentProfile().getName() + " " + body);
            //Date date = new Date(System.currentTimeMillis());


            Date date = new Date(System.currentTimeMillis());

            messageBuilder.addData("created_at", (date.getTime() + ""));
            messageBuilder.addData("userid", (Profile.getCurrentProfile().getId()));
            messageBuilder.addData("username", (Profile.getCurrentProfile().getName()));
            messageBuilder.addData("link", link);

            if (!link.startsWith("Message:")) {
                DatabaseReference n = FirebaseDatabase.getInstance().getReference("notifications").child(userid + "").push();
                n.child("userid").setValue(Profile.getCurrentProfile().getId());
                n.child("title").setValue(title);
                n.child("message").setValue(Profile.getCurrentProfile().getName() + " " + body);
                n.child("created_at").setValue(date.getTime());
                n.child("username").setValue(Profile.getCurrentProfile().getName());
                n.child("link").setValue(link);
                n.setPriority(date.getTime() * -1L);
            } else {


            }

            final LoggingService.Logger mLogger = new LoggingService.Logger(context);
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {

                    GcmServerSideSender sender = new GcmServerSideSender(apiname, mLogger);


                    try {
                        sender.sendHttpJsonDownstreamMessage(to, messageBuilder.build());

                    } catch (final IOException e) {
                        return e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    if (result != null) {
                    }
                }
            }.execute();

        }
    }


    public static void pushNotification(String title, String body, final String to, final String link, String userid, String pagename, String pageid, String pagepic) {
        context = MyApplication.getInstance().getApplicationContext();

        if (context != null && !userid.equals(Profile.getCurrentProfile().getId())) {
            final Message.Builder messageBuilder = new Message.Builder();
            messageBuilder.notificationTitle(title + "");
            messageBuilder.notificationBody(Profile.getCurrentProfile().getName() + " " + body);
            //Date date = new Date(System.currentTimeMillis());


            Date date = new Date(System.currentTimeMillis());

            messageBuilder.addData("created_at", (date.getTime() + ""));
            messageBuilder.addData("userid", (Profile.getCurrentProfile().getId()));
            messageBuilder.addData("username", (Profile.getCurrentProfile().getName()));
            messageBuilder.addData("link", link);

            if (!link.startsWith("Message:")) {
                DatabaseReference n = FirebaseDatabase.getInstance().getReference("notifications").child(userid + "").push();
                n.child("userid").setValue(pageid);
                n.child("title").setValue(title);
                n.child("message").setValue(pagename + " " + body);
                n.child("created_at").setValue(date.getTime());
                n.child("username").setValue(Profile.getCurrentProfile().getName());
                n.child("link").setValue(link);
                n.child("pagepic").setValue(pagepic);
                n.setPriority(date.getTime() * -1L);
            } else {


            }

            final LoggingService.Logger mLogger = new LoggingService.Logger(context);
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {

                    GcmServerSideSender sender = new GcmServerSideSender(apiname, mLogger);


                    try {
                        sender.sendHttpJsonDownstreamMessage(to, messageBuilder.build());

                    } catch (final IOException e) {
                        return e.getMessage();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    if (result != null) {
                    }
                }
            }.execute();

        }
    }

    public static boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        context = MyApplication.getInstance().getApplicationContext();
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            return haveConnectedWifi || haveConnectedMobile;
        }
        return true;
    }

    public static int getType() {
        return type;
    }

    public static void setType(int type) {
        MyData.type = type;
    }

    public static HashSet<String> getTags() {
        return tags;
    }

    public static void setTags(HashSet<String> tags) {
        MyData.tags = tags;
    }

}
