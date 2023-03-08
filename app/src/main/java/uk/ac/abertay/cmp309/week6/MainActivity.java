package uk.ac.abertay.cmp309.week6;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private final String PREF_STORAGE = "prefKey_storage";
    private final String KEY_TEXT = "key_text";
    public static final String DEBUG_TAG = "L6DT";
    private SQLiteTextHelper sqLiteTextHelper;
    private FileHelper fileHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqLiteTextHelper = SQLiteTextHelper.getInstance(getApplicationContext());
        fileHelper = FileHelper.getInstance(getApplicationContext());

        int check1 = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int check2 = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean needToAsk = !((check1 == PackageManager.PERMISSION_GRANTED) & (check2 == PackageManager.PERMISSION_GRANTED));

        if(needToAsk){
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 0);
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(PREF_STORAGE))
        {
            int storageOption = savedInstanceState.getInt(PREF_STORAGE);
            Log.i(DEBUG_TAG, "Loading storage option: " + storageOption);
            ((RadioGroup)findViewById(R.id.rbgGroup)).check(storageOption);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int storageOption = ((RadioGroup)findViewById(R.id.rbgGroup)).getCheckedRadioButtonId();
        Log.i(DEBUG_TAG, "Saving storage option: " + storageOption);
        outState.putInt(PREF_STORAGE, storageOption);

        super.onSaveInstanceState(outState);
    }

    public void processClicks(View view){
        TextInputEditText email = findViewById(R.id.Emailimput);
        TextInputEditText phone = findViewById(R.id.Phoneimput);
        TextInputEditText name = findViewById(R.id.nameimput);
        String[] contact = new String[]{name.getText().toString(),email.getText().toString(),phone.getText().toString()};
        switch (view.getId()){
            case R.id.btnSave:
                saveText(contact);
                break;
            case R.id.btnLoad:
                //text.setText(loadText());
                break;
            case R.id.btnClear:
                //text.setText("");
                break;
        }
    }

    private void saveText(String []text) {
        switch(((RadioGroup)findViewById(R.id.rbgGroup)).getCheckedRadioButtonId()){
            //case R.id.rbInternal: fileHelper.saveToInternalStorage("text.txt", text); break;
            //case R.id.rbExternal: fileHelper.saveToExternalStorage("text.txt", text); break;
            case R.id.rbSQLite: saveToSQLite(text); break;
            //case R.id.rbSharedPrefs: saveToSharedPrefs(text); break;
        }
    }

    private String loadText() {
        switch(((RadioGroup)findViewById(R.id.rbgGroup)).getCheckedRadioButtonId()){
            case R.id.rbInternal: return fileHelper.loadFromInternalStorage("text.txt");
            case R.id.rbExternal: return fileHelper.loadFromExternalStorage("text.txt");
            case R.id.rbSQLite: return loadFromSQLite();
            case R.id.rbSharedPrefs: return loadFromSharedPrefs();
            default: return "NOTHING LOADED!";
        }
    }

    private void saveToSharedPrefs(String text){
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TEXT, text);
        editor.commit();
    }

    private String loadFromSharedPrefs(){
        SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
        return prefs.getString(KEY_TEXT, "NO TEXT FOUND!");
    }

    private void saveToSQLite(String[] text){
        sqLiteTextHelper.saveText(text);
    }

    private String loadFromSQLite(){
        return sqLiteTextHelper.loadText();
    }
}