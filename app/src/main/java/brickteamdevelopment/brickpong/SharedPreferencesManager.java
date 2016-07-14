package brickteamdevelopment.brickpong;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    SharedPreferences           pref;
    SharedPreferences.Editor    editor;
    Context                     _context;
    int                         PRIVATE_MODE    = 0;

    private static final String PREF_NAME               =   "Pong";
    private static final String KEY_RECORD              =   "record";
    private static final String KEY_IS_FIRST_TIME       =   "isFirstTime";


    public SharedPreferencesManager(Context context) {
        this._context   =   context;
        pref            =   _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor          =   pref.edit();
    }

    public boolean isFirstTime(){return (pref.getInt(KEY_RECORD, 0) == 0)? true : false;}

    public boolean checkForRecord(int points) {

        if(points >= pref.getInt(KEY_RECORD, 0)){

            editor.putInt(KEY_RECORD, points);
            editor.commit();
            return true;
        }else 
            return false;
        
    }

    public int getRecord(){ return pref.getInt(KEY_RECORD, 0); }
}
