package com.example.kir.note;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TextEditor extends AppCompatActivity {

    public static final int VOICE_REC_REQUEST_CODE = 1001;
    public static final String EXTRA_TEXT = "text_key";
    public static final String EXTRA_HEADER = "header_key";
    public static final String EXTRA_NUMBER = "number_key";

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private EditText et, etHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_text_editor);
        setSupportActionBar(toolbar);

        //если метод вернет true то выведеться сообщение об отсутсвии средств для распознования голоса
        if (checkVoiceRecognition()) {
            Toast.makeText(this, R.string.ifNoRec, Toast.LENGTH_LONG).show();
        }

        et = (EditText) findViewById(R.id.et);
        etHeader = (EditText) findViewById(R.id.et_header);

        if (checkIntent()) takeIntent();

    }

    private boolean checkIntent(){
        setNumber(getIntent().getStringExtra(EXTRA_NUMBER));
        if (getNumber().equals("-1")) return false;
        return true;
    }

    private void takeIntent(){
        String savedText = getIntent().getStringExtra(EXTRA_TEXT);
        String savedHeader = getIntent().getStringExtra(EXTRA_HEADER);
        et.setText(savedText + "");
        etHeader.setText(savedHeader + "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        getMenuInflater().inflate(R.menu.menu_text_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String text;
        String header;

        switch (item.getItemId()) {

            case R.id.action_voice:
                //может не самая изящная проверка, но остальные крашаться на некоторых устройствах
                boolean flag = true;
                if (checkVoiceRecognition()) {
                    flag = false;
                }
                speak(this, flag);
                break;


            case R.id.action_save:
                text = et.getText().toString();
                header = etHeader.getText().toString();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(calendar.getTime());

                if (!checkIntent())
                    NoteDB.saveToDB(new View(this), text, header, formattedDate);

                if (checkIntent())
                    NoteDB.updateDB(new View(this), getNumber(), text, header, formattedDate);

                finish();
                break;


            case R.id.action_send:

                text = et.getText().toString();
                header = etHeader.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                if (etHeader.length() > 0)
                    intent.putExtra(Intent.EXTRA_SUBJECT, header);
                if (et.length() > 0)
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    //набор методов для распознования голоса
    public boolean checkVoiceRecognition(){
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        return (activities.size()==0);
    }

    //метод вызывающий активность для распознования голоса
    public void speak(TextEditor view, boolean flag){
        if (flag) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, et.getText().toString());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            startActivityForResult(intent, VOICE_REC_REQUEST_CODE);
        }
        else Toast.makeText(this, R.string.ifNoRec, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //если все ок, то полученные варианты распознования храним в списке
        if (resultCode == RESULT_OK){
            ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //если список не пуст, то берем 1й вариант и заносим его в edittext
            if (!textMatchList.isEmpty()){

                //!!!!!!!!!!!!!!!!!!!!!
               /* if (textMatchList.get(0).contains("search")){
                    String searchQuery = textMatchList.get(0).replace("search", " ");
                    Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                    search.putExtra(SearchManager.QUERY, searchQuery);
                    startActivity(search);
                }
                else {*/
                //lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, textMatchList));

                String text = et.getText().toString();
                //если в едит текст что то уже есть, то после голосового ввода вставляет это плюс пробел, плюс голосовой текст
                if (text.isEmpty())
                    et.setText(textMatchList.get(0));
                else
                    et.setText(text + " " + textMatchList.get(0));
                // }
            }
        }

        else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
            showErrorMsg(R.string.audio_error);
        }

        else if (resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
            showErrorMsg(R.string.client_error);
        }

        else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
            showErrorMsg(R.string.network_error);
        }

        else if (resultCode == RecognizerIntent.RESULT_NO_MATCH){
            showErrorMsg(R.string.nomatch_error);
        }

        else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
            showErrorMsg(R.string.server_error);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    void showErrorMsg(int msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}




