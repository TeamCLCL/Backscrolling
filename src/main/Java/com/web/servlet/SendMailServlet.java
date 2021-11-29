package com.web.servlet;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 注册：发送邮件验证邮箱
 * 找回密码：发送邮件验证邮箱
 */
public class SendMailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties prop = new Properties();
        /**
            // 开启debug调试，以便在控制台查看
            prop.setProperty("mail.debug", "true");
         */
        // 资源绑定器
        ResourceBundle bundle = ResourceBundle.getBundle("email");
        String host = bundle.getString("host");
        String auth = bundle.getString("auth");
        String protocol = bundle.getString("protocol");
        String user = bundle.getString("user");
        String password = bundle.getString("password");

        // 设置邮件服务器主机名
        prop.setProperty("mail.host", host);
        // 发送服务器需要身份验证
        prop.setProperty("mail.smtp.auth", auth);
        // 发送邮件协议名称
        prop.setProperty("mail.transport.protocol", protocol);
        try {
            // 开启SSL加密，否则会失败
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            // 创建session
            Session session = Session.getInstance(prop);
            // 通过session得到transport对象
            Transport ts = session.getTransport();
            // 连接邮件服务器: 邮箱类型，发送者帐号，POP3/SMTP协议授权码
            ts.connect(host, user, password);

            // 获取前端提交的邮箱
            String email = request.getParameter("email");
            // 创建邮件
            Message message = createSimpleMail(session, email);
            // 发送邮件
            ts.sendMessage(message, message.getAllRecipients());

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            // 发送验证码给前端
            String checkCode = message.getContent().toString().split(" ")[1];
            out.print("{checkCode : \"" + checkCode + "\"}");

            ts.close();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个普通文本邮件
     * @param session
     * @param email
     * @return
     * @throws Exception
     */
    private MimeMessage createSimpleMail(Session session, String email) throws Exception {
        // 获取6为随机验证码
        String[] letters = new String[] {
                "q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n","m",
                "A","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M",
                "0","1","2","3","4","5","6","7","8","9"};
        String str = "";
        for (int i = 0; i < 6; i++) {
            str = str + letters[(int)Math.floor(Math.random()*letters.length)];
        }

        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 指明邮件的发件人
        message.setFrom(new InternetAddress("3021805997@qq.com"));
        // 指明邮件的收件人，发件人和收件人如果是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        // 邮件的标题
        message.setSubject("GYW用户注册");
        // 邮件的文本内容
        message.setContent("您的账号注册验证码为(两分钟内有效): " + str + " <br><br>请勿回复此邮箱，谢谢！", "text/html;charset=UTF-8");
        // 返回创建好的邮件对象
        return message;
    }
}
