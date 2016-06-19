package cn.coselding.myblog.email;
import cn.coselding.myblog.domain.Article;
import cn.coselding.myblog.domain.Comment;
import cn.coselding.myblog.domain.Guest;
import cn.coselding.myblog.utils.ConfigUtils;
import org.apache.commons.logging.impl.NoOpLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
/*
邮件发送工具类
 */
public class JavaMailWithAttachment {

	private static MimeMessage message;
	private static Session session;
	private static Transport transport;
	private static String mailHost = "";
	private static String sender_username = "";
	private static String sender_password = "";
	private static String mine = "";
	private static Properties properties = new Properties();

	/*
	 * 初始化方法
	 */
	private JavaMailWithAttachment(boolean debug) {
		InputStream in = JavaMailWithAttachment.class
				.getResourceAsStream("MailServer.properties");
		try {
			properties.load(in);
			this.mailHost = properties.getProperty("mail.smtp.host");
			this.sender_username = properties
					.getProperty("mail.sender.username");
			this.sender_password = properties
					.getProperty("mail.sender.password");
			this.mine = ConfigUtils.getProperty("mine");
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		session = Session.getInstance(properties);
		session.setDebug(debug);// 开启后有调试信息
		message = new MimeMessage(session);
	}

	//单例
	private static JavaMailWithAttachment instance= null;
	public static JavaMailWithAttachment getInstance(){
		if(instance==null){
			instance = new JavaMailWithAttachment(false);
		}
		return instance;
	}

	/**
	 * 发送邮件
	 *
	 * @param subject
	 *            邮件主题
	 * @param sendHtml
	 *            邮件内容
	 * @param receiveUser
	 *            收件人地址
	 * @param attachment
	 *            附件
	 */
	public boolean doSendHtmlEmail(String subject, String sendHtml,
								String receiveUser,File attachment) {
		try {
			// 发件人
			InternetAddress from = new InternetAddress(sender_username);
			message.setFrom(from);

			// 收件人
			InternetAddress to = new InternetAddress(receiveUser);
			message.setRecipient(Message.RecipientType.TO, to);

			// 邮件主题
			// message.setSubject(subject);
			message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();

			// 添加邮件正文
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
			multipart.addBodyPart(contentPart);

			// 添加附件的内容
			if (attachment != null) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment);
				attachmentBodyPart.setDataHandler(new DataHandler(source));

				// 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
				// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
				// sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
				// messageBodyPart.setFileName("=?GBK?B?" +
				// enc.encode(attachment.getName().getBytes()) + "?=");

				// MimeUtility.encodeWord可以避免文件名乱码
				attachmentBodyPart.setFileName(MimeUtility
						.encodeWord(attachment.getName()));
				multipart.addBodyPart(attachmentBodyPart);
			}

			// 将multipart对象放到message中
			message.setContent(multipart);
			// 保存邮件
			message.saveChanges();

			transport = session.getTransport("smtp");
			// smtp验证，就是你用来发邮件的邮箱用户名密码
			transport.connect(mailHost, sender_username, sender_password);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());

			System.out.println("send success!");
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//生成邮件内容
	public String getContent(String gname,String title,String articleUrl,String notRssUrl,boolean isNew){
		String operation = null;
		if(isNew)
			operation = "发表";
		else operation = "修改";
		String content = "<!doctype html>\n" +
				"<html>\n" +
				"<head>\n" +
				"<meta charset=\"utf-8\">\n" +
				"<title>Coselding博客订阅通知</title>\n" +
				"</head>\n" +
				"<p>尊敬的"+gname+",您好！</p>\n" +
				"<p>Coselding博客最新"+operation+"了《"+title+"》文章，如果感兴趣的可以<a href=\""+articleUrl+"\">点击此链接进行阅读</a>。</p>\n" +
				" <p>如需取消订阅，可<a href=\""+notRssUrl+"\">点击此链接取消订阅</a>。</p>\n" +
				"<body>\n" +
				"</body>\n" +
				"</html>";
		return content;
	}

	//发送订阅用户通知
	public void sendRSS(final Article article, final List<Guest> guests, final String contextPath, final boolean isNew){
		//延时操作，开启子线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(Guest guest:guests){
					String articleUrl = ConfigUtils.getProperty("host")+contextPath+article.getStaticURL()+".html";
					String notRssUrl = ConfigUtils.getProperty("host")+contextPath+"/notRss.action?email="+guest.getGemail();
					String content = getContent(guest.getGname(),article.getTitle(),articleUrl,notRssUrl,isNew);
					boolean result = doSendHtmlEmail("Coselding博客",content,guest.getGemail(),null);
					if(!result){
						System.out.println("用户："+guest+" 的邮件发送失败！");
					}
				}
			}
		}).start();
	}

	public void sendCommentNotice(final Guest guest, final Comment comment,final String contextPath){
		//延时操作，开启子线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = ConfigUtils.getProperty("host")+contextPath+"/commentUI.action";
				String content = getNoticeContent(guest,comment.getComcontent(),url);
				boolean result = doSendHtmlEmail("Coselding博客留言通知",content,mine,null);
				if(!result){
					System.out.println("留言通知的邮件发送失败！");
				}
			}
		}).start();
	}

	//得到用户留言通知邮件通知
	public String getNoticeContent(Guest guest,String comcontent,String url){
		String content = "<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head>\n" +
				"\t<title>Coselding博客留言通知</title>\n" +
				"</head>\n" +
				"<body>\n" +
				"\t<p>用户："+guest.getGname()+" ,邮箱："+guest.getGemail()+"&nbsp;的用户给您留了言。</p>\n" +
				"\t<p>留言内容为："+comcontent+"</p>\n" +
				"\t<p>若需查看详细信息，<a href=\""+url+"\">点击此链接可查看</a>。</p>\n" +
				"</body>\n" +
				"</html>";
		return content;
	}

	public static void main(String[] args) {
		JavaMailWithAttachment se = new JavaMailWithAttachment(true);
		File affix = new File("D:\\1.txt");
		se.doSendHtmlEmail("邮件主题", "邮件内容", "1098129797@qq.com", affix);//
	}
}