package com.example.gotogether.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.gotogether.FirebaseHelper2;
import com.example.gotogether.ReportInfo;
import com.example.gotogether.R;
import com.example.gotogether.listener.OnPostListener;
import com.example.gotogether.view.ReadContentsView2;

public class Post2Activity extends BasicActivity {
    private ReportInfo reportInfo;
    private FirebaseHelper2 firebaseHelper2;
    private ReadContentsView2 readContentsView2;
    private LinearLayout contentsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);

        reportInfo = (ReportInfo) getIntent().getSerializableExtra("reportInfo");

        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsView2 = findViewById(R.id.ReadContentsView);

        firebaseHelper2 = new FirebaseHelper2(this);
        firebaseHelper2.setOnPostListener(onPostListener);
        uiUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    reportInfo = (ReportInfo)data.getSerializableExtra("reportinfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                firebaseHelper2.storageDelete(reportInfo);
                return true;
            case R.id.modify:
                myStartActivity(WriteReportActivity.class, reportInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete() {
            Log.e("로그 ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    private void uiUpdate(){
        setToolbarTitle(reportInfo.getTitle());
        readContentsView2.setReportInfo(reportInfo);

    }

    private void myStartActivity(Class c, ReportInfo reportInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("reportInfo", reportInfo);
        startActivityForResult(intent, 0);
    }
}

