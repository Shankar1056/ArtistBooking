package bigappcompany.com.artistbooking;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;
import bigappcompany.com.artistbooking.adapters.MonthsAdapter;

public class BookingActivity extends AppCompatActivity implements OnDateClickListener,View.OnClickListener{
    WeekCalendar calendar;
    RecyclerView months_rv;
    ArrayList<String> months;
    int selected=0;
    TextView txt_mng,txt_evng,txt_nt;
    TextView start,end,startAmPm,endAmPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" ");
        calendar=(WeekCalendar)findViewById(R.id.weekV);
        calendar.setOnDateClickListener(this);
        months_rv=(RecyclerView)findViewById(R.id.months_rv);

        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        months_rv.setLayoutManager(manager);
        setupData();
        txt_mng=(TextView)findViewById(R.id.txt_mng);
        txt_evng=(TextView)findViewById(R.id.txt_evng);
        txt_nt=(TextView)findViewById(R.id.txt_nt);
        (txt_mng).setOnClickListener(this);
        (txt_evng).setOnClickListener(this);
        (txt_nt).setOnClickListener(this);

        start=(TextView)findViewById(R.id.txt_startTime);
        end=(TextView)findViewById(R.id.txt_endTime);

        startAmPm=(TextView)findViewById(R.id.txt_startAmPm);
        endAmPm=(TextView)findViewById(R.id.txt_endAmPm);

        start.setOnClickListener(this);
        end.setOnClickListener(this);
    }

    private void setupData() {
        months=new ArrayList<>();
        months.add("JAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAY");
        months.add("JUN");
        months.add("JUL");
        months.add("AUG");
        months.add("SEP");
        months.add("OCT");
        months.add("NOV");
        months.add("DEC");

        try {

            months_rv.setAdapter(new MonthsAdapter(months));
            ((MonthsAdapter)months_rv.getAdapter()).setSelected(DateTime.now().getMonthOfYear()-1);
            date_selected=DateTime.now();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    DateTime date_selected;
    @Override
    public void onDateClick(DateTime dateTime) {
        date_selected=dateTime;
        ((MonthsAdapter)months_rv.getAdapter()).setSelected(dateTime.getMonthOfYear()-1);
        ((LinearLayoutManager)months_rv.getLayoutManager()).scrollToPositionWithOffset(dateTime.getMonthOfYear(),20);
    }


    public void setSelected(int selected) {
        date_selected=date_selected.withDate(date_selected.getYear(),selected+1,1);
        //date_selected=date_selected.withMonthOfYear(selected+1);
        Toast.makeText(this,date_selected.toString(),Toast.LENGTH_LONG).show();
        calendar.setSelectedDate(date_selected);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txt_evng:
                if(selected!=1) {
                    txt_evng.setBackgroundResource(R.drawable.holo_roundc);
                    txt_mng.setBackgroundResource(0);
                    txt_nt.setBackgroundResource(0);
                    selected = 1;
                    reset();
                }
                break;

            case R.id.txt_mng:
                if(selected!=0) {
                    selected = 0;
                    txt_evng.setBackgroundResource(0);
                    txt_mng.setBackgroundResource(R.drawable.holo_roundc);
                    txt_nt.setBackgroundResource(0);
                    reset();
                }
                break;

            case R.id.txt_nt:
                if(selected!=2) {
                    selected = 2;
                    txt_evng.setBackgroundResource(0);
                    txt_mng.setBackgroundResource(0);
                    txt_nt.setBackgroundResource(R.drawable.holo_roundc);
                    reset();
                }
                break;
            case R.id.txt_startTime:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        int dummy_time=selectedHour*100+selectedMinute;
                        if(selected==2)
                        {
                            if(selectedHour<12)
                            {
                                dummy_time=dummy_time+2400;
                            }
                        }
                        if(slots[selected][0]<=dummy_time) {
                            if(slots[selected][1]>=dummy_time) {

                                if(dummy_time>1200&&dummy_time<2400)
                                {
                                    selectedHour=selectedHour-12;
                                    startAmPm.setText("PM");
                                }
                                else
                                    startAmPm.setText("AM");
                                start_hour=selectedHour;
                                start_min=selectedMinute;
                                String hr=selectedHour+"";
                                String mn=selectedMinute+"";
                                if(start_hour<10)
                                    hr="0"+hr;
                                if(start_min<10)
                                    mn="0"+mn;
                                start.setText(hr + " : " + mn);
                            }
                            else
                            {
                                Toast.makeText(BookingActivity.this, "not satisfied", Toast.LENGTH_LONG).show();
                                start.setText("00 : 00");
                            }
                        }
                        else {
                            Toast.makeText(BookingActivity.this, "not satisfied", Toast.LENGTH_LONG).show();
                            start.setText("00 : 00");
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Start Time");
                mTimePicker.show();
                break;
            case R.id.txt_endTime:
                Calendar mcurrentTime1 = Calendar.getInstance();
                int hour1 = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                int minute1 = mcurrentTime1.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker1;
                mTimePicker1 = new TimePickerDialog(BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        int dummy_time=selectedHour*100+selectedMinute;
                        if(selected==2)
                        {
                            if(selectedHour<12)
                            {
                                dummy_time=dummy_time+2400;
                            }
                        }
                        if(slots[selected][0]<=dummy_time) {
                            if(slots[selected][1]>=dummy_time) {
                                if(dummy_time>1200&&dummy_time<2400)
                                {
                                    selectedHour=selectedHour-12;
                                    endAmPm.setText("PM");
                                }
                                else
                                    endAmPm.setText("AM");
                                end_hour=selectedHour;
                                end_min=selectedMinute;
                                String hr=selectedHour+"";
                                String mn=selectedMinute+"";
                                if(start_hour<10)
                                    hr="0"+hr;
                                if(start_min<10)
                                    mn="0"+mn;
                                end.setText(hr + " : " + mn);
                            }
                            else {
                                Toast.makeText(BookingActivity.this, "not satisfied", Toast.LENGTH_LONG).show();
                                end.setText("00 : 00");
                            }
                        }
                        else {
                            Toast.makeText(BookingActivity.this, "not satisfied", Toast.LENGTH_LONG).show();
                            end.setText("00 : 00");
                        }

                    }
                }, hour1, minute1, false);//Yes 24 hour time
                mTimePicker1.setTitle("Select End Time");
                mTimePicker1.show();

        }
    }
    int[][] slots=new int[][]{{600,1400},{1600,2100},{2200,2700}};
    int start_hour,start_min,end_hour,end_min;
    void reset(){
        start.setText("00 : 00");
        startAmPm.setText("AM");
        end.setText("00 : 00");
        endAmPm.setText("AM");
        start_hour=0;
        start_min=0;
    }
}
