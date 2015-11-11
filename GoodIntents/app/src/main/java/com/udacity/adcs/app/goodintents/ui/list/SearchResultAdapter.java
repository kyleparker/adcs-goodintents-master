package com.udacity.adcs.app.goodintents.ui.list;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Header;
import com.udacity.adcs.app.goodintents.objects.Search;
import com.udacity.adcs.app.goodintents.ui.view.BezelImageView;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyleparker on 11/11/2015.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;

    private Context context;
    private final ArrayList<LineItem> lineItems;
    private List<Search> items;
    private OnItemClickListener itemClickListener;
    private int numColumns;

    public SearchResultAdapter(Context context, int numColumns) {
        this.context = context;
        this.numColumns = numColumns;

        items = new ArrayList<>();
        lineItems = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_search_result_header, viewGroup, false);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_search_result, viewGroup, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        LineItem lineItem = lineItems.get(position);
        final View itemView = viewHolder.itemView;

        if (lineItem != null) {
            if (lineItem.isHeader) {
                Header header = lineItem.header;

                if (header != null) {
                    viewHolder.header.setText(header.getFirstLine());
                }
            } else {
                Search item = lineItem.search;

                if (item != null) {
                    viewHolder.name.setText(item.getName());

                    if (item.getTypeId() == Constants.Type.PERSON) {
                        String mName = item.getName();
                        mName = mName.replaceAll(" ", "_").toLowerCase();

                        Uri iconUri = Uri.parse("android.resource://" + context.getPackageName() + "/drawable/" + mName);

                        viewHolder.image.setImageURI(iconUri);
                        viewHolder.date.setText("Member of 3 shared groups and 5 events");
                    } else if (item.getTypeId() == Constants.Type.EVENT) {
                        viewHolder.date.setText(StringUtils.getDateString(item.event.getDate(), Constants.DATE_TIME_FORMAT_2));
                        Picasso.with(context).load(item.event.getPhotoUrl()).into(viewHolder.image);
                    }
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return lineItems.get(position).isHeader ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return lineItems.size();
    }

    public Search getItem(int position) {
        return lineItems.get(position).search;
    }

    public void addAll(List<Search> search) {
        items.clear();
        items.addAll(search);

        //Insert headers into list of items.
        Header header;
        long lastTypeId = -1L;
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < items.size(); i++) {
            long typeId = items.get(i).getTypeId();

            if (typeId != lastTypeId) {
                String type = "";

                // Insert new header view and update section data.
                sectionManager = (sectionManager + 1) % 2;
                sectionFirstPosition = i + headerCount;
                lastTypeId = typeId;
                headerCount += 1;

                if (typeId == Constants.Type.PERSON) {
                    type = context.getString(R.string.content_event_friends);
                } else if (typeId == Constants.Type.EVENT) {
                    type = context.getString(R.string.content_search_event);
                }

                header = new Header();
                header.setFirstLine(type);

                lineItems.add(new LineItem(null, header, true, sectionManager, sectionFirstPosition));
            }
            lineItems.add(new LineItem(items.get(i), null, false, sectionManager, sectionFirstPosition));
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date;
        private TextView name;
        private BezelImageView image;
        private ProgressBar progress;

        private TextView header;

        public ViewHolder(View base) {
            super(base);

            date = (TextView) base.findViewById(R.id.date);
            name = (TextView) base.findViewById(R.id.name);
            image = (BezelImageView) base.findViewById(R.id.image);
            progress = (ProgressBar) base.findViewById(R.id.progress);

            header = (TextView) base.findViewById(R.id.header);

            base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private static class LineItem {
        public int sectionManager;
        public int sectionFirstPosition;
        public boolean isHeader;
        public Search search;
        public Header header;

        public LineItem(Search search, Header header, boolean isHeader, int sectionManager, int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.search = search;
            this.header = header;
            this.sectionManager = sectionManager;
            this.sectionFirstPosition = sectionFirstPosition;
        }
    }
}
