package com.example.zooapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static void showToastLong(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static Drawable getDrawableByName(String name, Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(name, "drawable",
                context.getPackageName());
        return resources.getDrawable(resourceId);
    }
}
