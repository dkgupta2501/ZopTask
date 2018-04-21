package myapp.com.zoptask.mylibrary;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Dhananjay Gupta on 3/5/2018.
 */

public class CustomFontsLoader {
    public static final int ROBOTO_BOLD =   0;
    public static final int ROBOTO_LIGHT =   1;
    public static final int ROBOTO_REGULAR =   2;
    public static final int HELVETICALTSTD_LIGHT=    3;

    private static final int NUM_OF_CUSTOM_FONTS = 4;

    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[4];

    private static String[] fontPath = {
            "font/Roboto-Bold.ttf",
            "font/Roboto-Light.ttf",
            "font/Roboto-Regular.ttf",
            "font/HelveticaLTStd-Light.otf"
    };


    /**
     * Returns a loaded custom font based on it's identifier.
     *
     * @param context - the current context
     * @param fontIdentifier = the identifier of the requested font
     *
     * @return Typeface object of the requested font.
     */
    public static Typeface getTypeface(Context context, int fontIdentifier) {
        if (!fontsLoaded) {
            loadFonts(context);
        }
        return fonts[fontIdentifier];
    }


    private static void loadFonts(Context context) {
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        fontsLoaded = true;

    }
}
