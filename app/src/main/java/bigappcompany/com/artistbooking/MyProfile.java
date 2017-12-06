package bigappcompany.com.artistbooking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;
import bigappcompany.com.artistbooking.network.PicassoTrustAll;

public class MyProfile extends BaseActivity {
String url="http://starsunfolded.1ygkv60km.netdna-cdn.com/wp-content/uploads/2015/07/Prabhas-5.jpg";
    ImageView img;
    RippleView rp_edit;
    TextView tv_name,tv_address,tv_bio,tv_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

        tv_address=(TextView)findViewById(R.id.tv_addr);
        tv_bio=(TextView)findViewById(R.id.tv_bio);
        tv_contact=(TextView)findViewById(R.id.tv_ph);
        tv_name=(TextView)findViewById(R.id.tv_name);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");
        img= (ImageView) findViewById(R.id.img_profile);

        Download_web web=new Download_web(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                closeDialog();
                setupData(response);
            }
        });
        web.execute(ApiUrl.PRO_DETAILS
                +(getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).getString(JsonParser.CUSTOMER_ID,"")));
        showDailog();



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        ((RippleView)findViewById(R.id.rp_edit)).setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                startActivity(new Intent(MyProfile.this,EditProfileActivity.class));
            }
        });

        ((Button)findViewById(R.id.bt_requests)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this,RequestsActivity.class));
            }
        });

    }

    private void setupData(String response) {
        try {
            if(new JSONObject(response).getBoolean(JsonParser.RESPONSE_STATUS))
            {
                JSONObject data=new JSONObject(response).getJSONArray(JsonParser.DATA).getJSONObject(0);
                tv_name.setText(data.getString(JsonParser.CUST_NAME));
                tv_address.setText(data.getString(JsonParser.CUST_CTIY));
                tv_contact.setText(data.getString(JsonParser.CUST_MOB));
                url=(ApiUrl.BASE_URL)+(data.getString(JsonParser.IMG_PROFILE));
                ViewTreeObserver vto = img.getViewTreeObserver();
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        PicassoTrustAll.getInstance(getApplicationContext()).load(url).resize(img.getMeasuredWidth(), img.getMeasuredHeight()).centerCrop().into(img);
                        return true;
                    }
                });
            }
        }
        catch (JSONException je)
        {
            je.printStackTrace();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private static final String TAG = "SampleActivity";

    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(MyProfile.this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }
    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            ResultActivity.startWithUri(MyProfile.this, resultUri);
        } else {
            Toast.makeText(MyProfile.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(MyProfile.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MyProfile.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }
    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        destinationFileName += ".jpg";


        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

        uCrop.start(MyProfile.this);
    }
    private UCrop basisConfig(@NonNull UCrop uCrop) {
        uCrop = uCrop.withAspectRatio(1, 1);
        return uCrop;
    }
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();


        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(UCropActivity.DEFAULT_COMPRESS_QUALITY);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        return uCrop.withOptions(options);
    }

    ProgressDialog dialog;
    void showDailog()
    {
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading profile Information, Please Wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void closeDialog()
    {
        if(dialog!=null)
            dialog.cancel();
    }
}
