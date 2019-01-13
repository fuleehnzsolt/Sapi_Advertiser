package com.example.fuleehnzsolt.sapi_advertiser.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.fuleehnzsolt.sapi_advertiser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText logPhoneNumber, logCode;
    private Button logToRegisterButton, logGetCodeButton, loginButton;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks AuthCallback;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseFirestore db;

    private String phoneNumber;
    private String mVerificationId;
    private String verificationCode;
    private String pPhoneNumber, pFirstName, pLastName;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        logPhoneNumber = findViewById(R.id.logPhoneNumber);
        logCode = findViewById(R.id.logCode);
        logToRegisterButton = findViewById(R.id.logToRegisterButton);
        loginButton = findViewById(R.id.loginButton);
        logGetCodeButton = findViewById(R.id.logGetCodeButton);
        logCode.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);


        /**
         *  A MainActivity tartalmazza a Login felületet.
         *  Ha a bejelentkezni vágyó egyénnek nincs felhasználója, átirányitom a RegisterActivityre,
         *  ahol elkészitheti a felhasználóját és beléphet.
         * **/

        logToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

        /**
         * Bejelentkezésnél és regisztrálásnál is visszacsatolást alkalamzunk, tehát
         * nem elég csak beirni a kellő adatokat(vezetéknév, keresztnév illetve telefonszám), várnunk kell amig
         * visszajelzést nem kapunk a telefonunkra, mármint egy SMS-ben kapott kódot, ami által végül be tudunk jelentekzni
         * a felhasználónkba.
         * **/

        logGetCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = logPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber))
                {
                    Toast.makeText(MainActivity.this, "Enter your phone number!", Toast.LENGTH_LONG).show();

                } else {


                    /**
                     * Leteszteljük a bejeltnkezni vágyó telefonszámát, hogy megtalálható e az adatbázisban.
                     * Ha nem, akkor a felhasználó tudtára adjuk, hogy nincs regisztrálva ez a szám.
                     * Ha regisztrálva van azaz megkapta a phoneNumber párját az adatbázisban, szükséges a megerősités,
                     * ami úgymond egy másik lépcsőt eredményez a bejelentkezésben, az SMSben megkapott kód.
                     * */

                    CollectionReference collectionReference = db.collection("users");
                    Query query = collectionReference.whereEqualTo("phoneNumber", phoneNumber);

                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {

                                Toast.makeText(MainActivity.this, "This number isn't registered!", Toast.LENGTH_LONG).show();
                            } else {

                                /**
                                 *  Megkeressük a telefonszámot, majd a tulajdonosának nevét
                                 */

                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    if (documentSnapshot.getString("phoneNumber").equals(phoneNumber)) {
                                        pFirstName = documentSnapshot.getString("firstName");
                                        pLastName = documentSnapshot.getString("lastName");
                                        pPhoneNumber = documentSnapshot.getString("phoneNumber");

                                    }
                                }

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        phoneNumber,
                                        60,
                                        TimeUnit.SECONDS,
                                        MainActivity.this,
                                        AuthCallback
                                );
                            }
                        }
                    });

                }
            }

        });

        /**
         * Beijuk az SMSben megkapott kódot és bejelentkezünk.
         * */

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationCode = logCode.getText().toString();

                if (TextUtils.isEmpty(verificationCode)) {
                    Toast.makeText(MainActivity.this, "Enter the Code", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        AuthCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                logCode.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(MainActivity.this, "Code sent...", Toast.LENGTH_SHORT).show();

                logCode.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
            }
        };

    }

    /**
     * Sikeres bejeltnekzésnél átirányitjuk a flehasználót az AdvertiseActivitybe, illetve putExtrával az adatait továbbaduk, amit
     * az AdvertiseActivityben lekérhetünk(current userre nézve).
     * */

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, AdvertiseActivity.class);
                            intent.putExtra("USER_FIRSTNAME", pFirstName);
                            intent.putExtra("USER_LASTNAME", pLastName);
                            intent.putExtra("USER_PHONENUMBER", pPhoneNumber);
                            startActivity(intent);

                            finish();
                        } else {

                            String message = task.getException().toString();
                            Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
