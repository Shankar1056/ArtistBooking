package bigappcompany.com.artistbooking;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bigappcompany.com.artistbooking.network.ApiUrl;
import bigappcompany.com.artistbooking.network.Download_web;
import bigappcompany.com.artistbooking.network.JsonParser;
import bigappcompany.com.artistbooking.network.OnTaskCompleted;
import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;
import bigappcompany.com.artistbooking.HorizontalCalendar.HorizontalCalendar;
import bigappcompany.com.artistbooking.HorizontalCalendar.HorizontalCalendarListener;
import bigappcompany.com.artistbooking.adapters.MonthsAdapter;

public class BookingActivityNew extends AppCompatActivity implements OnDateClickListener,View.OnClickListener{
    WeekCalendar calendar;
    RecyclerView months_rv;
    ArrayList<String> months;
    int selected=0;
    TextView start,end,startAmPm,endAmPm;
    private HorizontalCalendar horizontalCalendar;
    private ImageView img_sprtr;
    Button bt_book;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_new);
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
        id=getIntent().getStringExtra(JsonParser.ID);
        bt_book=(Button)findViewById(R.id.bt_book);

        bt_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String venue=((EditText)findViewById(R.id.et_venue)).getText().toString().trim();
                String event=((EditText)findViewById(R.id.et_about)).getText().toString().trim();
                boolean canSend=true;
                if(venue.equals("")) {
                    canSend = false;
                    ((EditText) findViewById(R.id.et_venue)).setError("Empty");
                }if(event.equals("")) {
                    canSend = false;
                    ((EditText) findViewById(R.id.et_about)).setError("Empty");
                }
                if(canSend) {
                    Download_web web = new Download_web(getApplicationContext(), new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(String response) {
                            closeDialog();
                            finish();
                            Toast.makeText(getApplicationContext(), "Booking Successful", Toast.LENGTH_LONG).show();
                        }
                    });
                    web.setReqType(false);
                    try {
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(horizontalCalendar.getSelectedDate());

                        web.setData((new JSONObject().put(JsonParser.EVENT_VENUE, venue).put(JsonParser.EVENT_NAME, event).put(JsonParser.CUSTOMER_ID, (getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).getString(JsonParser.CUSTOMER_ID, ""))).put(JsonParser.ARTIST_ID, id).put(JsonParser.EVENT_DATE, date).put(JsonParser.EVENT_START, date + " " + start_hour + ":" + start_min + ":00").put(JsonParser.EVENT_END, date + " " + end_hour + ":" + end_min + ":00")).toString());
                        web.execute(ApiUrl.BOOK);
                        showDailog();
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                }
            }
        });

        start=(TextView)findViewById(R.id.txt_startTime);
        end=(TextView)findViewById(R.id.txt_endTime);

        startAmPm=(TextView)findViewById(R.id.txt_startAmPm);
        endAmPm=(TextView)findViewById(R.id.txt_endAmPm);

        start.setOnClickListener(this);
        end.setOnClickListener(this);
        img_sprtr=(ImageView)findViewById(R.id.time_sprtr);
        ViewTreeObserver vto = img_sprtr.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Picasso.with(img_sprtr.getContext()).load(R.drawable.seperator).resize(img_sprtr.getMeasuredWidth(),img_sprtr.getMeasuredHeight()).centerInside().into(img_sprtr);
                return true;
            }
        });


        setCalendar();
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

    void setCalendar()
    {
        /** end after 1 month from now */

        final Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 3);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        //startDate.add(Calendar.MONTH, -1);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayFormat("EEE")
                .dayNumberFormat("dd")
                .build();

        //horizontalCalendar.setCalendarSpeed();



        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                horizontalCalendar.getmCalendarAdapter().refresh();

                Toast.makeText(BookingActivityNew.this, DateFormat.getDateInstance().format(date) + " is selected!", Toast.LENGTH_SHORT).show();
            }

        });

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

            case R.id.txt_startTime:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookingActivityNew.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        int dummy_time=selectedHour*100+selectedMinute;


                                if(dummy_time>=1200&&dummy_time<2400)
                                {
                                    if(selectedHour!=12)
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
                        setDuration();
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
                mTimePicker1 = new TimePickerDialog(BookingActivityNew.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        int dummy_time=selectedHour*100+selectedMinute;


                                if(dummy_time>=1200&&dummy_time<2400)
                                {
                                    if(selectedHour!=12)
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

                        setDuration();

                    }
                }, hour1, minute1, false);//Yes 24 hour time
                mTimePicker1.setTitle("Select End Time");
                mTimePicker1.show();
        }
    }
    int diff_hr=0,diff_min=0;
    private void setDuration() {
        if(!(end_hour==0&&end_min==0)&&!(start_min==0&&start_hour==0)) {
            int dummy_start = start_hour * 100 + (start_min);
            int dummy_end = end_hour * 100 + (end_min);
            if(dummy_end<=dummy_start)
            {
                dummy_end=dummy_end+2400;
            }
            diff_hr = (dummy_end - dummy_start )/ 100;
            diff_min = 60 - start_min + end_min;
            if (diff_min >= 60)
                diff_min = diff_min - 60;

            ((TextView) findViewById(R.id.txt_diff)).setText(diff_hr + " h " + diff_min + " mn");
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

    ProgressDialog dialog;
    void showDailog()
    {
        dialog=new ProgressDialog(this);
        dialog.setMessage("Booking the Artist, Please Wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    void closeDialog()
    {
        if(dialog!=null)
            dialog.cancel();
    }
}
