package airprort_system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Airport {
   
    private String airportCode;
    private String name;
    private String city;
    private String country;
    private List<String> terminals;
    private List<String> facilities;
    private String timeZone;

    public Airport(String airportCode, String name, String city, String country, String timeZone) {
        this.airportCode = airportCode;
        this.name = name;
        this.city = city;
        this.country = country;
        this.timeZone = timeZone;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<String> terminals) {
        this.terminals = terminals;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    
    public void addFacilitys(String facility){
    if(!facilities.contains(facility)){
    facilities.add(facility);
    }
    }
    
    public boolean addTerminals(String terminal){
    if(!terminals.contains(terminal)){
    facilities.add(terminal);
    
         return true;
    }
        return false;
    }
    
    
    public Map<String, String> getAirportInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("Code", airportCode);
        info.put("Name", name);
        info.put("City", city);
        info.put("Country", country);
        info.put("Time Zone", timeZone);
        info.put("Terminals", String.join(", ", terminals));
        info.put("Facilities", String.join(", ", facilities));
        return info;
    }
    
    @Override
    public String toString(){
    return name+ "("+airportCode+") -"+city+","+country;
    }
}
    
