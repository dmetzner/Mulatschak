package heroiceraser.mulatschak.GameKeyBoard;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.DrawableBasicObjects.DrawableObject;

import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


public class MyKeyBoard extends DrawableObject {


    private boolean lower;
    private boolean upper;
    private boolean number;

    private List<MyKeyBoardButton> lowerKeyBoard;
    private List<MyKeyBoardButton> upperKeyBoard;
    private List<MyKeyBoardButton> numberKeyBoard;
    private List<MyKeyBoardButton> lastRowKeyBoard;


    public MyKeyBoard() {
        super();
        lowerKeyBoard = new ArrayList<>();
        upperKeyBoard = new ArrayList<>();
        numberKeyBoard = new ArrayList<>();
        lastRowKeyBoard = new ArrayList<>();
    }

    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        setPosition(new Point((int) (layout.getOnePercentOfScreenWidth() * 2),
                (int) (layout.getOnePercentOfScreenHeight() * 55) ));
        setWidth(layout.getScreenWidth() - (int) (layout.getOnePercentOfScreenWidth() * 4));
        setHeight((int) (layout.getOnePercentOfScreenHeight() * 40));

        int buttonWidth = getWidth() / 11;
        int buttonHeight = getHeight() / 5;

        Point position = new Point(getPosition());

        lower = true;
        upper = false;
        number = false;

        String[] lower_buttons = new String[3];
        lower_buttons[0] = "qwertzuiopü";
        lower_buttons[1] = "asdfghjklöä";
        lower_buttons[2] = "⇧yxcvbnmß⇦ ";

        String[] upper_buttons = new String[3];
        upper_buttons[0] = "QWERTZUIOPÜ";
        upper_buttons[1] = "ASDFGHJKLÖÄ";
        upper_buttons[2] = "⇩YXCVBNM?⇦";

        String[] number_buttons = new String[3];
        number_buttons[0] = "1234567890?";
        number_buttons[1] = "@+-*~=#><:;";
        number_buttons[2] = "!\"^$%&/()='°";

        for (int row = 0; row < 3; row++) {
            Point pos = new Point(position);
            pos.y += buttonHeight * row;
            for (int col = 0; col < 11; col++) {
                pos.x = position.x;
                pos.x += buttonWidth * col;

                MyKeyBoardButton tmp = new MyKeyBoardButton();

                int width = buttonWidth;
                if (row == 2 && col == 9) {
                    width *= 2;
                }
                tmp.init(view, new Point(pos), width, buttonHeight,
                        "" , "" + upper_buttons[row].charAt(col));
                lowerKeyBoard.add(tmp);
                if (row == 2 && col == 9) {
                    break;
                }
            }
        }

        for (int row = 0; row < 3; row++) {
            Point pos = new Point(position);
            pos.y += buttonHeight * row;
            for (int col = 0; col < 11; col++) {
                pos.x = position.x;
                pos.x += buttonWidth * col;

                MyKeyBoardButton tmp = new MyKeyBoardButton();

                int width = buttonWidth;
                if (row == 2 && col == 9) {
                    width *= 2;
                }
                tmp.init(view, new Point(pos), width, buttonHeight,
                        "" , "" + lower_buttons[row].charAt(col));
                upperKeyBoard.add(tmp);
                if (row == 2 && col == 9) {
                    break;
                }
            }
        }


        for (int row = 0; row < 3; row++) {
            Point pos = new Point(position);
            pos.y += buttonHeight * row;
            for (int col = 0; col < 11; col++) {
                pos.x = position.x;
                pos.x += buttonWidth * col;

                MyKeyBoardButton tmp = new MyKeyBoardButton();

                tmp.init(view, new Point(pos), buttonWidth, buttonHeight,
                        "" , "" + number_buttons[row].charAt(col));
                numberKeyBoard.add(tmp);
            }
        }

        String lastRow = "!123, .↵";


        MyKeyBoardButton tmp = new MyKeyBoardButton();
        Point pos = new Point(position);
        pos.y += buttonHeight * 3;
        pos.x += 0;
        tmp.init(view, new Point(pos), buttonWidth * 3, buttonHeight,
                "" , "" + lastRow.substring(0, 4));
        lastRowKeyBoard.add(tmp);

        tmp = new MyKeyBoardButton();
        pos.x += buttonWidth * 3;
        tmp.init(view, new Point(pos), buttonWidth, buttonHeight,
                "" , "" + lastRow.charAt(4));
        lastRowKeyBoard.add(tmp);

        tmp = new MyKeyBoardButton();
        pos.x += buttonWidth;
        tmp.init(view, new Point(pos), buttonWidth * 4, buttonHeight,
                "" , "" + lastRow.charAt(5));
        lastRowKeyBoard.add(tmp);

        tmp = new MyKeyBoardButton();
        pos.x += buttonWidth * 4;
        tmp.init(view, new Point(pos), buttonWidth, buttonHeight,
                "" , "" + lastRow.charAt(6));
        lastRowKeyBoard.add(tmp);

        tmp = new MyKeyBoardButton();
        pos.x += buttonWidth;
        tmp.init(view, new Point(pos), buttonWidth * 2, buttonHeight,
                "" , "" + lastRow.charAt(7));
        lastRowKeyBoard.add(tmp);

        setVisible(true);
    }

    public void draw(Canvas canvas) {

        if (!isVisible()) {
            return;
        }

        if (lower) {
            for (MyKeyBoardButton button : lowerKeyBoard) {
                button.draw(canvas);
            }
        }
        else if (upper) {
            for (MyKeyBoardButton button : upperKeyBoard) {
                button.draw(canvas);
            }
        }
        else if (number) {
            for (MyKeyBoardButton button : numberKeyBoard) {
                button.draw(canvas);
            }
        }

        // last row
        for (MyKeyBoardButton button : lastRowKeyBoard) {
            button.draw(canvas);
        }
    }


    // ToDo  touch event



}
