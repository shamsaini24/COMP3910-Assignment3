package ca.bcit.assignment3.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AuthToken")
public class TokenModel implements Serializable{
    
    private String token;
    private int empNum;
    private Date expDate;
    
    TokenModel(){   
    }
    
    
    public TokenModel(String token, int empNum, Date expDate) {
        super();
        this.token = token;
        this.empNum = empNum;
        this.expDate = expDate;
    }


    @XmlAttribute
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    @XmlElement
    public int getEmpNum() {
        return empNum;
    }
    
    public void setEmpNum(int empNum) {
        this.empNum = empNum;
    }
    
    @XmlElement
    public Date getExpDate() {
        return expDate;
    }
    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }
    
    
    
    

}
