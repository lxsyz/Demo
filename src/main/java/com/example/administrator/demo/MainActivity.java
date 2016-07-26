package com.example.administrator.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private List<Map<String, String>> gridData;
    private int mScreenWidth;

    private HorizontalScrollView mTouchView;
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        instance = this;

        listView = (ListView) findViewById(R.id.list);

        gridData = new ArrayList<Map<String, String>>();

        //测试表格行列数
        int rows = 12;
        int cols = 12;


        String[] colDatas = new String[0];
        int[] itemText = new int[0];
        for (int i = 0; i < rows; i++) {

            if (i == 0) {
                itemText = new int[cols];
            }

            colDatas = new String[cols];

            Map<String, String> map = new HashMap<>();

            for (int j = 0; j < cols; j++) {
                map.put("data" + j, "数据" + j);
                colDatas[j] = "data" + j;
//                                                    items[j] = R.id.item_datav1;
            }
            gridData.add(map);
        }

        Log.d("gridData", gridData.size() + " ");
        Log.d("colData", colDatas.length + " ");
        Log.d("items", itemText.length + " ");

        ScrollAdapter adapter = new ScrollAdapter(MainActivity.this, gridData, R.layout.common_item_hlistview,
                colDatas, itemText
        );

        adapter.setmScreenWidth(mScreenWidth);
        listView.setAdapter(adapter);
    }

    public HorizontalScrollView getmTouchView() {
        return mTouchView;
    }

    public void setmTouchView(HorizontalScrollView mTouchView) {
        this.mTouchView = mTouchView;
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (ScrollItem item : items) {
            //防止重复滑动
            if (mTouchView != item)
                item.smoothScrollTo(l, t);
        }
    }

    public void addHorizontalViews(final ScrollItem item) {
        if (!items.isEmpty()) {
            int size = items.size();
            final ScrollItem tempItem = items.get(size - 1);
            final int scrollX = tempItem.getScrollX();
            Log.d("scrollX", "->" + scrollX);
            if (scrollX != 0) {
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        //当listView刷新完成之后，把该条移动到最终位置
                        item.scrollTo(scrollX, 0);
                    }
                });
            }
        }

        items.add(item);
    }


    private List<ScrollItem> items = new ArrayList<>();

    public class ScrollAdapter extends SimpleAdapter {

        private int mScreenWidth;
        //手机单屏幕最多四列
        private final int MAX_COUNT = 4;


        private Context context;
        private int resource;
        private String[] from;
        private int[] to;
        private List<? extends Map<String, ?>> data;


        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public ScrollAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
            this.data = data;
            this.resource = resource;
            this.from = from;
            this.to = to;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View v = convertView;


            if (v == null) {
                v = LayoutInflater.from(context).inflate(resource, null);
                holder = new ViewHolder();

                holder.root = (LinearLayout) v.findViewById(R.id.root_view);
                holder.scrollItem = (ScrollItem) v.findViewById(R.id.item_scroll);
                addHorizontalViews(holder.scrollItem);
                holder.views = new View[to.length];
                holder.lineView = new View(this.context);
                holder.lineView.setBackgroundColor(this.context.getResources().getColor(R.color.line));
                holder.lineView.setLayoutParams(new ViewGroup.LayoutParams(dp2px(this.context, 1.2f),
                        ViewGroup.LayoutParams.MATCH_PARENT));


                //元素不足四个的时候
                if (to.length < MAX_COUNT) {
                    for (int i = 0; i < to.length; i++) {
                        TextView tv = new TextView(this.context);
                        tv.setGravity(Gravity.CENTER);

                        int width = mScreenWidth / to.length;

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
                        tv.setLayoutParams(lp);
                        holder.views[i] = tv;
                    }
                } else {
                    for (int i = 0; i < to.length; i++) {
                        TextView tv = new TextView(this.context);
                        tv.setGravity(Gravity.CENTER);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp2px(this.context, 200.0f),
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        tv.setLayoutParams(lp);
                        holder.views[i] = tv;
                    }
                }

                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            //View[] holders = (View[]) v.getTag();
            int scrollX = holder.scrollItem.getScrollX();

            Log.d("crolX", "scrolX->" + scrollX);

            int len = holder.views.length;

            //判断格子之间是否需要添加竖线分割
            boolean needLine = false;
            for (int i = 0; i < len; i++) {
//                View lineView = new View(this.context);
//                lineView.setBackgroundColor(this.context.getResources().getColor(R.color.line));
//                lineView.setLayoutParams(new ViewGroup.LayoutParams(dp2px(this.context, 1.2f),
//                        ViewGroup.LayoutParams.MATCH_PARENT));

                if (needLine) {
                    holder.root.removeView(holder.lineView);
                    holder.root.addView(holder.lineView);
                }
                needLine = true;

                if (this.data.get(position).get(from[i]) != null) {
                    ((TextView) holder.views[i]).setText(this.data.get(position).get(from[i]).toString());
                } else {
                    ((TextView) holder.views[i]).setText("");
                }
                //((TextView)holders[i]).setTag(position+" "+i);

                holder.root.removeView(holder.views[i]);
                holder.root.addView(holder.views[i]);
            }


            return v;
        }


        /*
         * dp -> px
         */
        public int dp2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        public void setmScreenWidth(int mScreenWidth) {
            this.mScreenWidth = mScreenWidth;
        }

        class ViewHolder {
            public LinearLayout root;
            public View[] views;
            public View lineView;
            public ScrollItem scrollItem;
        }
    }
}
