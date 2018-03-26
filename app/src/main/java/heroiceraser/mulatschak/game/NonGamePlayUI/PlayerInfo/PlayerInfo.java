package heroiceraser.mulatschak.game.NonGamePlayUI.PlayerInfo;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import at.heroiceraser.mulatschak.R;
import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.DrawableBasicObjects.MyButton;
import heroiceraser.mulatschak.DrawableBasicObjects.MyTextField;
import heroiceraser.mulatschak.game.BaseObjects.MyPlayer;
import heroiceraser.mulatschak.game.GameController;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  Player Info Class
//                      -> every player has a button with his profile image on the game field
//                      -> those buttonsDefault open a pop up if clicked
//                          * pop up contains player information
//                      -> there is a blue rectangle around the player image if it's his turn
//                      -> or an info text field for player 0
//
//
public class PlayerInfo extends DrawableObject implements
        PlayerInfoPopUpView.Listener,
        LastPlayerLeftPopUpView.Listener,
        PlayerLeftPopUpView.Listener {

    //----------------------------------------------------------------------------------------------
    //  Member Variables
    //
    private GameView view_;

    // The player profile image buttonsDefault; onclick they open a information pop up
    // bottom position (player 0) has no such image button
    private List<Rect> playersRectangles;
    private Paint rectanglePaint;
    private MyButton buttonLeftDefault;
    private MyButton buttonTopDefault;
    private MyButton buttonRightDefault;
    private List<MyButton> buttonsDefault;
    private List<MyButton> buttons;

    // highlight the player who has the turn with a colored rectangle
    // player 0 has a text field instead, that tells the players it's his turn
    private List<Rect> playersTurnRectangles;
    private Paint rectanglePaintHighlight;
    private MyTextField player0sTurnTextField;
    private int activePlayer;         // which player has the turn and should get highlighted
    private boolean showPlayer0Turn;    // needed if the players 0 turn info should be provided

    // the pop ups with the player info, if the buttonsDefault get clicked
    private int popUpWidth;
    private int popUpHeight;
    private PopupWindow popUp;

    private PopupWindow popUpPlayerLeft;
    private MyPlayer leftPlayer;
    private boolean popUpsBlocked;

    private PlayerPresentation presentation;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public PlayerInfo() {
        super();
        setVisible(false);
        popUpsBlocked = false;

        // buttonsDefault
        buttonLeftDefault = new MyButton();
        buttonTopDefault = new MyButton();
        buttonRightDefault = new MyButton();
        buttonsDefault = new ArrayList<>();
        buttonsDefault.add(new MyButton());
        buttonsDefault.add(buttonLeftDefault);
        buttonsDefault.add(buttonTopDefault);
        buttonsDefault.add(buttonRightDefault);

        buttons = new ArrayList<>();
        buttons.add(null);
        buttons.add(null);
        buttons.add(null);
        buttons.add(null);


        // active player
        playersTurnRectangles = new ArrayList<>();
        rectanglePaintHighlight = new Paint();
        player0sTurnTextField = new MyTextField();

        playersRectangles = new ArrayList<>();
        rectanglePaint = new Paint();


        presentation = new PlayerPresentation();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        view_ = view;
        GameLayout layout = view.getController().getLayout();

        int padding = layout.getPlayerInfoSize().x / 17;
        if (padding < 1) {
            padding = 1;
        }
        int paddingHighlight = layout.getPlayerInfoSize().x / 9;
        if (paddingHighlight < 3) {
            paddingHighlight = 3;
        }


        setWidth(layout.getPlayerInfoSize().x);
        setHeight(layout.getPlayerInfoSize().y);
        setPopDimensions();

        if (view.getController().getAmountOfPlayers() > 1) {
            buttonTopDefault.init(view, layout.getPlayerInfoTopPos(),
                    layout.getPlayerInfoSize(), "lil_robo_0", "");
        }

        if (view.getController().getAmountOfPlayers() > 2) {
            buttonLeftDefault.init(view, layout.getPlayerInfoLeftPos(),
                    layout.getPlayerInfoSize(), "lil_robo_1", "");
        }

        if (view.getController().getAmountOfPlayers() > 3) {
            buttonRightDefault.init(view, layout.getPlayerInfoRightPos(),
                    layout.getPlayerInfoSize(), "lil_robo_2", "");
        }

        // active player playersTurnRectangles
        for (int i = 0; i < view.getController().getAmountOfPlayers(); i++) {
            Rect rect = new Rect();
            Rect rectHighlight = new Rect();
            if (i != 0) {
                // second player is on top pos if only 2 players
                if (view.getController().getAmountOfPlayers() == 2) {
                    rect.set(buttonsDefault.get(2).getPosition().x - padding,
                            buttonsDefault.get(2).getPosition().y - padding,
                            buttonsDefault.get(2).getPosition().x + layout.getPlayerInfoSize().x + padding,
                            buttonsDefault.get(2).getPosition().y + layout.getPlayerInfoSize().y + padding);

                    rectHighlight.set(buttonsDefault.get(2).getPosition().x - paddingHighlight,
                            buttonsDefault.get(2).getPosition().y - paddingHighlight,
                            buttonsDefault.get(2).getPosition().x + layout.getPlayerInfoSize().x + paddingHighlight,
                            buttonsDefault.get(2).getPosition().y + layout.getPlayerInfoSize().y + paddingHighlight);
                }
                else {
                    rect.set(buttonsDefault.get(i).getPosition().x - padding,
                            buttonsDefault.get(i).getPosition().y - padding,
                            buttonsDefault.get(i).getPosition().x + layout.getPlayerInfoSize().x + padding,
                            buttonsDefault.get(i).getPosition().y + layout.getPlayerInfoSize().y + padding);

                    rectHighlight.set(buttonsDefault.get(i).getPosition().x - paddingHighlight,
                            buttonsDefault.get(i).getPosition().y - paddingHighlight,
                            buttonsDefault.get(i).getPosition().x + layout.getPlayerInfoSize().x + paddingHighlight,
                            buttonsDefault.get(i).getPosition().y + layout.getPlayerInfoSize().y + paddingHighlight);
                }
            }
            playersTurnRectangles.add(rectHighlight);
            playersRectangles.add(rect);
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

        rectanglePaintHighlight.setStyle(Paint.Style.FILL);
        rectanglePaintHighlight.setColor(view.getResources().getColor(R.color.metallic_blue));

        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.setColor(view.getResources().getColor(R.color.white));

        presentation.init(view);

        setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  setPopDimensions
    //
    private void setPopDimensions() {
        popUpWidth = (int) view_.getResources()
                .getDimension(R.dimen.player_info_pop_up_width);
        popUpHeight = (int) view_.getResources()
                .getDimension(R.dimen.player_info_pop_up_height);
    }


    //----------------------------------------------------------------------------------------------
    //  makePopupWindow
    //
    private PopupWindow makePopupWindow(int pos) {
        PlayerInfoPopUpView view = new PlayerInfoPopUpView(view_.getContext());
        view.setListener(this);
        MyPlayer p = view_.getController().getPlayerByPosition(pos);
        String top_display_name = "";
        if (p != null) {
            top_display_name = p.getDisplayName();
        }
        Bitmap bitmap = null;
        String text = "";
        switch (pos) {
            case 1:
                if (buttons.get(1) != null && buttons.get(1).getBitmap() != null) {
                    bitmap = buttons.get(1).getBitmap();
                }
                else {
                    bitmap = buttonLeftDefault.getBitmap();
                    text = "Beep, beep.";
                }
                break;
            case 2:
                if (buttons.get(2) != null && buttons.get(2).getBitmap() != null) {
                    bitmap = buttons.get(2).getBitmap();
                }
                else {
                    bitmap = buttonTopDefault.getBitmap();
                    text = "Beep, beep, beep?";

                }
                break;
            case 3:
                if (buttons.get(3) != null && buttons.get(3).getBitmap() != null) {
                    bitmap = buttons.get(3).getBitmap();
                }
                else {
                    bitmap = buttonRightDefault.getBitmap();
                    text = "Beeeeeep!";
                }
                break;
        }
        view.init(top_display_name, text, bitmap);
        return new PopupWindow(view, popUpWidth, popUpHeight, true);
    }


    //----------------------------------------------------------------------------------------------
    //  makePopupWindow
    //
    private PopupWindow makeLastPlayerLeftPopupWindow(String displayName) {
        LastPlayerLeftPopUpView view = new LastPlayerLeftPopUpView(view_.getContext());
        view.setListener(this);
        view.init(displayName);
        return new PopupWindow(view, popUpWidth, popUpHeight, false);
    }


    //----------------------------------------------------------------------------------------------
    //  makePopupWindow
    //
    private PopupWindow makePlayerLeftPopupWindow(String displayName) {
        PlayerLeftPopUpView view = new PlayerLeftPopUpView(view_.getContext());
        view.setListener(this);
        view.init(displayName);
        return new PopupWindow(view, popUpWidth, popUpHeight, false);
    }


    public void initButton(int pos, GameController controller) {
        if (pos >= 4) throw new AssertionError();
        MyButton b = new MyButton();
        switch (pos) {
            case GameLayout.POSITION_LEFT:
                b.init(controller.getView(), controller.getLayout().getPlayerInfoLeftPos(),
                        controller.getLayout().getPlayerInfoSize(),
                        controller.getPlayerByPosition(pos).getProfileImage(), "");
                break;
            case GameLayout.POSITION_TOP:
                b.init(controller.getView(), controller.getLayout().getPlayerInfoTopPos(),
                        controller.getLayout().getPlayerInfoSize(),
                        controller.getPlayerByPosition(pos).getProfileImage(), "");
                break;
            case GameLayout.POSITION_RIGHT:
                b.init(controller.getView(), controller.getLayout().getPlayerInfoRightPos(),
                        controller.getLayout().getPlayerInfoSize(),
                        controller.getPlayerByPosition(pos).getProfileImage(), "");
                break;
        }
        buttons.remove(pos);
        buttons.add(pos, b);
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
                canvas.drawRect(playersTurnRectangles.get(activePlayer), rectanglePaintHighlight);
            }

            for (Rect r : playersRectangles) {
                canvas.drawRect(r, rectanglePaint);
            }

            for (int i = 0; i < buttonsDefault.size(); i++) {

                if (buttons.get(i) != null && buttons.get(i).getBitmap() != null) {
                    buttons.get(i).draw(canvas);
                }
                else {
                    buttonsDefault.get(i).draw(canvas);
                }
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //  start Presentation
    //
    public void startPresentation(GameController controller) {
        presentation.start(controller);
    }


    //----------------------------------------------------------------------------------------------
    //  draw Presentation
    //
    public void drawPresentation(Canvas canvas, final GameController controller) {
        presentation.draw(canvas, buttonsDefault, controller);
    }


    //----------------------------------------------------------------------------------------------
    // Touch Events
    //
    public synchronized void touchEventDown(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        buttonLeftDefault.touchEventDown(X, Y);
        buttonTopDefault.touchEventDown(X, Y);
        buttonRightDefault.touchEventDown(X, Y);

        presentation.touchEventDown(X, Y);
    }

    public synchronized void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        buttonLeftDefault.touchEventMove(X, Y);
        buttonTopDefault.touchEventMove(X, Y);
        buttonRightDefault.touchEventMove(X, Y);

        // presentation.touchEventMove(X, Y, ui);
    }

    public synchronized void touchEventUp(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        if (buttonLeftDefault.touchEventDown(X, Y)) {
            popUpInfoLeft();
        }
        else if (buttonTopDefault.touchEventDown(X, Y)) {
            popUpInfoTop();
        }
        else if (buttonRightDefault.touchEventDown(X, Y)) {
            popUpInfoRight();
        }

        presentation.touchEventUp(X, Y);
    }


    //----------------------------------------------------------------------------------------------
    //  Pop up Fun
    //
    private void popUpInfoLeft() {
        popUp = makePopupWindow(GameLayout.POSITION_LEFT);
        Point pos = view_.getController().getLayout().getPlayerInfoLeftPos();
        popUp.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x, pos.y - (int) (popUpHeight / 3.0) + (getHeight() / 2));
    }

    private void popUpInfoTop() {
        popUp = makePopupWindow(GameLayout.POSITION_TOP);
        Point pos = view_.getController().getLayout().getPlayerInfoTopPos();
        popUp.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x - (int) (popUpWidth / 3.0) + (getWidth() / 2), pos.y);
    }

    private void popUpInfoRight() {
        popUp = makePopupWindow(GameLayout.POSITION_RIGHT);
        Point pos = view_.getController().getLayout().getPlayerInfoRightPos();
        popUp.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x  - popUpWidth + getWidth(), pos.y);
    }


    public void makeLastPlayerPopUp(MyPlayer leftPlayer) {
        this.leftPlayer = leftPlayer;
        popUpsBlocked = true;
        Point pos = view_.getController().getLayout().getCenter();
        popUpPlayerLeft = makeLastPlayerLeftPopupWindow(leftPlayer.getDisplayName());
        popUpPlayerLeft.setFocusable(false); //Setting this to true will prevent the events to reach activity below
        popUpPlayerLeft.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x - popUpWidth / 2, pos.y - popUpHeight / 2);
    }

    public void makeLeftPlayerPopUp(MyPlayer leftPlayer) {
        this.leftPlayer = leftPlayer;
        popUpsBlocked = true;
        Point pos = view_.getController().getLayout().getCenter();
        popUpPlayerLeft = makePlayerLeftPopupWindow(leftPlayer.getDisplayName());
        popUpPlayerLeft.setFocusable(false); //Setting this to true will prevent the events to reach activity below
        popUpPlayerLeft.showAtLocation(view_, Gravity.NO_GRAVITY,
                pos.x - popUpWidth / 2, pos.y - popUpHeight / 2);
    }


    //----------------------------------------------------------------------------------------------
    // closes pop up if the x is clicked
    @Override
    public void onBackButtonRequested() {
        if (popUp != null) {
            popUp.dismiss();
        }
    }


    //----------------------------------------------------------------------------------------------
    //
    @Override
    public void onYesButtonRequested() {
        if (popUpPlayerLeft != null) {
            popUpPlayerLeft.dismiss();
            popUpsBlocked = false;
            view_.getController().lastPlayerLeftSoLetMeWin();
        }
    }


    //----------------------------------------------------------------------------------------------
    //
    @Override
    public void onNoButtonRequested() {
        if (popUpPlayerLeft != null) {
            popUpPlayerLeft.dismiss();
            popUpsBlocked = false;
            view_.getController().playerLeftContinueWithAI(leftPlayer);
        }
    }

    //----------------------------------------------------------------------------------------------
    //
    @Override
    public void onOkButtonRequested() {
        if (popUpPlayerLeft != null) {
            popUpPlayerLeft.dismiss();
            popUpsBlocked = false;
            view_.getController().playerLeftContinueWithAI(leftPlayer);
        }
    }


    public void clear() {
        if (popUpPlayerLeft != null) {
            popUpPlayerLeft.dismiss();
        }
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

    public void setActivePlayer(int id) {
        this.activePlayer = id;
    }


    public boolean arePopUpsBlocked() {
        return popUpsBlocked;
    }
}
