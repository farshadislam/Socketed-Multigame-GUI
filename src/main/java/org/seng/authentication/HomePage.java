package org.seng.authentication;

public class HomePage {
    // defining instance variables
    private Player player;
    private CredentialsDatabase database;
    private Settings accountSettings;

    // creating the constructor to initialize the player and database in the HomePage class
    public HomePage(Player player, CredentialsDatabase database) {
        this.player = player;
        this.database = database;
        this.accountSettings = new Settings(player, database);
    }

    // created method to view logged-in player's record
    public Player viewYourOwnRecords(){
        return this.player;
    }

    // created method to view another player's records
    public Player viewOtherPlayerRecords(Player otherPlayer){
        return otherPlayer;
    }

    // created method to view account settings
    public Settings viewSettings(){
        return this.accountSettings;
    }

    public Player getPlayer(){
        return this.player;
    }

}
