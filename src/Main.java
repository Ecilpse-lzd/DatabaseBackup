import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {

        Properties props = new Properties();
        props.load(new FileReader(new File("user.properties")));
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        String to = props.getProperty("user.name.to");
        // 发件人电子邮箱
        String from = props.getProperty("user.name.from");
        String pass = props.getProperty("user.pass");
        try {
            msg.setSubject(MimeUtility.encodeText("数据备份"));
            // 设置邮件内容
            // 设置发件人
            msg.setFrom(new InternetAddress(from));

            BodyPart messageBodyPart = new MimeBodyPart();

            // 消息
            messageBodyPart.setText(MimeUtility.encodeText("数据备份"));

            // 创建多重消息
            MimeMultipart multipart = new MimeMultipart();

            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);

            // 附件部分
            String filename = props.getProperty("accessory.path");
            setFile(filename, multipart);
            msg.setContent(multipart);
            msg.setSentDate(new Date());

            Transport transport = session.getTransport();
            // 连接邮件服务器
            transport.connect(from, pass);
            // 发送邮件
            transport.sendMessage(msg, new Address[]{new InternetAddress(to)});
            // 关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void setFile(String filePath, MimeMultipart multipart) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File fileChild : files) {
                        setFile(fileChild.getAbsolutePath(), multipart);
                    }
                }
            } else if (file.exists()) {
                MimeBodyPart fileBody = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                fileBody.setDataHandler(new DataHandler(source));
                String fileName = MimeUtility.encodeText(file.getName());
                fileBody.setFileName(fileName);
                multipart.addBodyPart(fileBody);
            } else {
                System.out.println("文件不存在");
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
