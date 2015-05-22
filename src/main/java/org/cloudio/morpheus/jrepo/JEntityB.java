package org.cloudio.morpheus.jrepo;

/**
 * Created by zslajchrt on 02/03/15.
 */
public class JEntityB {

    private int id;
    private String name;
    private JEntityB other = new JEntityB();

    public JEntityB(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public JEntityB() {
        this(0, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int methodN(int i) {
        int oi = other.methodN(i);
        return 2 * templateN(oi);
    }

    protected int templateN(int i) {
        return i;
    }

}
