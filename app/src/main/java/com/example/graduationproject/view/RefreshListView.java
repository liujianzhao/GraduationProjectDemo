package com.example.graduationproject.view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.example.graduationproject.R;
import com.example.graduationproject.util.DensityUtil;

public class RefreshListView extends ListView implements OnScrollListener {

	private Context context;
	private RotateAnimation animation;//变成松开刷新状态时执行的动画
	private RotateAnimation reverseAnimation;//变成下拉刷新状态时执行的动画

	private final static int RATIO = 2;// 移动比例
	private final static int PULL_BACK_TASK_PERIOD = 10;// 每隔多久执行一次
	private static int PULL_TO_REFRESH_BACK_DISTANCE = 0;// 每次回弹动作滑动几个像素点,适应手机5dp的像素点数

	private int firstItemIndex;// 第一个view索引
	private int lastItemIndex;// 最后一个view索引
	private int AllItemSize;// item的总数(加上head与foot)

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		PULL_TO_REFRESH_BACK_DISTANCE = DensityUtil.dip2px(context,5);
		initView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		PULL_TO_REFRESH_BACK_DISTANCE = DensityUtil.dip2px(context,5);
		initView();
	}

	public RefreshListView(Context context) {
		super(context);
		this.context = context;
		PULL_TO_REFRESH_BACK_DISTANCE = DensityUtil.dip2px(context,5);
		initView();
	}

	// 初始化布局
	private void initView() {
		setOnScrollListener(this);
		initAnim();
		initHeadView();
		initFootView();
	}

	private void initAnim() {
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);
		
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}

	@SuppressLint("InflateParams")
	private void initHeadView() {
		headView = LayoutInflater.from(context).inflate(R.layout.item_headview,
				null);
		tv_headmsg = (TextView) headView.findViewById(R.id.tv_msg);
		img_headloading = (ImageView) headView.findViewById(R.id.img_loading);
		head_progressbar = (ProgressBar)headView.findViewById(R.id.head_progressBar);
		
		measureView(headView);
		headViewHeight = headView.getMeasuredHeight();
		// 默认高度为0
		headView.setPadding(0, -headViewHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView,null,false);
	}

	@SuppressLint("InflateParams")
	private void initFootView() {
		footerView = LayoutInflater.from(context).inflate(
				R.layout.item_footview, null);
		tv_footmsg = (TextView) footerView.findViewById(R.id.tv_msg);
		footer_progressbar = (ProgressBar)footerView.findViewById(R.id.footer_progressBar);
		
		measureView(footerView);
		footViewHeight = footerView.getMeasuredHeight();
		footerView.setVisibility(View.GONE);
		footer_progressbar.setVisibility(View.GONE);
		tv_footmsg.setVisibility(View.GONE);

		addFooterView(footerView,null,false);
	}

	// 初始化数据，调用一次下拉刷新
	public void firstLoadDatas() {
		RefreshState = PullToRefreshState.REFRESH_LOADING;
		changeRefreshType(RefreshState);
		iOnRefreshListener.OnRefresh();
	}

	/********************************************************************************************/

	/**
	 * 下拉刷新相关变量，状态，回调接口
	 * 
	 */
	private View headView;// 头布局
	private int headViewHeight;// 头的高度
	private TextView tv_headmsg;// 头布局里textview
	private ImageView img_headloading;// 头布局里的imageview
	private ProgressBar head_progressbar;
	private boolean inRefresh = false;
	private boolean inRollBackAndLoading = false;

	private int startY = 0;// 点击时的起始位置
	private int endY = 0;// 松手时的位置
	private int offset = 0;// 手滑动的距离

	private ScheduledExecutorService schedulor;
	private final static int RELEASE_BACK = -1;//松开刷新状态回弹
	private final static int LOADING_OVER_BACK = -2;//读取结束后返回过程
	private final static int PULL_BACK = -3;//下拉刷新状态回弹

	private class PullToRefreshState {// 下拉刷新四状态
		public static final int REFRESH_NORMAL = 0;
		public static final int REFRESH_PULL = 1;
		public static final int REFRESH_RELEASE = 2;
		public static final int REFRESH_LOADING = 3;
	}

	private int RefreshState = PullToRefreshState.REFRESH_NORMAL;// 下拉刷新当前状态

	private IOnRefreshListener iOnRefreshListener;// 下拉刷新监听器

	public interface IOnRefreshListener {
		public abstract void OnRefresh();
	}

	public void setOnRefreshListener(IOnRefreshListener iOnRefreshListener) {// 设置监听器
		this.iOnRefreshListener = iOnRefreshListener;
	}

	/**
	 * 下拉刷新相关操作处理
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (iOnRefreshListener == null) {
			return super.onTouchEvent(ev);
		}
		int action = ev.getAction();
		if (!inRollBackAndLoading) {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !inRefresh
						&& RefreshState == PullToRefreshState.REFRESH_NORMAL) {
					startY = (int) ev.getY();
					inRefresh = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (inRefresh) {
					endY = (int) ev.getY();// 手指向下移动增加的值与向上移动减少的值不相等引起了一系列问题；
					offset = (endY - startY) / RATIO;
					// 使用以下的这段代码解决了endY值不正确的问题；
					switch (RefreshState) {
					case PullToRefreshState.REFRESH_NORMAL: {
						if (offset > 0) {
							headView.setPadding(0, offset - headViewHeight, 0,
									0);
							RefreshState = PullToRefreshState.REFRESH_PULL;
							changeRefreshType(RefreshState);
						}
					}
						break;
					case PullToRefreshState.REFRESH_PULL: {
						setSelection(0);
						headView.setPadding(0, offset - headViewHeight, 0, 0);
						if (offset < 0) {
							RefreshState = PullToRefreshState.REFRESH_NORMAL;
							changeRefreshType(RefreshState);
						} else if (offset > headViewHeight) {
							RefreshState = PullToRefreshState.REFRESH_RELEASE;
							changeRefreshType(RefreshState);
						}
					}
						break;
					case PullToRefreshState.REFRESH_RELEASE: {
						setSelection(0);
						headView.setPadding(0, offset - headViewHeight, 0, 0);
						if (offset >= 0 && offset <= headViewHeight) {
							RefreshState = PullToRefreshState.REFRESH_PULL;
							changeRefreshType(RefreshState);
						} else if (offset < 0) {
							RefreshState = PullToRefreshState.REFRESH_NORMAL;
							changeRefreshType(RefreshState);
						}
					}
						break;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (inRefresh) {
					if (RefreshState == PullToRefreshState.REFRESH_RELEASE) {
						inRollBackAndLoading = true;
						refreshRollBack(RELEASE_BACK);
					} else if(RefreshState == PullToRefreshState.REFRESH_PULL){
						inRollBackAndLoading = true;
						refreshRollBack(PULL_BACK);
					}else{
						RefreshState = PullToRefreshState.REFRESH_NORMAL;
						changeRefreshType(RefreshState);
					}
					inRefresh = false;
				}
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	// 刷新完毕
	public void onRefreshComplete() {
		offset = headViewHeight;
		refreshRollBack(LOADING_OVER_BACK);
	}

	// 修改刷新的当前状态
	private void changeRefreshType(int type) {
		if (RefreshState == PullToRefreshState.REFRESH_NORMAL) {
			headView.setPadding(0, -headViewHeight, 0, 0);
			img_headloading.clearAnimation();
			img_headloading.setVisibility(View.VISIBLE);
			head_progressbar.setVisibility(View.GONE);
		} else if (RefreshState == PullToRefreshState.REFRESH_PULL) {
			img_headloading.clearAnimation();
			img_headloading.startAnimation(reverseAnimation);
			tv_headmsg.setText("下拉刷新！ ");
		} else if (RefreshState == PullToRefreshState.REFRESH_RELEASE) {
			img_headloading.clearAnimation();
			img_headloading.startAnimation(animation);
			tv_headmsg.setText("松手刷新！");
		} else if (RefreshState == PullToRefreshState.REFRESH_LOADING) {
			img_headloading.clearAnimation();
			img_headloading.setVisibility(View.GONE);
			head_progressbar.setVisibility(View.VISIBLE);
			headView.setPadding(0, 0, 0, 0);
			tv_headmsg.setText("加载中......");
		}
	}

	/**********************************************************************************************/

	/**
	 * 上拉加载更多相关变量，状态，回调接口
	 * 
	 */
	private View footerView;// 底部布局
	private int footViewHeight;// 底部的高度
	private TextView tv_footmsg;
	private ProgressBar footer_progressbar;
	private boolean isLoadOver = false;

	private class UpToLoadMoreState {// 上拉状态
		public static final int LOADMORE_NORMAL = 0;
		public static final int LOADMORE_LOADING = 1;
		public static final int LOADMORE_OVER = 2;
	}

	private int LoadMoreState = UpToLoadMoreState.LOADMORE_NORMAL;// 上拉当前状态

	private IOnLoadMoreListener iOnLoadMoreListener;

	public interface IOnLoadMoreListener {// 上拉接口
		public abstract void OnLoadMore();
	}

	public void setOnLoadMoreListener(IOnLoadMoreListener iOnLoadMoreListener) {// 设置上拉监听
		this.iOnLoadMoreListener = iOnLoadMoreListener;
	}

	/**
	 * 上拉加载更多相关操作处理
	 */
	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		firstItemIndex = firstVisibleItem;
		AllItemSize = totalItemCount;
		lastItemIndex = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		if (lastItemIndex == AllItemSize - 1 && !isLoadOver
				&& scrollState == SCROLL_STATE_IDLE
				&& iOnLoadMoreListener != null) {
			LoadMoreState = UpToLoadMoreState.LOADMORE_LOADING;
			changeLoadMoreType(LoadMoreState);
			iOnLoadMoreListener.OnLoadMore();
		}
	}

	// 加载完毕
	public void onLoadMoreComplete(boolean hasMore) {
		if (hasMore) {
			LoadMoreState = UpToLoadMoreState.LOADMORE_NORMAL;
			changeLoadMoreType(LoadMoreState);
		} else {
			LoadMoreState = UpToLoadMoreState.LOADMORE_OVER;
			changeLoadMoreType(LoadMoreState);
			isLoadOver = true;
		}
	}

	// 修改加载当前状态
	private void changeLoadMoreType(int type) {
		if (LoadMoreState == UpToLoadMoreState.LOADMORE_NORMAL) {
			footerView.setVisibility(View.GONE);
			footer_progressbar.setVisibility(View.GONE);
			tv_footmsg.setVisibility(View.GONE);
		} else if (LoadMoreState == UpToLoadMoreState.LOADMORE_LOADING) {
			footerView.setVisibility(View.VISIBLE);
			footer_progressbar.setVisibility(View.VISIBLE);
			tv_footmsg.setVisibility(View.VISIBLE);
			tv_footmsg.setText("加载中......");
		} else if (LoadMoreState == UpToLoadMoreState.LOADMORE_OVER) {
			footerView.setVisibility(View.VISIBLE);
			footer_progressbar.setVisibility(View.GONE);
			tv_footmsg.setVisibility(View.VISIBLE);
			tv_footmsg.setText("全部加载完毕！");
		}
	}

	/**
	 * 工具方法
	 */
	// 此方法直接照搬自网络上的一个下拉刷新的demo，计算headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);// 得到的是size。
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);// 得到的是子布局的实际大小。
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private void refreshRollBack(final int what) {
		// 执行回滚动画
		schedulor = Executors.newScheduledThreadPool(1);
		// 此处启动线程处理后，关闭线程的逻辑没有实现所以可能会产生线程冗余（需要优化）
		schedulor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				listhandler.sendEmptyMessage(what);
			}
		}, 0, PULL_BACK_TASK_PERIOD, TimeUnit.MILLISECONDS);
	}

	// 处理下拉刷新回弹的两个动作
	@SuppressLint("HandlerLeak")
	Handler listhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case PULL_BACK:
				if (!schedulor.isShutdown()) {
					offset -= PULL_TO_REFRESH_BACK_DISTANCE;
					headView.setPadding(0, offset - headViewHeight, 0, 0);
					// 重绘
					headView.invalidate();
					// 停止回弹时递减headView高度的任务
					if (offset <= -headViewHeight) {
						schedulor.shutdownNow();
						RefreshState = PullToRefreshState.REFRESH_NORMAL;
						changeRefreshType(RefreshState);
						inRollBackAndLoading = false;
					}
				}
				break;
			case RELEASE_BACK:
				if (!schedulor.isShutdown()) {
					offset -= PULL_TO_REFRESH_BACK_DISTANCE;
					headView.setPadding(0, offset - headViewHeight, 0, 0);
					// 重绘
					headView.invalidate();
					// 停止回弹时递减headView高度的任务
					if (offset <= headViewHeight) {
						schedulor.shutdownNow();// 此行代码因为处于UI线程中，所以只改了状态逻辑未实现（放到runnable里就可以实现逻辑）
						RefreshState = PullToRefreshState.REFRESH_LOADING;
						changeRefreshType(RefreshState);
						iOnRefreshListener.OnRefresh();
					}
				}
				break;
			case LOADING_OVER_BACK:
				if (!schedulor.isShutdown()) {
					offset -= PULL_TO_REFRESH_BACK_DISTANCE;
					headView.setPadding(0, offset - headViewHeight, 0, 0);
					// 重绘
					headView.invalidate();
					// 停止回弹时递减headView高度的任务
					if (offset <= -headViewHeight) {
						schedulor.shutdownNow();
						RefreshState = PullToRefreshState.REFRESH_NORMAL;
						changeRefreshType(RefreshState);
						inRollBackAndLoading = false;
					}
				}
				break;
			}
		}
	};
}
