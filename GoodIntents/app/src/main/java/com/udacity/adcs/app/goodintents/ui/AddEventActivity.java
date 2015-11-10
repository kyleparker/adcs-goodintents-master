package com.udacity.adcs.app.goodintents.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.objects.Event;
import com.udacity.adcs.app.goodintents.objects.PersonEvent;
import com.udacity.adcs.app.goodintents.ui.base.BaseActivity;
import com.udacity.adcs.app.goodintents.utils.Constants;
import com.udacity.adcs.app.goodintents.utils.IntentUtils;
import com.udacity.adcs.app.goodintents.utils.StringUtils;
import com.udacity.adcs.app.goodintents.utils.UIUtils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * Created by kyleparker on 11/9/2015.
 */
public class AddEventActivity extends BaseActivity {

    private static TextView mEventDate;
    private static TextView mEventTime;
    private EditText mEditName;
    private EditText mEditDesc;
    private EditText mEditOrganization;

    private TextInputLayout mInputLayoutName;

    private static String mDate;
    private static String mTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        setupToolbar();
        setupView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mActivity.getMenuInflater().inflate(R.menu.add_event, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                boolean success = validateInput(mEditName, mInputLayoutName, R.string.content_error_event_name);

                if (!success) {
                    saveEvent();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save the event info to the database
     */
    private void saveEvent() {
        String message;

        try {
            message = mActivity.getString(R.string.toast_success_event_saved);

            Event event = new Event();
            event.setName(mEditName.getText().toString());
            event.setDescription(mEditDesc.getText().toString());
            event.setOrganization(mEditOrganization.getText().toString());

            Uri uri = mProvider.insertEvent(event);
            long id = Long.valueOf(uri.getLastPathSegment());

            PersonEvent personEvent = new PersonEvent();

//            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Constants.LOCALE_DEFAULT);
//            Date eventDate = formatter.parse(mEditDate.getText().toString());
//            personEvent.setDate(eventDate.getTime());
            personEvent.setEventId(id);
            personEvent.setPersonId(mPerson.getId());

            Intent intent = IntentUtils.newIntent(mActivity, EventDetailActivity.class);
            intent.putExtra(Constants.Extra.EVENT_ID, id);

            mActivity.startActivity(intent);
            mActivity.finish();
        } catch (Exception ex) {
            ex.printStackTrace();
            message = mActivity.getString(R.string.toast_error_saving_event);
        }

        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Setup the toolbar for the activity
     */
    private void setupToolbar() {
        final Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_action_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(mActivity.getString(R.string.title_add_event));
            }
        });
    }

    private void setupView() {
        FloatingActionButton scan = (FloatingActionButton) findViewById(R.id.fab_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(new Intent(mActivity, ScanActivity.class));
            }
        });

        if (UIUtils.isLollipop()) {
            scan.setOnTouchListener(UIUtils.getFABTouchListener(mActivity, scan));
        }

        mEditName = (EditText) findViewById(R.id.edit_event_name);
        mEditDesc = (EditText) findViewById(R.id.edit_event_desc);
        mEditOrganization = (EditText) findViewById(R.id.edit_organization);

        mEventDate = (TextView) findViewById(R.id.event_date);
        mEventDate.setText(StringUtils.getDateString(System.currentTimeMillis(), Constants.FULL_DATE_FORMAT));
        mEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        mEventTime = (TextView) findViewById(R.id.event_time);
        mEventTime.setText(StringUtils.getDateString(System.currentTimeMillis(), Constants.TIME_FORMAT));
        mEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        mInputLayoutName = (TextInputLayout) findViewById(R.id.input_event_name);
    }

    private boolean validateInput(EditText editText, TextInputLayout inputLayout, int message) {
        if (editText.getText().toString().trim().isEmpty()) {
            inputLayout.setError(getString(message));
            requestFocus(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            mTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + ":00";

            SimpleDateFormat f = new SimpleDateFormat(Constants.FULL_TIME_FORMAT);
            try {
                Date d = f.parse(mTime);
                mEventTime.setText(StringUtils.getDateString(d.getTime(), Constants.TIME_FORMAT));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            mDate = String.valueOf(month+1) + "/" + String.valueOf(day) + "/" + String.valueOf(year);

            SimpleDateFormat f = new SimpleDateFormat(Constants.DATE_FORMAT);
            try {
                Date d = f.parse(mDate);
                mEventDate.setText(StringUtils.getDateString(d.getTime(), Constants.FULL_DATE_FORMAT));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
