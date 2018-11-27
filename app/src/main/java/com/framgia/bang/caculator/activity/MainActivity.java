package com.framgia.bang.caculator.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.framgia.bang.caculator.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "aaa";
    private static final String SAVE = "SAVE";
    private static final String SHARE = "SHARE";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private static Double tmp, result = 0.0;
    public EditText txt_show;
    private String number = "";
    boolean mAdd, mDiv, mMulti, mSub, mPer, mNev = false;
    private int mClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        share();
    }

    private void share() {
        mPreferences = getSharedPreferences(SHARE, Context.MODE_PRIVATE);
        String number = mPreferences.getString(SAVE, "");
        Log.d(TAG, "share: "+number);
        txt_show.setText(number);
    }

    private void addControls() {
        int[] idButton = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6,
                R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_result, R.id.btn_percent, R.id.btn_nev,
                R.id.btn_div, R.id.btn_dot, R.id.btn_multi, R.id.btn_clear, R.id.btn_sub,
                R.id.btn_add
        };
        txt_show = findViewById(R.id.txt_show);
        for (int id : idButton) {
            View v = findViewById(id);
            v.setOnClickListener(this);
        }
    }

    private void math(String symbol) {
        addControls();

        mClick++;
        switch (symbol) {
            case "+":
                math("=");
                mAdd = true;
                //result = Double.parseDouble(txt_show.getText().toString());
                result = result + tmp;
                number = "";
                break;
            case "-":
                math("=");
                mSub = true;
                if (mClick == 1) {
                    result = tmp;
                } else {
                    result = result - tmp;
                }
                number = "";
                break;
            case "*":
                math("=");
                mMulti = true;
                if (mClick == 1) {
                    result = tmp;
                } else {
                    result = result * tmp;
                }
                number = "";
                break;
            case "/":
                mDiv = true;
                if (mClick == 1) {
                    result = tmp;
                } else {
                    math("=");
                    result = result / tmp;
                }
                Log.d(TAG, "result: " + result);
                number = "";
                break;
            case "%":
                mPer = true;
                break;
            case "=":
                if (mAdd) {
                    result = result + tmp;
                    tmp = 0.0d;
                    symbol();
                }
                if (mSub) {
                    result = result - tmp;
                    tmp = 0.0d;
                    mClick = 0;
                    symbol();
                }
                if (mDiv) {
                    result = result / tmp;
                    Log.d(TAG, "math: " + result);
                    mClick = 0;
                    tmp = 0.0d;
                    symbol();
                }
                if (mMulti) {
                    result = result * tmp;
                    mClick = 0;
                    symbol();
                }
                if (mPer) {
                    String replace = number.replace("%", "");
                    int count = 0;
                    for (int i = 0; i < number.length(); i++) {
                        if (number.charAt(i) == '%') {
                            count++;
                        }
                        tmp = Double.parseDouble(replace) / (Math.pow(100, count));
                    }
                    symbol();
                }

                if (result == 0) {
                    result = tmp;
                }
                txt_show.setText(String.valueOf(result));
                tmp = 0.0d;
                number = "";
                Log.d(TAG, "math: " + result);
                //result = 0.0d;
                break;
        }
    }

    private void symbol() {
        mAdd = false;
        mPer = false;
        mMulti = false;
        mDiv = false;
        mSub = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                math("+");
                break;
            case R.id.btn_sub:
                math("-");
                break;
            case R.id.btn_multi:
                math("*");
                break;
            case R.id.btn_div:
                math("/");
                break;
            case R.id.btn_clear:
                clear();
                break;
            case R.id.btn_result:
                math("=");
                break;
            case R.id.btn_nev:
                String nev = "-";
                if (number.length() > 1) {
                    if (!mNev) {
                        txt_show.setText(nev.concat(number));
                        number = nev.concat(number);
                        tmp = Double.parseDouble(number);
                        mNev = true;
                    } else {
                        number = number.substring(0, 0) + number.substring(1);
                        txt_show.setText(number);
                        tmp = Double.parseDouble(number);
                        mNev = false;
                    }
                } else {
                    if (!mNev) {
                        number = nev.concat(number);
                        txt_show.setText(number);
                        mNev = true;
                    } else {
                        number = number.replace("-", "");
                        txt_show.setText(number);
                        mNev = false;
                    }
                }

                break;
            case R.id.btn_percent:
                String per = "%";
                if (number.length() > 0) {
                    number = number.concat(per);
                    txt_show.setText(number);
                    math("%");
                }
                break;
            case R.id.btn_dot:
                String dot = ".";
                if (!number.contains(dot)) {
                    number = number.concat(dot);
                    txt_show.setText(number);
                }
                break;
            default:
                if (number == null) {
                    number = "";
                } else {
                    number = number + ((Button) v).getText().toString();
                    txt_show.setText(number);
                    tmp = Double.parseDouble(number);
                }
        }
    }

    private void clear() {
        number = "";
        result = 0.0d;
        mClick = 0;
        mNev = false;
        txt_show.setText(number);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                clear();
                mEditor.clear();
                break;
            case R.id.save_result:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        mPreferences = getSharedPreferences(SHARE, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(SAVE, String.valueOf(result));
        mEditor.commit();
        Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
    }
}
