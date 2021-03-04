package com.example.newsbear2.ui.notifications;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.newsbear2.HomeActivity;
import com.example.newsbear2.R;

public class NotificationsFragment extends Fragment
{

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {

            }
        });
        return root;
    }
}