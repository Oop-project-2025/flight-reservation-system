package airprort_system;
import java.util.Date;
import java.util.List;

public class DeepFake {


public class DeepFakeUser {
    private User originalUser;
    private double detectionScore;
    private Date detictionDate;
    private List<String> SuspiciousActivities;
    private boolean isVerified;

    public DeepFakeUser(User originalUser, double detectionScore, Date detictionDate, List<String> suspiciousActivities, boolean isVerified) {
        this.originalUser = originalUser;
        this.detectionScore = detectionScore;
        this.detictionDate = detictionDate;
        SuspiciousActivities = suspiciousActivities;
        this.isVerified = isVerified;
    }

    public User getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(User originalUser) {
        this.originalUser = originalUser;
    }

    public double getDetectionScore() {
        return detectionScore;
    }

    public void setDetectionScore(double detectionScore) {
        this.detectionScore = detectionScore;
    }

    public Date getDetictionDate() {
        return detictionDate;
    }

    public void setDetictionDate(Date detictionDate) {
        this.detictionDate = detictionDate;
    }

    public List<String> getSuspiciousActivities() {
        return SuspiciousActivities;
    }

    public void setSuspiciousActivities(List<String> suspiciousActivities) {
        SuspiciousActivities = suspiciousActivities;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
    public boolean VerifyIdentity(){
        if (detectionScore<0.3){
            isVerified=true;
            System.out.println("User verified successfully!!");
        }
        System.out.println("verification failed!!");
        return false;
    }
    public boolean logSuspiciousActivity(String activity){
        if (activity==null || activity.isEmpty()){
            return false;
        }
        SuspiciousActivities.add(activity);
        System.out.println("Suspicious Activity logged : "+ activity);
        return true;
    }
    public boolean notifyAdmin(){
        System.out.println("Alert: admin notified about potential deep Fake for user ID:" +originalUser.getUserID() );
        return true ;
    }
    public boolean restrictAccount(){
        System.out.println("Account for User ID:"+originalUser.getUserID()+"has been restricted !!");  
        return true;
    }
    public static class BehaviorAnalysis {
        private String analysisResult;
        private int anomalyCount;

        public BehaviorAnalysis(String analysisResult, int anomalyCount) {
            this.analysisResult = analysisResult;
            this.anomalyCount = anomalyCount;
        }

        public String getAnalysisResult() {
            return analysisResult;
        }

        public void setAnalysisResult(String analysisResult) {
            this.analysisResult = analysisResult;
        }

        public int getAnomalyCount() {
            return anomalyCount;
        }

        public void setAnomalyCount(int anomalyCount) {
            this.anomalyCount = anomalyCount;
        }
    }
}
}