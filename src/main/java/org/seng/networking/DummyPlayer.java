//package main.java.org.seng.networking;
package org.seng.networking;
import java.util.ArrayList;


public class DummyPlayer extends Player {

    //calls upon these to play the game for it.



    private static final ArrayList<String> autoChatMessages = new ArrayList<>();
    /**
     * Creates an instance of a Dummy using AIBot constructor
     *
     * @param username  The username of the Dummy
     * @param password  Their passsword, although this is arbitrary
     */
    public DummyPlayer(String username, String password) {
        super(username, password);
        addMessagesToDummy();
    }

    public static void addMessagesToDummy() {
        autoChatMessages.add("You're donezo bozo");
        autoChatMessages.add("I already know that I'm going to win");
        autoChatMessages.add("You play like you're five years old");
        autoChatMessages.add("I'd like to thank the Academy...");
        autoChatMessages.add("Yooooooo you suckkkkkkkk");
    }


    /**
     * This logs the dummy in.
     * @param password input pw. Arbitrary
     * @return true, because the Dummy will log in without fail.
     */
    @Override
    public boolean login(String password) {
        return true;
    }

    @Override
    public void leaveMatch() {
        super.leaveMatch();
        disconnect();   //Dummy shouldn't exist when the Human Player leaves
    }

}
