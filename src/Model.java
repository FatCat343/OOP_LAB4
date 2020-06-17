import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
class speed {
    static final int normal = 700;
    static final int min = 0;
    static final int max = 1500;
}
class carcounter {
    static int max = 0;
}
public class Model {
}

class certificate{
    int curid = 0;
    public int verify(){
        curid++;
        return curid;
    }
    public int getmax(){
        return curid;
    }
}
class detail{
    int id;
    String type;
    detail(int i){
        id = i;
    }
    detail(){}
    int getid(){
        return id;
    }
    void setid(int i){
        id = i;
    }
}
final class body extends detail{}
final class power extends detail{}
final class acs extends detail{}
final class car extends detail{
    int bodyid;
    int acsid;
    int powerid;
    car(){}
    car(int b, int p, int a){
        bodyid = b;
        powerid = p;
        acsid = a;
    }
    int getpowerid(){
        return bodyid;
    }
    int getbodyid(){
        return powerid;
    }
    int getacsid(){
        return acsid;
    }
}
abstract class order implements Runnable{
    warehouse wh;
    certificate verifier;
    int delay;
    factory fact;
    order(){};
    public void setorder(warehouse w, factory f, certificate ver){
        verifier = ver;
        wh = w;
        //delay = d;
        fact = f;
    }
    synchronized public void run(){}
}
class bodyorder extends order{
    synchronized public void run(){
        delay = fact.getspeed();
        try {
            sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        body b = new body();
        int id;
        synchronized(verifier){
            id = verifier.verify();
        }
        b.setid(id);
        wh.push(b);
        notify();
    }
}
class powerorder extends order{
    synchronized public void run(){
        delay = fact.getspeed();
        try {
            sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        power b = new power();
        int id;
        synchronized(verifier){
            id = verifier.verify();
        }
        b.setid(id);
        wh.push(b);
        notify();
    }
}
class acsorder extends order{
    synchronized public void run(){
        delay = fact.getspeed();
        try {
            sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        acs b = new acs();
        int id;
        synchronized(verifier){
            id = verifier.verify();
        }
        b.setid(id);
        //System.out.println("created detail with id = " + b.getid());
        wh.push(b);

        notify();
    }
}
class carorder extends order{
    warehouse whacs;
    warehouse whbody;
    warehouse whpower;
    public void setordercars(warehouse wacs, warehouse wbody, warehouse wout, warehouse wpower, factory f, certificate ver){
        wh = wout;
        whacs = wacs;
        whpower = wpower;
        whbody = wbody;
        fact = f;//?
        verifier = ver;
    }
    public void run(){
        //System.out.println("started");
        detail a = whacs.pop();
        detail b = whbody.pop();
        detail p = whpower.pop();
        car c = new car(b.getid(), p.getid(), a.getid());
        //System.out.println("created car");
        int id;
        synchronized(verifier){
            id = verifier.verify();
        }
        b.setid(id);
        if (Area.logstatus == 1) {
            synchronized (Area.LOGGER) {
                Area.LOGGER.log(Level.INFO, " worker " + currentThread().getName() + " made car with id " + b.getid());
            }
        }
        wh.push(b);
    }
}
class purchaseorder extends order{
    static int max;
    synchronized public void run(){ //sunc or not?
        while (true) {
            try {
                delay = fact.getspeed();
                sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            detail d = wh.pop();
            int id = d.getid();
            //synchronized (carcounter.max) {
            if (max < id) {
                max = id;
                GUI.repaint("car", d.getid());
                //System.out.println("max is now = " + id);
            }
           // }
            //System.out.println("dealer got detail with id = " + d.getid());
        }
    }
}
abstract class factory implements Runnable{
    warehouse wh;
    int workers;
    int delay;
    int taskcount = 0;
    PoolExecutor pe;
    certificate verifier;
    synchronized public void addorder(){
        //pe.execute(task);
        taskcount++;
        notify();
    }
    factory(){
        verifier = new certificate();
    }
    public void setwarehouse(warehouse w){
        wh = w;
    };
    public void setthreads(int size){
        workers = size;
        pe = new PoolExecutor(size);
    }
    synchronized public void setspeed(int d){
        //System.out.println("speed changed to " + d);
        delay = d;
    }
    synchronized public int getspeed(){
        return delay;
    }
    synchronized public void run(){}
}
class bodyfactory extends factory{
    synchronized public void run(){
        while(true){
            while(taskcount == 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            taskcount--;
            bodyorder task = new bodyorder();
            task.setorder(wh, this, verifier);
            pe.execute(task);
        }
    }
}
class acsfactory extends factory{
    synchronized public void run(){
        while(true){
            while(taskcount == 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            taskcount--;
            acsorder task = new acsorder();
            task.setorder(wh, this, verifier);
            pe.execute(task);
        }
    }
}
class powerfactory extends factory{
    synchronized public void run(){
        while(true){
            while(taskcount == 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            taskcount--;
            powerorder task = new powerorder();
            task.setorder(wh, this, verifier);
            pe.execute(task);
        }
    }
}
class carfactory extends factory{
    warehouse whacs;
    warehouse whbody;
    warehouse whpower;
    WThreadPool wtp;
    public void setwhs(warehouse wa, warehouse wp, warehouse wb){
        whacs = wa;
        whbody = wb;
        whpower = wp;
        System.out.println("workers = "+workers);
        wtp = new WThreadPool(workers);
    }
    synchronized public void run(){
        while(true){
            while(taskcount == 0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            taskcount--;
            carorder task = new carorder();
            task.setordercars(whacs, whbody, wh, whpower, this, verifier);
            wtp.execute(task);
        }
    }
}
class dealer extends factory{
    synchronized public void run(){
        purchaseorder.max = 0;
        for (int i = 0; i < workers; i++) {
            purchaseorder task = new purchaseorder();
            task.setorder(wh, this, verifier);
            pe.execute(task);
        }
    }
}
class warehouse implements Runnable{
    int capacity;
    int ordered;
    String name;
    private List<detail> list;
    factory fact;
    warehouse(int size, String n){
        name = n;
        list = new ArrayList<detail>();
        capacity = size;
        ordered = 0;
    }
    public void setfact(factory f){
        fact = f;
    }
    synchronized void push(detail D){
        //System.out.println("size = " + capacity +" pushing the detail with id = " + D.getid());
        list.add(D);
        if (!name.equals("car")) GUI.repaint(name, list.size());
        notify();
    }
    synchronized public detail pop() {
        int length = list.size();
        while (length < 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            length = list.size();
        }
        //System.out.println("size = " + capacity +" poping the detail  ");
        detail out = list.remove(length-1);
        ordered--;
        if (!name.equals("car")) GUI.repaint(name, list.size());
        notify();
        return out;
    }
    synchronized public void run(){
       // System.out.println("running main thread -- " + this.capacity);
        try {
            for (int i = 0; i < capacity; i++) {
                fact.addorder();
                ordered++;
                //System.out.println("size = " + this.capacity + " added order n " + i);
            }
            while (true) {
                 do {
                    notify();
                    //System.out.println("size = " + this.capacity + " goes wait");
                    wait();
                } while (ordered >= capacity);
                int s = capacity - ordered;
                for (int i = 0; i < s; i++) {
                    fact.addorder();
                    ordered++;
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    int size(){
        return list.size();
    }
    int cap(){
        return capacity;
    }
}
//abstract class single implements Runnable{
//    final certificate verifier;
//    int time;
//    warehouse out;
//    single(){
//        verifier = new certificate();
//        out = null;
//    };
//    single(int sp, warehouse wh){
//        verifier = new certificate();
//        time = sp;
//        out = wh;
//    }
//}
//class bodyfactory extends single{
//    public void run(){
//        try {
//            sleep(time);
//            detail d = new body(); //set proper id
//            int id = verifier.verify();
//            d.setid(id);
//            synchronized (out) {
//                if (out.size() == out.cap()) wait();
//                out.push(d);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
//class powerfactory extends single{
//    public void run(){
//        try {
//            sleep(time);
//            detail d = new power(); //set proper id
//            int id = verifier.verify();
//            d.setid(id);
//            synchronized (out) {
//                if (out.size() == out.cap()) wait();
//                out.push(d);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
//class powerfacory implements Runnable{
//    public void run(){
//        try{
//            sleep(time);
//            detail d = new power();
//        }
//    }
//}
//abstract class multi extends single{
//    int size;
//    PoolExecutor units;
//    multi(){}
//}
//class dealer extends multi{ //warehouse in = out
//    dealer(int si, int sp, warehouse wh1){
//        size = si;
//        time = sp;
//        out = wh1;
//    }
//    public void run(){
//        try {
//            sleep(time);
//            synchronized (out) {
//                if (out.size() == 0) wait();
//                detail d = out.pop();
//            }
//            synchronized (verifier){
//                int id = verifier.verify();
//            }
//        } catch (InterruptedException | IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}
//class acsworker implements Runnable{
//    int time;
//    certificate verifier;
//    warehouse out;
//    acsworker(int t, certificate ver, warehouse w){
//        time = t;
//        out = w;
//        verifier = ver;
//    }
//    public void run(){
//        try {
//            sleep(time);
//            int id;
//            detail d = new acs(); //set proper id
//            synchronized(verifier) {
//                id = verifier.verify();
//            }
//            d.setid(id);
//            synchronized (out) {
//                if (out.size() == out.cap()) wait();
//                out.push(d);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
//class acsfactory extends multi{
//    //warehouse out;
//    acsfactory(int si, int sp, warehouse wh1){
//        size = si;
//        time = sp;
//        out = wh1;
//        units = new PoolExecutor(size);
//    }
//    public void run(){
//        for (int i = 0; i < size; i++){
//            acsworker w = new acsworker(time, verifier, out);
//            units.execute(w);
//        }
//    }
//}
//class carworker implements Runnable{
//    int time;
//    final warehouse inbody;
//    final warehouse inacs;
//    final warehouse inpower;
//    certificate verifier;
//    warehouse out;
//    carworker(int sp, warehouse body, warehouse power, warehouse acs, warehouse car, certificate ver){
//        time = sp;
//        inbody = body;
//        inpower = power;
//        inacs = acs;
//        out = car;
//        verifier = ver;
//    }
//    public void run(){
//        try {
//            synchronized (inbody) {
//                if (inbody.size() == 0) wait();
//                detail b = inbody.pop();
//            }
//            synchronized (inpower) {
//                if (inpower.size() == 0) wait();
//                detail p = inpower.pop();
//            }
//            synchronized (inacs) {
//                if (inacs.size() == 0) wait();
//                detail a = inacs.pop();
//            }
//            sleep(time);
//            detail d = new car(); // set proper id
//            int id;
//            synchronized (verifier){
//                id = verifier.verify();
//            }
//            d.setid(id);
//            synchronized (out) {
//                if (out.size() == out.capacity) wait();
//                out.push(d);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
//class carfactory extends multi{
//    final warehouse inbody;
//    final warehouse inacs;
//    final warehouse inpower;
//    carfactory(int si, int sp, warehouse body, warehouse power, warehouse acs, warehouse car){
//        size = si;
//        time = sp;
//        inbody = body;
//        inpower = power;
//        inacs = acs;
//        out = car;
//        units = new PoolExecutor(size);
//    }
//    public void run(){
//        try {
//            synchronized (inbody) {
//                detail b = inbody.pop();
//            }
//            synchronized (inpower) {
//                detail p = inpower.pop();
//            }
//            synchronized (inacs) {
//                detail a = inacs.pop();
//            }
//
//            detail sup = new detail(); // set proper id
//            synchronized (out) {
//                out.push(sup);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}

class Area{
    static int logstatus;
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config.txt")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Area.class.getName());
        } catch (Exception ignore){
            ignore.printStackTrace();
        }
    }
    HashMap<String, warehouse> whs;
    bodyfactory bf;
    powerfactory pf;
    acsfactory af;
    carfactory cf;
    dealer d;
    HashMap<String, String> speclist;
    Area(String config){
        try {
            //LOGGER.log(Level.INFO, " worker " + currentThread().getName() + " made car with id ");
            whs = new HashMap<String, warehouse>();
            speclist = new HashMap<String, String>();
            Properties prop = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(config);
            if (inputStream != null) prop.load(inputStream);
            else throw new IOException("unable to use config file");
            bf = new bodyfactory();
            bf.setthreads(1);
            bf.setspeed(speed.normal);
            new Thread(bf).start();
            pf = new powerfactory();
            pf.setthreads(1);
            pf.setspeed(speed.normal);
            new Thread(pf).start();
            for (String key : prop.stringPropertyNames()) {
                String line = prop.getProperty(key);
                System.out.println(key + "   " + line);
                if (key.equals("Acs")) {
                    //System.out.println("acs created");
                    af = new acsfactory();
                    af.setthreads(Integer.parseInt(line));
                    //af.setwarehouse(whs.get("acs"));
                    af.setspeed(speed.normal);
                    new Thread(af).start();
                }
                ;
                if (key.equals("Workers")) {
                    cf = new carfactory();
                    cf.setthreads(Integer.parseInt(line));
                    //cf.setwarehouse(whs.get("car"));
                    //cf.setwhs(whs.get("acs"), whs.get("body"), whs.get("power"));
                    cf.setspeed(speed.normal);
                    new Thread(cf).start();
                }
                ;
                if (key.equals("Dealers")) {
                    d = new dealer();
                    d.setthreads(Integer.parseInt(line));
                    //d.setwarehouse(whs.get("car"));
                    d.setspeed(speed.normal);
                }
                ;
            }
            for (String key : prop.stringPropertyNames()) {
                String line = prop.getProperty(key);
                System.out.println(key + "   " + line);
                if (key.equals("StorageBodySize")) {
                    warehouse w = new warehouse(Integer.parseInt(line), "body");
                    whs.put("body", w);
                    w.setfact(bf);
                    bf.setwarehouse(w);
                    new Thread(w).start();
                };
                if (key.equals("StoragePowerSize")) {
                    warehouse w = new warehouse(Integer.parseInt(line), "power");
                    w.setfact(pf);
                    pf.setwarehouse(w);
                    whs.put("power", w);
                    new Thread(w).start();
                };
                if (key.equals("StorageAcsSize")) {
                    warehouse w = new warehouse(Integer.parseInt(line), "acs");
                    w.setfact(af);
                    af.setwarehouse(w);
                    whs.put("acs", w);
                    new Thread(w).start();
                };
                if (key.equals("LogSale")) {
                    if (line.equals("true")) logstatus = 1;
                    else logstatus = 0;
                };
            }
            for (String key : prop.stringPropertyNames()) {
                String line = prop.getProperty(key);
                if (key.equals("StorageCarSize")) {
                    warehouse w = new warehouse(Integer.parseInt(line), "car");
                    w.setfact(cf);
                    d.setwarehouse(w);
                    new Thread(d).start();
                    cf.setwarehouse(w);
                    cf.setwhs(whs.get("acs"), whs.get("body"), whs.get("power"));
                    whs.put("car", w);
                    new Thread(w).start();
                }
                ;
            }
        }
        catch(IOException err){
            System.out.println(err.getMessage());
        }
        GUI.start(whs.get("acs").capacity, whs.get("body").capacity,whs.get("car").capacity, whs.get("power").capacity, bf, af, d, pf);
    }
}



