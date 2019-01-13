package com.example.fuleehnzsolt.sapi_advertiser.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fuleehnzsolt.sapi_advertiser.Data.Advertise;
import com.example.fuleehnzsolt.sapi_advertiser.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class AddNewAdvertise extends Fragment {

    private FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button addButton;
    private EditText addTitle;
    private EditText addShortDescription;
    private EditText addLongDescription;
    private EditText addLocation;
    private Button addChooseButton, addUploadImg;
    private ImageView imageView;
    private EditText addPhoneNumber;

    private Uri filePath;

    private String title, shortDesc, longDesc, phoneNum, location, image = "";
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_advertise, container, false);

        db = FirebaseFirestore.getInstance();

        /**
         * Inicializálás**/
        addChooseButton = view.findViewById(R.id.addChooseButton);
        addUploadImg = view.findViewById(R.id.addUploadImg);
        imageView = view.findViewById(R.id.imgView);
        addTitle = view.findViewById(R.id.addTitle);
        addShortDescription = view.findViewById(R.id.addShortDescription);
        addLongDescription = view.findViewById(R.id.addLongDescription);
        addPhoneNumber = view.findViewById(R.id.addPhoneNumber);
        addLocation = view.findViewById(R.id.addLocation);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Elmentem az advertise információit*/

                title = addTitle.getText().toString();
                shortDesc = addShortDescription.getText().toString();
                longDesc = addLongDescription.getText().toString();
                phoneNum = addPhoneNumber.getText().toString();
                location = addLocation.getText().toString();

                Advertise ad = new Advertise(title, shortDesc, longDesc, phoneNum, location, image);
                db.collection("advertises").add(ad).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Advertise uplaoded", Toast.LENGTH_LONG).show();
                    }
                });

                /**
                 *   Megnyitjuk a Home-ot egy új Advertise feltöltése után
                 */

                Fragment homeFragment = new Home();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        addChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        addUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        return view;

    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Upload...");
            progressDialog.show();


            String imageId = UUID.randomUUID().toString();

            final StorageReference ref = storageReference.child("images/" + imageId);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Upload failed:  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose a picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
