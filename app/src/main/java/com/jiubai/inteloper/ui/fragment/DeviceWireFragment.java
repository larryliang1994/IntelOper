package com.jiubai.inteloper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.bean.Device;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.config.Constants;
import com.jiubai.inteloper.net.UploadUtil;
import com.jiubai.inteloper.net.VolleyUtil;
import com.jiubai.inteloper.ui.activity.CheckPictureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by larry on 02/09/2017.
 */

public class DeviceWireFragment extends Fragment {
    @Bind(R.id.imageView)
    ImageView imageView;

    @Bind(R.id.button_addPicture)
    Button mAddPictureButton;

    private Device device;

    private String imageUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_wire, container, false);

        ButterKnife.bind(this, view);

        initView(view);

        return view;
    }

    private void initView(View view) {

        device = (Device) getArguments().getSerializable("device");

        if (TextUtils.isEmpty(device.getName())) {
            device.setName("测试1");
        }

        setupImage();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(imageUrl)) {
                    return;
                }

                ArrayList<String> pictureList = new ArrayList<>();
                pictureList.add(imageUrl);

                Intent intent = new Intent(getActivity(), CheckPictureActivity.class);
                intent.putExtra("pictureList", pictureList);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.scale_stay, R.anim.scale_stay);
            }
        });
    }

    private void setupImage() {
        String[] key = {"device_name"};
        String[] value = {device.getName()};

        VolleyUtil.request("http://dyjk.jiubaiwang.cn?_url=upload/getImageInfo", key, value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                imageUrl = jsonObject.getJSONObject("data").getString("full_img_url");

                                ImageLoader.getInstance().displayImage(imageUrl, imageView);

                                mAddPictureButton.setText("修改接线图");
                            } else {
                                mAddPictureButton.setText("+上传接线图");
                            }

                            UtilBox.dismissLoading();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "上传接线图数据源出错，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mAddPictureButton.setText("+上传接线图");
                    }
                });
    }

    @OnClick({R.id.button_addPicture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_addPicture:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);

                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                intent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);

                startActivityForResult(intent, 2);
                break;
        }
    }

    private void uploadImage(String imgPath) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "upload/image");

        UploadUtil.upload(getActivity(), params, device.getName(), imgPath, new UploadStatusDelegate() {
            @Override
            public void onProgress(Context context, UploadInfo uploadInfo) {

            }

            @Override
            public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                UtilBox.dismissLoading();

                Toast.makeText(getActivity(), "上传接线图失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());

                    String result = jsonObject.getString("code");

                    if (Constants.SUCCESS.equals(result)) {
                        imageUrl = jsonObject.getJSONObject("data").getString("full_img_url");

                        ImageLoader.getInstance().displayImage(imageUrl, imageView);
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                    UtilBox.dismissLoading();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "上传接线图数据源出错，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(Context context, UploadInfo uploadInfo) {
                Toast.makeText(getActivity(), "上传接线图失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    UtilBox.showLoading(getActivity());

                    Uri uri = data.getData();

                    String imgPath = UtilBox.getImageAbsolutePath(getActivity(), uri);

                    uploadImage(imgPath);
                }
                break;
        }
    }
}
