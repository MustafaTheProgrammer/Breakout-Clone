import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.util.ArrayList;

class breakOut extends JFrame implements KeyListener{
    //variables
    public static char[][] pixels = new char[19][28];
    public static boolean leftKey;
    public static boolean rightKey;
    public static int[] pos = {12, -17};
    public static int[] ballPos = {14, -16};
    public static int[] ballVel = {1, 1};
    public static int startTimer = 0;
    public static boolean running = true;
    public static ArrayList<Integer> tiles = new ArrayList<Integer>();
    public static int score = 0;
    
    //JFrame setup
    public breakOut(){
        this.setTitle("duck");
        this.setSize(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
        gameLoop();
    }

    static void gameLoop(){
        //add tiles
        for (int i = -3; i >= -12; i -= 3){
            for (int j = 2; j <= 22; j += 5){
                addTile(j, i, 1);
            }
        }
        
        while (running){
            //clear screen
            System.out.print("\033[H\033[2J");
            System.out.flush();

            //reset buffer
            for (int i = 0; i < 19; i++){
                for (int j = 0; j < 28; j++){
                    pixels[i][j] = ' ';
                }
            }

            //player
            if (leftKey){
                pos[0] -= 2;
                if (pos[0] < 0){
                    pos[0] = 0;
                }
            }
            if (rightKey){
                pos[0] += 2;
                if (pos[0] + 6 > 27){
                    pos[0] = 21;
                }
            }
            drawRect(pos[0], pos[1], 7, 1, '_');

            //ball
            if (startTimer < 11){
                startTimer++;
            }
            else{
                ballPos[0] += ballVel[0];
                ballPos[1] += ballVel[1];
                if (ballPos[0] == 0 || ballPos[0] == 27){
                    ballVel[0] *= -1;
                }
                if (ballPos[1] == 0){
                    ballVel[1] = -1;
                }
                if (ballPos[0] >= pos[0] && ballPos[0] <= pos[0] + 6 && ballPos[1] == pos[1]){
                    ballVel[1] *= -1;
                }
                if (ballPos[1] == -18 || score == 2000){
                    running = false;
                }
            }
            setPixel(ballPos[0], ballPos[1], 'o');

            //tiles
            for (int i = 0; i < tiles.size(); i += 3){
                if (tiles.get(i) <= ballPos[0] && tiles.get(i) + 3 >= ballPos[0] && (tiles.get(i+1) == ballPos[1] || tiles.get(i+2) + 1 == ballPos[1])){
                    tiles.set(i+2, 0);
                    ballVel[1] *= -1;
                    score += 100;
                }
                drawRect(tiles.get(i), tiles.get(i+1), 4, 2, '#');
            }

            //deleting tiles
            for (int i = tiles.size() - 3; i >= 0; i -= 3){
                if (tiles.get(i+2) == 0){
                    tiles.remove(i+2);
                    tiles.remove(i+1);
                    tiles.remove(i);
                }
            }

            //render screen
            System.out.println("Score: " + score);
            for (int i = 0; i < 19; i++){
                for (int j = 0; j < 28; j++){
                    System.out.print(pixels[i][j]);
                }
                System.out.println();
            }

            //delay
            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        if (score == 2000){
            System.out.print("\033[H\033[2J");
            System.out.print("Congrats! You won with a score of 2000");
        }
        else if (score < 2000){
            System.out.print("\033[H\033[2J");
            System.out.print("You unfortunately lost, with a score of " + score);
        }
    }

    public static void main(String[] args){
        new breakOut();
    }

    //other functions
    static void setPixel(int x, int y, char sym){
        pixels[Math.abs(y)][x] = sym;
    }

    static void drawRect(int x, int y, int width, int height, char sym){
        for (int i = x; i < x + width; i++){
            for (int j = y; j < y + height; j++){
                setPixel(i, j, sym);
            }
        }
    }

    static void addTile(int x, int y, int shown){
        tiles.add(x);
        tiles.add(y);
        tiles.add(shown);
    }

    public void keyTyped(KeyEvent e){
        
    }
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
            case 37: leftKey = true;
            break;
            case 39: rightKey = true;
            break;
        }
    }
    public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()){
            case 37: leftKey = false;
            break;
            case 39: rightKey = false;
            break;
        }
    }
}