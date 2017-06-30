package com.jiubai.inteloper.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.config.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hprose.client.HproseHttpClient;
import hprose.common.HproseCallback1;
import hprose.common.HproseErrorEvent;

public class NetworkTestActivity extends AppCompatActivity {

    @Bind(R.id.editText_url)
    EditText mUrlEditText;

    @Bind(R.id.editText_port)
    EditText mPortEditText;

    @Bind(R.id.editText_method)
    EditText mMethodEditText;

    @Bind(R.id.editText_parameters)
    EditText mParamEditText;

    @Bind(R.id.textView_response)
    TextView mResponseTextView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("请求中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        mUrlEditText.setText(Constants.SERVER_URL);

        mPortEditText.setText(Constants.PORT);

        mMethodEditText.setText("hello");

        mParamEditText.setText("name");
    }

    @OnClick({R.id.button_send_request})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send_request:
                mProgressDialog.show();
                try {
                    String url = mUrlEditText.getText().toString();

                    if (!TextUtils.isEmpty(mPortEditText.getText().toString())) {
                        url += ":" + mPortEditText.getText().toString();
                    }

                    HproseHttpClient client = new HproseHttpClient(url);
                    client.setTimeout(10000);
                    client.invoke(mMethodEditText.getText().toString(), new Object[]{mParamEditText.getText().toString()},
                            new HproseCallback1<String>() {
                                @Override
                                public void handler(String o) {
                                    mProgressDialog.dismiss();
                                    Log.i("intel success", o);
                                    mResponseTextView.setText("success" + "\n" + o);
                                }
                            },
                            new HproseErrorEvent() {
                                @Override
                                public void handler(String s, Throwable throwable) {
                                    mProgressDialog.dismiss();
                                    Log.i("intel error", s + " : " + throwable.toString());
                                    mResponseTextView.setText("error" + "\n" + throwable.toString());
                                }
                            });
                } catch (Exception exception) {
                    mProgressDialog.dismiss();

                    exception.printStackTrace();

                    Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
