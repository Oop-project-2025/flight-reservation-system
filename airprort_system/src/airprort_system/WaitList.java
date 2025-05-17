package airprort_system;

import java.time.LocalDateTime;
import java.util.List;

public class WaitList {
    private String waitListId;
    private Flight flight;
    private List<Customer> customers;
    private List<String> priorityOrder;
    private LocalDateTime creationDate;

    public WaitList(String waitListId, Flight flight, List<Customer> customers, List<String> priorityOrder, LocalDateTime creationDate) {
        this.waitListId = waitListId;
        this.flight = flight;
        this.customers = customers;
        this.priorityOrder = priorityOrder;
        this.creationDate = creationDate;
    }

    public String getWaitListId() {
        return waitListId;
    }

    public void setWaitListId(String waitListId) {
        this.waitListId = waitListId;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<String> getPriorityOrder() {
        return priorityOrder;
    }

    public void setPriorityOrder(List<String> priorityOrder) {
        this.priorityOrder = priorityOrder;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public boolean addCustomer (Customer customer){      //add Customer
        if (customer==null || customers.contains(customer))
          return false;
     customers.add(customer);
     priorityOrder.add(customer.getId());
     return true;
    }

    public boolean removeCustomer(String customerId){
        if (customerId==null || customerId.isEmpty())   return false;
        Customer toRemove =null ;
        for (Customer c :customers){
            if (customerId.equals(c.getId())){
                toRemove = c;
                break;
            }
        }
        if (toRemove != null){
            customers.remove(toRemove);
            priorityOrder.remove(customerId);
                return true;    }
        return false;
                }

    
    public boolean notifyNextCustomer(){        //notify Next Customer
        if (! priorityOrder.isEmpty()){
            String nextId=priorityOrder.get(0);
            System.out.println("Notifing customer with ID:" +nextId);
            return true;
        }
        return false;
    }
    public int checkPosition(String customerId){
        return priorityOrder.indexOf(customerId);
    }
    public boolean promoteCustomer (String customerId){
        int index= priorityOrder.indexOf(customerId);
        if (index>0){
            String temp =priorityOrder.get(index -1);
            priorityOrder.set(index ,temp); return true;
        }
        return false;
    }
    public boolean clearWaitList(){
        customers.clear();
        priorityOrder.clear();
        return true;
    }
}





