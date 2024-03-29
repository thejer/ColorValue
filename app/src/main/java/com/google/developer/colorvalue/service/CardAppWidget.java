package com.google.developer.colorvalue.service;

import static android.content.ContentValues.TAG;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.developer.colorvalue.CardDetailsActivity;
import com.google.developer.colorvalue.MainActivity;
import com.google.developer.colorvalue.R;
import com.google.developer.colorvalue.data.Card;
import com.google.developer.colorvalue.data.CardProvider;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class CardAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
            updateWidget(context, widgetId);
        }
    }

    private void updateWidget(Context context, int widgetId) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(CardProvider.Contract.CONTENT_URI,
                null, null, null, null);

        int cardCount;
        if (cursor != null) {
            cardCount = cursor.getCount();
        } else {
            Log.w(TAG, context.getString(R.string.unable_to_read_db_message));
            return;
        }

        Random random = new Random();
        int randomNumber = random.nextInt(cardCount);

        cursor.moveToPosition(randomNumber);

        Card card = new Card(cursor);
        cursor.close();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.card_widget);

        views.setTextViewText(R.id.widget_text, card.getHex());
        views.setInt(R.id.widget_background, "setBackgroundColor", card.getColorInt());

        Intent colorDetailIntent = new Intent(context, CardDetailsActivity.class);
        colorDetailIntent.putExtra(MainActivity.CARD_EXTRA, card);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                widgetId,
                colorDetailIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_background, pendingIntent);

        // Instruct the widget manager to update the widget
        widgetManager.updateAppWidget(widgetId, views);
    }


}

