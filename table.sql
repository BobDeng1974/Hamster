CREATE database myblog;
use myblog;
/*
用户表
 */
CREATE TABLE user
(
  uid int PRIMARY  KEY auto_increment,
  uname VARCHAR (40) NOT  NULL UNIQUE ,
  password VARCHAR (100) NOT  NULL ,
  utime datetime
)ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8;

/*
博客类别表
 */
CREATE TABLE category
(
  cid int PRIMARY  KEY auto_increment,
  cname VARCHAR (40) NOT  NULL ,
  ctime datetime
)ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8;

/*
博客文章表
 */
CREATE TABLE article
(
  artid int PRIMARY  KEY  auto_increment,
  time datetime,
  author VARCHAR (40) NOT NULL,
  likes int DEFAULT 0,
  looked int DEFAULT 0,
  title VARCHAR (60) NOT NULL ,
  meta VARCHAR (300) DEFAULT  NULL,
  content LONGTEXT NOT NULL,
  staticURL VARCHAR (100) DEFAULT NULL,
  uid int,
  cid int,
  type VARCHAR (10) NOT  NULL ,
  top int DEFAULT 0,
  md LONGTEXT,
  INDEX index_article_artid (artid),
  INDEX index_article_uid (uid),
  INDEX index_article_cid (cid)
)ENGINE=InnoDB auto_increment=100 DEFAULT CHARSET=utf8;

/*
访客表
 */
 CREATE TABLE guest
 (
  gid int PRIMARY KEY auto_increment,
  gname VARCHAR (40) NOT NULL ,
  gemail VARCHAR (100) NOT NULL UNIQUE ,
  rss int DEFAULT 0,
  INDEX index_guest_gemail (gemail),
  INDEX index_guest_gid (gid),
  INDEX index_guest_rss (rss)
)ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8;

/*
留言评论表
 */
 CREATE  TABLE comment
 (
  comid int PRIMARY  KEY auto_increment,
  comcontent VARCHAR (256) NOT  NULL ,
  gid int,
  artid int DEFAULT NULL ,
  comtime datetime
)ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8;

/*
外键约束
 */
ALTER TABLE article ADD CONSTRAINT fk_article_user FOREIGN KEY (uid) REFERENCES user(uid);
ALTER TABLE article ADD CONSTRAINT fk_article_category FOREIGN KEY (cid) REFERENCES category(cid);
/*ALTER TABLE comment ADD CONSTRAINT fk_comment_article FOREIGN KEY (artid) REFERENCES article(artid);*/
ALTER TABLE comment ADD CONSTRAINT fk_comment_guest FOREIGN KEY (gid) REFERENCES guest (gid);