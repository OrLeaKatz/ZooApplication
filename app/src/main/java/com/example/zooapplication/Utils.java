package com.example.zooapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class Utils {
    public static boolean isValidEmail(String str) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static String readStringFromRawResourceFile(Context ctx, String filenameWithoutExtension) {
        StringBuilder contents = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");

        try { // try w. resources is min. 18
            InputStream ins = ctx.getResources().openRawResource(
                    ctx.getResources().getIdentifier(filenameWithoutExtension,
                            "raw", ctx.getPackageName()));
            BufferedReader input =  new BufferedReader(new InputStreamReader(ins), 1024*8);

            try {
                String line;

                while (( line = input.readLine()) != null){
                    contents.append(line);
                    contents.append(lineSeparator);
                }
            }
            finally {
                input.close();
            }
        }
        catch (FileNotFoundException ex) {
            Log.e(TAG, "could not find file " + filenameWithoutExtension  + " " + ex);
            return null;
        }
        catch (IOException ex){
            Log.e(TAG, "Error reading file " + filenameWithoutExtension + " " + ex);
            return null;
        }

        return contents.toString();
    }

    public static String extractString(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8),1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static void showToastLong(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static Drawable getDrawableByName(String name, Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(name, "drawable",
                context.getPackageName());
        return resources.getDrawable(resourceId);
    }

    // returns server response in string format
    public static String sendHttpRequest(final String url) throws IOException {
        HttpURLConnection urlConnection = null;

        try {
            URL urlObject = new URL(url);
            urlConnection = (HttpURLConnection) urlObject.openConnection();

            final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String input = Utils.extractString(in);

            return input;
        }
        finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        // get currently focused view
        View view = activity.getCurrentFocus();

        //no view has focus - create a new one, in order to grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
