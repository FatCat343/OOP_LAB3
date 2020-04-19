import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Listener implements ActionListener {
    private JButton but;
    protected static int uses1 = 0;
    protected static int uses2 = 0;
    private int pushcount = 0;
    private player p;
    private String name;
    private int coord;
    //int phase = 0;
    Listener(JButton b, String name, int coord){
        super();
        this.but = b;
        System.out.println(name);
        if (name.equals("Player 1")) {
            //this.enemy = Controller.getp2();
            this.p = Controller.getp1();
        }
        else {
            //this.enemy = Controller.getp1();
            this.p = Controller.getp2();
        }
        this.coord = coord;
        this.name = name;
        uses1 = 0;
        uses2 = 0;
        //p.say();
        //System.out.println(coord);
    }
    public void actionPerformed(ActionEvent e) {
        //System.out.println(coord);
        //if (phase == 0) {
        System.out.println("l1");
        int uses;
        if (name.equals("Player 1")) uses = uses1;
        else uses = uses2;
        pushcount++;
        if (pushcount % 2 == 1) {
            try{
                p.placeship(coord);
                but.setBackground(Color.GREEN);
                uses++;

            }
            catch (IOException err){
                but.setBackground(Color.RED);
                GUI.errwindow(err.getLocalizedMessage());
            }
        }
        else {
            try {
                p.deleteship(coord);
                uses--;
            }
            catch (IOException err){}
            but.setBackground(Color.BLUE);
        }
        System.out.println("USES = " + uses);
        //p.showships();
        if (uses == 20) {
            int res = p.checkplacement();
            //if (res == 0) Controller.game_play();
        }
        if (name.equals("Player 1")) uses1 = uses;
        else uses2 = uses;
       // }
    }
}
class Listener2 implements ActionListener {
    private JButton but;
    //protected static int uses = 0;
    private int pushcount = 0;
    private player p;
    private player enemy;
    private int coord;
    //int phase = 0;
    Listener2(JButton b, String name, int coord){
        super();
        this.but = b;
        if (name.equals("Player 1")) {
            this.enemy = Controller.getp2();
            this.p = Controller.getp1();
        }
        else {
            this.enemy = Controller.getp1();
            this.p = Controller.getp2();
        }
        //this.enemy = Controller.getp2();
        this.coord = coord;
        //System.out.println(coord);
    }
    public void actionPerformed(ActionEvent e) {
        System.out.println("L2");
        int turns = Controller.getturn();
        if (((turns % 2 == 0) && (p.getname().equals("Player 2"))) || ((turns % 2 == 1) && (p.getname().equals("Player 1")))){
            GUI.errwindow("Not your turn!");
        }
        else {
            if (p.finish() != 1) p.attack(enemy, coord);
            turns++;
            Controller.setturn(turns);
            if (!manual.class.isAssignableFrom(enemy.getClass())) Controller.turn();
        }
/*        try{
            Controller.turn();
        }
        catch(IOException err){
            GUI.errwindow(err.getLocalizedMessage());
        }*/
    }
}
class MenuListener implements ActionListener {
    private int butvalue;

    MenuListener(int butvalue){
        super();
        this.butvalue = butvalue;
    }
    public void actionPerformed(ActionEvent e){

    }
}
public class Controller {
    private static player p1;
    private static String score;
    private static  player p2;
    private static int ready = 0;
    private static int turns = 0;
    static String getscore(){
        try {
            Scanner scan = new Scanner(new File("HighScores.txt"));
            String scoreline;
            scoreline = scan.nextLine();
            score = scoreline;
            System.out.println(score);
        }
        catch(FileNotFoundException err){
            System.out.println("No file with Scores");
        }
        return score;
    }
    static void setscore(String newscore){
        score = newscore;
        try {
            FileWriter out = new FileWriter("HighScores.txt");
            out.write(newscore);
            out.close();
        }
        catch(IOException err){
            System.out.println("No file with Scores");
        }

    }
    static player getp1(){
        return p1;
    }
    static player getp2(){
        return p2;
    }
    player initp(String config, String name) throws IOException {
        player p = null;
        if (config.equals("manual")) p = new manual(name);
        else {
            if (config.equals("clever")) p = new clever(name);
            else {
                if (config.equals("random")) p = new random(name);
                else throw new IOException("wrong type of player " + name);
            }
        }
        return p;
    }
    void game_start(String m1, String m2) {
        try{
            p1 = initp(m1, "Player 1");
            p1.place_ships();
            p2 = initp(m2, "Player 2");
            p2.place_ships();
        }
        catch (IOException err){
            GUI.errwindow(err.getLocalizedMessage());
        }
    }
    static void gotready(){
        ready++;
        if (ready == 2) {
            System.out.println("ready");
            turn();
        }
    }
    static int getturn(){
        return turns;
    }
    static void setturn(int a){
        turns = a;
    }
    static void turn(){
        System.out.println(turns + "in turn()");
        if (turns % 2 == 0){
            if (!manual.class.isAssignableFrom(p1.getClass())){
                System.out.println("Player 1");
                if (p1.finish() != 1) {
                    p1.attack(p2, 0);
                    turns++;
                    //System.out.println(turns);
                    if (!manual.class.isAssignableFrom(p2.getClass())) turn();

                }
                else {
                    p1.eog(p2);
                    deletestats();
                }

            }
            else return;
        }
        else {
            if (!manual.class.isAssignableFrom(p2.getClass())){
                System.out.println("Player 2");
                if (p2.finish() != 1) {
                    p2.attack(p1, 0);
                    turns++;
                    //System.out.println(turns);
                    if (!manual.class.isAssignableFrom(p1.getClass())) turn();

                }
                else {
                    p2.eog(p1);
                    deletestats();
                }
            }
            else return;
        }

    }
    static void deletestats(){
        turns = 0;
        ready = 0;
    }
}