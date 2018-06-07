package helpfire.emergency;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Roberta on 01/06/2018.
 */
public class SegnalazioneEmy extends AppCompatActivity {

    static final int REQUEST_GALLERY = 0;
    private LinearLayout contAnteprime;
    private EditText editLocation;
    private ImageButton btCamera, btPosizione;
    private String mCurrentPhotoPath;
    static final int REQUEST_CAMERA = 1;
    static final int ID_RICHIESTA_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione);
        btCamera = (ImageButton) findViewById(R.id.btCamera);
        btPosizione = (ImageButton) findViewById(R.id.btPosizione);
        contAnteprime = (LinearLayout) findViewById(R.id.contAnteprime);
        editLocation = (EditText) findViewById(R.id.editLocation);

        //permesssi
        int statoPermissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int statoPermissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int statoPermissionPos = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //int statoPermissionRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (statoPermissionWrite == PackageManager.PERMISSION_DENIED || statoPermissionCamera == PackageManager.PERMISSION_DENIED || statoPermissionPos == PackageManager.PERMISSION_DENIED) {
            //|| statoPermissionRead == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ID_RICHIESTA_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ID_RICHIESTA_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ID_RICHIESTA_PERMISSION);
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ID_RICHIESTA_PERMISSION);
        }

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SegnalazioneEmy.this);
                builder.setTitle("Choose Image Source");
                builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        // GET IMAGE FROM THE GALLERY
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");

                                        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                        chooser.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                        startActivityForResult(chooser, REQUEST_GALLERY);
                                        break;

                                    case 1:
                                        dispatchTakePictureIntent();
                                        //galleryAddPic();
                                        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                        }*/
                                        break;

                                    default:
                                        break;
                                }
                            }
                        });

                builder.show();

            }
        });

        btPosizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("POS","entro");
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Log.d("POS","location manager " +locationManager );
                LocationListener locationListener = new MyLocationListener();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                if (ActivityCompat.checkSelfPermission(SegnalazioneEmy.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SegnalazioneEmy.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(SegnalazioneEmy.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ID_RICHIESTA_PERMISSION);
                    ActivityCompat.requestPermissions(SegnalazioneEmy.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ID_RICHIESTA_PERMISSION);
                    //return;
                }

            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    //Toast.makeText(SegnalazioneEmy.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    ImageView imageview = new ImageView(getApplicationContext());
                    imageview.setMaxWidth(5);
                    imageview.setMaxHeight(7);
                    imageview.setImageBitmap(bitmap);
                    contAnteprime.addView(imageview);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SegnalazioneEmy.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);
            ImageView imageview = new ImageView(getApplicationContext());
            imageview.setMaxWidth(5);
            imageview.setMaxHeight(7);
            imageview.setImageBitmap(thumbnail);
            contAnteprime.addView(imageview);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + "/DCIM/Emrgency");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,new String[]{f.getPath()},new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File root = Environment.getExternalStorageDirectory();
        //File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Log.d("FOTO","root "+ root);
        File directory = new File(root.getAbsolutePath()+"/DCIM/Emergency1");

        directory.mkdir();
        Log.d("FOTO","dir "+directory);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                directory      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("FOTO"," mCurrentPhotoPath "+ mCurrentPhotoPath);
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
                Uri photoURI = FileProvider.getUriForFile(this, "com.helpfire.emergency.fileprovider", photoFile);
                Log.d("FOTO","uri "+ photoURI);
                takePictureIntent.putExtra("uri", photoURI);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private void galleryAddPic() {
        Log.d("FOTO","galleryaddpic");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        Log.d("FOTO","galleryaddpic "+ contentUri.toString());
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

    //classe per le coordinate
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            Log.d("POS","locationListener");
            editLocation.setText("");
            //pb.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.d("POS", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.d("POS", latitude);

        /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
            editLocation.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

}
