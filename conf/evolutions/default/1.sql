#User schema

#--- !Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE user (
	id integer NOT NULL DEFAULT nextval('user_id_seq'), 
	firstName varchar(255),
	lastName varchar(255),
	email varchar(255)
);

#--- !Downs

DROP TABLE user;
DROP SEQUENCE user_id_seq;