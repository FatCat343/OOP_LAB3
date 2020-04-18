import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;

public class GUI {
    private HashMap<Integer, JButton> buttons;
    private HashMap<Integer, JButton> panels;
    private JFrame window;
    private JFrame placement;
    private String name;
    GUI(String n){
        name = n;
    }
    static void menu(){
        JFrame f = new JFrame("Main Menu");
        JPanel panel = new JPanel();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField textField1 = new JTextField();
        textField1.setBackground(Color.WHITE);
        textField1.setColumns(14); //ширина поля

        JTextField textField2 = new JTextField();
        textField2.setBackground(Color.WHITE);
        textField2.setColumns(14); //ширина поля
        Controller c = new Controller();
        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                c.game_start(textField1.getText(), textField2.getText());
                f.setVisible(false); //you can't see me!
                f.dispose();
            }
        });
        panel.add(start);
        panel.add(textField1);
        panel.add(textField2);

        f.getContentPane().add(panel);

        f.setSize(400,400);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    void place() {
        //Создадим окно и установим заголовок
        window = new JFrame("Caption");
        window.setTitle(name +" Place ships");

        //Событие "закрыть" при нажатии по крестику окна
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Текстовое поле
        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        textField.setColumns(14); //ширина поля

        buttons = new HashMap<>();
        for (int i = 0; i < 100; i++){
            buttons.put(i, new JButton());
            JButton b = buttons.get(i);
            //System.out.println(b.hashCode());
            b.setBackground(Color.BLUE);
            b.addActionListener(new Listener(b, name, i));
            window.add(b);
        }
        window.setSize(400,400);
        window.setLayout(new GridLayout(10, 10));
        //Разместим программу по центру
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    void play(player p){
        if (manual.class.isAssignableFrom(p.getClass())) {
            for (int i = 0; i < 100; i++) {
                //buttons.put(i, new JButton());
                JButton b = buttons.get(i);
                ActionListener[] list = b.getActionListeners();
                //System.out.println(b.hashCode());
                b.removeActionListener(list[0]);
                b.setBackground(Color.BLUE);
                b.addActionListener(new Listener2(b, name, i));
                //window.add(b);
            }
            window.setTitle(name + " battle area");
        }
        placement = new JFrame("Caption");
        placement.setTitle(name +" Placed ships");

        //Событие "закрыть" при нажатии по крестику окна
        placement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panels = new HashMap<>();
        for (int i = 0; i < 100; i++){
            panels.put(i, new JButton());
            JButton b = panels.get(i);
            //System.out.println(b.hashCode());
            int res = p.isship(i);
            if (res == 0) b.setBackground(Color.BLUE);
            else b.setBackground(Color.GREEN);
            //b.addActionListener(new Listener(b, name, i));
            placement.add(b);
        }
        placement.setSize(200,200);
        placement.setLayout(new GridLayout(10, 10));
        //Разместим программу по центру
        placement.setLocationRelativeTo(null);
        placement.setVisible(true);
        Controller.gotready();

    }
    void repaintsd(char[] deck){
        System.out.println("repaint called");
        for (int i = 0; i < 100; i++){
            JButton b = panels.get(i);
            if (deck[i] == 0) b.setBackground(Color.BLUE);
            if (deck[i] == 1) b.setBackground(Color.GREEN);
            if (deck[i] == 2) b.setBackground(Color.RED);
        }
        //Thread.sleep(20000);
        //Controller.turn();
    }
    void repaintbd(char[] deck){
        for (int i = 0; i < 100; i++){
            JButton b = buttons.get(i);
            //System.out.println(b.hashCode());
            //int res = p.isship(i);
            if (deck[i] == 0) b.setBackground(Color.BLUE);
            if (deck[i] == 1) b.setBackground(Color.GREEN);
            if (deck[i] == 3) b.setBackground(Color.RED);
            if (deck[i] == 2) b.setBackground(Color.YELLOW);
        }
        //Thread.sleep(20000);
        //Controller.turn();
    }

    static void errwindow(String err){
        JFrame f = new JFrame("Error!");
        JPanel panel = new JPanel();
        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        textField.setColumns(14); //ширина поля
        textField.setText(err);
        panel.add(textField);

        f.getContentPane().add(panel);

        f.setSize(400,400);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    void finishwindow(String message){
        JFrame f = new JFrame("FINISH!");
        JPanel panel = new JPanel();
        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        textField.setColumns(14); //ширина поля
        textField.setText(message);
        panel.add(textField);

        f.getContentPane().add(panel);

        f.setSize(400,400);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        GUI.menu();
    }
    void closeframes(player p){
        if (manual.class.isAssignableFrom(p.getClass())) {
            window.setVisible(false); //you can't see me!
            window.dispose();
        }
        placement.setVisible(false); //you can't see me!
        placement.dispose();
    }
    //Запускаем
    public static void main(String[] args)
    {
        menu();
        //run();
    }

    //Больше документации https://docs.oracle.com/javase/tutorial/uiswing/components/componentlist.html
}

