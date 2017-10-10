package bigappcompany.com.artistbooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONException;
import org.json.JSONObject;

import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;

public class RegisterActivity extends AppCompatActivity {
    EditText et_name,et_phone,et_email;
    AutoCompleteTextView at_city;
    Button bt_submit;
    private boolean canSubmit;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_name = (EditText) findViewById(R.id.txt_name);
        et_email = (EditText) findViewById(R.id.txt_email);
        et_phone = (EditText) findViewById(R.id.txt_mobile);
        at_city = (AutoCompleteTextView) findViewById(R.id.txt_city);

        String phone = getIntent().getStringExtra(JsonParser.CUST_MOB);
        if (phone!=null)
        {
            et_phone.setText(phone);
            et_phone.setFocusable(false);
        }
        bt_submit=(Button)findViewById(R.id.bt_sign);

        sp=getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canSubmit=true;
                check(et_name);
                check(et_email);
                check(et_phone);
                if(at_city.getText().toString().trim().equals(""))
                {
                    at_city.setError("Empty");
                    canSubmit=false;
                }
                if(!isValidEmail(et_email.getText().toString().trim())) {
                    canSubmit = false;
                    et_email.setError("Invalid Email");
                }
                if(canSubmit)
                {
                    try {
                        Download_web web=new Download_web(RegisterActivity.this, new OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(String response) {
                                try {
                                    closeDialog();
                                    if(new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS)) {
                                        SharedPreferences.Editor edit = sp.edit();
                                        edit.putBoolean(JsonParser.GO,true);
                                        edit.putString(JsonParser.CUST_NAME, et_name.getText().toString().trim());
                                        edit.putString(JsonParser.CUSTOMER_ID, new JSONObject(response).getJSONObject(JsonParser.DATA).getString(JsonParser.CUSTOMER_ID));
                                        edit.commit();

                                        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Unable to register",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        JSONObject object=new JSONObject();
                        object.put(JsonParser.DEVICE_ID,FirebaseInstanceId.getInstance().getToken());
                        object.put(JsonParser.CUST_NAME,et_name.getText().toString());
                        object.put(JsonParser.CUST_CTIY,at_city.getText().toString());
                        object.put(JsonParser.CUST_EMAIL,et_email.getText().toString());
                        object.put(JsonParser.CUST_MOB,et_phone.getText().toString());

                        web.setReqType(false);
                        web.setData(object.toString());
                        web.execute(ApiUrl.REGISTER);
                        showDailog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void check(EditText et) {
        if(et.getText().toString().trim().equals("")) {
            canSubmit = false;
            et.setError("Empty");
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    ProgressDialog dialog;
    void showDailog()
    {
        dialog=new ProgressDialog(this);
        dialog.setMessage("Saving Data, Please Wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void closeDialog()
    {
        if(dialog!=null)
            dialog.cancel();
    }
}
