public class LoginPage {
    private CredentialsDatabase database;

    public LoginPage(CredentialsDatabase database){
        this.database = database;
    }
    public void login(String username, String password){
        //(We will basically be doing a lookup in the database object like Player player =
        //database.findPlayerByUsername(username) and then we will check if the player
        //is not null (the username exists in the database and the password entered is the
        //same as player.getPassword and everything is correct, we will create a new
        //Homepage with the player and database as its parameter and return that, so
        //basically the homepage will be associated to that player, or if not then we will
        //return null. So the existence of homePage will determine if the login was
        //successful or not. Something like:
        //HomePage home = loginPage.login("player1", "password123");
        //if (home != null) { System.out.println("Login successful!"); )
    }
}
