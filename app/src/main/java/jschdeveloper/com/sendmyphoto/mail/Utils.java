package jschdeveloper.com.sendmyphoto.mail;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import jschdeveloper.com.sendmyphoto.MailData;

/**
 * Created by jesus.sanchez on 06/09/2016.
 */
public class Utils {
    Context mContext;
    private MailData mailData;

    public static final Integer ARCHIVO_VIDEO = 0;
    public static final Integer ARCHIVO_IMAGEN = 1;

    public Utils(Context mContext) {
        this.mContext = mContext;
    }

    public void sendEmail(boolean withImg, Integer tipoArchivo, String asunto) {

        mailData = getSP();

        if (!TextUtils.isEmpty(mailData.getEmail()) && !TextUtils.isEmpty(mailData.getPass()) && !TextUtils.isEmpty(mailData.getMailOrigen())) {
            //Creating SendMail object
            SendMail sm = new SendMail(mContext, mailData, withImg, tipoArchivo, asunto);

            //Executing sendmail to send email
            sm.execute();
        }

    }

    public void testEmail(boolean withImg, Integer tipoArchivo) {

        mailData = getSP();

        if (!TextUtils.isEmpty(mailData.getEmail()) && !TextUtils.isEmpty(mailData.getPass()) && !TextUtils.isEmpty(mailData.getMailOrigen())) {
            //Creating SendMail object
            SendMail sm = new SendMail(mContext, mailData, withImg, tipoArchivo);

            //Executing sendmail to send email
            sm.execute();
        }

    }

    public SharedPreferences getPreferences() {
        return mContext.getSharedPreferences("dataMail", mContext.MODE_PRIVATE);
    }

    public String saveSP(MailData mailData) {
        SharedPreferences.Editor editor = getPreferences().edit();

        // Saving long
        editor.putString("email", mailData.getEmail());
        editor.putString("subject", mailData.getSubject());
        editor.putString("message", mailData.getMessage());
        editor.putString("mailOrigen", mailData.getMailOrigen());
        editor.putString("pass", mailData.getPass());
        editor.commit();

        return "OK";
    }

    public MailData getSP() {
        MailData mailData = new MailData();
        mailData.setEmail(getPreferences().getString("email", "e"));
        mailData.setSubject(getPreferences().getString("subject", "s"));
        mailData.setMessage(getPreferences().getString("message", "m"));
        mailData.setMailOrigen(getPreferences().getString("mailOrigen", "mo"));
        mailData.setPass(getPreferences().getString("pass", "p"));

        return mailData;
    }

    public void clearSP(SharedPreferences.Editor editor) {
        editor.clear();
        editor.commit(); // commit changes
    }
}
