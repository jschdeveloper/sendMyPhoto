package jschdeveloper.com.sendmyphoto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jschdeveloper.com.sendmyphoto.mail.Utils;

public class ConfigActivity extends AppCompatActivity {

    private EditText editDestino;
    private EditText editAsunto;
    private EditText editMsj;
    private EditText editOrigen;
    private EditText editPassword;
    private Button btnEnviar;
    private Button btnGuardar;
    private Button btnClearOrigen;
    private Button btnClearDestino;
    private Button btnClearMensaje;
    private Button btnClearAsunto;
    private Button btnClearPass;
    private CoordinatorLayout coordinatorLayout;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        utils = new Utils(getApplicationContext());
        //Initializing the views
        editDestino = (EditText) findViewById(R.id.editDestino);
        editAsunto = (EditText) findViewById(R.id.editAsunto);
        editMsj = (EditText) findViewById(R.id.editMsj);
        editOrigen = (EditText) findViewById(R.id.editOrigen);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnClearDestino = (Button) findViewById(R.id.btnClearDestino);
        btnClearOrigen = (Button) findViewById(R.id.btnClearOrigen);
        btnClearMensaje = (Button) findViewById(R.id.btnClearMensaje);
        btnClearAsunto = (Button) findViewById(R.id.btnClearAsunto);
        btnClearPass = (Button) findViewById(R.id.btnClearPass);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clEmail);


        btnClearDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDestino.setText("");
            }
        });
        btnClearOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editOrigen.setText("");
            }
        });
        btnClearMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editMsj.setText("");
            }
        });
        btnClearAsunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editAsunto.setText("");
            }
        });
        btnClearPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editPassword.setText("");
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(editDestino.getText().toString().toLowerCase().trim()) &&
                        !TextUtils.isEmpty(editOrigen.getText().toString().toLowerCase().trim()) &&
                        !TextUtils.isEmpty(editPassword.getText().toString().toLowerCase().trim())) {

                    utils.clearSP(utils.getPreferences().edit());
                    utils.saveSP(new MailData(editDestino.getText().toString().trim(),
                            editAsunto.getText().toString().trim(),
                            editMsj.getText().toString().trim(),
                            editOrigen.getText().toString().trim(),
                            editPassword.getText().toString().trim()));
                    mostrarMensaje("Se guardo la información correctamente");
                } else {
                    mostrarMensaje("Es necesario llenar todos los campos");
                }
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    if (!TextUtils.isEmpty(editDestino.getText().toString().toLowerCase().trim()) &&
                            !TextUtils.isEmpty(editOrigen.getText().toString().toLowerCase().trim()) &&
                            !TextUtils.isEmpty(editPassword.getText().toString().toLowerCase().trim())) {
                        utils.testEmail(false, -1);
                    } else {
                        mostrarMensaje("Es necesario llenar todos los campos");
                    }
                } else {
                    mostrarMensaje("No hay conexión a internet vuelva a intentarlo");
                }
            }
        });
    }


    public void mostrarMensaje(String msj) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msj, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
