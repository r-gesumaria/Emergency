package helpfire.emergency;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

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
public class Segnalazione2 extends AppCompatActivity {

    private static final int ID_RICHIESTA_PERMISSION = 3;
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int PLACE_PICKER_REQUEST = 2;
    private static final int REQUEST_POS = 10;

    private MyLocationListener listener;

    private LinearLayout contAnteprime;
    private EditText editLocation, editDescrizione, textLocation;
    private ImageButton btCamera;
    private Button btGetPosizione, btPosizione;
    private String mCurrentPhotoPath, latitudine, longitudine, tipo = "", posizione = "";

    private LocationManager locationManager;
    private RadioButton rdIncendio, rdNeve, rdAltro, rdGas, rdFrana;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione);

        btCamera = (ImageButton) findViewById(R.id.btCamera);
        btPosizione = (Button) findViewById(R.id.btGetPosizione2);
        btGetPosizione = (Button) findViewById(R.id.btPosizione2);
        contAnteprime = (LinearLayout) findViewById(R.id.contAnteprime);

        editLocation = (EditText) findViewById(R.id.editLocation);
        editLocation.setKeyListener(null);
        textLocation = (EditText) findViewById(R.id.textLocation);
        textLocation.setKeyListener(null);
        editDescrizione = (EditText) findViewById(R.id.editDescrizione);

        rdIncendio = (RadioButton) findViewById(R.id.rdIncendio);
        rdNeve = (RadioButton) findViewById(R.id.rdNeve);
        rdGas = (RadioButton) findViewById(R.id.rdGas);
        rdAltro = (RadioButton) findViewById(R.id.rdAltro);
        rdFrana = (RadioButton) findViewById(R.id.rdFrana);

        listener = new MyLocationListener();

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editLocation.getText().toString().length() != 0) {
                    Log.d("POS", "entro nel long");
                    elimina((EditText) view);
                }
            }
        });

        textLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textLocation.getText().toString().equals("")) {
                    Log.d("POS", "entro nel long");
                    elimina((EditText) view);
                }
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Segnalazione2.this);
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
                int statoPermissionPos = ContextCompat.checkSelfPermission(Segnalazione2.this, Manifest.permission.ACCESS_FINE_LOCATION);
                int statoPermissionLoc = ContextCompat.checkSelfPermission(Segnalazione2.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                if (statoPermissionPos == PackageManager.PERMISSION_DENIED || statoPermissionLoc == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(Segnalazione2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ID_RICHIESTA_PERMISSION);
                    ActivityCompat.requestPermissions(Segnalazione2.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ID_RICHIESTA_PERMISSION);
                }
                locationManager.removeUpdates(listener);
                if(textLocation.getText().toString().length() == 0) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(Segnalazione2.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Segnalazione2.this, "Hai già inserito una posizione.", Toast.LENGTH_LONG).show();
                }
            }
        });

        ottieniPosizione();

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
                    Toast.makeText(Segnalazione2.this, "Compilare i campi obbligatori!", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Segnalazione2.this);
                    LayoutInflater inflater = Segnalazione2.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alert_invio_segnalazione,null);
                    builder.setView(dialogView);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            clear();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("POS","requestcode "+requestCode);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                    ImageView imageView = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                    layoutParams.setMargins(0,0,10,10);
                    imageView.setLayoutParams(layoutParams);

                    imageView.setImageBitmap(bitmap);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            Log.d("FOTO","long");
                            AlertDialog.Builder builder = new AlertDialog.Builder(Segnalazione2.this);
                            builder.setTitle(R.string.titoloElimina);
                            builder.setMessage(R.string.dialog_message_foto);
                            builder.setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    contAnteprime.removeView(view);
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                        }
                    });
                    contAnteprime.addView(imageView);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Segnalazione2.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail);

            ImageView imageView = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
            layoutParams.setMargins(0,0,10,10);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(thumbnail);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Log.d("FOTO","long");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Segnalazione2.this);
                    builder.setTitle(R.string.titoloElimina);
                    builder.setMessage(R.string.dialog_message_foto);
                    builder.setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            contAnteprime.removeView(view);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });
            contAnteprime.addView(imageView);
        }else if (requestCode == PLACE_PICKER_REQUEST) {
            Place place = PlacePicker.getPlace(data, this);
            Log.d("POS","Place"+ place.getName());
            posizione = String.valueOf(place.getLatLng());
            textLocation.setText("");
            editLocation.setText(place.getName());
        }
    }

    private void elimina(final EditText editText){
        Log.d("POS","entro in elimina");
        AlertDialog.Builder builder = new AlertDialog.Builder(Segnalazione2.this);
        builder.setTitle(R.string.titoloElimina);
        builder.setMessage(R.string.dialog_message_pos);
        builder.setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                editText.setText("");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

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

    //classe per le coordinate
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if(textLocation.getText().toString().equals("")) {
                Log.d("MIO", "onLocationChanged");
                longitudine = String.valueOf(location.getLongitude());
                latitudine = String.valueOf(location.getLatitude());
                posizione = "(" + longitudine + "," + latitudine + ")";
                /*----------to get City-Name from coordinates -------------*/
                String cityName = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        /*Log.d("MIO", "località " + addresses.get(0).getLocality());
                        Log.d("MIO", "country name " + addresses.get(0).getCountryName());
                        Log.d("MIO", "country code " + addresses.get(0).getCountryCode());
                        Log.d("MIO", "postal code " + addresses.get(0).getPostalCode());
                        Log.d("MIO", "admin area " + addresses.get(0).getAdminArea());
                        Log.d("MIO", "sublocality " + addresses.get(0).getAddressLine(0));*/
                        cityName = addresses.get(0).getAddressLine(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textLocation.setText(cityName);
                int statoPermissionPos = ContextCompat.checkSelfPermission(Segnalazione2.this, Manifest.permission.ACCESS_FINE_LOCATION);
                int statoPermissionLoc = ContextCompat.checkSelfPermission(Segnalazione2.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                if (statoPermissionPos == PackageManager.PERMISSION_DENIED || statoPermissionLoc == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(Segnalazione2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ID_RICHIESTA_PERMISSION);
                    ActivityCompat.requestPermissions(Segnalazione2.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ID_RICHIESTA_PERMISSION);
                }
                locationManager.removeUpdates(listener);
            }
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
                ottieniPosizione();
                break;
            default:
                break;
        }
    }

    private void ottieniPosizione() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,REQUEST_POS);
            }
            return;
        }

        //ottieni posizione corrente
        btGetPosizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editLocation.getText().toString().length() == 0) {
                    //noinspection MissingPermission
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                }else
                    Toast.makeText(Segnalazione2.this, "Hai già inserito una posizione.", Toast.LENGTH_LONG).show();
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
        textLocation.setText("");
        editDescrizione.setText("");
        contAnteprime.removeAllViews();
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
            Toast.makeText(Segnalazione2.this, "Guida utente", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void permessi(){
        int statoPermissionPos = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int statoPermissionLoc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (statoPermissionPos == PackageManager.PERMISSION_DENIED || statoPermissionLoc == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ID_RICHIESTA_PERMISSION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ID_RICHIESTA_PERMISSION);
        }
    }
}