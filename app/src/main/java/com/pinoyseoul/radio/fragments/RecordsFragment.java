package com.pinoyseoul.radio.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.pinoyseoul.radio.R;
import com.pinoyseoul.radio.adapter.AudioAdapter;
import com.pinoyseoul.radio.databinding.FragmentRecordsBinding;
import com.pinoyseoul.radio.dialog.DeleteRecordDialog;
import com.pinoyseoul.radio.radiosHelper.RadioService;
import com.pinoyseoul.radio.settings.Configs;
import com.pinoyseoul.radio.utils.Utils;
import java.io.File;
import java.util.ArrayList;

public class RecordsFragment extends Fragment {

    private FragmentRecordsBinding binding;
    private static final String TAG = "TAG";
    private static RecordsFragment instance = null;
    private AudioAdapter audioAdapter;
    //  ArrayList<JcAudio> jcAudios = new ArrayList<>();

    public RecordsFragment() {
    }

    public static RecordsFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecordsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        instance = this;
        getRecords();
        Intent intent_close = new Intent(getContext(), RadioService.class);
        intent_close.setAction(RadioService.ACTION_STOP);
        getContext().startService(intent_close);

        return rootView;
    }

    private void getRecords() {

        String dirPath = Utils.getRootDirPath(requireActivity().getApplicationContext()) + Configs.FOLDER_REC;
        File rirr = new File(dirPath);
        File[] contents = rirr.listFiles();

        if (contents == null) {

        } else if (contents.length == 0) {

        } else {

            if (rirr.isDirectory()) {

                for (File f : rirr.listFiles()) {

                    if (f.isFile()) {

                        String namez = f.getName().replace(".mp3", "");
                        String end_title = namez.replace("_", " ");
                        String[] z = end_title.split("-XYZ-");
                        String filedir = rirr + File.separator + f.getName();

                        // jcAudios.add(JcAudio.createFromFilePath(z[1], filedir));

                    }

                    // if (jcAudios.size() < 1) {

                    // } else {

                    //     player.initPlaylist(jcAudios, this);

                    //     adapterSetup();
                    // }

                }

            }

        }

    }

    protected void adapterSetup() {
        // audioAdapter = new AudioAdapter(player.getMyPlaylist());
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // player.playAudio(player.getMyPlaylist().get(position));

            }

            @Override
            public void onSongItemDeleteClicked(int position) {

                // final DeleteRecordDialog deleteRecordDialog = new DeleteRecordDialog(getActivity(), jcAudios.get(position).getTitle());
                // deleteRecordDialog.show();
                // deleteRecordDialog.findViewById(R.id.btn_delete_d).setOnClickListener(new View.OnClickListener() {
                //     @Override
                //     public void onClick(View v) {

                //         String path = player.getMyPlaylist().get(position).getPath();
                //         new File(path).delete();

                //         jcAudios.remove(position);
                //         audioAdapter.notifyDataSetChanged();
                //         deleteRecordDialog.dismiss();

                //     }
                // });
                // deleteRecordDialog.findViewById(R.id.btn_share_mp3).setOnClickListener(new View.OnClickListener() {
                //     @Override
                //     public void onClick(View v) {

                //         String path = player.getMyPlaylist().get(position).getPath();
                //         Uri uri = Uri.parse(path);
                //         Intent share = new Intent(Intent.ACTION_SEND);
                //         share.setType("audio/*");
                //         share.putExtra(Intent.EXTRA_STREAM, uri);
                //         startActivity(Intent.createChooser(share, "Share Sound File"));
                //         deleteRecordDialog.dismiss();
                //     }
                // });
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(audioAdapter);

        ((SimpleItemAnimator) binding.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }

    @Override
    public void onStop() {
        super.onStop();
       // player.createNotification();
    }

    @Override
    public void onPause() {
        super.onPause();
       // player.createNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // player.kill();
    }
}
