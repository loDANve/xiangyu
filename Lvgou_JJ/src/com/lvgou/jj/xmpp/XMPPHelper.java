package com.lvgou.jj.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;
import com.lvgou.jj.Constants;

/**
 * 单例模式创建一个唯一的xmpp连接.
 * 
 * @author oldfeel
 * 
 */
public class XMPPHelper {
	/** openfire服务器ip */
	public static final String IP = "192.168.0.198";
	/** 连接失败 */
	public static final int CONNECT_FAIL = 1003;
	/** 连接成功 */
	public static final int CONNECT_SUCCESS = 1;
	/** 登录成功 */
	public static final int LOGIN_SUCCESS = 1;
	/** 登录失败 */
	public static final int LOGIN_FAIL = 1001;
	/** 注册失败,服务器没有响应 */
	public static final int REG_NO_RESPONSE = 1002;
	/** 注册成功 */
	public static final int REG_SUCCESS = 2;
	/** 注册失败,错误 409 */
	public static final int REG_FAIL_409 = 1002;
	/** 注册失败,其他错误 */
	public static final int REG_FAIL_OTHER = 1002;
	/** 修改密码成功 */
	public static final int CHANGE_PASSWORD_SUCCESS = 8;
	/** 修改密码失败 */
	public static final int CHANGE_PASSWORD_FAIL = 9;
	/** 删除帐号成功 */
	public static final int DELETE_SUCCESS = 10;
	/** 删除帐号失败 */
	public static final int DELETE_FAIL = 11;
	/** 状态,离线 */
	public static final int STATUS_UNAVAILABLE = 12;
	/** 状态,隐身 */
	public static final int STATUS_NOT_AVAILABLE = 13;
	/** 状态,离开 */
	public static final int STATUS_AWAY = 14;
	/** 状态,忙碌 */
	public static final int STATUS_DND = 15;
	/** 状态,Q我吧 */
	public static final int STATUS_CHAT = 16;
	/** 状态,在线 */
	public static final int STATUS_AVAILABLE = 17;
	/** 添加好友成功 */
	public static final int ADD_SUCCESS = 18;
	/** 添加好友失败 */
	public static final int ADD_FAIL = 19;
	/** 聊天室实例 */
	// public static MultiUserChat muc;
	private static XMPPHelper helper;
	public static XMPPConnection connection;

	/**
	 * 初始化一个xmpp连接.
	 * 
	 * @return
	 */
	public static XMPPHelper getInstance() {
		if (helper == null) {
			helper = new XMPPHelper();
		}
		if (connection == null || !connection.isConnected()) {
			XMPPUtil.configure(ProviderManager.getInstance());
			ConnectionConfiguration config = new ConnectionConfiguration(IP,
					5222);
			config.setSecurityMode(SecurityMode.disabled);
			config.setSecurityMode(SecurityMode.required);
			config.setSASLAuthenticationEnabled(false);
			config.setCompressionEnabled(false);
			connection = new XMPPConnection(config);
		}
		return helper;
	}

	/**
	 * 测试连接xmpp服务器是否成功
	 * 
	 * @param handler
	 *            连接后发送消息,0为连接失败,1为连接成功
	 * @param userInfo
	 */
	public void connect(final Handler handler, final String account,
			final String password) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					connection.connect();
					// handler.sendEmptyMessage(CONNECT_SUCCESS); // 连接成功.发送1
					if(connection.isConnected()){
						connection.login(account, password);
						handler.sendEmptyMessage(LOGIN_SUCCESS);
					} else {
						handler.sendEmptyMessage(CONNECT_FAIL); 
					}
				} catch (XMPPException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(CONNECT_FAIL); // 连接失败.发送0
				}
			}
		}.start();
	}

	/**
	 * 登录
	 * 
	 * @param handler
	 * @param account
	 * @param password
	 */
	public void login(final Handler handler, final String account,
			final String password) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					if (connection == null || !connection.isConnected()) {
						handler.sendEmptyMessage(CONNECT_FAIL);
						return;
					}
					if (!connection.isAuthenticated()) {
						connection.login(account, password,
								Constants.XMPP_RESOURCE_NAME);
						handler.sendEmptyMessage(LOGIN_SUCCESS);
					} else {
						handler.sendEmptyMessage(LOGIN_SUCCESS);
					}
				} catch (XMPPException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(LOGIN_FAIL);
				}
			}
		}.start();

	}

	/**
	 * 注册
	 * 
	 * @param handler
	 * @param account
	 * @param password
	 */
	public void regist(final Handler handler, final String account,
			final String password) {
		new Thread() {
			public void run() {
				if (connection == null || !connection.isConnected()) {
					handler.sendEmptyMessage(CONNECT_FAIL);
					return;
				}
				Registration registration = new Registration();
				registration.setType(IQ.Type.SET);
				registration.setTo(connection.getServiceName());
				registration.setUsername(account);
				registration.setPassword(password);
				registration.addAttribute("android", "created by android");
				PacketFilter filter = new AndFilter(new PacketIDFilter(
						registration.getPacketID()), new PacketTypeFilter(
						IQ.class));
				PacketCollector collector = connection
						.createPacketCollector(filter);
				connection.sendPacket(registration);
				IQ result = (IQ) collector.nextResult(SmackConfiguration
						.getPacketReplyTimeout());
				collector.cancel(); // 停止请求
				if (result == null) {
					handler.sendEmptyMessage(REG_NO_RESPONSE);
				} else if (result.getType() == IQ.Type.RESULT) {
					handler.sendEmptyMessage(REG_SUCCESS);
					try {
						// 登录
						connection.login(account, password);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				} else {
					if (result.getError().toString()
							.equalsIgnoreCase("conflict(409)")) {
						handler.sendEmptyMessage(REG_FAIL_409);
					} else {
						handler.sendEmptyMessage(REG_FAIL_OTHER);
					}
					// LogUtil.showLog("注册失败,错误:" + result.getError());
				}
			};
		}.start();
	}

	/**
	 * 修改密码
	 * 
	 * @param handler
	 * @param newPassword
	 */
	public static void changePassword(final Handler handler,
			final String newPassword) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				if (connection == null || !connection.isConnected()) {
					handler.sendEmptyMessage(CONNECT_FAIL);
					return;
				}
				try {
					connection.getAccountManager().changePassword(newPassword);
					handler.sendEmptyMessage(CHANGE_PASSWORD_SUCCESS);
				} catch (XMPPException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(CHANGE_PASSWORD_FAIL);
				}
			}
		}.start();
	}

	/**
	 * 设置状态
	 * 
	 * @param handler
	 * @param which
	 *            0为在线,1为Q我吧,2为忙碌,3为离开,4为隐身,5为离线
	 */
	public void setStatus(final Handler handler, final int which) {
		new Thread() {
			public void run() {
				if (connection == null || !connection.isConnected()) {
					handler.sendEmptyMessage(CONNECT_FAIL);
					return;
				}
				Presence presence;
				switch (which) {
				case 0:
					presence = new Presence(Presence.Type.available);
					connection.sendPacket(presence);
					Log.v("state", "设置在线");
					handler.sendEmptyMessage(STATUS_AVAILABLE);
					break;
				case 1:
					presence = new Presence(Presence.Type.available);
					presence.setMode(Presence.Mode.chat);
					connection.sendPacket(presence);
					Log.v("state", "设置Q我吧");
					System.out.println(presence.toXML());
					handler.sendEmptyMessage(STATUS_CHAT);
					break;
				case 2:
					presence = new Presence(Presence.Type.available);
					presence.setMode(Presence.Mode.dnd);
					connection.sendPacket(presence);
					Log.v("state", "设置忙碌");
					System.out.println(presence.toXML());
					handler.sendEmptyMessage(STATUS_DND);
					break;
				case 3:
					presence = new Presence(Presence.Type.available);
					presence.setMode(Presence.Mode.away);
					connection.sendPacket(presence);
					Log.v("state", "设置离开");
					System.out.println(presence.toXML());
					handler.sendEmptyMessage(STATUS_AWAY);
					break;
				case 4:
					Roster roster = connection.getRoster();
					Collection<RosterEntry> entries = roster.getEntries();
					for (RosterEntry entry : entries) {
						presence = new Presence(Presence.Type.unavailable);
						presence.setPacketID(Packet.ID_NOT_AVAILABLE);
						presence.setFrom(connection.getUser());
						presence.setTo(entry.getUser());
						connection.sendPacket(presence);
						System.out.println(presence.toXML());
					}
					// 向同一用户的其他客户端发送隐身状态
					presence = new Presence(Presence.Type.unavailable);
					presence.setPacketID(Packet.ID_NOT_AVAILABLE);
					presence.setFrom(connection.getUser());
					presence.setTo(StringUtils.parseBareAddress(connection
							.getUser()));
					connection.sendPacket(presence);
					Log.v("state", "设置隐身");
					handler.sendEmptyMessage(STATUS_NOT_AVAILABLE);
					break;
				case 5:
					presence = new Presence(Presence.Type.unavailable);
					connection.sendPacket(presence);
					Log.v("state", "设置离线");
					handler.sendEmptyMessage(STATUS_UNAVAILABLE);
					break;
				default:
					break;
				}
			};
		}.start();
	}

	/**
	 * 获取所有组
	 * 
	 * @param roster
	 * 
	 * @return 所有组集合
	 */
	public void getGroups(final Handler handler) {
		new Thread() {
			public void run() {
				Roster roster = connection.getRoster();
				List<RosterGroup> grouplist = new ArrayList<RosterGroup>();
				Collection<RosterGroup> rosterGroup = roster.getGroups();
				Iterator<RosterGroup> i = rosterGroup.iterator();
				while (i.hasNext()) {
					grouplist.add(i.next());
				}
				Message msg = new Message();
				msg.obj = grouplist;
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 获取好友列表
	 * 
	 * @param handler
	 */
	public void getFriendList(final Handler handler) {
		new Thread() {
			public void run() {
				Roster roster = connection.getRoster();
				Collection<RosterEntry> it = roster.getEntries();
				ArrayList<String> friends = new ArrayList<String>();
				for (RosterEntry rosterEnter : it) {
					if (rosterEnter.getType().name().equals("both")) { // 双向好友
						friends.add(rosterEnter.getUser());
					}
				}

				if (friends.size() == 0) {
					friends.add("暂无好友");
				}
				Message msg = new Message();
				msg.obj = friends;
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 添加好友
	 * 
	 * @param handler
	 * @param name
	 */
	public void addFriend(final Handler handler, final String name) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// connection.getRoster().createEntry(name, null,
					// new String[] { "friends" });
					UserSearchManager search = new UserSearchManager(connection);
					Form searchForm = search.getSearchForm("search."
							+ connection.getServiceName());
					Form answerForm = searchForm.createAnswerForm();
					answerForm.setAnswer("Username", true);
					answerForm.setAnswer("search", name);
					ReportedData data = search.getSearchResults(answerForm,
							"search." + connection.getServiceName());
					Iterator<Row> iterator = data.getRows();
					while (iterator.hasNext()) {
						Row row = iterator.next();
						String queryResult = row.getValues("Username").next()
								.toString();
					}
					handler.sendEmptyMessage(ADD_SUCCESS);
				} catch (XMPPException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(ADD_FAIL);
				}
			}
		}.start();
	}

	/**
	 * 获取房间列表前置方法，获取聊天室服务列表
	 */
	public void getRooms(Handler handler) {
		Collection<HostedRoom> hostrooms;
		List<String> jids = new ArrayList<String>();
		try {
			hostrooms = MultiUserChat.getHostedRooms(connection,
					connection.getServiceName());
			LogUtils.e("ServiceName : " + connection.getServiceName());
			for (HostedRoom entry : hostrooms) {
				jids.add(entry.getJid());
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getRooms(handler, jids);
	}

	/**
	 * 获取房间列表
	 */
	private void getRooms(Handler handler, List<String> jids) {
		List<DiscoverItems.Item> list = new ArrayList<DiscoverItems.Item>();
		// 获得与XMPPConnection相关的ServiceDiscoveryManager
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager
				.getInstanceFor(connection);

		// 获得指定XMPP实体的项目
		// 这个例子获得与在线目录服务相关的项目
		DiscoverItems discoItems;
		try {
			if (jids != null && jids.size() > 0) {
				for (String jid : jids) {
					discoItems = discoManager.discoverItems(jid);
					// 获得被查询的XMPP实体的要查看的项目
					Iterator<DiscoverItems.Item> it = discoItems.getItems();
					// 显示远端XMPP实体的项目
					while (it.hasNext()) {
						DiscoverItems.Item item = (DiscoverItems.Item) it
								.next();
						System.out.println(item.getEntityID());
						System.out.println(item.getName());
						list.add(item);
					}
				}
			}
			Message msg = new Message();
			msg.obj = list;
			handler.sendMessage(msg);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加入聊天室
	 */
	public void addRoom(String user, String roomsName, Handler handler_add,
			Handler handler_chat) {
		addRoom(user, null, roomsName, handler_add, handler_chat);
	}

	/**
	 * 加入聊天室
	 */
	public void addRoom(String user, String password, String roomsName,
			Handler handler_add, Handler handler_chat) {
		try {
			// 使用XMPPConnection创建一个MultiUserChat窗口
			MultiUserChat muc = new MultiUserChat(connection, roomsName);
			// 聊天室服务将会决定要接受的历史记录数量
			// DiscussionHistory history = new DiscussionHistory();
			// history.setMaxStanzas(0);
			// history.setSince(new Date());
			// 用户加入聊天室
			// muc.join(user, password, history,
			// SmackConfiguration.getPacketReplyTimeout());
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxStanzas(15);
			ChatPacketListener cpl = new ChatPacketListener(handler_chat, muc);
			muc.addMessageListener(cpl);
			if (null != password) {
				muc.join(user, password, history,
						SmackConfiguration.getPacketReplyTimeout());
			} else {
				muc.join(user, "", history,
						SmackConfiguration.getPacketReplyTimeout());
			}
			Message msg = new Message();
			msg.obj = muc;
			msg.what = 1;
			handler_add.sendMessage(msg);
			System.out.println("会议室加入成功........");
			// return muc;
		} catch (XMPPException e) {
			e.printStackTrace();
			handler_add.sendEmptyMessage(2);
			System.out.println("会议室加入失败........");
			// return null;
		}
	}

	/**
	 * 断开连接
	 */
	public static void disConnect() {
		connection.disconnect();
	}

	public XMPPConnection getConnect() {
		return connection;
	}

	private static void openConnection() {
		try {
			ConnectionConfiguration connConfig = new ConnectionConfiguration(
					IP, 5222);
			connection = new XMPPConnection(connConfig);
			connection.connect();
		} catch (XMPPException xe) {
			xe.printStackTrace();
		}
	}

	public static XMPPConnection getConnection() {
		if (connection == null) {
			openConnection();
		}
		return connection;
	}
}
