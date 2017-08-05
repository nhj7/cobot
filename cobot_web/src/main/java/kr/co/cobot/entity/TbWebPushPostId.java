package kr.co.cobot.entity;
// Generated 2017. 8. 5 ���� 10:22:22 by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TbWebPushPostId generated by hbm2java 
 */
@Embeddable
public class TbWebPushPostId implements java.io.Serializable {

	private String siteDvcd;
	private String categoryDvcd;
	private String postDvcd;
	private String tagDvcd;
	private String postUrl;
	private String postTitle;
	private String postImgUrl;
	private Date regDttm;
	private Date modDttm;

	public TbWebPushPostId() {
	}

	public TbWebPushPostId(String siteDvcd, String categoryDvcd, String postDvcd, String tagDvcd, String postUrl,
			String postImgUrl) {
		this.siteDvcd = siteDvcd;
		this.categoryDvcd = categoryDvcd;
		this.postDvcd = postDvcd;
		this.tagDvcd = tagDvcd;
		this.postUrl = postUrl;
		this.postImgUrl = postImgUrl;
	}

	public TbWebPushPostId(String siteDvcd, String categoryDvcd, String postDvcd, String tagDvcd, String postUrl,
			String postTitle, String postImgUrl, Date regDttm, Date modDttm) {
		this.siteDvcd = siteDvcd;
		this.categoryDvcd = categoryDvcd;
		this.postDvcd = postDvcd;
		this.tagDvcd = tagDvcd;
		this.postUrl = postUrl;
		this.postTitle = postTitle;
		this.postImgUrl = postImgUrl;
		this.regDttm = regDttm;
		this.modDttm = modDttm;
	}

	@Column(name = "SITE_DVCD", nullable = false, length = 20)
	public String getSiteDvcd() {
		return this.siteDvcd;
	}

	public void setSiteDvcd(String siteDvcd) {
		this.siteDvcd = siteDvcd;
	}

	@Column(name = "CATEGORY_DVCD", nullable = false, length = 20)
	public String getCategoryDvcd() {
		return this.categoryDvcd;
	}

	public void setCategoryDvcd(String categoryDvcd) {
		this.categoryDvcd = categoryDvcd;
	}

	@Column(name = "POST_DVCD", nullable = false, length = 20)
	public String getPostDvcd() {
		return this.postDvcd;
	}

	public void setPostDvcd(String postDvcd) {
		this.postDvcd = postDvcd;
	}

	@Column(name = "TAG_DVCD", nullable = false, length = 20)
	public String getTagDvcd() {
		return this.tagDvcd;
	}

	public void setTagDvcd(String tagDvcd) {
		this.tagDvcd = tagDvcd;
	}

	@Column(name = "POST_URL", nullable = false, length = 256)
	public String getPostUrl() {
		return this.postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	@Column(name = "POST_TITLE", length = 256)
	public String getPostTitle() {
		return this.postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	@Column(name = "POST_IMG_URL", nullable = false, length = 1024)
	public String getPostImgUrl() {
		return this.postImgUrl;
	}

	public void setPostImgUrl(String postImgUrl) {
		this.postImgUrl = postImgUrl;
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
		if (!(other instanceof TbWebPushPostId))
			return false;
		TbWebPushPostId castOther = (TbWebPushPostId) other;

		return ((this.getSiteDvcd() == castOther.getSiteDvcd()) || (this.getSiteDvcd() != null
				&& castOther.getSiteDvcd() != null && this.getSiteDvcd().equals(castOther.getSiteDvcd())))
				&& ((this.getCategoryDvcd() == castOther.getCategoryDvcd())
						|| (this.getCategoryDvcd() != null && castOther.getCategoryDvcd() != null
								&& this.getCategoryDvcd().equals(castOther.getCategoryDvcd())))
				&& ((this.getPostDvcd() == castOther.getPostDvcd()) || (this.getPostDvcd() != null
						&& castOther.getPostDvcd() != null && this.getPostDvcd().equals(castOther.getPostDvcd())))
				&& ((this.getTagDvcd() == castOther.getTagDvcd()) || (this.getTagDvcd() != null
						&& castOther.getTagDvcd() != null && this.getTagDvcd().equals(castOther.getTagDvcd())))
				&& ((this.getPostUrl() == castOther.getPostUrl()) || (this.getPostUrl() != null
						&& castOther.getPostUrl() != null && this.getPostUrl().equals(castOther.getPostUrl())))
				&& ((this.getPostTitle() == castOther.getPostTitle()) || (this.getPostTitle() != null
						&& castOther.getPostTitle() != null && this.getPostTitle().equals(castOther.getPostTitle())))
				&& ((this.getPostImgUrl() == castOther.getPostImgUrl()) || (this.getPostImgUrl() != null
						&& castOther.getPostImgUrl() != null && this.getPostImgUrl().equals(castOther.getPostImgUrl())))
				&& ((this.getRegDttm() == castOther.getRegDttm()) || (this.getRegDttm() != null
						&& castOther.getRegDttm() != null && this.getRegDttm().equals(castOther.getRegDttm())))
				&& ((this.getModDttm() == castOther.getModDttm()) || (this.getModDttm() != null
						&& castOther.getModDttm() != null && this.getModDttm().equals(castOther.getModDttm())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getSiteDvcd() == null ? 0 : this.getSiteDvcd().hashCode());
		result = 37 * result + (getCategoryDvcd() == null ? 0 : this.getCategoryDvcd().hashCode());
		result = 37 * result + (getPostDvcd() == null ? 0 : this.getPostDvcd().hashCode());
		result = 37 * result + (getTagDvcd() == null ? 0 : this.getTagDvcd().hashCode());
		result = 37 * result + (getPostUrl() == null ? 0 : this.getPostUrl().hashCode());
		result = 37 * result + (getPostTitle() == null ? 0 : this.getPostTitle().hashCode());
		result = 37 * result + (getPostImgUrl() == null ? 0 : this.getPostImgUrl().hashCode());
		result = 37 * result + (getRegDttm() == null ? 0 : this.getRegDttm().hashCode());
		result = 37 * result + (getModDttm() == null ? 0 : this.getModDttm().hashCode());
		return result;
	}

}
