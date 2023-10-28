package moonsnake;

import javax.print.DocFlavor;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.sound.sampled.*;

public class MPanel extends JPanel implements KeyListener, ActionListener {
    ImageIcon title = new ImageIcon("resource\\images\\title.jpg");
    ImageIcon body = new ImageIcon("resource\\images\\body.png");
    ImageIcon up = new ImageIcon("resource\\images\\up.png");
    ImageIcon down = new ImageIcon("resource\\images\\down.png");
    ImageIcon left = new ImageIcon("resource\\images\\left.png");
    ImageIcon right = new ImageIcon("resource\\images\\right.png");
    ImageIcon food = new ImageIcon("resource\\images\\food.png");

    int len = 3;  // 蛇的初始化长度
    int score = 0;  // 分数
    int[] snakex = new int[750];  // 定义数组存放蛇每一块的x坐标
    int[] snakey = new int[750];  // 定义数组存放蛇每一块的y坐标
    String fx = "R";  //表示方向：R，L，U，D
    boolean isStarted = false;  // 游戏是否开始
    boolean isFailed = false;  // 游戏是否失败
    Timer timer = new Timer(100, this); //定义时钟，刷新速度100ms
    int foodx;  // 食物的x坐标
    int foody;  // 食物的y坐标
    Random random = new Random();

    Clip bgm;


    public MPanel(){
        initSnake();
        this.setFocusable(true);
        this.addKeyListener(this);
        timer.start();

        loadBGM();
    }

    private void loadBGM() {  // 加载音乐
        try {
            bgm = AudioSystem.getClip();  // 使用SudioSystem创建了一个Clip歌
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("music\\bgm2.wav");  // 加载文件，使用InputStream定义一个对象
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);  // 将InputStream转换为AudioInputStream
            bgm.open(ais);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playBGM(){
        bgm.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void stopBGM(){
        bgm.stop();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        title.paintIcon(this, g, 25, 11);

        g.fillRect(25, 75, 850, 600);
        g.setColor(Color.WHITE);
        g.setFont(new Font("宋体", Font.ITALIC,15));
        g.drawString("长度  " + len, 750, 35);
        g.drawString("分数  " + score, 750, 50);


        switch(fx){
            case "R":
                right.paintIcon(this, g, snakex[0], snakey[0]);
                break;
            case "L":
                left.paintIcon(this, g, snakex[0], snakey[0]);
                break;
            case "U":
                up.paintIcon(this, g, snakex[0], snakey[0]);
                break;
            case "D":
                down.paintIcon(this, g, snakex[0], snakey[0]);
                break;
        }

        for(int i = 1; i<len; i++){
            body.paintIcon(this, g, snakex[i], snakey[i]);
        }

        food.paintIcon(this, g, foodx, foody);

        if(!isStarted){
            g.setColor(Color.PINK);
            g.setFont(new Font("楷体", Font.BOLD, 40));
            g.drawString("按空格键开始", 300, 550);
        }

        if(isFailed){
            g.setColor(Color.GREEN);

            g.setFont(new Font("楷体", Font.BOLD, 40));
            g.drawString("游戏失败（按空格键重新开始）", 170, 550);
        }

    }

    public void initSnake(){
        len = 3;
        snakex[0] = 100;
        snakey[0] = 100;
        snakex[1] = 75;
        snakey[1] = 100;
        snakex[2] = 50;
        snakey[2] = 100;  // 蛇的初始化位置
        foodx = 25 + 25 * random.nextInt(34);
        foody = 75 + 25 * random.nextInt(24);
        fx = "R";  // 将方向初始化为向右，避免重新开始出现错误
        score = 0;  // 重新开始分数初始化为0
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_SPACE){
            if(isFailed){
                isFailed = false;
                initSnake();
            }else {
                isStarted = !isStarted;
            }

            if(isStarted) playBGM();
            else if(!isStarted) stopBGM();

            repaint();
        }else if(keyCode == KeyEvent.VK_LEFT && (!fx.equals("R"))){
            fx = "L";
        }else if(keyCode == KeyEvent.VK_RIGHT  && (!fx.equals("L"))){
            fx = "R";
        }else if(keyCode == KeyEvent.VK_UP && (!fx.equals("D"))){
            fx = "U";
        }else if(keyCode == KeyEvent.VK_DOWN && (!fx.equals("U"))){
            fx = "D";
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isStarted && !isFailed){  // 使用空格键控制游戏开始暂停
            for(int i=len-1; i>0; i--){
                snakex[i] = snakex[i-1];
                snakey[i] = snakey[i-1];
            }
            if(fx.equals("R")){
                snakex[0] = snakex[0] + 25;
                if(snakex[0] > 850) snakex[0] = 25;  // 碰壁后穿越
            } else if (fx.equals("L")) {
                snakex[0] = snakex[0] - 25;
                if(snakex[0] < 25) snakex[0] = 850;  // 碰壁后穿越
            } else if (fx.equals("U")){
                snakey[0] = snakey[0] - 25;
                if(snakey[0] < 75) snakey[0] = 650;  // 碰壁后穿越
            } else if (fx.equals("D")) {
                snakey[0] = snakey[0] + 25;
                if(snakey[0] > 670) snakey[0] = 75;  // 碰壁后穿越
            }

            if(snakex[0]  == foodx && snakey[0] == foody){  // 蛇吃到食物（）
                len++;
                score += 10;
                foodx = 25 + 25 * random.nextInt(34);
                foody = 75 + 25 * random.nextInt(24);
            }

            for (int i=1; i<len; i++){
                if(snakex[0] == snakex[i] && snakey[0] == snakey[i]){
                    isFailed = true;
                }
            }

            repaint();
        }
        timer.start();
    }
}
