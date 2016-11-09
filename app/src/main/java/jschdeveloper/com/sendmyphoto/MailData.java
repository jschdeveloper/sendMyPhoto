package jschdeveloper.com.sendmyphoto;

/**
 * Created by jesus.sanchez on 08/09/2016.
 */
public class MailData {
    private String email;
    private String subject;
    private String message;
    private String mailOrigen;
    private String pass;

    public MailData() {
    }

    public MailData(String email, String subject, String message, String mailOrigen, String pass) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.mailOrigen = mailOrigen;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMailOrigen() {
        return mailOrigen;
    }

    public void setMailOrigen(String mailOrigen) {
        this.mailOrigen = mailOrigen;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "MailData{" +
                "email='" + email + '\'' +
                ", mailOrigen='" + mailOrigen + '\'' +
                ", pass='" + pass + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
