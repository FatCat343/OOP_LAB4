import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Listener implements ChangeListener {
    factory fact;
    JSlider slide;
    Listener(factory f, JSlider sl){
        fact = f;
        slide = sl;
    }
    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        //System.out.println("speed changes to " + slide.getValue());
        fact.setspeed(speed.max - slide.getValue());
    }
}
public class GUI{
    private static JFrame window;
    private static JSlider acs;
    private static JSlider power;
    private static JSlider body;
    private static JSlider dealers;
    private static int min = speed.min;
    private static HashMap<String, JTextField> currval;
    private static int max = speed.max;
    synchronized static public void start(int asize, int bsize, int csize, int psize, factory bf, factory af, factory df, factory pf){
        window = new JFrame("Control Panel");
        JPanel panela = new JPanel();
        JPanel panelp = new JPanel();
        JPanel panelb = new JPanel();
        JPanel paneld = new JPanel();
        panela.add(new JLabel("accessories"));
        panelb.add(new JLabel("body              "));
        panelp.add(new JLabel("power           "));
        paneld.add(new JLabel("dealers"));
        acs = new JSlider(JSlider.HORIZONTAL, min, max, speed.normal);
        acs.addChangeListener(new Listener(af, acs));
        power = new JSlider(JSlider.HORIZONTAL, min, max, speed.normal);
        power.addChangeListener(new Listener(pf, power));
        body = new JSlider(JSlider.HORIZONTAL, min, max, speed.normal);
        body.addChangeListener(new Listener(bf, body));
        dealers = new JSlider(JSlider.HORIZONTAL, min, max, speed.normal);
        dealers.addChangeListener(new Listener(df, dealers));
        panela.add(acs);
        panelp.add(power);
        panelb.add(body);
        paneld.add(dealers);
        currval = new HashMap<String, JTextField>();
        JTextField textamax = new JTextField();
        JTextField textacurr = new JTextField();
        JTextField textbmax = new JTextField();
        JTextField textbcurr = new JTextField();
        JTextField textpmax = new JTextField();
        JTextField textpcurr = new JTextField();
        JTextField textdmax = new JTextField();
        JTextField textdcurr = new JTextField();
        currval.put("acs", textacurr);
        currval.put("body", textbcurr);
        currval.put("power", textpcurr);
        currval.put("car", textdcurr);
        textamax.setText(Integer.toString(asize));
        textbmax.setText(Integer.toString(bsize));
        textdmax.setText(Integer.toString(csize));
        textpmax.setText(Integer.toString(psize));
        textamax.setColumns(7);
        textpmax.setColumns(7);
        textbmax.setColumns(7);
        textdmax.setColumns(7);
        textacurr.setColumns(7);
        textpcurr.setColumns(7);
        textbcurr.setColumns(7);
        textdcurr.setColumns(7);

        panela.add(textamax);
        panelp.add(textpmax);
        panelb.add(textbmax);
        paneld.add(textdmax);
        panela.add(textacurr);
        panelp.add(textpcurr);
        panelb.add(textbcurr);
        paneld.add(textdcurr);
        //add(Panel, BorderLayout.CENTER);
        //add(textField, BorderLayout.SOUTH);
        panela.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelp.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelb.setLayout(new FlowLayout(FlowLayout.LEFT));
        paneld.setLayout(new FlowLayout(FlowLayout.LEFT));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(panela, BorderLayout.SOUTH);
        window.add(panelb, BorderLayout.WEST);
        window.add(panelp, BorderLayout.NORTH);
        window.add(paneld, BorderLayout.CENTER);
        //TextField text1 = new TextField();
        //TextField text2 = new TextField();
        //window.add(text1);
        //window.add(text2);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    synchronized public static void repaint(String type, int amount){
        //if (type.equals("car"))System.out.println(type + "  was repainted with  "+ amount);
        currval.get(type).setText(Integer.toString(amount));
    }
}
//public class GUI {
//    private HashMap<Integer, JButton> buttons;
//    private HashMap<Integer, JButton> panels;
//    private JFrame window;
//    private JFrame placement;
//    private String name;
//    GUI(String n){
//        name = n;
//    }
//    static void menu(){
//        JFrame f = new JFrame("Main Menu");
//        JPanel panel = new JPanel();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JTextField textField1 = new JTextField();
//        textField1.setBackground(Color.WHITE);
//        textField1.setColumns(14); //ширина поля
//
//        JTextField textField2 = new JTextField();
//        textField2.setBackground(Color.WHITE);
//        textField2.setColumns(14); //ширина поля
//        Controller c = new Controller();
//        JButton start = new JButton("Start");
//        JButton score = new JButton("Highscore");
//        start.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                c.game_start(textField1.getText(), textField2.getText());
//                f.setVisible(false); //you can't see me!
//                f.dispose();
//            }
//        });
//        score.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                JFrame f = new JFrame("HighScore");
//                JPanel panel = new JPanel();
//                //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                JTextField textField = new JTextField();
//                textField.setBackground(Color.WHITE);
//                textField.setColumns(14); //ширина поля
//                String scoreline = Controller.getscore();
//                textField.setText(scoreline);
//                //textField.setText("score");
//                panel.add(textField);
//
//                f.getContentPane().add(panel);
//
//                f.setSize(400,400);
//                f.pack();
//                f.setLocationRelativeTo(null);
//                f.setVisible(true);
//            }
//        });
//        panel.add(start);
//        panel.add(score);
//        panel.add(textField1);
//        panel.add(textField2);
//
//        f.getContentPane().add(panel);
//
//        f.setSize(400,400);
//        f.pack();
//        f.setLocationRelativeTo(null);
//        f.setVisible(true);
//    }
//    void newHighScore(int score){
//        JFrame f = new JFrame("HighScore");
//        JPanel panel = new JPanel();
//        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JTextField textField = new JTextField();
//        textField.setBackground(Color.WHITE);
//        textField.setColumns(14); //ширина поля
//        textField.setText("Enter Your name here");
//        panel.add(textField);
//        JButton ok = new JButton("ok");
//        ok.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                Controller.setscore(textField.getText() + " " + score);
//                f.setVisible(false); //you can't see me!
//                f.dispose();
//            }
//        });
//        panel.add(ok);
//        f.getContentPane().add(panel);
//
//        f.setSize(400,400);
//        f.pack();
//        f.setLocationRelativeTo(null);
//        f.setVisible(true);
//    }
//    void place() {
//        //Создадим окно и установим заголовок
//        window = new JFrame("Caption");
//        window.setTitle(name +" Place ships");
//
//        //Событие "закрыть" при нажатии по крестику окна
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //Текстовое поле
//        JTextField textField = new JTextField();
//        textField.setBackground(Color.WHITE);
//        textField.setColumns(14); //ширина поля
//
//        buttons = new HashMap<>();
//        for (int i = 0; i < 100; i++){
//            buttons.put(i, new JButton());
//            JButton b = buttons.get(i);
//            //System.out.println(b.hashCode());
//            b.setBackground(Color.BLUE);
//            b.addActionListener(new Listener(b, name, i));
//            window.add(b);
//        }
//        window.setSize(400,400);
//        window.setLayout(new GridLayout(10, 10));
//        //Разместим программу по центру
//        window.setLocationRelativeTo(null);
//        window.setVisible(true);
//    }
//    void play(player p){
//        if (manual.class.isAssignableFrom(p.getClass())) {
//            for (int i = 0; i < 100; i++) {
//                //buttons.put(i, new JButton());
//                JButton b = buttons.get(i);
//                ActionListener[] list = b.getActionListeners();
//                //System.out.println(b.hashCode());
//                b.removeActionListener(list[0]);
//                b.setBackground(Color.BLUE);
//                b.addActionListener(new Listener2(b, name, i));
//                //window.add(b);
//            }
//            window.setTitle(name + " battle area");
//        }
//        placement = new JFrame("Caption");
//        placement.setTitle(name +" Placed ships");
//
//        //Событие "закрыть" при нажатии по крестику окна
//        placement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        panels = new HashMap<>();
//        for (int i = 0; i < 100; i++){
//            panels.put(i, new JButton());
//            JButton b = panels.get(i);
//            //System.out.println(b.hashCode());
//            int res = p.isship(i);
//            if (res == 0) b.setBackground(Color.BLUE);
//            else b.setBackground(Color.GREEN);
//            //b.addActionListener(new Listener(b, name, i));
//            placement.add(b);
//        }
//        placement.setSize(200,200);
//        placement.setLayout(new GridLayout(10, 10));
//        //Разместим программу по центру
//        placement.setLocationRelativeTo(null);
//        placement.setVisible(true);
//        Controller.gotready();
//
//    }
//    void repaintsd(char[] deck){
//        System.out.println("repaint called");
//        for (int i = 0; i < 100; i++){
//            JButton b = panels.get(i);
//            if (deck[i] == 0) b.setBackground(Color.BLUE);
//            if (deck[i] == 1) b.setBackground(Color.GREEN);
//            if (deck[i] == 2) b.setBackground(Color.RED);
//        }
//        //Thread.sleep(20000);
//        //Controller.turn();
//    }
//    void repaintbd(char[] deck){
//        for (int i = 0; i < 100; i++){
//            JButton b = buttons.get(i);
//            //System.out.println(b.hashCode());
//            //int res = p.isship(i);
//            if (deck[i] == 0) b.setBackground(Color.BLUE);
//            if (deck[i] == 1) b.setBackground(Color.GREEN);
//            if (deck[i] == 3) b.setBackground(Color.RED);
//            if (deck[i] == 2) b.setBackground(Color.YELLOW);
//        }
//        //Thread.sleep(20000);
//        //Controller.turn();
//    }
//
//    static void errwindow(String err){
//        JFrame f = new JFrame("Error!");
//        JPanel panel = new JPanel();
//        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JTextField textField = new JTextField();
//        textField.setBackground(Color.WHITE);
//        textField.setColumns(14); //ширина поля
//        textField.setText(err);
//        panel.add(textField);
//
//        f.getContentPane().add(panel);
//
//        f.setSize(400,400);
//        f.pack();
//        f.setLocationRelativeTo(null);
//        f.setVisible(true);
//    }
//    void finishwindow(String message){
//        JFrame f = new JFrame("FINISH!");
//        JPanel panel = new JPanel();
//        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JTextField textField = new JTextField();
//        textField.setBackground(Color.WHITE);
//        textField.setColumns(14); //ширина поля
//        textField.setText(message);
//        panel.add(textField);
//
//        f.getContentPane().add(panel);
//
//        f.setSize(400,400);
//        f.pack();
//        f.setLocationRelativeTo(null);
//        f.setVisible(true);
//        GUI.menu();
//    }
//    void closeframes(player p){
//        if (manual.class.isAssignableFrom(p.getClass())) {
//            window.setVisible(false); //you can't see me!
//            window.dispose();
//        }
//        placement.setVisible(false); //you can't see me!
//        placement.dispose();
//    }
//    //Запускаем
//    public static void main(String[] args)
//    {
//        menu();
//        //run();
//    }
//
//    //Больше документации https://docs.oracle.com/javase/tutorial/uiswing/components/componentlist.html
//}


