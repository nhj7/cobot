package kr.co.cobot.entity;
// Generated 2017. 8. 5 ���� 10:22:22 by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TbPostMarketInfo generated by hbm2java 
 */
@Entity
@Table(name = "tb_post_market_info", catalog = "cobot")
public class TbPostMarketInfo implements java.io.Serializable {

	private long siteId;
	private int postId;
	private String author;
	private String permLink;
	private String prodName;
	private Short method;
	private Short category;
	private Short status;
	private Date auctionEndDttm;
	private Double oriAmt;
	private Double sellAmt;
	private Double auctionUnitAmt;
	private Double lastSellAmt;
	private Double voteAmt;
	private Double voteRatio;
	private Double realAmt;
	private Date postRegDttm;
	private Date postUpdateDttm;
	private String postImgUrl;
	private String prodImgUrl;
	private String cachePostImgUrl;
	private String cacheProdImgUrl;
	private Integer replyCnt;
	private Date regDttm;
	private Date modDttm;

	public TbPostMarketInfo() {
	}

	public TbPostMarketInfo(long siteId, int postId, String permLink, String prodName) {
		this.siteId = siteId;
		this.postId = postId;
		this.permLink = permLink;
		this.prodName = prodName;
	}

	public TbPostMarketInfo(long siteId, int postId, String author, String permLink, String prodName, Short method,
			Short category, Short status, Date auctionEndDttm, Double oriAmt, Double sellAmt, Double auctionUnitAmt,
			Double lastSellAmt, Double voteAmt, Double voteRatio, Double realAmt, Date postRegDttm, Date postUpdateDttm,
			String postImgUrl, String prodImgUrl, String cachePostImgUrl, String cacheProdImgUrl, Integer replyCnt,
			Date regDttm, Date modDttm) {
		this.siteId = siteId;
		this.postId = postId;
		this.author = author;
		this.permLink = permLink;
		this.prodName = prodName;
		this.method = method;
		this.category = category;
		this.status = status;
		this.auctionEndDttm = auctionEndDttm;
		this.oriAmt = oriAmt;
		this.sellAmt = sellAmt;
		this.auctionUnitAmt = auctionUnitAmt;
		this.lastSellAmt = lastSellAmt;
		this.voteAmt = voteAmt;
		this.voteRatio = voteRatio;
		this.realAmt = realAmt;
		this.postRegDttm = postRegDttm;
		this.postUpdateDttm = postUpdateDttm;
		this.postImgUrl = postImgUrl;
		this.prodImgUrl = prodImgUrl;
		this.cachePostImgUrl = cachePostImgUrl;
		this.cacheProdImgUrl = cacheProdImgUrl;
		this.replyCnt = replyCnt;
		this.regDttm = regDttm;
		this.modDttm = modDttm;
	}

	@Id

	@Column(name = "SITE_ID", unique = true, nullable = false)
	public long getSiteId() {
		return this.siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	@Column(name = "POST_ID", nullable = false)
	public int getPostId() {
		return this.postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	@Column(name = "AUTHOR", length = 64)
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "PERM_LINK", nullable = false, length = 256)
	public String getPermLink() {
		return this.permLink;
	}

	public void setPermLink(String permLink) {
		this.permLink = permLink;
	}

	@Column(name = "PROD_NAME", nullable = false, length = 256)
	public String getProdName() {
		return this.prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	@Column(name = "METHOD")
	public Short getMethod() {
		return this.method;
	}

	public void setMethod(Short method) {
		this.method = method;
	}

	@Column(name = "CATEGORY")
	public Short getCategory() {
		return this.category;
	}

	public void setCategory(Short category) {
		this.category = category;
	}

	@Column(name = "STATUS")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUCTION_END_DTTM", length = 19)
	public Date getAuctionEndDttm() {
		return this.auctionEndDttm;
	}

	public void setAuctionEndDttm(Date auctionEndDttm) {
		this.auctionEndDttm = auctionEndDttm;
	}

	@Column(name = "ORI_AMT", precision = 22, scale = 0)
	public Double getOriAmt() {
		return this.oriAmt;
	}

	public void setOriAmt(Double oriAmt) {
		this.oriAmt = oriAmt;
	}

	@Column(name = "SELL_AMT", precision = 22, scale = 0)
	public Double getSellAmt() {
		return this.sellAmt;
	}

	public void setSellAmt(Double sellAmt) {
		this.sellAmt = sellAmt;
	}

	@Column(name = "AUCTION_UNIT_AMT", precision = 22, scale = 0)
	public Double getAuctionUnitAmt() {
		return this.auctionUnitAmt;
	}

	public void setAuctionUnitAmt(Double auctionUnitAmt) {
		this.auctionUnitAmt = auctionUnitAmt;
	}

	@Column(name = "LAST_SELL_AMT", precision = 22, scale = 0)
	public Double getLastSellAmt() {
		return this.lastSellAmt;
	}

	public void setLastSellAmt(Double lastSellAmt) {
		this.lastSellAmt = lastSellAmt;
	}

	@Column(name = "VOTE_AMT", precision = 22, scale = 0)
	public Double getVoteAmt() {
		return this.voteAmt;
	}

	public void setVoteAmt(Double voteAmt) {
		this.voteAmt = voteAmt;
	}

	@Column(name = "VOTE_RATIO", precision = 22, scale = 0)
	public Double getVoteRatio() {
		return this.voteRatio;
	}

	public void setVoteRatio(Double voteRatio) {
		this.voteRatio = voteRatio;
	}

	@Column(name = "REAL_AMT", precision = 22, scale = 0)
	public Double getRealAmt() {
		return this.realAmt;
	}

	public void setRealAmt(Double realAmt) {
		this.realAmt = realAmt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POST_REG_DTTM", length = 19)
	public Date getPostRegDttm() {
		return this.postRegDttm;
	}

	public void setPostRegDttm(Date postRegDttm) {
		this.postRegDttm = postRegDttm;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POST_UPDATE_DTTM", length = 19)
	public Date getPostUpdateDttm() {
		return this.postUpdateDttm;
	}

	public void setPostUpdateDttm(Date postUpdateDttm) {
		this.postUpdateDttm = postUpdateDttm;
	}

	@Column(name = "POST_IMG_URL", length = 2048)
	public String getPostImgUrl() {
		return this.postImgUrl;
	}

	public void setPostImgUrl(String postImgUrl) {
		this.postImgUrl = postImgUrl;
	}

	@Column(name = "PROD_IMG_URL", length = 2048)
	public String getProdImgUrl() {
		return this.prodImgUrl;
	}

	public void setProdImgUrl(String prodImgUrl) {
		this.prodImgUrl = prodImgUrl;
	}

	@Column(name = "CACHE_POST_IMG_URL", length = 64)
	public String getCachePostImgUrl() {
		return this.cachePostImgUrl;
	}

	public void setCachePostImgUrl(String cachePostImgUrl) {
		this.cachePostImgUrl = cachePostImgUrl;
	}

	@Column(name = "CACHE_PROD_IMG_URL", length = 64)
	public String getCacheProdImgUrl() {
		return this.cacheProdImgUrl;
	}

	public void setCacheProdImgUrl(String cacheProdImgUrl) {
		this.cacheProdImgUrl = cacheProdImgUrl;
	}

	@Column(name = "REPLY_CNT")
	public Integer getReplyCnt() {
		return this.replyCnt;
	}

	public void setReplyCnt(Integer replyCnt) {
		this.replyCnt = replyCnt;
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
