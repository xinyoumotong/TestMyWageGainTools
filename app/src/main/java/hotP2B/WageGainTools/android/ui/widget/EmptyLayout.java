
package hotP2B.WageGainTools.android.ui.widget;

import hotP2B.WageGainTools.android.R;

import org.kymjs.kjframe.utils.StringUtils;
import org.kymjs.kjframe.utils.SystemTool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EmptyLayout extends LinearLayout implements
        View.OnClickListener {

    public static final int NETWORK_ERROR = 1; // 网络错误
    public static final int NETWORK_LOADING = 2; // 加载中
    public static final int NODATA = 3; // 没有数据
    public static final int HIDE_LAYOUT = 4; // 隐藏
    private int mErrorState = NETWORK_LOADING;

    private Context context;
    private OnClickListener listener;
    private boolean clickEnable = true;
    private String strNoDataContent = "";

    private TextView tv;
    public ImageView img;
    private ProgressBar animProgress;

    public EmptyLayout(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_error_layout, null);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        animProgress = (ProgressBar) view.findViewById(R.id.animProgress);

        setBackgroundColor(-1);
        setOnClickListener(this);
        setErrorType(NETWORK_LOADING);

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        this.addView(view);
    }

    @Override
    public void onClick(View v) {
        if (clickEnable && listener != null) {
            listener.onClick(v);
        }
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }
    
    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }

    public void setErrorMessage(String msg) {
        tv.setText(msg);
    }

    public void setErrorImag(int imgResource) {
        try {
            img.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setTvNoDataContent() {
        if (StringUtils.isEmpty(strNoDataContent)) {
            tv.setText("没有数据,点击重试下吧~");
        } else {
            tv.setText(strNoDataContent);
        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
        case NETWORK_ERROR:
            mErrorState = NETWORK_ERROR;
            if(SystemTool.checkNet(context))
            {
              tv.setText("获取数据失败,点击重试下吧~");
              img.setBackgroundResource(R.mipmap.page_icon_fail);
            }
            else
            {
              tv.setText("亲,没有网络呀,点击重试下吧~");
              img.setBackgroundResource(R.mipmap.page_icon_network);
            }
            img.setVisibility(View.VISIBLE);
            animProgress.setVisibility(View.GONE);
            clickEnable = true;
            break;
        case NETWORK_LOADING:
            mErrorState = NETWORK_LOADING;
            animProgress.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);
            tv.setText("加载中…");
            clickEnable = false;
            break;
        case NODATA:
            mErrorState = NODATA;
            img.setBackgroundResource(R.mipmap.page_icon_empty);
            img.setVisibility(View.VISIBLE);
            animProgress.setVisibility(View.GONE);
            setTvNoDataContent();
            clickEnable = true;
            break;
        case HIDE_LAYOUT:
            dismiss();
            break;
        default:
            break;
        }
    }
    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }
}