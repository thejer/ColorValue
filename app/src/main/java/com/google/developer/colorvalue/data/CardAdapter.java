package com.google.developer.colorvalue.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.developer.colorvalue.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Cursor mCursor;

    public interface OnColorClickedListener{
        void onColorClicked(int id);
    }

    private OnColorClickedListener mColorClickedListener;


    public CardAdapter(OnColorClickedListener colorClickedListener) {
        mColorClickedListener = colorClickedListener;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(CardProvider.Contract.Columns._ID);
        int hexIndex = mCursor.getColumnIndex(CardProvider.Contract.Columns.COLOR_HEX);
//        int nameIndex = mCursor.getColumnIndex(CardProvider.Contract.Columns.COLOR_NAME);

        mCursor.moveToPosition(position);
        int id = mCursor.getInt(idIndex);
        String hex = mCursor.getString(hexIndex);
//        String name = mCursor.getString(nameIndex);

        holder.itemView.setTag(id);
        holder.name.setText(hex);
        holder.itemView.setBackgroundColor(Color.parseColor(hex));
//        holder.name.setTextColor((Color.luminance(Color.parseColor(hex)) >= 0.5) ? Color.BLACK : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * Return a {@link Card} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Card}
     */
    public Card getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new Card(mCursor);
        }
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.color_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int id = mCursor.getInt(mCursor.getColumnIndex(CardProvider.Contract.Columns._ID));
            mColorClickedListener.onColorClicked(id);
        }
    }



}
