package br.com.pointel.jinpo;

import br.com.pointel.jarch.mage.WizArray;

public class Jinpo {

    private Jinpo() {}

    private static final String[] cmdHelp = new String[]{"-h", "--help"};

    public static void main(String[] args) {
        if (WizArray.hasAny(cmdHelp, args)) {
            System.out.println("Jinpo is a Java Interpoler for Text Files.");
        } else {
            Desk.start(args);
        }
    }

}
