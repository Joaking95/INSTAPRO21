package ht.bekend.instapro.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import ht.bekend.instapro.MainActivity;
import ht.bekend.instapro.Models.post;
import ht.bekend.instapro.R;

public class ComposeFragment extends Fragment {


    public void showAlertDialogButtonClicked(View view ) {

        //setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //add the buttons
        builder.setPositiveButton("TAKE PICTURE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                LaunchCamera();
            }
        });
        //create and show the alert dialog
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("AlertDialogExample");
        alert . show ();
    }

    public static final String TAG ="ComposeFragment";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    Button SumbitB;
    Button ButtonTakePic;
    EditText Description;
    ImageView PostImage;
    public String photoFileName = "photo.jpg";
    File photoFile;


    public ComposeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SumbitB = view.findViewById(R.id.SumbitB);
        ButtonTakePic =view.findViewById(R.id.ButtonTakePic);
        Description = view.findViewById(R.id.Description);
        PostImage = view.findViewById(R.id.PostImage);


        ButtonTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(v);
                if(PostImage!=null){
                    Description.setVisibility(View.VISIBLE);
                    SumbitB.setVisibility(View.VISIBLE);
                }

            }
        });


        SumbitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = Description.getText().toString();

                if (description.isEmpty()){
                    Toast.makeText(getContext(), "can't be empty",Toast.LENGTH_LONG).show();
                    return;
                }
                if(photoFile== null||PostImage.getDrawable()== null){
                    Toast.makeText(getContext(),"ERROR PICTURE", Toast.LENGTH_LONG).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description,currentUser,photoFile);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContainer, new PostFragment())
                        .commit();
            }
        });

    }



    private void LaunchCamera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Bitmap Takemap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                PostImage.setImageBitmap(Takemap);
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }



    private void savePost(String description, ParseUser currentUser, File photoFile) {
        post Post = new post();
        Post.setDescription(description);
        Post.setImage(new ParseFile(photoFile));
        Post.setUser(currentUser);
        Post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!= null){
                    Log.e(TAG, "error while save", e);
                    Toast.makeText(getContext(),"error save", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG,"Save post");
                Description.setText("");
                PostImage.setImageResource(0);
            }
        });
    }
}