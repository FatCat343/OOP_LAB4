import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

public class Controller {
    static HashMap<String, warehouse> whs;
    static bodyfactory bf;
    static powerfactory pf;
    static acsfactory af;
    static carfactory cf;
    static dealer d;
    public static void main(String[] args) {
        Area a = new Area("conf.txt");
    }
}

