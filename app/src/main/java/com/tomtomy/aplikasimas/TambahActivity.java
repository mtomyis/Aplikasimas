package com.tomtomy.aplikasimas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.tomtomy.aplikasimas.MainActivity.hostname;

public class TambahActivity extends AppCompatActivity {

    EditText tgl, jam, nama, keterangan, solusi, jam_selesai;
    Spinner spinner;
    String stgl, sjam, snama, sspinner, sketerangan, ssolusi, sjam_selesai, tglawal1, tglawal_asli;
    private static String et_json_url = "http://"+hostname+"/masilham/simpan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        init();

        final Calendar myCalendark = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datek = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendark.set(Calendar.YEAR, year);
                myCalendark.set(Calendar.MONTH, month);
                myCalendark.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tgl.setText(sdf.format(myCalendark.getTime()));

                tglawal1 = tgl.getText().toString().trim();
                //int tgl = Integer.parseInt(ambiltanggalnya);
                Date dpersem = null;
                try {
                    dpersem = sdf.parse(tglawal1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(LihatPengingatJagungActivity.this, "tanggal: "+ dpersem.getDate() +"/"+(dpersem.getMonth()+1) +"/"+ (dpersem.getYear()+1900), Toast.LENGTH_SHORT).show();
                tglawal_asli = ""+(dpersem.getYear()+1900)+"-"+(dpersem.getMonth()+1) +"-"+dpersem.getDate();

                //Toast.makeText(LihatPengingatJagungActivity.this, "tanggal: "+tanggalpersemaian, Toast.LENGTH_SHORT).show();
            }
        };

        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TambahActivity.this, datek, myCalendark
                        .get(Calendar.YEAR), myCalendark.get(Calendar.MONTH), myCalendark.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TambahActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        jam.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Jam");
                mTimePicker.show();
            }
        });

        jam_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TambahActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        jam_selesai.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Jam selesai");
                mTimePicker.show();
            }
        });
    }

    private void init() {
        spinner = findViewById(R.id.id_kategori);
        tgl = findViewById(R.id.id_tgl);
        jam = findViewById(R.id.id_jam);
        jam_selesai = findViewById(R.id.id_jam_selesai);
        nama = findViewById(R.id.id_user);
        keterangan = findViewById(R.id.id_ket);
        solusi = findViewById(R.id.id_solusi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idsave:
                simpandata();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void simpandata() {
//        stgl = tgl.getText().toString().trim();
        sjam = jam.getText().toString().trim();
        sjam_selesai = jam_selesai.getText().toString().trim();
        snama = nama.getText().toString().trim();
        sketerangan = keterangan.getText().toString().trim();
        sspinner = spinner.getSelectedItem().toString();
        ssolusi = solusi.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, et_json_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String berhasil = jsonObject.getString("success");

                            if (berhasil.equals("1")){
                                Intent intent = new Intent(TambahActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(TambahActivity.this,"Success", Toast.LENGTH_SHORT).show();

                            }else if (berhasil.equals("0")){
                                Toast.makeText(TambahActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahActivity.this,"Gagal " +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahActivity.this,"Gagal " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tgl", tglawal_asli);
                params.put("jam", sjam);
                params.put("jam_selesai", sjam_selesai);
                params.put("user",snama);
                params.put("kategori",sspinner);
                params.put("keterangan", sketerangan);
                params.put("solusi", ssolusi);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
