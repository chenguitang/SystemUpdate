package com.posin.systemupdate.module.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.posin.systemupdate.ui.contract.HomeContract;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * FileName: ProgressResponseBody
 * Author: Greetty
 * Time: 2018/6/8 15:27
 * Desc: TODO
 */
public class ProgressResponseBody extends ResponseBody {

    private static final String TAG = "ProgressResponseBody";
    private static final int UPDATE_PROGRESS_CODE = 101;
    private ResponseBody mResponseBody;
    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;
    private HomeContract.IHomeView mHomeView;
    private MyHandler myHandler;

    public ProgressResponseBody(ResponseBody responseBody, HomeContract.IHomeView homeView) {

        this.mResponseBody = responseBody;
        this.mHomeView = homeView;

        if (myHandler == null) {
            myHandler = new MyHandler();
        }
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(parseSource(mResponseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 计算下载进度
     *
     * @param source Source
     * @return Source
     */
    private Source parseSource(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
//                Log.e("ProgressResponseBody", "read progress: " + (int) (totalBytesRead * 100 /
//                        mResponseBody.contentLength()));
                if (null != mHomeView) {
                    if (bytesRead != -1) {
                        float progress = (float) (totalBytesRead * 100 / mResponseBody.contentLength());
                        myHandler.obtainMessage(UPDATE_PROGRESS_CODE, progress).sendToTarget();
//                        mHomeView.updateDownloadProgress((int) (totalBytesRead * 100 /
//                                mResponseBody.contentLength()));
                    }
                }
                return bytesRead;
            }
        };

    }

    class MyHandler extends Handler {

        public MyHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS_CODE:
                    float progress = (float) msg.obj;
                    mHomeView.updateDownloadProgress(progress);


                default:
                    break;
            }
        }

    }

}
