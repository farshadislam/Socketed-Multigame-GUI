# Use Case: View Player Profile

## Use Case ID: UC-001

### Actors:
- **Primary Actor:** Player (User viewing the profile)
- **Secondary Actor:** System (retrieves and displays profile data)

### Preconditions:
- The user must be **logged into** the system.
- The profile being viewed **must exist** in the database.

### Trigger:
- The user selects **a player’s name or avatar** from the leaderboard, match history, or search function.

## Main Flow:
1. The user selects a player’s profile from a **list (e.g., leaderboard, recent matches, search results).**
2. The system retrieves the **player’s profile data** from the database.
3. The system displays the profile, showing:
    - Username and avatar
    - Games played
    - Win/Loss record
    - Current ranking
    - Recent match history
4. The user can **exit the profile view** and return to the previous screen.

## Alternative Flows:
### **4a. Player is viewing their own profile**
- The system **adds an "Edit Profile" button** (not available when viewing other players' profiles).

### **4b. Profile data fails to load (e.g., network error)**
- The system displays a **"Profile could not be loaded" message** with a retry option.

## Postconditions:
- The system successfully **displays the requested profile**.
- If an error occurs, the user is informed **with an appropriate message**.

## Exceptions:
- If the player’s profile **no longer exists** (e.g., account deleted), the system should return a **"User not found" error**.
- If the **database is unreachable**, an **error message** should be shown.

# **Use Case: Display Leaderboard**

## **Use Case ID:** UC-002

### **Actors:**
- **Primary Actor:** Player (User viewing the leaderboard)
- **Secondary Actor:** System (Retrieves and displays leaderboard data)

### **Preconditions:**
- The user must be logged into the system.
- The leaderboard data must exist and be retrievable from the database.

### **Trigger:**
- The user selects the **"Leaderboard"** option from the game menu or UI.

## **Main Flow:**
1. The user selects the **Leaderboard** option from the main menu or another interface.
2. The system retrieves the **latest leaderboard data** from the database.
3. The system displays the leaderboard, showing:
   - **Top-ranked players** (sorted by score, rank, or achievements)
   - **Player rankings**, including their username and avatar
   - **Player statistics**, such as total wins, losses, and performance rating
   - **User’s own rank** highlighted (if applicable)
4. The user can:
   - Scroll through the leaderboard
   - Search for a specific player
   - Select a player’s profile for more details (triggers **View Profile Use Case**)
5. The user can **exit the leaderboard view** and return to the previous screen.

## **Alternative Flows:**
### **4a. Leaderboard is empty (No data available)**
- The system displays a message: **"No leaderboard data available yet."**

### **4b. Network or database failure**
- The system displays a message: **"Leaderboard could not be loaded. Please try again later."**
- A retry button is provided.

### **4c. User wants to filter the leaderboard**
- The system provides filtering options, such as:
   - **Global vs. Regional Leaderboard**
   - **Friends-Only Leaderboard**
   - **Leaderboard by Game Mode (e.g., Ranked, Casual, Tournaments)**

## **Postconditions:**
- The system successfully **displays the leaderboard**.
- If an error occurs, the user is informed with an **appropriate message**.

## **Exceptions:**
- If the leaderboard data **is corrupted or missing**, the system logs an error and informs the user.
- If the **player’s rank cannot be determined**, the system highlights this with a **“Rank Unavailable”** message.


