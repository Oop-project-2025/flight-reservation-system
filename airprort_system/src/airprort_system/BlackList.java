package airprort_system;

import airprort_system.Administrator;
import airprort_system.User;
import java.util.ArrayList;
import java.util.List;

public class BlackList {
   private String blackListID;
   private User user ;
   private String reason;
   private Administrator addedBy;     
   private Boolean isActive ;

public BlackList(String blackListID, User user, String reason, Administrator addedBy, Boolean isActive) {
        this.blackListID = blackListID;
        this.user=user;
        this.reason = reason;
        this.addedBy=addedBy;
        this.isActive = isActive;
    }

    public String getBlackListID() {
        return blackListID;
    }

    public void setBlackListID(String blackListID) {
        this.blackListID = blackListID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

public class BlackListrecord{
        private List<BlackList> records =new ArrayList<>();
        public boolean addUser(User user, String reason, Administrator admin) {   
        String id = "Bl_1" + (records.size() + 1); 
        BlackList record = new BlackList(id, user, reason, admin, true);
        return records.add(record);
    }
         public boolean removeUser(String userId) {     //remove User
            for (BlackList record : records) {
            if (record.user.getUserID().equals(userId) && record.isActive) {
                record.isActive = false;
                return true;
            }
        }
        return false;
    }
        public Boolean checkUserStatus(User user) {
    if (user == null) {
        System.out.println("Invalid user.");
        return false;
    }

    if (user.isBlacklisted()) {
        System.out.println("You are on the Blacklist! User: " + user.getUsername() +
                ", Blacklist ID: " + user.getBlackListID() +
                ", Reason: " + user.getBlacklistReason());
        return true;
    } else {
        System.out.println("User is not on the Blacklist.");
        return false;
    }
}


}


    
}

