package map.dev.ipath;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

//import com.example.mapdemo.R;

import map.dev.ipath.constant.Constant;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by adrian on 19.03.2017.
 */

public class ForgotPasswordActivity extends Activity{

    private TextView tvForgotPassword;
    private TextView tvLogin;

    private EditText etEmail;

    KProgressHUD kpHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initWidget();

        onClickBtn();
    }

    private void initWidget() {
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        tvForgotPassword = (TextView)findViewById(R.id.tvForgotPassword);
        SpannableString content = new SpannableString("FORGOT PASSWORD");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvForgotPassword.setText(content);

        etEmail = (EditText) findViewById(R.id.etEmail);

        kpHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
//                .setLabel("Login")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
    }

    private void onClickBtn() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        kpHUD.show();
                    }
                });

                String userEMAIL = etEmail.getText().toString();

//                userEMAIL = "devapp3@outlook.com";
//                userEMAIL = "eric.petrov0123@gmail.com";
                userEMAIL = "angelmob@outlook.com";


                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("email", userEMAIL);

                asyncHttpClient.post(Constant.forgotPasswordURL, params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        kpHUD.dismiss();

                        // todo go to reset password

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        kpHUD.dismiss();

                        try {
                            JSONObject responseJsonObject = new JSONObject(responseString);
                            String stateStr = responseJsonObject.getString("state");
                            String dataString = responseJsonObject.getString("text");
                            String passString = responseJsonObject.getString("pass");


                            if(stateStr.equals("100")) {
                                showAlert(passString);
                            }
                        } catch (JSONException jsonEx) {
                            jsonEx.printStackTrace();
                        } catch (OutOfMemoryError outOfMemErr) {
                            outOfMemErr.printStackTrace();
                        }


//                        if(statusCode == 200){
//
//                        }
                        if(statusCode == 200){
//                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
//                            finish();
                        }
                    }
                });
            }
        });
    }

    private void showAlert(String content){
        final Dialog dialog =  new Dialog(ForgotPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_alert);

        TextView tvAlert = (TextView)dialog.findViewById(R.id.tvAlert);
        TextView tvClose = (TextView)dialog.findViewById(R.id.tvClose);
        tvAlert.setText(content);

        dialog.setCancelable(false);

        dialog.show();

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//            }
//        },1500);
    }
}
