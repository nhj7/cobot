package kr.co.cobot.entity;
// Generated 2017. 7. 19 ���� 1:12:19 by Hibernate Tools 5.2.3.Final

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
 * TbAn1 generated by hbm2java
 */
@Entity
@Table(name = "tb_an_1", catalog = "cobot")
public class TbAn1 implements java.io.Serializable {

	private TbAn1Id id;
	private float riseVol;
	private BigDecimal minVol;
	private long riseVolPer;
	private float risePer;
	private float risePer1;
	private String catRiseVol;
	private String catRisePer;
	private String catPrice;
	private BigDecimal low24hr;
	private float rise24per;
	private int score;
	private Date regDttm;
	private Date modDttm;

	public TbAn1() {
	}

	public TbAn1(TbAn1Id id, float riseVol, BigDecimal minVol, long riseVolPer, float risePer, float risePer1,
			String catRiseVol, String catRisePer, String catPrice, BigDecimal low24hr, float rise24per, int score) {
		this.id = id;
		this.riseVol = riseVol;
		this.minVol = minVol;
		this.riseVolPer = riseVolPer;
		this.risePer = risePer;
		this.risePer1 = risePer1;
		this.catRiseVol = catRiseVol;
		this.catRisePer = catRisePer;
		this.catPrice = catPrice;
		this.low24hr = low24hr;
		this.rise24per = rise24per;
		this.score = score;
	}

	public TbAn1(TbAn1Id id, float riseVol, BigDecimal minVol, long riseVolPer, float risePer, float risePer1,
			String catRiseVol, String catRisePer, String catPrice, BigDecimal low24hr, float rise24per, int score,
			Date regDttm, Date modDttm) {
		this.id = id;
		this.riseVol = riseVol;
		this.minVol = minVol;
		this.riseVolPer = riseVolPer;
		this.risePer = risePer;
		this.risePer1 = risePer1;
		this.catRiseVol = catRiseVol;
		this.catRisePer = catRisePer;
		this.catPrice = catPrice;
		this.low24hr = low24hr;
		this.rise24per = rise24per;
		this.score = score;
		this.regDttm = regDttm;
		this.modDttm = modDttm;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "srchDttm", column = @Column(name = "SRCH_DTTM", nullable = false, length = 19)),
			@AttributeOverride(name = "ccd", column = @Column(name = "CCD", nullable = false, length = 5)) })
	public TbAn1Id getId() {
		return this.id;
	}

	public void setId(TbAn1Id id) {
		this.id = id;
	}

	@Column(name = "RISE_VOL", nullable = false, precision = 12, scale = 0)
	public float getRiseVol() {
		return this.riseVol;
	}

	public void setRiseVol(float riseVol) {
		this.riseVol = riseVol;
	}

	@Column(name = "MIN_VOL", nullable = false, precision = 18, scale = 8)
	public BigDecimal getMinVol() {
		return this.minVol;
	}

	public void setMinVol(BigDecimal minVol) {
		this.minVol = minVol;
	}

	@Column(name = "RISE_VOL_PER", nullable = false, precision = 10, scale = 0)
	public long getRiseVolPer() {
		return this.riseVolPer;
	}

	public void setRiseVolPer(long riseVolPer) {
		this.riseVolPer = riseVolPer;
	}

	@Column(name = "RISE_PER", nullable = false, precision = 12, scale = 0)
	public float getRisePer() {
		return this.risePer;
	}

	public void setRisePer(float risePer) {
		this.risePer = risePer;
	}

	@Column(name = "RISE_PER_1", nullable = false, precision = 12, scale = 0)
	public float getRisePer1() {
		return this.risePer1;
	}

	public void setRisePer1(float risePer1) {
		this.risePer1 = risePer1;
	}

	@Column(name = "CAT_RISE_VOL", nullable = false, length = 50)
	public String getCatRiseVol() {
		return this.catRiseVol;
	}

	public void setCatRiseVol(String catRiseVol) {
		this.catRiseVol = catRiseVol;
	}

	@Column(name = "CAT_RISE_PER", nullable = false, length = 50)
	public String getCatRisePer() {
		return this.catRisePer;
	}

	public void setCatRisePer(String catRisePer) {
		this.catRisePer = catRisePer;
	}

	@Column(name = "CAT_PRICE", nullable = false, length = 100)
	public String getCatPrice() {
		return this.catPrice;
	}

	public void setCatPrice(String catPrice) {
		this.catPrice = catPrice;
	}

	@Column(name = "LOW24HR", nullable = false, precision = 10, scale = 8)
	public BigDecimal getLow24hr() {
		return this.low24hr;
	}

	public void setLow24hr(BigDecimal low24hr) {
		this.low24hr = low24hr;
	}

	@Column(name = "RISE24PER", nullable = false, precision = 12, scale = 0)
	public float getRise24per() {
		return this.rise24per;
	}

	public void setRise24per(float rise24per) {
		this.rise24per = rise24per;
	}

	@Column(name = "SCORE", nullable = false)
	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
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
