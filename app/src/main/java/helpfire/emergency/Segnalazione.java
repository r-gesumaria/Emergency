package helpfire.emergency;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Roberta on 01/06/2018.
 */
public class Segnalazione extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1 ;
    private LinearLayout contAnteprime;
    private ImageButton btCamera, btPosizione;
    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int ID_RICHIESTA_PERMISSION = 1;
    static final int ACTION_REQUEST_GALLERY = 1;
    static final int ACTION_REQUEST_CAMERA = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione);

        btCamera = (ImageButton) findViewById(R.id.btCamera);
        btPosizione = (ImageButton) findViewById(R.id.btPosizione);
        contAnteprime = (LinearLayout) findViewById(R.id.contAnteprime);

        //permesssi
        int statoPermissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int statoPermissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int statoPermissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (statoPermissionWrite == PackageManager.PERMISSION_DENIED || statoPermissionCamera == PackageManager.PERMISSION_DENIED
                || statoPermissionRead == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ID_RICHIESTA_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ID_RICHIESTA_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ID_RICHIESTA_PERMISSION);
        }

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Segnalazione.this);
                builder.setTitle("Choose Image Source");
                builder.setItems(new CharSequence[] {"Gallery", "Camera"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // GET IMAGE FROM THE GALLERY
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");

                                        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                        startActivityForResult(chooser, REQUEST_IMAGE_CAPTURE);

                                        break;

                                    case 1:
                                        Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");

                                        File cameraFolder;

                                        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))cameraFolder = new File(android.os.Environment.getExternalStorageDirectory(),"Emergency/");
                                        else
                                            cameraFolder= Segnalazione.this.getCacheDir();
                                        if(!cameraFolder.exists())
                                            cameraFolder.mkdirs();

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                                        String timeStamp = dateFormat.format(new Date());
                                        String imageFileName = "picture_" + timeStamp + ".jpg";

                                        File photo = new File(Environment.getExternalStorageDirectory(),
                                                "Emergency/" + imageFileName);
                                        Uri initialURI = Uri.fromFile(photo);
                                        Log.d("MIO","uri " + initialURI);
                                        getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, initialURI);
                                        getCameraImage.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                        //Uri initialURI = Uri.fromFile(photo);

                                        startActivityForResult(getCameraImage, REQUEST_IMAGE_CAPTURE);

                                        break;

                                    default:
                                        break;
                                }
                            }
                        });

                builder.show();
               /* Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                try {
                    createImageFile();
                    galleryAddPic();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //dispatchTakePictureIntent();

            }
                /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }*/
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    //visualizzazione anteprima foto scattata
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK) {
                Log.d("MIO","entro per l'anteprima");
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView mImageView = new ImageView(getApplicationContext());
                mImageView.setImageBitmap(imageBitmap);
                contAnteprime.addView(mImageView);
                Log.d("MIO","entro");

                //saveToInternalStorage(imageBitmap);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        Context cw = getApplicationContext();
        // path to /data/data/yourapp/app_data/imageDir
        //File directory = cw.getDir("imageDir", Context.);
        //File directory = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File root = Environment.getExternalStorageDirectory();
        File directory = new File(root.getAbsolutePath()+"/DCIM/Emergency");
        Log.d("MIO","salvataggio --- " + root.getAbsolutePath());
        Log.d("MIO","salvataggio dir" + directory.getPath());
        // Create imageDir
        directory.mkdir();
        File mypath=new File(directory,"profile0.jpg");
        Log.d("MIO","salvataggio mypath" + mypath.getPath());
        FileOutputStream fos = null;
        Log.d("MIO","fos1 "+fos);
        try {
            fos = new FileOutputStream(mypath);
            Log.d("MIO","fos "+fos);
            // Use the compress method on the BitMap object to write image to the OutputStream

            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return directory.getAbsolutePath();
    }

    private File createImageFile() throws IOException {
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
        Log.d("MIO","directory -- "+mCurrentPhotoPath.toString());
        return image;
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opzioni, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editAccount) {
            return true;
        }else if( id == R.id.action_help){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
