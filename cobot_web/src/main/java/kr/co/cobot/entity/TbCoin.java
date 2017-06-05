package kr.co.cobot.entity;
// Generated 2017. 6. 5 ���� 2:50:00 by Hibernate Tools 5.2.3.Final


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TbCoin generated by hbm2java
 */
@Entity
@Table(name="tb_coin"
    ,catalog="cobot"
)
public class TbCoin  implements java.io.Serializable {


     private int cid;
     private String ccd;
     private String cnm;
     private String cnmKo;
     private String cnote;
     private Date regDttm;
     private Date modDttm;

    public TbCoin() {
    }

	
    public TbCoin(int cid, String ccd) {
        this.cid = cid;
        this.ccd = ccd;
    }
    public TbCoin(int cid, String ccd, String cnm, String cnmKo, String cnote, Date regDttm, Date modDttm) {
       this.cid = cid;
       this.ccd = ccd;
       this.cnm = cnm;
       this.cnmKo = cnmKo;
       this.cnote = cnote;
       this.regDttm = regDttm;
       this.modDttm = modDttm;
    }
   
     @Id 

    
    @Column(name="CID", unique=true, nullable=false)
    public int getCid() {
        return this.cid;
    }
    
    public void setCid(int cid) {
        this.cid = cid;
    }

    
    @Column(name="CCD", nullable=false, length=5)
    public String getCcd() {
        return this.ccd;
    }
    
    public void setCcd(String ccd) {
        this.ccd = ccd;
    }

    
    @Column(name="CNM", length=50)
    public String getCnm() {
        return this.cnm;
    }
    
    public void setCnm(String cnm) {
        this.cnm = cnm;
    }

    
    @Column(name="CNM_KO", length=100)
    public String getCnmKo() {
        return this.cnmKo;
    }
    
    public void setCnmKo(String cnmKo) {
        this.cnmKo = cnmKo;
    }

    
    @Column(name="CNOTE", length=800)
    public String getCnote() {
        return this.cnote;
    }
    
    public void setCnote(String cnote) {
        this.cnote = cnote;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="REG_DTTM", length=19)
    public Date getRegDttm() {
        return this.regDttm;
    }
    
    public void setRegDttm(Date regDttm) {
        this.regDttm = regDttm;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_DTTM", length=19)
    public Date getModDttm() {
        return this.modDttm;
    }
    
    public void setModDttm(Date modDttm) {
        this.modDttm = modDttm;
    }




}


