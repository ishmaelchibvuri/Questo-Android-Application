package com.tdevelopers.questo.Pushes;

import com.facebook.Profile;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    public FirebaseInstanceIDService() {


        //  Log.d("gccm", "just started");
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        sendRegistrationToServer(refreshedToken);
//        FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("gcmid").setValue(refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.

    }


    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        if (Profile.getCurrentProfile() != null) {
            FirebaseDatabase.getInstance().getReference("myUsers").child(Profile.getCurrentProfile().getId()).child("gcmid").setValue(token);
        }
    }
}
