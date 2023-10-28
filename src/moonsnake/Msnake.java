package moonsnake;

import javax.swing.*;

public class Msnake {

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 900, 720);  // 设置窗口大小和指针位置
        frame.setResizable(false);  // 能否手动去改变窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置窗口关闭方式
        frame.add(new MPanel());

        frame.setVisible(true);  // 使窗口可见
    }
}
