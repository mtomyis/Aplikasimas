package com.tomtomy.aplikasimas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tomtomy.aplikasimas.adaptor.adaptormas;
import com.tomtomy.aplikasimas.model.modelmas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MainActivity extends AppCompatActivity implements adaptormas.OnItemClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

//    public static String hostname = "tipoliwangi.000webhostapp.com";
    public static String hostname = "192.168.1.11";

    EditText tglawal, tglakhir;
    Button filter;
    String vtglawal, vtglakhir, tglakhir_asli, tglawal_asli;

    public static final String EXTRA_no = "no";
    public static final String EXTRA_tgl = "tgl";
    public static final String EXTRA_jam_selesai = "jam_selesai";
    public static final String EXTRA_jam = "jam";
    public static final String EXTRA_nama = "nama";
    public static final String EXTRA_kategri = "kategori";
    public static final String EXTRA_keterangan = "keterangan";
    public static final String EXTRA_status = "status";
    public static final String EXTRA_solusi = "solusi";
    private final static int KODEINTERNET = 0;

    ProgressBar p_Tunggu;

    private RecyclerView mRecyclerView;
    private adaptormas mmasAdaptor;
    private ArrayList<modelmas> mmasModel;
    private RequestQueue mRequestQueue;
    private static String et_json_url = "http://"+hostname+"/masilham/lengkap.php";
    private static String et_json_urlfilter = "http://"+hostname+"/masilham/filter.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p_Tunggu = findViewById(R.id.idtunggum);
        checkReadExternalStoragePermission();

        filter = findViewById(R.id.btnfilter);
        tglawal = findViewById(R.id.tglawal);
        tglakhir = findViewById(R.id.tglakhir);

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //digae manual ae wess
        mmasModel = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        parseJSON();

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tglakhir.setText(sdf.format(myCalendar.getTime()));

                vtglakhir = tglakhir.getText().toString().trim();
                //int tgl = Integer.parseInt(ambiltanggalnya);
                Date dpersem = null;
                try {
                    dpersem = sdf.parse(vtglakhir);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(LihatPengingatJagungActivity.this, "tanggal: "+ dpersem.getDate() +"/"+(dpersem.getMonth()+1) +"/"+ (dpersem.getYear()+1900), Toast.LENGTH_SHORT).show();
                tglakhir_asli = ""+(dpersem.getYear()+1900)+"-"+(dpersem.getMonth()+1) +"-"+dpersem.getDate();

                //Toast.makeText(LihatPengingatJagungActivity.this, "tanggal: "+tanggalpersemaian, Toast.LENGTH_SHORT).show();
            }
        };

        final Calendar myCalendark = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datek = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendark.set(Calendar.YEAR, year);
                myCalendark.set(Calendar.MONTH, month);
                myCalendark.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tglawal.setText(sdf.format(myCalendark.getTime()));

                vtglawal = tglawal.getText().toString().trim();
                //int tgl = Integer.parseInt(ambiltanggalnya);
                Date dpersem = null;
                try {
                    dpersem = sdf.parse(vtglawal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(LihatPengingatJagungActivity.this, "tanggal: "+ dpersem.getDate() +"/"+(dpersem.getMonth()+1) +"/"+ (dpersem.getYear()+1900), Toast.LENGTH_SHORT).show();
                tglawal_asli = ""+(dpersem.getYear()+1900)+"-"+(dpersem.getMonth()+1) +"-"+dpersem.getDate();

                //Toast.makeText(LihatPengingatJagungActivity.this, "tanggal: "+tanggalpersemaian, Toast.LENGTH_SHORT).show();
            }
        };

        tglawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, datek, myCalendark
                        .get(Calendar.YEAR), myCalendark.get(Calendar.MONTH), myCalendark.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tglakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void checkReadExternalStoragePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) ==
                    PackageManager.PERMISSION_GRANTED) {
            } else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)) {
                    Toast.makeText(this, "App needs to view permission", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[] {Manifest.permission.INTERNET},
                        KODEINTERNET);
            }
        } else {
            // Start cursor loader
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case KODEINTERNET:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void parseJSON() {
        mmasModel.clear();
        p_Tunggu.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, et_json_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());
                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                String no = result.getString("no");
                                String tgl = result.getString("tgl");
                                String jam = result.getString("jam");
                                String jam_selesai = result.getString("jamm");
                                String nama = result.getString("user");
                                String kategori = result.getString("kategori");
                                String keterangan = result.getString("keterangan");
                                String solusi =result.getString("jammm");
                                String status = result.getString("status");

                                mmasModel.add(new modelmas(no, tgl, jam, jam_selesai, nama, kategori, keterangan, solusi, status));
                            }
                            p_Tunggu.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mmasAdaptor= new adaptormas(MainActivity.this, mmasModel);
                            mRecyclerView.setAdapter(mmasAdaptor);
                            mmasAdaptor.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Gagal " + e.toString(), Toast.LENGTH_SHORT).show();
                            p_Tunggu.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Gagal meneh " + error.toString(), Toast.LENGTH_SHORT).show();
                p_Tunggu.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        mRequestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 60000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent detail = new Intent(this, EditActivity.class);
        modelmas clicked = mmasModel.get(position);

        detail.putExtra(EXTRA_no, clicked.getNo());
        detail.putExtra(EXTRA_tgl, clicked.getTgl());
        detail.putExtra(EXTRA_jam, clicked.getJam());
        detail.putExtra(EXTRA_jam_selesai, clicked.getJam_Selesai());
        detail.putExtra(EXTRA_nama, clicked.getUser());
        detail.putExtra(EXTRA_kategri, clicked.getKategori());
        detail.putExtra(EXTRA_keterangan, clicked.getKeterangan());
        detail.putExtra(EXTRA_solusi, clicked.getSolusi());
        detail.putExtra(EXTRA_status, clicked.getStatus());
        startActivity(detail);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idprint:
                printdong();
                return true;
            case R.id.idrefresh:
                tglawal.setText(null);
                tglakhir.setText(null);
                parseJSON();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void printdong() {
        File sd = Environment.getExternalStorageDirectory();
        String csvFile = "myData.xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {
            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);

            sheet.addCell(new Label(0, 0, "UserName")); // column and row
            sheet.addCell(new Label(1, 0, "PhoneNumber"));

//            if (cursor.moveToFirst()) {
//                do {
//                    String title = cursor.getString(cursor.getColumnIndex("user_name"));
//                    String phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));
//
//                    int i = cursor.getPosition() + 1;
//                    sheet.addCell(new Label(0, i, title));
//                    sheet.addCell(new Label(1, i, phoneNumber));
//                } while (cursor.moveToNext());
//            }
//            //closing cursor
//            cursor.close();
            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(), "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tambah(View view) {
        Intent intent = new Intent(this, TambahActivity.class);
        startActivity(intent);
        finish();
    }

    public void filter(View view) {

        mmasModel.clear();
        p_Tunggu.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, et_json_urlfilter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());
                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                String no = result.getString("no");
                                String tgl = result.getString("tgl");
                                String jam = result.getString("jam");
                                String jam_selesai = result.getString("jamm");
                                String nama = result.getString("user");
                                String kategori = result.getString("kategori");
                                String keterangan = result.getString("keterangan");
                                String solusi =result.getString("jammm");
                                String status = result.getString("status");

                                mmasModel.add(new modelmas(no, tgl, jam, jam_selesai, nama, kategori, keterangan, solusi, status));
                            }
                            p_Tunggu.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mmasAdaptor= new adaptormas(MainActivity.this, mmasModel);
                            mRecyclerView.setAdapter(mmasAdaptor);
                            mmasAdaptor.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Gagal " + e.toString(), Toast.LENGTH_SHORT).show();
                            p_Tunggu.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Gagal meneh " + error.toString(), Toast.LENGTH_SHORT).show();
                p_Tunggu.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("awal", tglawal_asli);
                params.put("akhir", tglakhir_asli);
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 60000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }
}