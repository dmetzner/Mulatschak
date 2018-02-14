package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.PopupWindow;
import java.util.ArrayList;
import java.util.List;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextButton;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;
import heroiceraser.mulatschak.game.DrawableObjects.MyPlayer;


//--------------------------------------------------------------------------------------------------
//  Player Info Class
//                      -> every player has a button with his profile image on the game field
//                      -> those buttons open a pop up if clicked
//                          * pop up contains player information
//                      -> there is a blue rectangle around the player image if it's his turn
//                      -> or an info text field for player 0
//
//
public class PlayerInfo extends DrawableObject implements PlayerInfoPopUpView.Listener{

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private GameView view_;

    // The player profile image buttons; onclick they open a information pop up
    // bottom position (player 0) has no such image button
    private MyTextButton buttonLeft;
    private MyTextButton buttonTop;
    private MyTextButton buttonRight;
    private List<MyTextButton> buttons;

    // highlight the player who has the turn with a colored rectangle
    // player 0 has a text field instead, that tells the players it's his turn
    private List<Rect> playersTurnRectangles;
    private Paint rectanglePaint;
    private MyTextField player0sTurnTextField;
    private int activePlayer;         // which player has the turn and should get highlighted
    private boolean showPlayer0Turn;    // needed if the players 0 turn info should be provided
    
    // the pop ups with the player info, if the buttons get clicked
    private int popUpWidth;
    private int popUpHeight;
    private PopupWindow popUp;

    // presentation
    private long startTime;
    private Point endPos;
    private MyTextField presentationTextField;
    private int presentationId;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public PlayerInfo() {
        super();
        setVisible(false);

        // buttons
        buttonLeft = new MyTextButton();
        buttonTop = new MyTextButton();
        buttonRight = new MyTextButton();
        buttons = new ArrayList<>();
        buttons.add(new MyTextButton());
        buttons.add(buttonLeft);
        buttons.add(buttonTop);
        buttons.add(buttonRight);

        // active player
        playersTurnRectangles = new ArrayList<>();
        rectanglePaint = new Paint();
        player0sTurnTextField = new MyTextField();

        // presentation
        presentationTextField = new MyTextField();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        view_ = view;
        GameLayout layout = view.getController().getLayout();
        int active_player = GameController.NOT_SET;

        int padding = layout.getPlayerInfoSize().x / 12;
        if (padding < 1) {
            padding = 1;
        }

        setWidth(layout.getPlayerInfoSize().x);
        setHeight(layout.getPlayerInfoSize().y);
        setPopDimensions();

        if (view.getController().getAmountOfPlayers() > 1) {
            buttonTop.init(view, layout.getPlayerInfoTopPos(),
                    layout.getPlayerInfoSize(), "lil_robo_0", "");
        }

        if (view.getController().getAmountOfPlayers() > 2) {
            buttonLeft.init(view, layout.getPlayerInfoLeftPos(),
                    layout.getPlayerInfoSize(), "lil_robo_1", "");
        }

        if (view.getController().getAmountOfPlayers() > 3) {
            buttonRight.init(view, layout.getPlayerInfoRightPos(),
                    layout.getPlayerInfoSize(), "lil_robo_2", "");
        }

        // active player playersTurnRectangles
        for (int i = 0; i < view.getController().getAmountOfPlayers(); i++) {
            Rect rect = new Rect();
            if (i != 0) {

                // second player is on top pos if only 2 players
                if (view.getController().getAmountOfPlayers() == 2) {
                    rect.set(buttons.get(2).getPosition().x - padding,
                            buttons.get(2).getPosition().y - padding,
                            buttons.get(2).getPosition().x + layout.getPlayerInfoSize().x + padding,
                            buttons.get(2).getPosition().y + layout.getPlayerInfoSize().y + padding);
                }
                else {
                    rect.set(buttons.get(i).getPosition().x - padding,
                            buttons.get(i).getPosition().y - padding,
                            buttons.get(i).getPosition().x + layout.getPlayerInfoSize().x + padding,
                            buttons.get(i).getPosition().y + layout.getPlayerInfoSize().y + padding);
                }
            }
            playersTurnRectangles.add(rect);
        }

        String text = view.getResources().getString(R.string.player_info_active_player_is_0);
        player0sTurnTextField.setText(text);
        TextPaint tp = new TextPaint();
        tp.setAntiAlias(true);
        tp.setTextSize(layout.getDealerButtonSize() * (2/3f));
        tp.setTextAlign(Paint.Align.CENTER);
        tp.setColor(Color.WHITE);
        player0sTurnTextField.setTextPaint(tp);
        player0sTurnTextField.setBorder(view.getResources().getColor(R.color.metallic_blue), 0.25f);
        player0sTurnTextField.setMaxWidth(layout.getScreenWidth() / 2);
        player0sTurnTextField.setPosition(new Point(layout.getScreenWidth() / 2,
                (int) (layout.getDealerButtonPosition(0).y + layout.getDealerButtonSize() / 2.0) ));
        player0sTurnTextField.setVisible(true);

        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.setColor(view.getResources().getColor(R.color.metallic_blue));

        // presentation
        presentationTextField.setText("Test Feld");
        TextPaint tp2 = new TextPaint();
        tp2.setAntiAlias(true);
        tp2.setTextSize(layout.getDealerButtonSize());
        tp2.setTextAlign(Paint.Align.CENTER);
        tp2.setColor(Color.WHITE);
        presentationTextField.setTextPaint(tp2);
        presentationTextField.setBorder(view.getResources().getColor(R.color.metallic_blue), 0.15f);
        presentationTextField.setMaxWidth(layout.getScreenWidth() / 2);
        presentationTextField.setPosition(new Point((int) (layout.getScreenWidth() / 2.0),
                (int) (layout.getScreenHeight() / 2.0)) );
        presentationTextField.setVisible(true);

        setVisible(true);
    }

    private void setPopDimensions() {
        popUpWidth = (int) view_.getResources()
                .getDimension(R.dimen.player_info_pop_up_width);
        popUpHeight = (int) view_.getResources()
                .getDimension(R.dimen.player_info_pop_up_height);
    }

    private PopupWindow makePopupWindow(int pos) {
        PlayerInfoPopUpView view = new PlayerInfoPopUpView(view_.getContext());
        view.setListener(this);
        MyPlayer p = view_.getController().getPlayerByPosition(pos);
        String top_display_name = p.getDisplayName();
        Bitmap bitmap = null;
        String text = "";
        switch (pos) {
            case 1:
                bitmap = buttonLeft.getBitmap();
                text = "Beep, beep.";
                break;
            case 2:
                bitmap = buttonTop.getBitmap();
                text = "Beep, beep, beep?";
                break;
            case 3:
                text = "Beeeeeep!";
                bitmap = buttonRight.getBitmap();
                break;
        }
        view.init(top_display_name, text, bitmap);
        return new PopupWindow(view, popUpWidth, popUpHeight, true);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public void draw(Canvas canvas) {
        if (isVisible()) {

            if (activePlayer == 0 && showPlayer0Turn) {
                player0sTurnTextField.draw(canvas);
            }

            if (activePlayer >= 0 && activePlayer < playersTurnRectangles.size()){
                canvas.drawRect(playersTurnRectangles.get(activePlayer), rectanglePaint);
            }

            for (MyTextButton b : buttons) {
                b.draw(canvas);
            }
        }
    }


    // ToDo
    public void startPresentation(final GameController controller) {
        startTime = System.currentTimeMillis();
        controller.setPlayerPresentation(true);
        presentationId = 0;
    }

    // ToDo
    public void drawPresentation(Canvas canvas, final GameController controller) {

        double maxTime = 2500;
        long timeNow = System.currentTimeMillis();
        long timeSinceStart = timeNow - startTime;

        for (int i = 0; i <= presentationId; i++) {
            if (buttons != null && buttons.size() > i && i != presentationId) {
                if (buttons.get(i) != null) {
                    buttons.get(i).draw(canvas);
                }
            }
        }

        // player 0 not important
        if (presentationId == 0) {
            if (timeSinceStart < maxTime / 2) {
                presentationTextField.setText("Hi " + controller.getPlayerById(0).getDisplayName());
            }
            else {
                presentationTextField.setText("Deine Gegner sind");
            }
        }
        else {
            presentationTextField.setText(controller.getPlayerById(presentationId).getDisplayName());
            double percentage = timeSinceStart / (maxTime / 4.0);

            if (percentage > 3) {
                percentage = 0;
            }
            else if (percentage > 2) {
                percentage = 3 - percentage;
            }
            else if (percentage > 1) {
                percentage = 1;
            }
            int alpha = (int) (255 * percentage);
            presentationTextField.updateAlpha(alpha);
        }

        presentationTextField.draw(canvas);

        if (presentationId != 0) {
            double p = timeSinceStart / (maxTime / 3.0);
            if (p > 1) {
                p = 1;
            }
            Point center = new Point(controller.getLayout().getScreenWidth() / 2,
                    controller.getLayout().getScreenHeight() / 2);

            buttons.get(controller.getPlayerById(presentationId).getPosition()).setPosition(
                    new Point((int) (center.x + (endPos.x - center.x) * p),
                    (int) (center.y + (endPos.y - center.y) * p)));
            buttons.get(controller.getPlayerById(presentationId).getPosition()).draw(canvas);
        }

        if (timeSinceStart > maxTime) {
            presentationId++;
            // presentations done
            if (presentationId >= controller.getAmountOfPlayers()) {
                controller.setPlayerPresentation(false);
                controller.startGame();
                return;
            }
            startTime = System.currentTimeMillis();
            endPos = new Point(buttons.get(controller.getPlayerById(presentationId).getPosition()).getPosition());
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Pop up Fun
    //
    public void popUpInfoLeft() {
        popUp = makePopupWindow(GameLayout.POSITION_LEFT);
        Point pos = view_.getController().getLayout().getPlayerInfoLeftPos();
        popUp.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x, pos.y - (int) (popUpHeight / 3.0) + (getHeight() / 2));
    }
    public void popUpInfoTop() {
        popUp = makePopupWindow(GameLayout.POSITION_TOP);
        Point pos = view_.getController().getLayout().getPlayerInfoTopPos();
        popUp.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x - (int) (popUpWidth / 3.0) + (getWidth() / 2), pos.y);
    }
    public void popUpInfoRight() {
        popUp = makePopupWindow(GameLayout.POSITION_RIGHT);
        Point pos = view_.getController().getLayout().getPlayerInfoRightPos();
        popUp.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x  - popUpWidth + getWidth(), pos.y);
    }

    // closes the back button if the x is clicked
    @Override
    public void onBackButtonRequested() {
        if (popUp != null) {
            popUp.dismiss();
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Getter & Setter
    //
    public void setShowPlayer0Turn(boolean showPlayer0Turn) {
        this.showPlayer0Turn = showPlayer0Turn;
    }

    public MyTextButton getButtonLeft() {
        return buttonLeft;
    }

    public MyTextButton getButtonTop() {
        return buttonTop;
    }

    public MyTextButton getButtonRight() {
        return buttonRight;
    }
}
