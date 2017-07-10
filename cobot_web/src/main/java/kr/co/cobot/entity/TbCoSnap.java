package kr.co.cobot.entity;
// Generated 2017. 7. 11 ���� 1:11:18 by Hibernate Tools 5.2.3.Final

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TbCoSnap generated by hbm2java
 */
@Entity
@Table(name = "tb_co_snap", catalog = "cobot")
public class TbCoSnap implements java.io.Serializable {

	private TbCoSnapId id;
	private String ccd;
	private BigDecimal price;
	private BigDecimal perCh;
	private BigDecimal baseVol;
	private BigDecimal quoteVol;
	private Boolean isFrozen;
	private BigDecimal high24hr;
	private BigDecimal low24hr;
	private Date regDttm;
	private Date modDttm; 

	public TbCoSnap() {
	}

	public TbCoSnap(TbCoSnapId id) {
		this.id = id;
	}

	public TbCoSnap(TbCoSnapId id, String ccd, BigDecimal price, BigDecimal perCh, BigDecimal baseVol,
			BigDecimal quoteVol, Boolean isFrozen, BigDecimal high24hr, BigDecimal low24hr, Date regDttm,
			Date modDttm) {
		this.id = id;
		this.ccd = ccd;
		this.price = price;
		this.perCh = perCh;
		this.baseVol = baseVol;
		this.quoteVol = quoteVol;
		this.isFrozen = isFrozen;
		this.high24hr = high24hr;
		this.low24hr = low24hr;
		this.regDttm = regDttm;
		this.modDttm = modDttm;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "srchDttm", column = @Column(name = "SRCH_DTTM", nullable = false, length = 19)),
			@AttributeOverride(name = "eid", column = @Column(name = "EID", nullable = false)),
			@AttributeOverride(name = "unitCid", column = @Column(name = "UNIT_CID", nullable = false)),
			@AttributeOverride(name = "cid", column = @Column(name = "CID", nullable = false)) })
	public TbCoSnapId getId() {
		return this.id;
	}

	public void setId(TbCoSnapId id) {
		this.id = id;
	}

	@Column(name = "CCD", length = 5)
	public String getCcd() {
		return this.ccd;
	}

	public void setCcd(String ccd) {
		this.ccd = ccd;
	}

	@Column(name = "PRICE", precision = 18, scale = 8)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "PER_CH", precision = 18, scale = 8)
	public BigDecimal getPerCh() {
		return this.perCh;
	}

	public void setPerCh(BigDecimal perCh) {
		this.perCh = perCh;
	}

	@Column(name = "BASE_VOL", precision = 18, scale = 8)
	public BigDecimal getBaseVol() {
		return this.baseVol;
	}

	public void setBaseVol(BigDecimal baseVol) {
		this.baseVol = baseVol;
	}

	@Column(name = "QUOTE_VOL", precision = 20, scale = 8)
	public BigDecimal getQuoteVol() {
		return this.quoteVol;
	}

	public void setQuoteVol(BigDecimal quoteVol) {
		this.quoteVol = quoteVol;
	}

	@Column(name = "IS_FROZEN")
	public Boolean getIsFrozen() {
		return this.isFrozen;
	}

	public void setIsFrozen(Boolean isFrozen) {
		this.isFrozen = isFrozen;
	}

	@Column(name = "HIGH24HR", precision = 18, scale = 8)
	public BigDecimal getHigh24hr() {
		return this.high24hr;
	}

	public void setHigh24hr(BigDecimal high24hr) {
		this.high24hr = high24hr;
	}

	@Column(name = "LOW24HR", precision = 18, scale = 8)
	public BigDecimal getLow24hr() {
		return this.low24hr;
	}

	public void setLow24hr(BigDecimal low24hr) {
		this.low24hr = low24hr;
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
