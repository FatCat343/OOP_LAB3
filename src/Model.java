import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

abstract class player {
    void ship_delete(char[] battle_deck, int coordinate) {
        if (((coordinate >= 10) && (battle_deck[coordinate - 10] == 2)) || ((coordinate <= 89) && (battle_deck[coordinate + 10] == 2))) { //vertical
            int start = coordinate, finish = coordinate;
            while ((start >= 10) && (battle_deck[start - 10] == 2)) {
                start = start - 10;
            }
            while ((finish <= 89) && (battle_deck[finish + 10] == 2)) {
                finish = finish + 10;
            }
            for (int i = start; i <= finish; i = i + 10) {
                battle_deck[i] = 3;
            }
        }

        if (((coordinate % 10 >= 1) && (battle_deck[coordinate - 1] == 2)) || ((coordinate % 10 <= 8) && (battle_deck[coordinate + 1] == 2))) { //horizontal
            int start = coordinate, finish = coordinate;
            while ((start % 10 >= 1) && (battle_deck[start - 1] == 2)) {
                start = start - 1;
            }
            while ((finish % 10 <= 8) && (battle_deck[finish + 1] == 2)) { //was 98?
                finish = finish + 1;
            }
            for (int i = start; i <= finish; i = i + 1) {
                battle_deck[i] = 3;
            }
        }
        battle_deck[coordinate] = 3;
    }
    void deleteship(int coord) throws IOException{

    };
    void placeship(int coord) throws IOException{

    }
    void say(){
        System.out.println("hi");
    }
    int checkplacement(){
        return 0;
    }
    void place_ship(char[] ship_deck, int coordinate, String orient, int towers) throws IOException {
        if ((coordinate > 99) || (coordinate < 0)) throw new IOException("wrong coordinate value. expect from 0 to 99");
        if (orient.equals("vertical")) {
            if (coordinate / 10 > 10 - towers) throw new IOException("not enough space for the ship");
            for (int i = 0; i < towers; i++) {
                if ((coordinate + 10*i < 100) && (ship_deck[coordinate + 10 * i] == 1)) throw new IOException("this space is not free");
                if (((coordinate + 10 * i) % 10 <= 8) && (ship_deck[coordinate + i * 10 + 1] == 1))
                    throw new IOException("can not be placed near another ship");
                if (((coordinate + 10 * i) % 10 >= 1) && (ship_deck[coordinate + i * 10 - 1] == 1))
                    throw new IOException("can not be placed near another ship");
                if (((coordinate + 10 * i) / 10 <= 8) && (ship_deck[coordinate + i * 10 + 10] == 1))
                    throw new IOException("can not be placed near another ship");
                if (((coordinate + 10 * i) / 10 >= 1) && (ship_deck[coordinate + i * 10 - 10] == 1))
                    throw new IOException("can not be placed near another ship");

            }
            for (int i = 0; i < towers; i++) {
                ship_deck[coordinate + 10 * i] = 1;
            }
        } else {
            if (orient.equals("horizontal")) {
                if (coordinate % 10 > 10 - towers) throw new IOException("not enough space for the ship");
                for (int i = 0; i < towers; i++) {
                    if ((coordinate + 10 * i < 100) && (ship_deck[coordinate + 10 * i] == 1))  throw new IOException("this space is not free");
                    if (((coordinate + i) % 10 <= 8) && (ship_deck[coordinate + i + 1] == 1))
                        throw new IOException("can not be placed near another ship");
                    if (((coordinate + i) % 10 >= 1) && (ship_deck[coordinate + i - 1] == 1))
                        throw new IOException("can not be placed near another ship");
                    if (((coordinate + i) / 10 <= 8) && (ship_deck[coordinate + i + 10] == 1))
                        throw new IOException("can not be placed near another ship");
                    if (((coordinate + i) / 10 >= 1) && (ship_deck[coordinate + i - 10] == 1))
                        throw new IOException("can not be placed near another ship");

                }
                for (int i = 0; i < towers; i++) {
                    ship_deck[coordinate + i] = 1;
                }
            } else throw new IOException("wrong orientation type");
        }
    }

    protected char[] ship_deck;
    protected GUI gui;
    protected char[] battle_deck;
    protected int status; //1 for winner, 2 for loser, 0 for not state
    protected String name;

    player(String name) {
        this.name = name;
        ship_deck = new char[100];
        battle_deck = new char[100];
        for (int i = 0; i <= 99; i++) {
            ship_deck[i] = 0;
            battle_deck[i] = 0;
        }
        status = 0;
        gui = new GUI(name);
    }
    player(){}
    String getname(){
        return name;
    }
    GUI getgui(){
        return gui;
    }
    void eog(player p){
        gui.finishwindow(p.getname() + " win!!!!");
        //gui.menu();
        String score = Controller.getscore();
        int turns = Controller.getturn();
        int pturns = turns / 2 + turns % 2;
        String[] parsedscore = score.split(" ");
        int scoreturn = Integer.parseInt(parsedscore[1]);
        if (scoreturn > pturns) gui.newHighScore(pturns);
        gui.closeframes(this);
        p.getgui().closeframes(p);
    }
    int isship(int coord){
        if (ship_deck[coord] == 1) return 1;
        else return 0;
    }

    abstract void place_ships();

    abstract void attack(player p, int coord);

    int finish() {
        if (status > 0) return 1;
        else return 0;
    }
    int getstatus() {
        return status;
    }

    void erase() {
        for (int i = 0; i <= 99; i++) {
            ship_deck[i] = 0;
            battle_deck[i] = 0;
        }
        status = 0;
    }

    int hit(int coordinate) {
        int orient = 0; //1 for vert, 2 for hor
        char result;
        char not_damaged = 0;
        if (ship_deck[coordinate] == 0) {
            gui.repaintsd(ship_deck);
            return 0;
        }
        if (ship_deck[coordinate] == 1) {
            ship_deck[coordinate] = 2;
            gui.repaintsd(ship_deck);
            //check for losing
            int exist = 0;
            for (int i = 0; i <= 99; i++) {
                if (ship_deck[i] == 1) exist++;
            }
            if (exist == 0) status = 2;

            if (((coordinate >= 10) && (ship_deck[coordinate - 10] != 0)) || ((coordinate <= 89) && (ship_deck[coordinate + 10] != 0))) {
                orient = 1;
                int start = coordinate, finish = coordinate;
                while ((start >= 10) && (ship_deck[start - 10] != 0)) {
                    start = start - 10;
                }
                while ((finish <= 89) && (ship_deck[finish + 10] != 0)) {
                    finish = finish + 10;
                }
                for (int i = start; i <= finish; i = i + 10) {
                    if (ship_deck[i] != 2) not_damaged++;
                }
//			//cout << start << finish << endl;
                if (not_damaged == 0) return 2;
                else return 1;
            }

            if (((coordinate % 10 >= 1) && (ship_deck[coordinate - 1] != 0)) || ((coordinate % 10 <= 8) && (ship_deck[coordinate + 1] != 0))) {
                orient = 2;
                int start = coordinate, finish = coordinate;
                while ((start % 10 >= 1) && (ship_deck[start - 1] != 0)) {
                    start = start - 1;
                }
                while ((finish % 10 <= 8) && (ship_deck[finish + 1] != 0)) {
                    finish = finish + 1;
                }
                for (int i = start; i <= finish; i = i + 1) {
                    if (ship_deck[i] != 2) not_damaged++;
                }
                if (not_damaged == 0) return 2;
                else return 1;
            }

            return 2; // no if was triggered  -  solo ship
        }
        return 0;
    }

}
class manual extends player {
    HashMap<Integer, Integer> ships = new HashMap<>();
    private int placed = 0;
    void placeship(int coord) throws IOException {
        if (ship_deck[coord] == 1) throw new IOException("place is not empty");
        int vstart = coord, vfinish = coord;
        while ((vstart >= 10) && (ship_deck[vstart - 10] == 1)) {
            vstart = vstart - 10;
        }
        while ((vfinish <= 89) && (ship_deck[vfinish + 10] == 1)) {
            vfinish = vfinish + 10;
        }
        int vert = (vfinish - vstart) / 10 + 1;

        System.out.println(vstart + "   " + vfinish);
        int hstart = coord;
        int hfinish = coord;
        while ((hstart % 10 >= 1) && (ship_deck[hstart - 1] == 1)) {
            hstart = hstart - 1;
        }
        while ((hfinish % 10 <= 8) && (ship_deck[hfinish + 1] == 1)) { //was 98?
            hfinish = hfinish + 1;
        }
        //System.out.println
        int hor = hfinish - hstart + 1;

        //System.out.println(hor);
        if (((hor > 1) && (vert > 1)) || (vert > 4) || (hor > 4)) throw new IOException("wrong place");/*{
            Integer key1 = 5;
            Integer k1 = ships.get(key1);
            k1++;
            ships.put(key1, k1);
        }*/
        else {
            Integer key;
            if (hor >= vert) {
                key = hor;
                if ((coord % 10 < 9) && (ship_deck[coord + 1] == 1)){
                    int vstart1 = coord + 1, vfinish1 = coord + 1;
                    while ((vstart1 >= 10) && (ship_deck[vstart1 - 10] == 1)) {
                        vstart1 = vstart1 - 10;
                    }
                    while ((vfinish1 <= 89) && (ship_deck[vfinish1 + 10] == 1)) {
                        vfinish1 = vfinish1 + 10;
                    }
                    int vert1 = (vfinish1 - vstart1) / 10 + 1;
                    if (vert1 > 1) throw new IOException("wrong place");
                }
                if ((coord % 10 > 0) && (ship_deck[coord - 1] == 1)) {
                    int vstart1 = coord - 1, vfinish1 = coord - 1;
                    while ((vstart1 >= 10) && (ship_deck[vstart1 - 10] == 1)) {
                        vstart1 = vstart1 - 10;
                    }
                    while ((vfinish1 <= 89) && (ship_deck[vfinish1 + 10] == 1)) {
                        vfinish1 = vfinish1 + 10;
                    }
                    int vert1 = (vfinish1 - vstart1) / 10 + 1;
                    if (vert1 > 1) throw new IOException("wrong place");
                }

            }
            else {
                key = vert;
                if ((coord <= 89)&&(ship_deck[coord+10] == 1)) {
                    int hstart1 = coord + 10;
                    int hfinish1 = coord + 10;
                    while ((hstart1 % 10 >= 1) && (ship_deck[hstart1 - 1] == 1)) {
                        hstart1 = hstart1 - 1;
                    }
                    while ((hfinish1 % 10 <= 8) && (ship_deck[hfinish1 + 1] == 1)) { //was 98?
                        hfinish1 = hfinish1 + 1;
                    }
                    //System.out.println
                    int hor1 = hfinish1 - hstart1 + 1;
                    if (hor1 > 1) throw new IOException("wrong place");
                }
                if ((coord >= 10)&&(ship_deck[coord-10] == 1)) {
                    int hstart1 = coord - 10;
                    int hfinish1 = coord - 10;
                    while ((hstart1 % 10 >= 1) && (ship_deck[hstart1 - 1] == 1)) {
                        hstart1 = hstart1 - 1;
                    }
                    while ((hfinish1 % 10 <= 8) && (ship_deck[hfinish1 + 1] == 1)) { //was 98?
                        hfinish1 = hfinish1 + 1;
                    }
                    //System.out.println
                    int hor1 = hfinish1 - hstart1 + 1;
                    if (hor1 > 1) throw new IOException("wrong place");
                }
            }
            Integer k = ships.get(key);
            //if (k >= 5 - key) throw new IOException("more ships then possible");
/*            if (key > 1) {
                Integer key1 = key - 1;
                Integer k1 = ships.get(key1);
                k1--;
                ships.put(key1, k1);
            }*/
            k++;
            ships.put(key, k);
            //System.out.println(key + "  " + k);
            ship_deck[coord] = 1;
            placed++;
            if ((vfinish - coord) / 10  > 0){
                Integer key1 = (vfinish - coord) / 10;
                Integer k1 = ships.get(key1);
                k1--;
                ships.put(key1, k1);
            }
            if ((coord - vstart) / 10  > 0){
                Integer key1 = (coord - vstart) / 10;
                Integer k1 = ships.get(key1);
                k1--;
                ships.put(key1, k1);
            }
            if ((hfinish - coord)   > 0){
                Integer key1 = (hfinish - coord);
                Integer k1 = ships.get(key1);
                k1--;
                ships.put(key1, k1);
            }
            if ((coord - hstart)  > 0){
                Integer key1 = (coord - hstart);
                Integer k1 = ships.get(key1);
                k1--;
                ships.put(key1, k1);
            }
        }
    }
    int checkplacement() {
        int res = 0;
        for (int i = 1; i < 5; i++) {
            Integer k = ships.get(i);
            System.out.println(k);
            if (k > 5 - i) res = 1;
        }
        System.out.println("RESULT == " + res);
        if (res == 0) gui.play(this);
        return res;
    }
    void showships(){
        for (int i = 1; i < 5; i++) {
            Integer k = ships.get(i);
            System.out.println(i + " tower = " + k);
        }

    }

    void deleteship(int coord) throws IOException {
        if (ship_deck[coord] == 0) throw new IOException("place is not used");
        int start = coord, finish = coord;
        while ((start >= 10) && (ship_deck[start - 10] == 1)) {
            start = start - 10;
        }
        while ((finish <= 89) && (ship_deck[finish + 10] == 1)) {
            finish = finish + 10;
        }
        int vert = (finish - start) / 10 + 1;
/*        if (vert >= 1) {
            Integer key = vert;
            Integer k = ships.get(key);
            k--;
            ships.put(key, k);
        }*/
        System.out.println(start + "   " + finish);
        if ((finish - coord) / 10  > 0){
            Integer key1 = (finish - coord) / 10;
            Integer k1 = ships.get(key1);
            k1++;
            ships.put(key1, k1);
        }
        if ((coord - start) / 10  > 0){
            Integer key1 = (coord - start) / 10;
            Integer k1 = ships.get(key1);
            k1++;
            ships.put(key1, k1);
        }
        start = coord;
        finish = coord;
        while ((start % 10 >= 1) && (ship_deck[start - 1] == 1)) {
            System.out.println(start);
            start = start - 1;
        }
        while ((finish % 10 <= 8) && (ship_deck[finish + 1] == 1)) { //was 98?
            finish = finish + 1;
        }
        int hor = finish - start + 1;
/*        if (hor > 1) {
            Integer key = vert;
            Integer k = ships.get(key);
            k--;
            ships.put(key, k);
        }*/
        System.out.println(start + "   " + finish);
        if ((finish - coord)   > 0){
            Integer key1 = (finish - coord);
            Integer k1 = ships.get(key1);
            k1++;
            ships.put(key1, k1);
        }
        if ((coord - start)  > 0){
            Integer key1 = (coord - start);
            Integer k1 = ships.get(key1);
            k1++;
            ships.put(key1, k1);
        }
        Integer key;
        if (hor >= vert) key = hor;
        else key = vert;
        Integer k = ships.get(key);
        k--;
        ships.put(key, k);
        ship_deck[coord] = 0;

    }
    manual(String na){
        ship_deck = new char[100];
        battle_deck = new char[100];
        for (int i = 0; i <= 99; i++) {
            ship_deck[i] = 0;
            battle_deck[i] = 0;
        }
        status = 0;
        for (int i = 1; i <= 5; i++){
            Integer k = i;
            ships.put(k, 0);
        }
        //GUI g = new GUI();
        this.name = na;
        gui = new GUI(name);
    }
    void attack(player enemy, int coord) {
        int coordinate = coord;

        if (battle_deck[coordinate] == 0) battle_deck[coordinate] = 1;
        else {
            return;
        }

        int res = enemy.hit(coordinate);

        if (res == 2) {
            ship_delete(battle_deck, coordinate);
        }

        if (res == 1) {
            battle_deck[coordinate] = 2;
        }

        if (res == 0) {
            battle_deck[coordinate] = 1;
        }
        System.out.println("res = " + res);
        gui.repaintbd(battle_deck);

    } //coord not initialized

    void place_ships() {
        gui.place();
    }
}
class clever extends player {
    clever(String name){
        this.name = name;
        ship_deck = new char[100];
        battle_deck = new char[100];
        for (int i = 0; i <= 99; i++) {
            ship_deck[i] = 0;
            battle_deck[i] = 0;
        }
        status = 0;
        gui = new GUI(name);
        //GUI g = new GUI();
        //GUI.place(name);
    }
    void place_ships() {
        for (int i = 0; i < 10; i++) {
            ship_deck[i * 10] = 1;
            ship_deck[i * 10 + 2] = 1;
        }
        ship_deck[20] = 0;
        ship_deck[50] = 0;
        ship_deck[62] = 0;
        ship_deck[22] = 0;

        ship_deck[99] = 1;
        ship_deck[9] = 1;
        Random rand = new Random();
        for (int i = 0; i < 2; i++) {

            int coord = 5 + (rand.nextInt(4)) + 10*(rand.nextInt(3)) + 20 + 30*i;
            ship_deck[coord] = 1;
        }
        gui.play(this);
    }
    void attack(player enemy, int coord) {
        int coordinate = -1;
        int counter = 0;
        for (int i = 0; i < 100; i++) {
            if (battle_deck[i] == 2) break;
            counter++;
        }
        if (counter == 100) { //did not find damaged check
            for (int i = 0; i < 100; i++) {
                coordinate = i;
                if (battle_deck[i] == 0) break;
            }
        }
        else {
            coordinate = counter;
            int hor = coordinate;
            int vert = coordinate;
            while ((vert / 10 < 9) && (battle_deck[vert + 10] == 2)) vert = vert + 10;
            while ((hor % 10 < 9) && (battle_deck[hor + 1] == 2)) hor = hor + 1;
            if ((hor == coordinate) && (vert + 10 < 100) && (battle_deck[vert + 10] == 0)) coordinate = vert + 10;
            else coordinate = hor + 1;
        }

        int res = enemy.hit(coordinate);

        if (res == 2) {
            ship_delete(battle_deck, coordinate);
        }

        if (res == 1) {
            battle_deck[coordinate] = 2;
        }

        if (res == 0) {
            battle_deck[coordinate] = 1;
        }
    }

}
class random extends player {
    random (String name){
        this.name = name;
        ship_deck = new char[100];
        battle_deck = new char[100];
        for (int i = 0; i <= 99; i++) {
            ship_deck[i] = 0;
            battle_deck[i] = 0;
        }
        status = 0;
        gui = new GUI(name);
    }
    void place_ships() {
        int coordinate;
        String orient;
        int temp = 5;
        Random rand = new Random();
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < i; j++) {
                temp = 5;
                while (temp == 5) {
                    temp = rand.nextInt(2);
                    if (temp == 0) orient = "horizontal";
                    else orient = "vertical";
                    coordinate = rand.nextInt(100);
                    //cout << orient << coordinate << endl;
                    try {
                        place_ship(ship_deck, coordinate, orient, 5 - i);
                    }
                    catch (IOException err) {
                        temp = 5;
                    }
                }
            }
        }
        gui.play(this);
    }
    void attack(player enemy, int coord) {
        Random rand = new Random();
        int coordinate = rand.nextInt(100);
        while (battle_deck[coordinate] != 0) {
            coordinate = rand.nextInt(100);
        }
        int res = enemy.hit(coordinate);

        if (res == 2) {
            ship_delete(battle_deck, coordinate);
        }

        if (res == 1) {
            battle_deck[coordinate] = 2;
        }
        if (res == 0) {
            battle_deck[coordinate] = 1;
        }
    }

}

public class Model {
    //player initPlayer();
}
