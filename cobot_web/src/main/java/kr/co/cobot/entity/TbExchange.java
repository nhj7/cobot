package kr.co.cobot.entity;
// Generated 2017. 7. 19 ���� 1:12:19 by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TbExchange generated by hbm2java
 */
@Entity
@Table(name = "tb_exchange", catalog = "cobot")
public class TbExchange implements java.io.Serializable {

	private int eid;
	private String ecd;
	private String enm;
	private String enmKo;
	private String enmJp;
	private Date regDttm;
	private Date modDttm;

	public TbExchange() {
	}

	public TbExchange(int eid, String ecd) {
		this.eid = eid;
		this.ecd = ecd;
	}

	public TbExchange(int eid, String ecd, String enm, String enmKo, String enmJp, Date regDttm, Date modDttm) {
		this.eid = eid;
		this.ecd = ecd;
		this.enm = enm;
		this.enmKo = enmKo;
		this.enmJp = enmJp;
		this.regDttm = regDttm;
		this.modDttm = modDttm;
	}

	@Id

	@Column(name = "EID", unique = true, nullable = false)
	public int getEid() {
		return this.eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	@Column(name = "ECD", nullable = false, length = 5)
	public String getEcd() {
		return this.ecd;
	}

	public void setEcd(String ecd) {
		this.ecd = ecd;
	}

	@Column(name = "ENM", length = 50)
	public String getEnm() {
		return this.enm;
	}

	public void setEnm(String enm) {
		this.enm = enm;
	}

	@Column(name = "ENM_KO", length = 50)
	public String getEnmKo() {
		return this.enmKo;
	}

	public void setEnmKo(String enmKo) {
		this.enmKo = enmKo;
	}

	@Column(name = "ENM_JP", length = 50)
	public String getEnmJp() {
		return this.enmJp;
	}

	public void setEnmJp(String enmJp) {
		this.enmJp = enmJp;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REG_DTTM", length = 19)
	public Date getRegDttm() {
		return this.regDttm;
	}

	public void setRegDttm(Date regDttm) {
		this.regDttm = regDttm;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MOD_DTTM", length = 19)
	public Date getModDttm() {
		return this.modDttm;
	}

	public void setModDttm(Date modDttm) {
		this.modDttm = modDttm;
	}

}
