package io.hefuyi.zhihudaily.ui.adapter;

import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.hefuyi.zhihudaily.R;
import io.hefuyi.zhihudaily.interfaces.NavigationDrawerCallbacks;
import io.hefuyi.zhihudaily.mvp.model.Theme;
import io.hefuyi.zhihudaily.util.L;
import io.hefuyi.zhihudaily.util.UIUtils;

/**
 * Created by hefuyi on 16/8/1.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = NavigationDrawerAdapter.class.getSimpleName();
    private final List<Theme> mThemes;
    private final boolean mIsKitKatWithNavigation;
    private int mSelectedPosition = -1;
    private NavigationDrawerCallbacks mCallbacks;

    public static final class Type {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_ITEM = 1;
        public static final int TYPE_BOTTOM_SPACE = 2;
    }

    public NavigationDrawerAdapter(boolean isKitKatWithNavigation) {
        mThemes = new ArrayList<>();
        mIsKitKatWithNavigation = isKitKatWithNavigation;
    }

    public void setThemes(List<Theme> themes) {
        mThemes.clear();
        mThemes.addAll(themes);
        notifyDataSetChanged();
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsKitKatWithNavigation) {
            if (position == 0) {
                return Type.TYPE_HEADER;
            } else if (position == mThemes.size() + 2) {
                return Type.TYPE_BOTTOM_SPACE;
            } else {
                return Type.TYPE_ITEM;
            }
        } else {
            return position == 0 ? Type.TYPE_HEADER : Type.TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case Type.TYPE_HEADER:
                itemView = UIUtils.inflate(R.layout.nav_drawer_header, parent);
                return new HeaderViewHolder(itemView);
            case Type.TYPE_ITEM:
                itemView = UIUtils.inflate(R.layout.nav_drawer_item, parent);
                final ItemViewHolder itemviewHolder = new ItemViewHolder(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPosition(itemviewHolder.getAdapterPosition()-1);
                    }
                });
                return itemviewHolder;
            case Type.TYPE_BOTTOM_SPACE:
                View view = new View(parent.getContext());
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getNavigationBarHeight(parent.getContext())));
                UIUtils.setAccessiblityIgnore(view);
                return new BottomViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case Type.TYPE_HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                bindHeaderData(headerViewHolder, position);
                break;
            case Type.TYPE_ITEM:
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                bindItemData(itemViewHolder, position);
                break;
            case Type.TYPE_BOTTOM_SPACE:
                break;
            default:
                throw new IllegalArgumentException(TAG + " error view type!");
        }
    }

    @Override
    public int getItemCount() {
        if (mIsKitKatWithNavigation) {
            return mThemes != null ? mThemes.size() + 3 : 3;
        } else {
            return mThemes != null ? mThemes.size() + 2 : 2;
        }
    }

    public void selectPosition(int position) {
        int realPosition = position + 1;
        int lastPosition = mSelectedPosition;

        if (lastPosition != -1 && lastPosition != realPosition) {
            notifyItemChanged(lastPosition);
        }

        if (mSelectedPosition != realPosition) {
            mSelectedPosition = realPosition;
            notifyItemChanged(mSelectedPosition);
        }

        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    private void bindHeaderData(HeaderViewHolder viewHolder, int position) {

    }

    private void bindItemData(ItemViewHolder viewHolder, int position) {
        Resources resources = viewHolder.itemView.getContext().getResources();
        if (position == 1) {
            viewHolder.ivItemIcon.setVisibility(View.VISIBLE);
            viewHolder.ivItemIcon.setBackgroundResource(R.drawable.menu_home);
            viewHolder.tvItemName.setText(resources.getString(R.string.title_activity_main));
        } else {
            viewHolder.ivItemIcon.setBackgroundDrawable(null);
            viewHolder.ivItemIcon.setVisibility(View.GONE);
            viewHolder.tvItemName.setText(mThemes.get(position - 2).getName());
        }

        if (mSelectedPosition == position) {
            L.i(TAG, "selected item = " + position);
            viewHolder.itemView.setBackgroundColor(resources.getColor(R.color.navigation_item_selected));
            viewHolder.tvItemName.setTextColor(resources.getColor(R.color.navdrawer_text_color_selected));
        } else if (position!=0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                TypedValue outValue = new TypedValue();
                viewHolder.itemView.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                viewHolder.itemView.setBackgroundResource(outValue.resourceId);
            }
            TypedValue textcolor = new TypedValue();
            Resources.Theme theme = viewHolder.itemView.getContext().getTheme();
            theme.resolveAttribute(R.attr.navdrawer_text_color, textcolor, true);
            viewHolder.tvItemName.setTextColor(resources.getColor(textcolor.resourceId));
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView tvItemName;
        final ImageView ivItemIcon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            ivItemIcon = (ImageView) itemView.findViewById(R.id.ivItemIcon);
        }
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {
        public BottomViewHolder(View itemView) {
            super(itemView);
        }
    }

}
