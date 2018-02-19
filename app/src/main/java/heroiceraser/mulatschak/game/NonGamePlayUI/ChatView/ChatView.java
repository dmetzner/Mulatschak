package heroiceraser.mulatschak.game.NonGamePlayUI.ChatView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;


//--------------------------------------------------------------------------------------------------
//  Chat View Class
//
public class ChatView extends DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private Rect background;
    private Paint backgroundPaint;
    private Point monitorSize;
    private Point boardToTextOffset;

    private Point position;
    private TextPaint textPaint;
    private List<MyTextField> messageQueue;
    private MyTextField trash;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public ChatView() {
        super();
        setVisible(false);
        messageQueue = new ArrayList<>();
        monitorSize = new Point();
        boardToTextOffset = new Point();
        position = new Point();
        trash = null;
        background = new Rect();
        backgroundPaint = new Paint();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        setWidth(view.getController().getLayout().getRoundInfoSize().x);
        setHeight(view.getController().getLayout().getRoundInfoSize().y);
        setPosition(view.getController().getLayout().getRoundInfoPositon());
        String image_name = "round_info";
        setBitmap(HelperFunctions.loadBitmap(view, image_name , getWidth(), getHeight()));
        setVisible(true);
        boardToTextOffset.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 8);
        boardToTextOffset.y = (int) (view.getController().getLayout().getOnePercentOfScreenHeight() * 4);
        monitorSize.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 80);
        monitorSize.y = view.getController().getLayout().getSectors().get(2).y - 2 * boardToTextOffset.y;

        background = new Rect(0, 0, view.getController().getLayout().getScreenWidth(),
                view.getController().getLayout().getSectors().get(2).y);
        backgroundPaint.setColor(Color.DKGRAY);

        position = new Point(boardToTextOffset);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        String font = "fonts/nk57-monospace-no-rg.ttf";
        Typeface tf = Typeface.createFromAsset(view.getContext().getAssets(), font);
        textPaint.setTypeface(tf);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextScaleX(0.7f);
        textPaint.setTextSize( (int) ((monitorSize.y / 100.0) * 15));

        trash = null;
    }


    //----------------------------------------------------------------------------------------------
    //  add message
    //
    private void addMessage(String player_name, String message) {

        final int MAX_MESSAGES = 5;

        MyTextField textField = new MyTextField();
        textField.setVisible(true);

        String text = player_name + ": " + message;
        // no name? only print message!
        if (player_name.equals("")) {
            text = message;
        }

        textField.setText(text);
        textField.setTextPaint(textPaint);

        if (messageQueue.size() >= MAX_MESSAGES)
        {
            trash = messageQueue.get(0);
            messageQueue.remove(0);
        }
        messageQueue.add(textField);
    }

    //----------------------------------------------------------------------------------------------
    //  add message
    //
    public void addMessage(MyPlayer player, String message) {

        String player_name = "";

        if (player != null) {
            player_name = player.getDisplayName();
        }

        // make the player name fit a fixed size!
        player_name = HelperFunctions.getStringOfFixedLength(player_name, 9);

        addMessage(player_name, message);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (!isVisible()) {
            return;
        }

        // draw bg
        canvas.drawRect(background, backgroundPaint);

        // draw last removed message
        if (trash != null) {
            trash.setPosition(position);
            trash.draw(canvas);
        }

        // draw chat messages
        int i = 1;
        float offset = textPaint.getTextSize() * 1.2f;
        for (MyTextField tf : messageQueue) {
            tf.setPosition(new Point(position.x, position.y + (int) (offset * i)));
            tf.draw(canvas);
            i++;
        }

        // draw monitor borders
        canvas.drawBitmap(getBitmap(), getPosition().x, getPosition().y, null);
    }


    //----------------------------------------------------------------------------------------------
    //  clear chat
    //
    public void clearChat() {
        trash = null;
        messageQueue.clear();
    }


    //----------------------------------------------------------------------------------------------
    //  Getter
    //
    public TextPaint getTextPaint() {
        return textPaint;
    }
}
