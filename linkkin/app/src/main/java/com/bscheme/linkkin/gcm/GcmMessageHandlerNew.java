package com.bscheme.linkkin.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bscheme.linkkin.R;

public class GcmMessageHandlerNew extends IntentService {
	NotificationManager notificationmanager = null;
	String message, title;
	String type = "";

	String result="";
	private Handler handler;
	MediaPlayer player1, player2;
	String help,name, phone,address, location,email, time;
    String userID="";
	public final static String MY_ACTION = "MY_ACTION_REQUEST";

	SharedPreferences prefNotify;

	public GcmMessageHandlerNew() {
		super("GcmMessageHandlerNew");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		handler = new Handler();
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		prefNotify=getSharedPreferences("Push_Notification", Context.MODE_PRIVATE);
		boolean notify=prefNotify.getBoolean("notify",false);
		if(notify)
		{
			Bundle extras = intent.getExtras();
			String title=extras.getString("title");
			String message=extras.getString("message");

			//showToast("Title: "+title+"\n"+"Message: "+message);

			RemoteViews remoteviews = new RemoteViews(getPackageName(), R.layout.notify_layout);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
					.setSmallIcon(R.drawable.ic_action_bar_linkin)
					.setTicker("Linkkin has new Update")
					.setContent(remoteviews);

			// Creates an Intent for the Activity
			Intent notifyIntent =
					new Intent(this, ResultActivity.class);
// Sets the Activity to start in a new, empty task
			notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
					Intent.FLAG_ACTIVITY_CLEAR_TASK);
			// Creates the PendingIntent
			PendingIntent pendingNotifyIntent =
					PendingIntent.getActivity(
							this,
							0,
							notifyIntent,
							PendingIntent.FLAG_UPDATE_CURRENT
					);
			//	remoteviews.setImageViewResource(R.id.imgNotify, R.drawable.ic_linkin_launcher);
			remoteviews.setTextViewText(R.id.txtTitle, title);
			remoteviews.setTextViewText(R.id.txtDesc,message);
			NotificationManager notificationmanager=(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
			//notificationmanager.notify(0,builder.build());


			builder.setContentIntent(pendingNotifyIntent); // Puts the PendingIntent into the notification builder

			Notification noti = builder.build();

			// PendingIntent i=PendingIntent.getActivity(MainActivity.this, 0,new Intent(MainActivity.this, SecondActivity.class),0);
		/*	Intent switchIntent = new Intent(MainActivity.this, switchButtonListener.class);
			PendingIntent i = PendingIntent.getBroadcast(MainActivity.this, 0, switchIntent, 0);
			remoteviews.setOnClickPendingIntent(R.id.linear, i);   */

			//	noti.flags |= builder.build().FLAG_NO_CLEAR | builder.build().FLAG_ONGOING_EVENT;
			noti.flags |= builder.build().FLAG_AUTO_CANCEL;

			notificationmanager.notify(0,noti);

		}
		//else showToast("Notification not enabled");


		//GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		// String messageType = gcm.getMessageType(intent);

		//mes = extras.getString("message");
		//type = extras.getString("type");
		//title = extras.getString("title");
		// help=extras.getString("type");

		 
		 
		 
		 
		 
		 
		 

			

			// ----------------------

	/*		RemoteViews remoteviews = new RemoteViews(getPackageName(),
					R.layout.customnotification);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					getApplicationContext())
					.setSmallIcon(R.drawable.robot2)
					.setTicker("You have a notification")
					.setContent(remoteviews);
			remoteviews.setImageViewResource(R.id.imagenotileft,R.drawable.robot2);
			// remoteviews.setTextViewText(R.id.imagenotiright,sdf.format(cal.getTime()));
			remoteviews.setTextViewText(R.id.title, "Need Blood");
			remoteviews.setTextViewText(R.id.text,"Data recieved");
			notificationmanager = (NotificationManager) getApplicationContext()
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// notificationmanager.notify(0,builder.build());

			Notification noti = builder.build();

			// PendingIntent i=PendingIntent.getActivity(MainActivity.this,
			// 0,new Intent(MainActivity.this, SecondActivity.class),0);   */

			/*
			 * Intent switchIntent = new Intent(MainActivity.this,
			 * switchButtonListener.class); PendingIntent i =
			 * PendingIntent.getBroadcast(MainActivity.this, 0, switchIntent,
			 * 0); remoteviews.setOnClickPendingIntent(R.id.linear, i);
			 */
			// noti.flags |= builder.build().FLAG_ONGOING_EVENT;
	/*		noti.flags |= builder.build().FLAG_AUTO_CANCEL;

			notificationmanager.notify(0, noti);

			// --------------------------

			// We need to notify the broadcast reciever of request inbox to show
			// the request    */

		
			// ___________________________________________________________________________

		

		// showToast();

		//showToast();

	//	stopSelf();

	}

	public void showToast(final String text) {
		handler.post(new Runnable() {
			public void run() {

				Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
			}
		});

	}

	private void openIntent(){

	}
}