package org.cloudio.morpheus.samples;

/**
 * Created by zslajchrt on 13/01/15.
 */
public class Sample1 {

    static {
        System.out.println("Hello");
    }

    protected String u = "abc";

    public static Object getEntity(Object entity) {
        return null;
    }

    public String getU() {
        return u;
    }

    public static void main(String[] args) {
        Object a = null;
        Object r = getEntity(a);

    }
}
