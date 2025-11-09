package com.pinoyseoul.radio.adapter;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.pinoyseoul.radio.R;
import com.pinoyseoul.radio.model.Audio;


public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioAdapterViewHolder> {
    private static final String TAG = AudioAdapter.class.getSimpleName();
    private static OnItemClickListener mListener;
    private List<Audio> audioList;
    private SparseArray<Float> progressMap = new SparseArray<>();

    public AudioAdapter(List<Audio> audioList) {
        this.audioList = audioList;
        setHasStableIds(true);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public AudioAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        return new AudioAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AudioAdapterViewHolder holder, int position) {
        String title = audioList.get(position).getTitle();
        holder.audioTitle.setText(title);
        holder.itemView.setTag(audioList.get(position));

        applyProgressPercentage(holder, progressMap.get(position, 0.0f));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void applyProgressPercentage(AudioAdapterViewHolder holder, float percentage) {
        Log.d(TAG, "applyProgressPercentage() with percentage = " + percentage);
        LinearLayout.LayoutParams progress = (LinearLayout.LayoutParams) holder.viewProgress.getLayoutParams();
        LinearLayout.LayoutParams antiProgress = (LinearLayout.LayoutParams) holder.viewAntiProgress.getLayoutParams();

        progress.weight = percentage;
        holder.viewProgress.setLayoutParams(progress);

        antiProgress.weight = 1.0f - percentage;
        holder.viewAntiProgress.setLayoutParams(antiProgress);
    }

    @Override
    public int getItemCount() {
        return audioList == null ? 0 : audioList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onSongItemDeleteClicked(int position);
    }

    static class AudioAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView audioTitle;
        private View viewProgress;
        private View viewAntiProgress;
        private Button  btnDelete;

        public AudioAdapterViewHolder(View view) {
            super(view);
            this.audioTitle = view.findViewById(R.id.audio_title);
            viewProgress = view.findViewById(R.id.song_progress_view);
            viewAntiProgress = view.findViewById(R.id.song_anti_progress_view);
            btnDelete = view.findViewById(R.id.button_dell);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onSongItemDeleteClicked(getAdapterPosition());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (mListener != null) mListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
