package heroiceraser.mulatschak.game.NonGamePlayUI.GameKeyBoard;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import heroiceraser.mulatschak.drawableBasicObjects.DrawableObject;
import heroiceraser.mulatschak.game.GameLayout;
import heroiceraser.mulatschak.game.GameView;


//--------------------------------------------------------------------------------------------------
//  Key Board class
//
public class MyKeyBoard extends DrawableObject {

    //----------------------------------------------------------------------------------------------
    //  Member variables
    //
    private boolean lower;
    private boolean upper;
    private boolean number;

    private List<MyKeyBoardButton> lowerKeyBoard;
    private List<MyKeyBoardButton> upperKeyBoard;
    private List<MyKeyBoardButton> numberKeyBoard;
    private List<MyKeyBoardButton> lastRowKeyBoard;


    //----------------------------------------------------------------------------------------------
    //  Constructor
    //
    public MyKeyBoard() {
        super();
        lowerKeyBoard = new ArrayList<>();
        upperKeyBoard = new ArrayList<>();
        numberKeyBoard = new ArrayList<>();
        lastRowKeyBoard = new ArrayList<>();
    }


    //----------------------------------------------------------------------------------------------
    //  init
    //
    public void init(GameView view) {
        GameLayout layout = view.getController().getLayout();

        setPosition(new Point((int) (layout.getOnePercentOfScreenWidth() * 2),
                (int) (layout.getOnePercentOfScreenHeight() * 55)));
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

        String lastRow = "!123, .SEND↵";

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
                        "", "" + lower_buttons[row].charAt(col));
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
                        "", "" + upper_buttons[row].charAt(col));
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
                        "", "" + number_buttons[row].charAt(col));
                numberKeyBoard.add(tmp);
            }
        }


        MyKeyBoardButton tmp = new MyKeyBoardButton();
        Point pos = new Point(position);
        pos.y += buttonHeight * 3;
        tmp.init(view, new Point(pos), buttonWidth * 2, buttonHeight,
                "", "" + lastRow.substring(0, 4));
        lastRowKeyBoard.add(tmp);

        tmp = new MyKeyBoardButton();
        pos.x += buttonWidth * 2;
        tmp.init(view, new Point(pos), buttonWidth, buttonHeight,
                "", "" + lastRow.charAt(4));
        lastRowKeyBoard.add(tmp);

        tmp = new MyKeyBoardButton();
        pos.x += buttonWidth;
        tmp.init(view, new Point(pos), buttonWidth * 4, buttonHeight,
                "", "" + lastRow.charAt(5));
        lastRowKeyBoard.add(tmp);

        tmp = new MyKeyBoardButton();
        pos.x += buttonWidth * 4;
        tmp.init(view, new Point(pos), buttonWidth, buttonHeight,
                "", "" + lastRow.charAt(6));
        lastRowKeyBoard.add(tmp);

        tmp = new MyKeyBoardButton();
        pos.x += buttonWidth;
        tmp.init(view, new Point(pos), buttonWidth * 3, buttonHeight,
                "", "SEND");
        lastRowKeyBoard.add(tmp);

        setVisible(true);
    }


    //----------------------------------------------------------------------------------------------
    //  draw
    //
    public synchronized void draw(Canvas canvas) {

        if (!isVisible()) {
            return;
        }

        if (lower) {
            for (MyKeyBoardButton button : lowerKeyBoard) {
                button.draw(canvas);
            }
        } else if (upper) {
            for (MyKeyBoardButton button : upperKeyBoard) {
                button.draw(canvas);
            }
        } else if (number) {
            for (MyKeyBoardButton button : numberKeyBoard) {
                button.draw(canvas);
            }
        }

        // last row
        for (MyKeyBoardButton button : lastRowKeyBoard) {
            button.draw(canvas);
        }
    }


    //----------------------------------------------------------------------------------------------
    //  Touch Events
    //
    public void touchEventDown(int X, int Y) {
        if (!isVisible()) {
            return;
        }
        if (lower) {
            for (MyKeyBoardButton button : lowerKeyBoard) {
                button.touchEventDown(X, Y);
            }
        } else if (upper) {
            for (MyKeyBoardButton button : upperKeyBoard) {
                button.touchEventDown(X, Y);
            }
        } else if (number) {
            for (MyKeyBoardButton button : numberKeyBoard) {
                button.touchEventDown(X, Y);
            }
        }

        for (MyKeyBoardButton button : lastRowKeyBoard) {
            button.touchEventDown(X, Y);
        }
    }

    public void touchEventMove(int X, int Y) {
        if (!isVisible()) {
            return;
        }

        if (lower) {
            for (MyKeyBoardButton button : lowerKeyBoard) {
                button.touchEventMove(X, Y);
            }
        } else if (upper) {
            for (MyKeyBoardButton button : upperKeyBoard) {
                button.touchEventMove(X, Y);
            }
        } else if (number) {
            for (MyKeyBoardButton button : numberKeyBoard) {
                button.touchEventMove(X, Y);
            }
        }

        for (MyKeyBoardButton button : lastRowKeyBoard) {
            button.touchEventMove(X, Y);
        }
    }

    public List<String> touchEventUp(int X, int Y) {
        if (!isVisible()) {
            return null;
        }

        List<String> keys = new ArrayList<>();

        if (lower) {
            for (MyKeyBoardButton button : lowerKeyBoard) {
                if (button.touchEventUp(X, Y)) {
                    keys.add(button.getText());
                }
            }
        } else if (upper) {
            for (MyKeyBoardButton button : upperKeyBoard) {
                if (button.touchEventUp(X, Y)) {
                    keys.add(button.getText());
                }
            }
        } else if (number) {
            for (MyKeyBoardButton button : numberKeyBoard) {
                if (button.touchEventUp(X, Y)) {
                    keys.add(button.getText());
                }
            }
        }

        for (MyKeyBoardButton button : lastRowKeyBoard) {
            if (button.touchEventUp(X, Y)) {
                keys.add(button.getText());
            }
        }

        int i = 0;
        for (String key : keys) {
            switch (key) {
                // change keyBoard
                case "⇧":
                    lower = false;
                    upper = true;
                    number = false;
                    keys.remove(i);
                    i--;
                    break;
                case "⇩":
                    lower = true;
                    upper = false;
                    number = false;
                    keys.remove(i);
                    i--;
                    break;
                case "!123":
                    if (number) {
                        lower = true;
                        upper = false;
                        number = false;
                    } else {
                        lower = false;
                        upper = false;
                        number = true;
                    }
                    keys.remove(i);
                    i--;
                    break;
                case "⇦":
                    keys.remove(i);
                    key = "del";
                    keys.add(i, key);
                    break;
                case "SEND":
                    keys.remove(i);
                    key = "send";
                    keys.add(i, key);
                    break;
            }
            i++;
        }
        return keys;
    }
}
