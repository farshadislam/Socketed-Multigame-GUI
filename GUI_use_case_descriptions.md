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

