package com.example.toshiba.airbnb.Profile.BecomeAHost.GetReady;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.toshiba.airbnb.DatabaseInterface;
import com.example.toshiba.airbnb.Explore.POJOListingData;
import com.example.toshiba.airbnb.Profile.ViewListingAndYourBooking.EditListing.EditListingDTO.BookingDTO;
import com.example.toshiba.airbnb.Profile.ViewListingAndYourBooking.EditListing.EditListingDTO.TitleDTO;
import com.example.toshiba.airbnb.Profile.ViewListingAndYourBooking.ViewListingAndYourBookingAdapter;
import com.example.toshiba.airbnb.Util.KeyboardUtil;
import com.example.toshiba.airbnb.R;
import com.example.toshiba.airbnb.Util.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TOSHIBA on 09/08/2017.
 */

public class BookingFragment extends Fragment {
    public static final String BOOKING_SP = "BOOKING_SP";

    public static final String MAX_MONTH = "MAX_MONTH";
    public static final String ARRIVE_AFTER = "ARRIVE_AFTER";
    public static final String LEAVE_BEFORE = "LEAVE_BEFORE";
    public static final String MAX_STAY = "MAX_STAY";
    public static final String MIN_STAY = "MIN_STAY";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (getArguments() == null) {
            ProgressBar basicProgressBar = (ProgressBar) getActivity().findViewById(R.id.basicProgressBar);
            basicProgressBar.setProgress(75);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        ;
        sharedPreferences = getActivity().getSharedPreferences(BOOKING_SP, Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Button bNext = (Button) view.findViewById(R.id.bNext);
        final EditText etMaxMonth = (EditText) view.findViewById(R.id.etMaxMonth);
        final EditText etArriveAfter = (EditText) view.findViewById(R.id.etArriveAfter);
        final EditText etLeaveBefore = (EditText) view.findViewById(R.id.etLeaveBefore);
        final EditText etMinStay = (EditText) view.findViewById(R.id.etMinStay);
        final EditText etMaxStay = (EditText) view.findViewById(R.id.etMaxStay);
        //load from database
        if (getArguments() != null) {
            if (getArguments().containsKey(ViewListingAndYourBookingAdapter.LISTING_ID)) {
                Log.d("loveusomuch", "listing_id");
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Loading...");
                dialog.setCancelable(false);
                dialog.show();
                bNext.setText(getString(R.string.save));
                bNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("loveusomuch", "clicked");
                        KeyboardUtil.hideKeyboard(getActivity());
                        DatabaseInterface retrofit = RetrofitUtil.retrofitBuilderForDatabaseInterface();
                        final ProgressDialog dialog = new ProgressDialog(getActivity());
                        dialog.setMessage("Updating data...");
                        dialog.setCancelable(false);

                        dialog.show();
                        retrofit.updateBooking(getArguments().getInt(ViewListingAndYourBookingAdapter.LISTING_ID),
                                new BookingDTO(etMaxMonth.getText().toString(), etArriveAfter.getText().toString(),
                                        etLeaveBefore.getText().toString(), etMaxStay.getText().toString(),
                                        etMinStay.getText().toString())).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_LONG).show();
                                getFragmentManager().popBackStack();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), getString(R.string.failedToUpdate), Toast.LENGTH_LONG);

                            }
                        });
                    }
                });
                DatabaseInterface retrofit = RetrofitUtil.retrofitBuilderForDatabaseInterface();
                retrofit.getListingData(getArguments().getInt(ViewListingAndYourBookingAdapter.LISTING_ID)).enqueue(new Callback<POJOListingData>() {
                    @Override
                    public void onResponse(Call<POJOListingData> call, Response<POJOListingData> response) {
                        POJOListingData body = response.body();
                        etMaxMonth.setText(body.getListingLength());
                        etArriveAfter.setText(body.getArriveAfter());
                        etLeaveBefore.setText(body.getLeaveBefore());
                        etMinStay.setText(body.getMinStay());
                        etMaxStay.setText(body.getMaxStay());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<POJOListingData> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                });
            }
        } else {
            etMaxMonth.setText(sharedPreferences.getString(MAX_MONTH, ""));
            etArriveAfter.setText(sharedPreferences.getString(ARRIVE_AFTER, ""));
            etLeaveBefore.setText(sharedPreferences.getString(LEAVE_BEFORE, ""));
            etMinStay.setText(sharedPreferences.getString(MIN_STAY, ""));
            etMaxStay.setText(sharedPreferences.getString(MAX_STAY, ""));

            bNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardUtil.hideKeyboard(getActivity());
                    if (etMaxMonth.getText().length() > 0 &&
                            etArriveAfter.getText().length() > 0 &&
                            etLeaveBefore.getText().length() > 0 &&
                            etMinStay.getText().length() > 0) {


                        edit.putString(MAX_MONTH, etMaxMonth.getText().toString());
                        edit.putString(ARRIVE_AFTER, etArriveAfter.getText().toString());
                        edit.putString(LEAVE_BEFORE, etLeaveBefore.getText().toString());
                        edit.putString(MIN_STAY, etMinStay.getText().toString());

                        //etMaxStay is optional
                        if (etMaxStay.getText().length() > 0) {
                            if (Integer.parseInt(etMaxStay.getText().toString()) >
                                    Integer.parseInt(etMinStay.getText().toString())) {
                                edit.putString(MAX_STAY, etMaxStay.getText().toString());
                            } else {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                dialog.setMessage("Maximum stay must be greater than minimum stay");
                                dialog.setCancelable(false);
                                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                                return;
                            }

                        }

                        edit.apply();

                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setMessage("Please fill in the required fields");
                        dialog.setCancelable(false);
                        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }
            });

        }
        TextWatcher monthTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (etMaxMonth.getText().length() > 0) {
                    int val = Integer.parseInt(s.toString());
                    if (val > 12) {
                        s.replace(0, s.length(), "12", 0, 2);
                        Toast.makeText(getActivity(), "Months must be less or equal than to 12", Toast.LENGTH_LONG).show();
                    }
                    if (val == 0) {
                        s.replace(0, s.length(), "1", 0, 1);
                        Toast.makeText(getActivity(), "Cannot have an input of zero", Toast.LENGTH_LONG).show();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
        etMaxMonth.addTextChangedListener(monthTextWatcher);

        TextWatcher stayTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (etMinStay.getText().length() > 0) {
                    int val = Integer.parseInt(s.toString());
                    if (val == 0) {
                        s.replace(0, s.length(), "1", 0, 1);
                        Toast.makeText(getActivity(), "Cannot have an input of zero", Toast.LENGTH_LONG).show();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
//        etMaxMonth.addTextChangedListener(stayTextWatcher);
        etMinStay.addTextChangedListener(stayTextWatcher);

    }
}
