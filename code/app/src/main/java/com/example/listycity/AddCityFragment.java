package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City city, int position);
    }

    private AddCityDialogListener listener;
    private City city;
    private int position;

    public static AddCityFragment newInstance(City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putInt("position", position);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        if (getArguments() != null) {
            city = (City) getArguments().getSerializable("city");
            position = getArguments().getInt("position");

            if (city != null) {
                editCityName.setText(city.getName());
                editProvinceName.setText(city.getProvince());
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String title = (city != null) ? "Edit City" : "Add City";

        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (city != null) {

                        city.setName(cityName);
                        city.setProvince(provinceName);
                        listener.editCity(city, position);
                    } else {

                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
