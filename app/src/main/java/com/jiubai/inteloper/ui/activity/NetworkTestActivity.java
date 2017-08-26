package com.jiubai.inteloper.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.common.DataTypeConverter;
import com.jiubai.inteloper.net.RequestUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NetworkTestActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.textView_response)
    TextView mResponseTextView;

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
    }

    @OnClick({R.id.button_send_request, R.id.button_listen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send_request:
                mProgressDialog.show();

                mResponseTextView.setText("response");

                byte[] bid = DataTypeConverter.int2byte(1); // 操作码
                byte[] bnum = DataTypeConverter.int2byte(3); // 消息数
                Charset cs = Charset.forName("UTF-8");
                byte[] b1 = "122160226379053350".getBytes(cs);
                byte[] b2 = "122160226378973383".getBytes(cs);
                byte[] b3 = "115404869887590405".getBytes(cs);

                // 把所有字节合并成一条
                byte[] bdata = DataTypeConverter.concatAll(bid, bnum, b1, b2, b3);

                final int requestMsgLength = 8 + 32 + 4 + 4;

                RequestUtil.request(bdata, 2, requestMsgLength, false, new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {
                        byte[] id = new byte[8];
                        byte[] desrc = new byte[32];
                        byte[] value = new byte[4];
                        byte[] v = new byte[4];

                        final StringBuffer buffer = new StringBuffer();

                        for (int i = 0; i < msgNum; i++) {
                            id = DataTypeConverter.readBytes(msgContent, i * requestMsgLength, 8);
                            desrc = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 8, 32);
                            value = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 8 + 32, 4);
                            v = DataTypeConverter.readBytes(msgContent, i * requestMsgLength + 8 + 32 + 4, 4);

                            try {
                                buffer.append(  "id:    ").append(DataTypeConverter.byte2long(id) + "").append("\n")
                                        .append("desrc: ").append(new String(desrc, "GBK")).append("\n")
                                        .append("value: ").append(DataTypeConverter.byte2int(value) + "").append("\n")
                                        .append("v:     ").append(DataTypeConverter.byte2float(v) + "").append("\n");

                                System.out.println("id: " + DataTypeConverter.byte2long(id));
                                System.out.println("desrc: " + new String(desrc, "GBK"));
                                System.out.println("value: " + DataTypeConverter.byte2int(value));
                                System.out.println("v: " + DataTypeConverter.byte2float(v));

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResponseTextView.setText(buffer.toString());

                                mProgressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void error(String info, final Exception exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResponseTextView.setText(exception.toString());

                                mProgressDialog.dismiss();
                            }
                        });
                    }
                });

                break;

            case R.id.button_listen:
                mResponseTextView.setText("response");

                // 如果正在监听，就停下来
                if (RequestUtil.keepListen) {
                    RequestUtil.keepListen = false;

                    return;
                }

                final int listenMsgLength = 4 + 20 + 500;

                RequestUtil.listen(2, listenMsgLength, new RequestUtil.RequestCallback() {
                    @Override
                    public void success(int msgNum, byte[] msgContent) {
                        byte[] status = new byte[4];
                        byte[] occur_time = new byte[20];
                        byte[] warn_str = new byte[500];

                        final StringBuffer buffer = new StringBuffer();

                        for (int i = 0; i < msgNum; i++) {
                            status = DataTypeConverter.readBytes(msgContent, i * listenMsgLength, 4);
                            occur_time = DataTypeConverter.readBytes(msgContent, i * listenMsgLength + 4, 20);
                            warn_str = DataTypeConverter.readBytes(msgContent, i * listenMsgLength + 4 + 20, 500);
                        }

                        buffer.append("\n").append("msgNum:").append(msgNum + "");

                        final String displayText = mResponseTextView.getText() + buffer.toString();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResponseTextView.setText(displayText);

                                mProgressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void error(String info, final Exception exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResponseTextView.setText(exception.toString());

                                mProgressDialog.dismiss();
                            }
                        });
                    }
                });
                break;
        }
    }
}
