package com.example.rafael.flashback.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rafael.flashback.R;

import static com.example.rafael.flashback.utils.CalendarMock.setDay;
import static com.example.rafael.flashback.utils.CalendarMock.setHour;
import static com.example.rafael.flashback.utils.CalendarMock.setMinute;
import static com.example.rafael.flashback.utils.CalendarMock.setMockTime;
import static com.example.rafael.flashback.utils.CalendarMock.setMonth;
import static com.example.rafael.flashback.utils.CalendarMock.setSecond;
import static com.example.rafael.flashback.utils.CalendarMock.setYear;

/**
 * Created by russe on 3/16/2018.
 */

public class TimeFragment extends android.support.v4.app.Fragment {
    private Button Update;
    private EditText Minute;
    private EditText Second;
    private EditText Hour;
    private EditText Day;
    private EditText Month;
    private EditText Year;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.time_fragment, container, false);

        //Download Music from URL
        Update = (Button) rootView.findViewById(R.id.update_time);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hour =  (EditText) rootView.findViewById(R.id.set_hour);
                Minute =  (EditText) rootView.findViewById(R.id.set_minute);
                Second =  (EditText) rootView.findViewById(R.id.set_second);
                Day =  (EditText) rootView.findViewById(R.id.set_day);
                Month =  (EditText) rootView.findViewById(R.id.set_month);
                Year =  (EditText) rootView.findViewById(R.id.set_year);
                int hours = Integer.parseInt(Hour.getText().toString());
                int minutes = Integer.parseInt(Minute.getText().toString());
                int seconds = Integer.parseInt(Second.getText().toString());
                int days = Integer.parseInt(Day.getText().toString());
                int months = Integer.parseInt(Month.getText().toString());
                int years = Integer.parseInt(Year.getText().toString());
                if (seconds >= 0 && seconds <= 59) {
                    if (minutes >= 0 && minutes <= 59) {
                        if (hours >= 0 && hours <= 23) {
                            if (days >= 0 && days <= 28) {
                                if (months >= 1 && months <= 12) {
                                    if (years >= 1900 && months <= 2020) {
                                        setDay(days);
                                        setMonth(months);
                                        setYear(years);
                                        setHour(hours);
                                        setSecond(seconds);
                                        setMinute(minutes);
                                        setMockTime(true);
                                        Toast toast = Toast.makeText(getContext(),
                                                "Update Success!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.TOP, 25, 400);
                                        toast.show();

                                    } else {
                                        Toast toast = Toast.makeText(getContext(),
                                                "Update Failed!\n Years not set properly!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.TOP, 25, 400);
                                        toast.show();
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getContext(),
                                            "Update Failed!\n Months not set properly!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP, 25, 400);
                                    toast.show();

                                }
                            } else {
                                Toast toast = Toast.makeText(getContext(),
                                        "Update Failed!\n Days not set properly!", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP, 25, 400);
                                toast.show();

                            }
                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    "Update Failed!\n Hours not set properly!", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 25, 400);
                            toast.show();

                        }
                    } else {
                        Toast toast = Toast.makeText(getContext(),
                                "Update Failed!\n Minutes not set properly!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 25, 400);
                        toast.show();

                    }
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Update Failed!\n Seconds not set properly!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 25, 400);
                    toast.show();

                }
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
