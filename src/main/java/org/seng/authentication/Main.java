package org.seng.authentication;

public class Main {
        public static void main(String[] args) {
            CredentialsDatabase db = new CredentialsDatabase();
//
//            Player p1 = new Player("Hamna", "hamna@gmail.com", "password1234");
//            p1.setSymbol('B');
//
//            Player p2 = new Player("Maham", "maham@gmail.com", "maham1234");
//            p2.setSymbol('R');
//
//            Player p3 = new Player("Wissal", "wissal@gmai.com", "wissal1234");
//            p3.setSymbol('Y');

//            db.addNewPlayer("Hamna", p1);
//            db.addNewPlayer("Maham", p2);
//            db.addNewPlayer("Wissal", p3);
//
//            db.saveDatabase();

            db.loadDatabase("output.txt");
            System.out.println(db.usernameLookup("Vairhsa"));



        }
}

