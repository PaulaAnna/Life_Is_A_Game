package intlig.lifeisagame.Classes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by benjamindeutinger on 03.04.16.
 */
public class Constants {
    public static final String FIREBASE_URL = "https://blinding-heat-2666.firebaseio.com/";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final int AUTHENTICATION_POSSIBLE_TRIES = 10;
    public static final String MAP_RES_DIR_PATH= Environment.getExternalStorageDirectory().getPath()+ "/" + "SKMaps/";

    public static boolean isEmptyEditText(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public static boolean isValidEmail(String enteredEmail){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(enteredEmail);
        return ((!enteredEmail.isEmpty()) && (matcher.matches()));
    }

    public static void getAlertDialog(String msg, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
