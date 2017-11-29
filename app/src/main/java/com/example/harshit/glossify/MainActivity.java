package com.example.harshit.glossify;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;



import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.harshit.glossify.R.id.text;
//import static com.example.harshit.glossify.R.id.imgtext;
//import static com.example.harshit.glossify.R.id.imgtext;
import static java.io.FileDescriptor.out;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView myImageView;
    Button process;
    String mCurrentPhotoPath;
    String path;
    Uri mainURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        Button mybutton = (Button) findViewById(R.id.mybutton);
        myImageView = (ImageView) findViewById(R.id.myImageView);

        if(!hasCamera()) {

            mybutton.setEnabled(false);
        }

    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,height, filter);
        return newBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = null;
        Bitmap temp = BitmapFactory.decodeFile(mCurrentPhotoPath);
        if(temp!=null)
        {bitmap = scaleDown(temp, 3000, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
       // byte[] imageInByte = stream.toByteArray();
        //long length = imageInByte.length;
        process = (Button) findViewById(R.id.process);
       // myImageView.setImageBitmap(bitmap);

        //TextView txtview = (TextView) findViewById(R.id.imgtext);
        //length /= 1024;
       // txtview.setText(" " + length + "KB");

        Bitmap out = scaleDown(temp, 3000, true);
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File file = new File(dir, imageFileName+".jpg");
        FileOutputStream fOut;
        try {

            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            out.recycle();
        } catch (Exception e) {
        }
        path = file.getAbsolutePath();
        //txtview.setText(path);
        mCurrentPhotoPath = path;
        temp = BitmapFactory.decodeFile(path);
        myImageView.setImageBitmap(temp);

        mainURI = FileProvider.getUriForFile(this,
                "com.example.android.fileprovider",
                file);
        UploadImage();}

    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
    storageDir      /* directory */
     );

    // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void takePic(View view) {
        // Ensure that there's a camera activity to handle the intent
        Button bt = (Button) findViewById(R.id.process);
        bt.setEnabled(false);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
               takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
              // pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void api_call(View view){

        Intent intent = new Intent(this, process.class);
        if(intent!=null)
        {startActivity(intent);}

    }


    public void UploadImage()
    {
        try {
            final InputStream imageStream = getContentResolver().openInputStream(mainURI);
            final int imageLength = imageStream.available();

            final Handler handler = new Handler();

            Thread th = new Thread(new Runnable() {
                public void run() {

                    try {

                        final String imageName = ImageManager.UploadImage(imageStream, imageLength);

                        handler.post(new Runnable() {

                            public void run() {
                                Button bt = (Button) findViewById(R.id.process);
                                bt.setEnabled(true);
                               // Toast.makeText(MainActivity.this, "Image Uploaded Successfully. Name = " + imageName, Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, "Image Uploaded Successfully , Click the Process Button", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch(Exception ex) {
                        final String exceptionMessage = ex.getMessage();
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, exceptionMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }});
            th.start();
        }
        catch(Exception ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }




}
