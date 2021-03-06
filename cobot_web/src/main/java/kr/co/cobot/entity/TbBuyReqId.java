package kr.co.cobot.entity;
// Generated 2017. 7. 19 ���� 1:12:19 by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TbBuyReqId generated by hbm2java
 */
@Embeddable
public class TbBuyReqId implements java.io.Serializable {

	private String userId;
	private Integer eid;
	private Integer cid;
	private String ccd;
	private String purPrice;
	private String purAmount;
	private Date reqDttm;
	private Date regDttm;
	private Date modDttm;

	public TbBuyReqId() {
	}

	public TbBuyReqId(String userId, Integer eid, Integer cid, String ccd, String purPrice, String purAmount,
			Date reqDttm, Date regDttm, Date modDttm) {
		this.userId = userId;
		this.eid = eid;
		this.cid = cid;
		this.ccd = ccd;
		this.purPrice = purPrice;
		this.purAmount = purAmount;
		this.reqDttm = reqDttm;
		this.regDttm = regDttm;
		this.modDttm = modDttm;
	}

	@Column(name = "USER_ID", length = 20)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "EID")
	public Integer getEid() {
		return this.eid;
	}

	public void setEid(Integer eid) {
		this.eid = eid;
	}

	@Column(name = "CID")
	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	@Column(name = "CCD", length = 5)
	public String getCcd() {
		return this.ccd;
	}

	public void setCcd(String ccd) {
		this.ccd = ccd;
	}

	@Column(name = "PUR_PRICE", length = 5)
	public String getPurPrice() {
		return this.purPrice;
	}

	public void setPurPrice(String purPrice) {
		this.purPrice = purPrice;
	}

	@Column(name = "PUR_AMOUNT", length = 5)
	public String getPurAmount() {
		return this.purAmount;
	}

	public void setPurAmount(String purAmount) {
		this.purAmount = purAmount;
	}

	@Column(name = "REQ_DTTM", length = 19)
	public Date getReqDttm() {
		return this.reqDttm;
	}

	public void setReqDttm(Date reqDttm) {
		this.reqDttm = reqDttm;
	}

	@Column(name = "REG_DTTM", length = 19)
	public Date getRegDttm() {
		return this.regDttm;
	}

	public void setRegDttm(Date regDttm) {
		this.regDttm = regDttm;
	}

	@Column(name = "MOD_DTTM", length = 19)
	public Date getModDttm() {
		return this.modDttm;
	}

	public void setModDttm(Date modDttm) {
		this.modDttm = modDttm;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TbBuyReqId))
			return false;
		TbBuyReqId castOther = (TbBuyReqId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())))
				&& ((this.getEid() == castOther.getEid()) || (this.getEid() != null && castOther.getEid() != null
						&& this.getEid().equals(castOther.getEid())))
				&& ((this.getCid() == castOther.getCid()) || (this.getCid() != null && castOther.getCid() != null
						&& this.getCid().equals(castOther.getCid())))
				&& ((this.getCcd() == castOther.getCcd()) || (this.getCcd() != null && castOther.getCcd() != null
						&& this.getCcd().equals(castOther.getCcd())))
				&& ((this.getPurPrice() == castOther.getPurPrice()) || (this.getPurPrice() != null
						&& castOther.getPurPrice() != null && this.getPurPrice().equals(castOther.getPurPrice())))
				&& ((this.getPurAmount() == castOther.getPurAmount()) || (this.getPurAmount() != null
						&& castOther.getPurAmount() != null && this.getPurAmount().equals(castOther.getPurAmount())))
				&& ((this.getReqDttm() == castOther.getReqDttm()) || (this.getReqDttm() != null
						&& castOther.getReqDttm() != null && this.getReqDttm().equals(castOther.getReqDttm())))
				&& ((this.getRegDttm() == castOther.getRegDttm()) || (this.getRegDttm() != null
						&& castOther.getRegDttm() != null && this.getRegDttm().equals(castOther.getRegDttm())))
				&& ((this.getModDttm() == castOther.getModDttm()) || (this.getModDttm() != null
						&& castOther.getModDttm() != null && this.getModDttm().equals(castOther.getModDttm())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result + (getEid() == null ? 0 : this.getEid().hashCode());
		result = 37 * result + (getCid() == null ? 0 : this.getCid().hashCode());
		result = 37 * result + (getCcd() == null ? 0 : this.getCcd().hashCode());
		result = 37 * result + (getPurPrice() == null ? 0 : this.getPurPrice().hashCode());
		result = 37 * result + (getPurAmount() == null ? 0 : this.getPurAmount().hashCode());
		result = 37 * result + (getReqDttm() == null ? 0 : this.getReqDttm().hashCode());
		result = 37 * result + (getRegDttm() == null ? 0 : this.getRegDttm().hashCode());
		result = 37 * result + (getModDttm() == null ? 0 : this.getModDttm().hashCode());
		return result;
	}

}
