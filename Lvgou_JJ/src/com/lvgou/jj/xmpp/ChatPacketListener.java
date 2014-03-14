package com.lvgou.jj.xmpp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DelayInformation;

import android.os.Handler;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.jj.Constants;
import com.lvgou.jj.been.ChatMessage;

public class ChatPacketListener implements PacketListener {

	private Handler handler;
	private MultiUserChat muc;

	public ChatPacketListener(Handler handler, MultiUserChat muc) {
		this.handler = handler;
		this.muc = muc;
	}

	@Override
	public void processPacket(Packet packet) {
		Message message = (Message) packet;
		String from = message.getFrom();
		ChatMessage cm;
		if (message.getBody() != null) {
			cm = new ChatMessage();
			cm.setFrom(from.substring(from.indexOf("/") + 1));
			cm.setTo(message.getTo());
			cm.setBody(message.getBody());
			DelayInformation inf = (DelayInformation) message.getExtension("x",
					"jabber:x:delay");
			Date sentDate;
			if (inf != null) {
				cm.setIsnew(false);
				sentDate = inf.getStamp();
			} else {
				cm.setIsnew(true);
				sentDate = new Date();
			}
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			cm.setDate(format.format(sentDate));

			LogUtils.i("Receive old message: date=" + cm.getDate()
					+ " ; message=" + message.getBody());
			android.os.Message msg = new android.os.Message();
			msg.what = Constants.RECEIVEMSG;
			msg.obj = cm;
			handler.sendMessage(msg);
		}
	}

}
