package com.hospital_management.model;

public class Patient {
    private  Long id;
        private String Patient_Name;
        private long Mobile;
        private String issue;
        
        // Constructors
        public Patient() {}
        
        public Patient(Long id, String Patient_Name,long Mobile,String issue) {
            this.id = id;
            this.Patient_Name = Patient_Name;
            this.Mobile = Mobile;
            this.issue = issue;
           
        }
    
        // Getters and Setters
        public  Long getId() { 
            return id; 
    }
    public void setId(Long id) {
         this.id = id; 
        }
    public  String getName() {
         return Patient_Name; 
        }
    public void setName(String Patient_Name ) {
         this.Patient_Name=Patient_Name; 
        }
    public long getMobile() { 
        return Mobile; 
    }
    public void setMobile(long Mobile) { 
       this.Mobile=Mobile;

    }
    public String getissue() { 
        return issue;
     }
    public void setissue(String issue) { 
        this.issue = issue; 
    }
   
}
