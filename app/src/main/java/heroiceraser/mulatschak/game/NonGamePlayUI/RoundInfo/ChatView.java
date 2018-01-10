package heroiceraser.mulatschak.game.NonGamePlayUI.RoundInfo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.DrawableBasicObjects.TextField;
import heroiceraser.mulatschak.game.DrawableObjects.MulatschakDeck;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GamePlay.TrickBids.MakeBidsAnimation;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.helpers.HelperFunctions;


//--------------------------------------------------------------------------------------------------
//  Round Info Class
//
public class ChatView extends DrawableObject {

    private final int MAX_MESSAGES = 5;

    private Rect background;
    private Paint backgroundPaint;
    private Point monitor_size_;
    private Point board_to_text_offset_;

    private Point position;
    private TextPaint textPaint;
    private List<MyTextField> messageQueue;
    private MyTextField trash;

    public ChatView() {
        super();
        setVisible(false);
        messageQueue = new ArrayList<>();
        monitor_size_ = new Point();
        board_to_text_offset_ = new Point();
        position = new Point();
        trash = null;
        background = new Rect();
        backgroundPaint = new Paint();
    }

    public void init(GameView view) {
        setWidth(view.getController().getLayout().getRoundInfoSize().x);
        setHeight(view.getController().getLayout().getRoundInfoSize().y);
        setPosition(view.getController().getLayout().getRoundInfoPositon());
        String image_name = "round_info";
        setBitmap(HelperFunctions.loadBitmap(view, image_name , getWidth(), getHeight()));
        setVisible(true);
        board_to_text_offset_.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 8);
        board_to_text_offset_.y = (int) (view.getController().getLayout().getOnePercentOfScreenHeight() * 4);
        monitor_size_.x = (int) (view.getController().getLayout().getOnePercentOfScreenWidth() * 80);
        monitor_size_.y = view.getController().getLayout().getSectors().get(2).y - 2 * board_to_text_offset_.y;

        background = new Rect(0, 0, view.getController().getLayout().getScreenWidth(),
                view.getController().getLayout().getSectors().get(2).y);
        backgroundPaint.setColor(Color.DKGRAY);

        position = new Point(board_to_text_offset_);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize( (int) ((monitor_size_.y / 100.0) * 15));

        trash = null;
    }

    public void addMessage(MyPlayer player, String message) {
        MyTextField textField = new MyTextField();
        textField.setVisible(true);

        String player_name = player.getDisplayName();

        if (player_name.length() > 15) {
            player_name = player_name.substring(0, 14);
        }

        String text = player_name + ": " + message;

        textField.setText(text);
        textField.setTextPaint(textPaint);

        if (messageQueue.size() >= MAX_MESSAGES)
        {
            trash = messageQueue.get(0);
            messageQueue.remove(0);
        }
        messageQueue.add(textField);
    }


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

    public void clearChat() {
        trash = null;
        messageQueue.clear();
    }

}
