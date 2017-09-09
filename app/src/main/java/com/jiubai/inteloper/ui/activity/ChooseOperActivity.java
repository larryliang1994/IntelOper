package com.jiubai.inteloper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiubai.inteloper.R;
import com.jiubai.inteloper.adapter.DeviceListAdapter;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.widget.IndexBar;
import com.jiubai.inteloper.widget.RippleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by larry on 02/09/2017.
 */

public class ChooseOperActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.ripple_oper1)
    RippleView mOper1Ripple;

    @Bind(R.id.ripple_oper2)
    RippleView mOper2Ripple;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_oper);

        ButterKnife.bind(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseOperActivity.this.onBackPressed();
            }
        });

        mOper1Ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                IndexBar.offset = UtilBox.dip2px(ChooseOperActivity.this, 50);

                Intent intent = new Intent(ChooseOperActivity.this, DeviceListActivity.class);
                intent.putExtra("source", DeviceListActivity.SOURCE_STATION);
                UtilBox.startActivity(ChooseOperActivity.this, intent, false);
            }
        });

        mOper2Ripple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                IndexBar.offset = UtilBox.dip2px(ChooseOperActivity.this, 0);

                Intent intent = new Intent(ChooseOperActivity.this, DeviceListActivity.class);
                intent.putExtra("source", DeviceListActivity.SOURCE_LIMIT);
                UtilBox.startActivity(ChooseOperActivity.this, intent, false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }
}
