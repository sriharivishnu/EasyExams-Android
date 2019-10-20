package com.corp.srihari.deca;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by sriharivishnu on 2018-09-27.
 */

public class DatabaseUtils extends Application {
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ArrayList<Integer> scores;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty())
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    public FirebaseDatabase getDatabaseInstance() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
        }
        return mDatabase;
    }

    public FirebaseAuth getAuthInstance() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }
    public FirebaseUser getUser() {
        return getAuthInstance().getCurrentUser();
    }
    public void signOutUser() {
        getAuthInstance().signOut();
        mAuth = null;
    }
    public String getEmailAddress() {
        return getUser().getEmail();
    }
    public String getUserID() {
        return getUser().getUid();
    }
    public void resetPassword(final Activity activity, String emailAddress) {
        getAuthInstance().sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("YE", "Email sent.");
                            Toast.makeText(activity,"Email Sent", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "Reset Password Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
