package kr.co.cobot.entity;
// Generated 2017. 8. 5 ���� 10:22:22 by Hibernate Tools 5.2.3.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TbWebPushPost generated by hbm2java 
 */
@Entity
@Table(name = "tb_web_push_post", catalog = "cobot")
public class TbWebPushPost implements java.io.Serializable {

	private TbWebPushPostId id;

	public TbWebPushPost() {
	}

	public TbWebPushPost(TbWebPushPostId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "siteDvcd", column = @Column(name = "SITE_DVCD", nullable = false, length = 20)),
			@AttributeOverride(name = "categoryDvcd", column = @Column(name = "CATEGORY_DVCD", nullable = false, length = 20)),
			@AttributeOverride(name = "postDvcd", column = @Column(name = "POST_DVCD", nullable = false, length = 20)),
			@AttributeOverride(name = "tagDvcd", column = @Column(name = "TAG_DVCD", nullable = false, length = 20)),
			@AttributeOverride(name = "postUrl", column = @Column(name = "POST_URL", nullable = false, length = 256)),
			@AttributeOverride(name = "postTitle", column = @Column(name = "POST_TITLE", length = 256)),
			@AttributeOverride(name = "postImgUrl", column = @Column(name = "POST_IMG_URL", nullable = false, length = 1024)),
			@AttributeOverride(name = "regDttm", column = @Column(name = "REG_DTTM", length = 19)),
			@AttributeOverride(name = "modDttm", column = @Column(name = "MOD_DTTM", length = 19)) })
	public TbWebPushPostId getId() {
		return this.id;
	}

	public void setId(TbWebPushPostId id) {
		this.id = id;
	}

}
