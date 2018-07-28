package com.posin.systemupdate.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.posin.systemupdate.R;
import com.posin.systemupdate.base.BaseDialog;
import com.posin.systemupdate.bean.UpdateDetail;
import com.posin.systemupdate.utils.DensityUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * FileName: ConfirmDownloadDialog
 * Author: Greetty
 * Time: 2018/7/23 16:31
 * Desc: TODO
 */
public class ConfirmDownloadDialog extends BaseDialog {

    @BindView(R.id.tv_current_version)
    TextView tvCurrentVersion;
    @BindView(R.id.tv_update_version)
    TextView tvUpdateVersion;
    @BindView(R.id.tv_upload_time)
    TextView tvUploadTime;
    @BindView(R.id.tv_update_message)
    TextView tvUpdateMessage;

    private static final String TAG = "ConfirmDownloadDialog";

    private Context mContext;
    private String mCurrentVersion;
    private boolean isSpk;
    private UpdateDetail mUpdateDetail;
    private ConfirmDialogListener mConfirmDialogListener;

    public ConfirmDownloadDialog(Context context, String currentVersion, boolean isSpk,
                                 UpdateDetail updateDetail, ConfirmDialogListener confirmDialogListener) {
        super(context);
        this.mContext = context;
        this.mCurrentVersion = currentVersion;
        this.isSpk = isSpk;
        this.mUpdateDetail = updateDetail;
        this.mConfirmDialogListener = confirmDialogListener;
    }


    @Override
    public int getLayoutId() {
        return R.layout.alert_confirm;
    }

    @Override
    public void initData() {
        //修改dialog弹框大小
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;

        lp.width = DensityUtils.dp2px(mContext, 600);
        lp.height = DensityUtils.dp2px(mContext, 367);
        this.getWindow().setAttributes(lp);

        setCancelable(false);

        tvCurrentVersion.setText(mCurrentVersion);
        tvUpdateVersion.setText(mUpdateDetail.getVersion());
        tvUploadTime.setText(mUpdateDetail.getUploaddate());
        tvUpdateMessage.setText(mUpdateDetail.getInstruction());

    }

    @OnClick({R.id.btn_ok, R.id.btn_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                dismiss();
                mConfirmDialogListener.confirm(isSpk, mUpdateDetail.getUrl());
                break;
            case R.id.btn_cancel:
                dismiss();
                mConfirmDialogListener.cancel();
                break;
            default:
                break;

        }
    }

    public interface ConfirmDialogListener {

        /**
         * 确认
         *
         * @param isSpk       是否为SPK
         * @param downloadUrl 下载地址
         */
        void confirm(boolean isSpk, String downloadUrl);

        /**
         * 取消
         */
        void cancel();
    }

}
