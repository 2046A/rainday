/**
 * Created by ivan on 16/9/29.
 * Org.Dean.Raindrop.Reflect
 */
package Org.Dean.Raindrop.Reflect;

import Test.Help;
import java.util.ArrayList;
import java.lang.reflect.*;

public class Parser {
    public Parser(){}
    public static void parseHelp(){
        String className = "Test.Help";
        try {
            Class info = Class.forName(className);
            Constructor[] cons = info.getConstructors();
            for(int i=0;i<cons.length;i++) {
                Constructor<?> constructor = cons[i];
                Class[] pt = constructor.getParameterTypes();
                ArrayList<Object> params = new ArrayList<Object>();
                Class[] ano = new Class[pt.length];
                for(int j=0;j<pt.length;j++){
                    Class type = pt[j];
                    ano[j] = type;
                    params.add(Class.forName(type.getName()).newInstance());
                }
                Object[] finalParams = params.toArray();
                Constructor c = info.getConstructor(ano);
                Object finalInstance = c.newInstance(finalParams);
                Help h = (Help)finalInstance;
                h.setFinalMsg("切爱");
                System.out.println(h.getFinalMsg());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
