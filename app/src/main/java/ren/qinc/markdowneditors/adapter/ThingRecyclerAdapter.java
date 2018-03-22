package ren.qinc.markdowneditors.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.model.Thing;


//笔记的文章适配器写在这！！！
public class ThingRecyclerAdapter extends BaseRecyclerAdapter<Thing> {

    @Override
    public ThingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ThingHolder(view);
    }
//绑定了列表中的子视图R.layout.list_item解析并且绑定视图。

    public class ThingHolder extends BaseRecyclerAdapter<Thing>.ViewHolder {

        private TextView titleTextView;

        public ThingHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.testtitle);

        }

        public void populate(Thing item) {
            titleTextView.setText(item.text);
        }
    }
}
