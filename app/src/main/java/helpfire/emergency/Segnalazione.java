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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
public class Segnalazione extends AppCompatActivity {

    static final int REQUEST_GALLERY = 0;
    private LinearLayout contAnteprime;
    private EditText editLocation,editDescrizione;
    private TextView tipoSegn;
    private ImageButton btCamera, btPosizione;
    private String mCurrentPhotoPath,latitudine, longitudine, tipo="", posizione="";
    static final int REQUEST_CAMERA = 1;
    static final int REQUEST_POS = 10;
    private LocationManager locationManager;
    private RadioButton rdIncendio, rdNeve, rdAltro, rdGas, rdFrana;
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione);

        btCamera = (ImageButton) findViewById(R.id.btCamera);
        btPosizione = (ImageButton) findViewById(R.id.btPosizione);
        contAnteprime = (LinearLayout) findViewById(R.id.contAnteprime);
        editLocation = (EditText) findViewById(R.id.editLocation);
        editDescrizione = (EditText) findViewById(R.id.editDescrizione);
        tipoSegn = (TextView) findViewById(R.id.tipoSegn);

        rdIncendio = (RadioButton)findViewById(R.id.rdIncendio);
        rdNeve = (RadioButton)findViewById(R.id.rdNeve);
        rdGas = (RadioButton)findViewById(R.id.rdGas);
        rdAltro = (RadioButton)findViewById(R.id.rdAltro);
        rdFrana = (RadioButton)findViewById(R.id.rdFrana);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Segnalazione.this);
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

        ottieniPosizoine();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rdIncendio.isChecked()){
                    tipo = rdIncendio.getText().toString();
                }else if(rdGas.isChecked()){
                    tipo = rdGas.getText().toString();
                }else if(rdNeve.isChecked()){
                    tipo = rdNeve.getText().toString();
                }else if(rdFrana.isChecked()){
                    tipo = rdFrana.getText().toString();
                }else if(rdAltro.isChecked()){
                    tipo = rdAltro.getText().toString();
                }
               if(tipo.equals("") || (posizione.equals(""))){
                    Toast.makeText(Segnalazione.this, "Compilare i campi obbligatori!", Toast.LENGTH_SHORT).show();
               }else{
                    Toast.makeText(Segnalazione.this, "Segnalazione inviata con successo.", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(Segnalazione.this);
                    builder.setMessage(R.string.dialog_message);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            clear();
                        }
                    });
                    builder.show();
               }
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
                    //Toast.makeText(Segnalazione.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    ImageView imageView = new ImageView(getApplicationContext());
                    FrameLayout frameLayout = new FrameLayout(getApplicationContext());

                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(30, 30);
                    frameLayout.setLayoutParams(layoutParams1);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    imageView.setLayoutParams(layoutParams);

                    imageView.setImageBitmap(bitmap);
                    contAnteprime.addView(imageView);
                    contAnteprime.addView(frameLayout);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Segnalazione.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);

            ImageView imageView = new ImageView(getApplicationContext());
            FrameLayout frameLayout = new FrameLayout(getApplicationContext());

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(30, 30);
            frameLayout.setLayoutParams(layoutParams1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
            imageView.setLayoutParams(layoutParams);

            imageView.setImageBitmap(thumbnail);
            contAnteprime.addView(imageView);
            contAnteprime.addView(frameLayout);
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
            startActivity(new Intent(getApplicationContext(),EditAccount.class));
            return true;
        }else if( id == R.id.action_help){
            Toast.makeText(Segnalazione.this, "Guida utente", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //classe per le coordinate
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("MIO","onLocationChanged");
            longitudine = String.valueOf(location.getLongitude());
            latitudine = String.valueOf(location.getLatitude());
            posizione = longitudine +":"+latitudine;
            /*----------to get City-Name from coordinates -------------*/
            String cityName=null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    Log.d("MIO", "località " + addresses.get(0).getLocality());
                    Log.d("MIO", "country name " + addresses.get(0).getCountryName());
                    Log.d("MIO", "country code " + addresses.get(0).getCountryCode());
                    Log.d("MIO", "postal code " + addresses.get(0).getPostalCode());
                    Log.d("MIO", "admin area " + addresses.get(0).getAdminArea());
                    Log.d("MIO", "sublocality " + addresses.get(0).getAddressLine(0));
                    cityName = addresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            editLocation.setText(cityName);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.d("MIO","onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d("MIO","onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.d("MIO","onProviderDisabled");
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_POS:
                ottieniPosizoine();
                break;
            default:
                break;
        }
    }

    private void ottieniPosizoine() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,REQUEST_POS);
            }
            return;
        }

        btPosizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, new MyLocationListener());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void clear(){
        rdIncendio.setChecked(false);
        rdGas.setChecked(false);
        rdNeve.setChecked(false);
        rdFrana.setChecked(false);
        rdAltro.setChecked(false);
        editLocation.setText("");
        editDescrizione.setText("");
        contAnteprime.removeAllViews();
    }
}
