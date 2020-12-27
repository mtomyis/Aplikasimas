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

import static com.tomtomy.aplikasimas.MainActivity.EXTRA_jam;
import static com.tomtomy.aplikasimas.MainActivity.EXTRA_jam_selesai;
import static com.tomtomy.aplikasimas.MainActivity.EXTRA_kategri;
import static com.tomtomy.aplikasimas.MainActivity.EXTRA_keterangan;
import static com.tomtomy.aplikasimas.MainActivity.EXTRA_nama;
import static com.tomtomy.aplikasimas.MainActivity.EXTRA_no;
import static com.tomtomy.aplikasimas.MainActivity.EXTRA_solusi;
import static com.tomtomy.aplikasimas.MainActivity.EXTRA_status;
import static com.tomtomy.aplikasimas.MainActivity.EXTRA_tgl;
import static com.tomtomy.aplikasimas.MainActivity.hostname;

public class EditActivity extends AppCompatActivity {

    EditText tgl, jam, jam_selesai, nama, keterangan, solusi, kategori;
    private static String et_json_url = "http://"+hostname+"/masilham/edit.php";
    private static String et_json_urlbelum = "http://"+hostname+"/masilham/editbelum.php";
    private static String et_json_urlhapus = "http://"+hostname+"/masilham/delete.php";
    String stgl, sjam, snama, sspinner, sketerangan, ssolusi, sjam_selesai, tglawal1, tglawal_asli;
    String tno;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mRequestQueue = Volley.newRequestQueue(EditActivity.this);

        init();

        Intent intent = getIntent();
        tno = intent.getStringExtra(EXTRA_no);
        String ttgl = intent.getStringExtra(EXTRA_tgl);
        String tjam = intent.getStringExtra(EXTRA_jam);
        String tnama = intent.getStringExtra(EXTRA_nama);
        String tkategori = intent.getStringExtra(EXTRA_kategri);
        String tKeterangan = intent.getStringExtra(EXTRA_keterangan);
        String tstatus = intent.getStringExtra(EXTRA_status);
        String tjam_selesai = intent.getStringExtra(EXTRA_jam_selesai);
        String tsolusi = intent.getStringExtra(EXTRA_solusi);

        tgl.setText(ttgl);
        jam_selesai.setText(tjam_selesai);
        jam.setText(tjam);
        nama.setText(tnama);
        keterangan.setText(tKeterangan);
        solusi.setText(tsolusi);
        kategori.setText(tkategori);

//        Toast.makeText(getApplicationContext(),"jam selesai :"+tjam_selesai, Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),"solusi :"+tsolusi, Toast.LENGTH_LONG).show();

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
                new DatePickerDialog(EditActivity.this, datek, myCalendark
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
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {

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
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {

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
        tgl = findViewById(R.id.id_etgl);
        jam_selesai = findViewById(R.id.id_ejam_selesai);
        jam = findViewById(R.id.id_ejam);
        nama = findViewById(R.id.id_euser);
        keterangan = findViewById(R.id.id_eket);
        solusi = findViewById(R.id.id_esolusi);
        kategori = findViewById(R.id.id_ekategori);
    }

    public void done(View view) {
        final String stgl = tgl.getText().toString().trim();
        final String sjam = jam.getText().toString().trim();
        final String sjam_selesai = jam_selesai.getText().toString().trim();
        final String snama = nama.getText().toString().trim();
        final String sketerangan = keterangan.getText().toString().trim();
        final String skategori = kategori.getText().toString().trim();
        final String ssolusi = solusi.getText().toString().trim();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, et_json_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String berhasil = jsonObject.getString("success");

                                if (berhasil.equals("1")){
                                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(EditActivity.this,"Success", Toast.LENGTH_SHORT).show();

                                }else if (berhasil.equals("0")){
                                    Toast.makeText(EditActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditActivity.this,"Gagal " +e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EditActivity.this,"Gagal " +error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("tgl", stgl);
                    params.put("jam", sjam);
                    params.put("jam_selesai", sjam_selesai);
                    params.put("user",snama);
                    params.put("kategori",skategori);
                    params.put("keterangan", sketerangan);
                    params.put("solusi", ssolusi);
                    params.put("no", tno);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }

    public void progres(View view) {
        final String stgl = tgl.getText().toString().trim();
        final String sjam = jam.getText().toString().trim();
        final String sjam_selesai = jam_selesai.getText().toString().trim();
        final String snama = nama.getText().toString().trim();
        final String sketerangan = keterangan.getText().toString().trim();
        final String skategori = kategori.getText().toString().trim();
        final  String ssolusi = solusi.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, et_json_urlbelum,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String berhasil = jsonObject.getString("success");

                            if (berhasil.equals("1")){
                                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(EditActivity.this,"Success", Toast.LENGTH_SHORT).show();

                            }else if (berhasil.equals("0")){
                                Toast.makeText(EditActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditActivity.this,"Gagal " +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditActivity.this,"Gagal " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tgl", stgl);
                params.put("jam", sjam);
                params.put("jam_selesai", sjam_selesai);
                params.put("user",snama);
                params.put("kategori",skategori);
                params.put("keterangan", sketerangan);
                params.put("solusi", ssolusi);
                params.put("no", tno);
                return params;
            }
        };

        RequestQueue rrequestQueue = Volley.newRequestQueue(this);
        rrequestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iddelete:
                hapusdata();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hapusdata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, et_json_urlhapus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String berhasil = jsonObject.getString("success");

                            if (berhasil.equals("1")){
                                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(EditActivity.this,"Success", Toast.LENGTH_SHORT).show();

                            }else if (berhasil.equals("0")){
                                Toast.makeText(EditActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditActivity.this,"Gagal " +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditActivity.this,"Gagal " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("no", tno);
                return params;
            }
        };

        RequestQueue rrequestQueue = Volley.newRequestQueue(this);
        rrequestQueue.add(stringRequest);
    }
}
