package com.example.pianoapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pianoapp.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        final TextView imageView = binding.textDashboard; // Assuming you have an ImageView in your layout
        final TextView dataTextView = binding.textDashboard; // Assuming you have a TextView in your layout

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), text -> {
            // Update UI with text
            textView.setText(text);
        });

        dashboardViewModel.getImageResource().observe(getViewLifecycleOwner(), imageResource -> {
            // Update UI with image
            imageView.getResources();
        });

        dashboardViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            // Update UI with data
            dataTextView.setText(data);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
