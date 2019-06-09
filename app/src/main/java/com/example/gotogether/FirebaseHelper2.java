package com.example.gotogether;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.gotogether.listener.OnPostListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.gotogether.Util.isStorageUrl;
import static com.example.gotogether.Util.showToast;
import static com.example.gotogether.Util.storageUrlToName;

public class FirebaseHelper2 {
    private Activity activity;
    private OnPostListener onPostListener;
    private int successCount;

    public FirebaseHelper2(Activity activity) {
        this.activity = activity;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        this.onPostListener = onPostListener;
    }

    public void storageDelete(ReportInfo reportInfo){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final String id = reportInfo.getId();
        ArrayList<String> contentsList =reportInfo.getContents();
        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUrl(contents)) {
                successCount++;
                StorageReference desertRef = storageRef.child("reports/" + id + "/" + storageUrlToName(contents));
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        storeDelete(id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast(activity, "Error");
                    }
                });
            }
        }
        storeDelete(id);
    }

    private void storeDelete(final String id) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if (successCount == 0) {
            firebaseFirestore.collection("reports").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(activity, "게시글을 삭제하였습니다.");
                            onPostListener.onDelete();
                            //postsUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast(activity, "게시글을 삭제하지 못하였습니다.");
                        }
                    });
        }
    }
}

