package com.example.linux.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity  extends  TabActivity {


    //public static String ip="http://10.87.196.113/";
    public static String ip="http://kkucleft.kku.ac.th/";
    //http://kkucleft.kku.ac.th/json2/selProvince.php


    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
   // private EditText  edtDate;
    private int year, month, day;

    private Spinner spin1;
    public   String[] iplTeam;

    //String url = "http://10.87.196.113/json2/selProvince.php";
    String url =   ip  +  "json2/selProvince.php";

    InputStream is=null;

    String result=null;
    String line=null;

    String Id_per;
    Intent intent;

  //  int id_weight;
    public String[]  arr_prov_id=new String[1000];


    EditText name;
    EditText lastname;
    EditText id_card;
    EditText  telephone;
    RadioGroup  id_sex;
    TextView   birthdate;
    EditText   address;
    AutoCompleteTextView   province_id;
    EditText   diagnosis;

    //insert  value
    String  strname;
    String  strlastname;
    String   strid_card;
    String strtelephone;




    String  val_sex;


    String  str_id_sex1;
    String strbirthdate;
    String  straddress;
    String  strprovince_id;
    String   strdiagnosis;





    public   String[]  arrProv=new String[1000];

     public   String[]   allprovince=new String[1000];

    int code;
    String  testjson;

    String  stauts_insert = "บันทึกสำเร็จ";






    //public  String urlinsert="http://10.87.196.113/json2/insertPatient.php";
     public  String urlinsert=  ip +  "json2/insertPatient.php";

    //---- Autocomplete  PROVINCE จัวงหว
    private AutoCompleteTextView autocomplete1;
    private  List<String> arrList = new ArrayList<String>();
    private  ArrayAdapter<String> adapter;


    //----- spinner  text ---
    String[] arr = { "---เลือก---","ปากแหว่ง", "เพดานโหว่", "ปากแหว่งและเพดานโหว่", "อื่นๆ" };
    Spinner  spinner1;

    //tabbar
    TabHost mTabHost;

    //เพิ่ม field
    String  strdetail_diagnosis;  //ระบุการวินิจฉัยโรค อื่นๆ
    EditText detail_diagnosis;

    String  strinformative_name;  //ชื่อผู้ให้ ข้อมูล
    EditText informative_name;

    String  strinformative_lastname; //นามสกุลผู้ให้ข้อมูล
    EditText informative_lastname;

    EditText informative_tel; //เบอร์โทรศัพท์ผู้ให้ข้อมูล
    String  strinformative_tel;

    ImageButton img1; //image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //Main program OK
       // setContentView(R.layout.loginpage);

        intent=getIntent();
        Id_per=intent.getStringExtra("Id_per");

/*    //check id_per from  table intent
        final AlertDialog.Builder adb=new AlertDialog.Builder(this);
        AlertDialog ad=adb.create();
        ad.setMessage(Id_per);
        ad.show();
 */

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

         mTabHost = (TabHost) findViewById(android.R.id.tabhost);
         mTabHost = getTabHost();
         mTabHost = getTabHost();
         mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("Page 1").setContent(R.id.tab1));
         mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("Page 2").setContent(R.id.tab2));
        // mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("TAB 3").setContent(R.id.tab3));

        dateView = (TextView) findViewById(R.id.birthdate);
       // edtDate = (EditText) findViewById(R.id.edtDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR) ;
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);


        //String url = "http://10.87.196.113/json2/selProvince.php";
        //callJSON(); //CALL JSON MYSQL SERVER  for  spinner


        autoProvince();  //call  autocomplete province
        autocomplete1 = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, arrList);
        autocomplete1.setAdapter(adapter);


        name=(EditText)findViewById(R.id.name);
        lastname=(EditText)findViewById(R.id.lastname);
        id_card=(EditText)findViewById(R.id.id_card);
        telephone=(EditText)findViewById(R.id.telephone);

        id_sex=(RadioGroup)findViewById(R.id.id_sex);

        birthdate=(TextView)findViewById(R.id.birthdate);
        address=(EditText)findViewById(R.id.address);
       // province_id=(Spinner)findViewById(R.id.province_id);
         province_id=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        // diagnosis=(EditText)findViewById(R.id.diagnosis);
       // diagnosis=(Spinner)findViewById(R.id.spinner1);


        spinner1=(Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> arrAd=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,arr);
        arrAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrAd);

        //ระบุการวินิจฉัยโรค อื่นๆ
        detail_diagnosis=(EditText)findViewById(R.id.detail_diagnosis);
       // detail_diagnosis.setEnabled(false);
       // strdetail_diagnosis=detail_diagnosis.getText().toString();



        //ชื่อผู้ให้ ข้อมูล
        informative_name=(EditText)findViewById(R.id.informative_name);
        informative_name.setText("วิชิต");
        strinformative_name=informative_name.getText().toString();

        ////นามสกุลผู้ให้ข้อมูล
        informative_lastname=(EditText)findViewById(R.id.informative_lastname);
        informative_lastname.setText("ศรีเชียง");
        strinformative_lastname=informative_lastname.getText().toString();


        //เบอร์โทรศัพท์ผู้ให้ข้อมูล
        informative_tel=(EditText)findViewById(R.id.informative_tel);
        informative_tel.setText("0858539042");
        strinformative_tel=informative_tel.getText().toString();


        //testing insert field
        name.setText("กานดา");
        lastname.setText("บุญประครอง");
        id_card.setText("362510478524");
        telephone.setText("0855241258");
        address.setText("857/74 ถ.ชาตุผดุง ต.ในเมือง อ.เมือง");
     //   diagnosis.setText("เพดานโหว่");
      //insert  to table


        /*  //disable  spinner
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner1.getSelectedItem().toString() == "อื่นๆ") {
                    detail_diagnosis.setEnabled(true);
                    detail_diagnosis.setText("เป็นนอกเหนือจากที่ระบุ");
                    strdetail_diagnosis=detail_diagnosis.getText().toString();
                } else {
                    detail_diagnosis.setText("");
                    detail_diagnosis.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
*/


        Button btn_save=(Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strname = name.getText().toString();
                strlastname = lastname.getText().toString();
                strid_card = id_card.getText().toString();
                strtelephone = telephone.getText().toString();
                val_sex = ((RadioButton) findViewById(id_sex.getCheckedRadioButtonId())).getText().toString(); //เพศ
                strbirthdate = birthdate.getText().toString();
                straddress = address.getText().toString();
                //   strprovince_id = province_id.getSelectedItem().toString();
                strprovince_id = province_id.getText().toString();
                //  strdiagnosis = diagnosis.getText().toString();
                strdiagnosis = spinner1.getSelectedItem().toString();
                // testalert("testing to click!!");

                insertPatient();
                // insertPatient2();
            }
        });

    }








    private void insertPatient2()
    {
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("id_user","income"));

        params.add(new BasicNameValuePair("name", strname));
        params.add(new BasicNameValuePair("lastname", strlastname));
        params.add(new BasicNameValuePair("id_card", strid_card));
        params.add(new BasicNameValuePair("telephone", strtelephone));
        params.add(new BasicNameValuePair("id_sex", val_sex));
        params.add(new BasicNameValuePair("birthdate", strbirthdate));
        params.add(new BasicNameValuePair("address", straddress));
        params.add(new BasicNameValuePair("province_id", strprovince_id));
        params.add(new BasicNameValuePair("diagnosis", strdiagnosis));

        /*
        params.add(new BasicNameValuePair("detail_diagnosis", strdetail_diagnosis )); //ระบุการวินิจฉัยโรค อื่นๆ
        params.add(new BasicNameValuePair("informative_name", strinformative_name )); //ชื่อผู้ให้ ข้อมูล
        params.add(new BasicNameValuePair("informative_lastname", strinformative_lastname )); //นามสกุลผู้ให้ข้อมูล
        params.add(new BasicNameValuePair("informative_tel", strinformative_tel )); //เบอร์โทรศัพท์ผู้ให้ข้อมูล
        */

        try{
            JSONArray data = new  JSONArray(getHttpPost( urlinsert ,params ));  //post value in table


            for(int i=0;i<data.length();i++)
            {
                JSONObject c=data.getJSONObject(i);
               // stackArr[i]=c.getInt("val");
                //code= c.getInt("code");
                String code=c.getString("code");
                String name= c.getString("name");


            }

            if( code == 1 )
            {
                Toast.makeText(getBaseContext(),"บันทึกสำเร็จ  " + name ,Toast.LENGTH_SHORT).show();
            }else if(  code==0 )
            {
                Toast.makeText(getBaseContext(),"Sorry, Try Again",Toast.LENGTH_LONG).show();
            }

             /*
            code= json_data.getInt("code");
            String name= json_data.getString("name");
            if( code==1 )
            {
                Toast.makeText(getBaseContext(),"บันทึกสำเร็จ  " + name ,Toast.LENGTH_SHORT).show();
            }else if(  code==0 )
            {
                Toast.makeText(getBaseContext(),"Sorry, Try Again",Toast.LENGTH_LONG).show();
            }
             */

            /// openChart( stackArr);



        }catch (JSONException e)
        {
            e.printStackTrace();
            Log.e("Fail ", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address", Toast.LENGTH_LONG);
            StringBuilder sb=new StringBuilder();
        }

    }

    private void insertPatient()   // `tb_patientcleft`
    {


        ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("name",strname ));
        nameValuePairs.add(new BasicNameValuePair("lastname",strlastname ));
        nameValuePairs.add(new BasicNameValuePair("id_card",strid_card ));
        nameValuePairs.add(new BasicNameValuePair("telephone",strtelephone ));
        nameValuePairs.add(new BasicNameValuePair("id_sex",val_sex ));
        nameValuePairs.add(new BasicNameValuePair("birthdate",strbirthdate ));
        nameValuePairs.add(new BasicNameValuePair("address",straddress ));
        nameValuePairs.add(new BasicNameValuePair("province_id",strprovince_id ));
        nameValuePairs.add(new BasicNameValuePair("diagnosis",strdiagnosis ));

        nameValuePairs.add(new BasicNameValuePair("detail_diagnosis", strdetail_diagnosis )); //ระบุการวินิจฉัยโรค อื่นๆ   //strdetail_diagnosis
        nameValuePairs.add(new BasicNameValuePair("informative_name", strinformative_name )); //ชื่อผู้ให้ ข้อมูล
        nameValuePairs.add(new BasicNameValuePair("informative_lastname", strinformative_lastname )); //นามสกุลผู้ให้ข้อมูล
        nameValuePairs.add(new BasicNameValuePair("informative_tel", strinformative_tel )); //เบอร์โทรศัพท์ผู้ให้ข้อมูล

        try
        {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost   httpPost=new  HttpPost(urlinsert);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));  //insert thai in support mysql
            HttpResponse response=httpClient.execute(httpPost);
            HttpEntity entity=response.getEntity();
            is=entity.getContent();
            Log.e("pass 1","connection success");

        }catch (Exception e)
        {
            Log.e("Fail ",e.toString());
            Toast.makeText(getApplicationContext(),"Invalid IP Address",Toast.LENGTH_LONG);
            StringBuilder sb=new StringBuilder();

        }


        try{


            /*
            String data  = URLEncoder.encode("name", "UTF-8")  + "=" + URLEncoder.encode( strname, "UTF-8");
            data += URLEncoder.encode("lastname", "UTF-8")  + "=" + URLEncoder.encode( strlastname , "UTF-8");

            URL url = new URL(urlinsert);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            */


           // BufferedReader reader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            BufferedReader reader=new BufferedReader(new InputStreamReader(is,"UTF-8"),8);

            StringBuilder sb=new StringBuilder();
            while((line=reader.readLine()) != null )
            {
                sb.append(line+"\n");
               // wr.write(data);
               // wr.flush();

            }



            is.close();
            result=sb.toString();
            Log.e("pass 2 ", "connection success");




        }catch (Exception e)
        {
            Log.e("Fail 2", e.toString());
        }


        try {


            JSONObject json_data=new JSONObject(result);
            code= json_data.getInt("code");
            //String name= json_data.getString("name");

            if( code==1 )
            {
               // Toast.makeText(getBaseContext(),"บันทึกสำเร็จ   : " +  strdetail_diagnosis  ,Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(),"บันทึกสำเร็จ"  ,Toast.LENGTH_SHORT).show();
            }else if(  code==0 )
            {
                Toast.makeText(getBaseContext(),"Sorry, Try Again", Toast.LENGTH_LONG).show();
            }





        }catch (Exception e)
        {
            Log.e("Fail 3", e.toString());
        }



    }






    public void testalert(String str)
    {
         final AlertDialog.Builder adb=new AlertDialog.Builder(this);
        AlertDialog ad=adb.create();
        ad.setMessage(str);
        ad.show();
    }


    public void  autoProvince()
    {
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        try{
            //String url = "http://10.87.196.113/json2/selProvince.php";
            JSONArray data = new  JSONArray(getHttpPost(url,params));  //post value in table
            for(int i=0;i<data.length();i++)
            {
                JSONObject c = data.getJSONObject(i);
                // id_weight=c.getInt("id_weight");
                arr_prov_id[i]=c.getString("PROVINCE_ID");
                arrProv[i]=c.getString("PROVINCE_NAME");
                allprovince[i]=arr_prov_id[i]+ "-" + arrProv[i];
                arrList.add(   arrProv[i] + "-" + arr_prov_id[i] );
            }

            /*
            Log.e("auto complete province","");

            final AlertDialog.Builder adb=new AlertDialog.Builder(this);
            AlertDialog ad=adb.create();
            ad.setMessage("autocomplete province " + arrProv );
            ad.show();
            */

        }catch (JSONException e) {
            // e.printStackTrace();
            Log.e("false autocomplete", e.toString());

        }

    }



     public void callJSON()
     {
         List<NameValuePair> params=new ArrayList<NameValuePair>();
         try{
             //String url = "http://10.87.196.113/json2/selProvince.php";
             JSONArray data = new  JSONArray(getHttpPost(url,params));  //post value in table
             for(int i=0;i<data.length();i++)
             {
                 JSONObject c = data.getJSONObject(i);
                // id_weight=c.getInt("id_weight");
                  arr_prov_id[i]=c.getString("PROVINCE_ID");
                  arrProv[i]=c.getString("PROVINCE_NAME");
                  allprovince[i]=arr_prov_id[i]+ "-" + arrProv[i];
             }
             String[] iplTeam= {"KKR", "CSK", "RR", "KXIP", "RR", "MI" };
          //   spin1=(Spinner) findViewById(R.id.province_id); //OK
             ArrayAdapter adapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item,  allprovince  );
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             spin1.setAdapter(adapter);
         }catch (JSONException e)
         {
             // e.printStackTrace();
             Log.e("try fail 1",e.toString());
             final AlertDialog.Builder adb=new AlertDialog.Builder(this);
             AlertDialog ad=adb.create();
             ad.setMessage("fail connect  SERVER !!");
             ad.show();
         }
     }



    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();




        } catch (IOException e) {
            e.printStackTrace();




        }
        return str.toString();
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year  , month, day);
        }
        return null;
    }



    private DatePickerDialog.OnDateSetListener myDateListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1  , arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {


        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year+543));


        //edtDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));

    }



    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), " เลือกวันที่  ", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
