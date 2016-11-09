package jschdeveloper.com.sendmyphoto;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.File;

import jschdeveloper.com.sendmyphoto.mail.Utils;


public class MainActivity extends AppCompatActivity {
    MaterialIconView btnFoto;
    MaterialIconView btnVideo;
    MaterialIconView btnConfig;
    CoordinatorLayout coordinatorLayout;
    private final int IDRESULT = 100;
    private static final int REQUEST_VIDEO_CAPTURE = 1;
    String ruta;
    Activity myActivity;
    File imgTemp;
    EditText editAsunto;
    String asunto;
    Button btnClear;

    LinearLayout linFoto, linVideo, linConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myActivity = (Activity) MainActivity.this;
        btnFoto = (MaterialIconView) findViewById(R.id.btnFoto);
        btnVideo = (MaterialIconView) findViewById(R.id.btnVideo);
        btnConfig = (MaterialIconView) findViewById(R.id.btnAjustes);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clMain);
        linFoto = (LinearLayout) findViewById(R.id.linCamara);
        linVideo = (LinearLayout) findViewById(R.id.linVideo);
        linConfig = (LinearLayout) findViewById(R.id.linAjustes);
        editAsunto = (EditText) findViewById(R.id.editAsunto);
        btnClear = (Button) findViewById(R.id.btnClear);

        Utils utils = new Utils(myActivity);


        checkPermisos();

        //SharedPreferences temp = utils.getPreferences();
        //temp.edit().clear().commit();
        //checkInfo(utils.getSP());

        ruta = Environment.getExternalStorageDirectory() + File.separator;
        linFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asunto = editAsunto.getText().toString();
                Intent iCamara = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                imgTemp = getFile();
                iCamara.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgTemp));
                startActivityForResult(iCamara, IDRESULT);
            }
        });

        linVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asunto = editAsunto.getText().toString();
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                imgTemp = null;
                imgTemp = getFile();
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgTemp));
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        });

        linConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(myActivity, ConfigActivity.class);
                myActivity.startActivity(i);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAsunto.setText("");
            }
        });
    }

    private File getFile() {
        final File path = new File(ruta, getResources().getString(R.string.nombre_carpeta_temporal));
        if (!path.exists()) {
            path.mkdir();
        }
        return new File(path, getResources().getString(R.string.nombre_archivo_temporal));
    }

    public void mostrarMensaje(String msj) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msj, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils utils = new Utils(getApplication().getApplicationContext());
        switch (requestCode) {
            case IDRESULT:
                if (requestCode == IDRESULT && resultCode == RESULT_OK) {
                    utils.sendEmail(true, utils.ARCHIVO_IMAGEN, asunto);
                }

            case REQUEST_VIDEO_CAPTURE:
                if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
                    utils.sendEmail(true, utils.ARCHIVO_VIDEO, asunto);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                //Si la petición es cancelada, el resultado estará vacío.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permiso aceptado, se podría acceder a los contactos del dispositivo.
                    linFoto.setEnabled(true);
                    linVideo.setEnabled(true);
                    linConfig.setEnabled(true);
                } else {
                    //Permiso denegado. Desactivar la funcionalidad que dependía de dicho permiso.
                    linFoto.setEnabled(false);
                    linVideo.setEnabled(false);
                    linConfig.setEnabled(false);
                    mostrarMensaje("Es necesario el permiso para usar el app");
                }
                return;
            }

            // A continuación, se expondrían otras posibilidades de petición de permisos.
        }
    }

    public void checkInfo(MailData checkData) {
        if (!TextUtils.isEmpty(checkData.getMailOrigen()) && !TextUtils.isEmpty(checkData.getEmail()) && !TextUtils.isEmpty(checkData.getPass())) {
            linFoto.setEnabled(true);
            linVideo.setEnabled(true);
        } else {
            mostrarMensaje("Llenar datos");
            linFoto.setEnabled(false);
            linVideo.setEnabled(false);
        }
    }

    public void checkPermisos() {
        if (ContextCompat.checkSelfPermission(myActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(myActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


            /* Aquí se mostrará la explicación al usuario de porqué es necesario el uso de un determinado permiso, pudiéndose mostrar de manera asíncrona, o lo que es lo mismo, desde un
                    hilo secundario, sin bloquear el hilo principal, y a la espera de que el usuario concede el permiso necesario tras visualizar la explicación.*/
            } else {

                /* Se realiza la petición del permiso. En este caso permisos para leer los contactos.*/
                ActivityCompat.requestPermissions(myActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            }
        }
    }

}
