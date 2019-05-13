package com.example.gotogether;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberInitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);
        // Initialize Firebase Auth

        findViewById(R.id.signUpButton).setOnClickListener(OnClikListener);
    }

    View.OnClickListener OnClikListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) { //클릭이되었을때
                case R.id.signUpButton:
                    profileupdate();
                    break;
            }
        }
    };

    private void profileupdate(){
        //입력한 Text를 얻어오는 작업
        String name=((EditText)findViewById(R.id.nameEditText)).getText().toString();
        String phoneNumber=((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString();
        String birthDay=((EditText)findViewById(R.id.birthDayEditText)).getText().toString();
        String address=((EditText)findViewById(R.id.addressEditText)).getText().toString();


        //password확인이 맞으면
        if(name.length()>0 && phoneNumber.length()>9 && birthDay.length() > 5 && address.length() >0 ){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
        } else{
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
