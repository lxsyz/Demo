package com.example.administrator.demo;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/24.
 */
public class ScrollAdapter extends SimpleAdapter {

    private int mScreenWidth;
    //手机单屏幕最多四列
    private final int MAX_COUNT = 4;


    private Context context;
    private int resource;
    private String[] from;
    private int[] to;
    private List<? extends Map<String,?>> data;

    private List<ScrollItem> items = new ArrayList<>();


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

        View v = convertView;
        LinearLayout root;

        if (v == null) {
            v = LayoutInflater.from(context).inflate(resource,null);
            root = (LinearLayout)v.findViewById(R.id.root_view);

            addHorizontalViews((ScrollItem)v.findViewById(R.id.item_scroll));
            View[] views = new View[to.length];
            //元素不足四个的时候
            if (to.length < MAX_COUNT) {
                for (int i = 0; i < to.length;i++) {
                    TextView tv = new TextView(this.context);
                    tv.setGravity(Gravity.CENTER);

                    int width = mScreenWidth / to.length;

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
                    tv.setLayoutParams(lp);
                    views[i] = tv;
                }
            } else {
                for (int i = 0;i < to.length;i++) {
                    TextView tv = new TextView(this.context);
                    tv.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp2px(this.context, 200.0f),
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    tv.setLayoutParams(lp);
                    views[i] = tv;
                }
            }

            v.setTag(views);
        } else
            root = (LinearLayout) v.findViewById(R.id.root_view);
        View[] holders = (View[]) v.getTag();
        int len = holders.length;

        //判断格子之间是否需要添加竖线分割
        boolean needLine = false;
        for(int i = 0 ; i < len; i++) {
            View lineView = new View(this.context);
            lineView.setBackgroundColor(this.context.getResources().getColor(R.color.line));
            lineView.setLayoutParams(new ViewGroup.LayoutParams(dp2px(this.context, 1.2f),
                    ViewGroup.LayoutParams.MATCH_PARENT));

            if (needLine) {
                root.addView(lineView);
            }
            needLine = true;

            if (this.data.get(position).get(from[i]) != null) {
                ((TextView) holders[i]).setText(this.data.get(position).get(from[i]).toString());
            } else {
                ((TextView) holders[i]).setText("");
            }
            //((TextView)holders[i]).setTag(position+" "+i);
            root.addView(holders[i]);
        }


        return super.getView(position, convertView, parent);
    }

    private void addHorizontalViews(ScrollItem item) {
        if (!items.isEmpty()) {
            int size = items.size();
            ScrollItem tempItem = items.get(size - 1);
            final int scrollX = tempItem.getScrollX();
            if (scrollX != 0) {

            }
        }

        items.add(item);
    }



    /*
     * dp -> px
     */
    public int dp2px (Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setmScreenWidth(int mScreenWidth) {
        this.mScreenWidth = mScreenWidth;
    }
}
