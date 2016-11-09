package jschdeveloper.com.sendmyphoto.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import jschdeveloper.com.sendmyphoto.MailData;
import jschdeveloper.com.sendmyphoto.R;

/**
 * Created by Belal on 10/30/2015.
 */


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void, Void, Void> {

    public static final String EXTENSION_MP4 = ".mp4";
    public static final String EXTENSION_JPG = ".jpg";
    public static final String EXTENSION_TEMP = ".temp";

    private Multipart _multipart;

    //Declaring Variables
    private Context context;
    private Session session;
    private File foto;
    private MailData mailData;
    private boolean withImg;
    private Integer tipoArchivo;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;


    public SendMail(Context context, MailData mailData, boolean withImg, Integer tipoArchivo, String asunto) {
        //Initializing variables
        this.context = context;
        this.mailData = mailData;
        this.withImg = withImg;
        this.tipoArchivo = tipoArchivo;
        ;
        this.mailData.setSubject(asunto);
        Toast.makeText(context, "Enviando..", Toast.LENGTH_LONG).show();
    }

    public SendMail(Context context, MailData mailData, boolean withImg, Integer tipoArchivo) {
        //Initializing variables
        this.context = context;
        this.mailData = mailData;
        this.withImg = withImg;
        this.tipoArchivo = tipoArchivo;
        Toast.makeText(context, "Enviando..", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailData.getMailOrigen(), mailData.getPass());
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(mailData.getMailOrigen()));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mailData.getEmail()));
            //Adding subject
            mm.setSubject(mailData.getSubject());
            //Adding message
            mm.setText(mailData.getMessage());

            if (withImg) {
                mm.setContent(addFile(new MimeMultipart()));
            }
            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        _multipart = new MimeMultipart();
        return null;

    }

    private Multipart addFile(Multipart multipart) throws Exception {
        String tempName = context.getResources().getString(R.string.nombre_archivo_temporal);
        String newName = context.getResources().getString(R.string.nombre_archivo_enviado);
        String dirFile = context.getResources().getString(R.string.nombre_carpeta_temporal);

        foto = new File(android.os.Environment.getExternalStorageDirectory() + File.separator +
                dirFile + File.separator + tempName);
        DataSource source = new FileDataSource(foto);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(tipoArchivo == new Utils(context).ARCHIVO_IMAGEN ? foto.getName().replace(EXTENSION_TEMP, EXTENSION_JPG) : foto.getName().replace(EXTENSION_TEMP, EXTENSION_MP4));
        multipart.addBodyPart(messageBodyPart);
        return multipart;
    }
}
